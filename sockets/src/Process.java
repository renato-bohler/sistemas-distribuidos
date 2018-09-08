import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import constants.DisplayConstants;
import constants.NetworkConstants;
import enums.EnumMessageType;
import enums.EnumResourceId;
import enums.EnumResourceStatus;

public class Process extends Thread {

	private String identificador;
	private byte[] chavePublica;
	private byte[] chavePrivada;

	private Map<EnumResourceId, EnumResourceStatus> situacaoRecursos;
	private Map<EnumResourceId, Set<String>> listasEsperasRecursos;
	private Map<String, byte[]> listaChavesPublicas;
	private Boolean inicializado;

	private MulticastSocket socket;
	private MulticastSocket socketRespostasRequisicoes;
	private InetAddress group;

	/**
	 * Construtor do processo peer to peer
	 * 
	 * @param identificador
	 * @param chavePublica
	 * @param chavePrivada
	 * @throws Exception
	 */
	public Process(String identificador, byte[] chavePublica, byte[] chavePrivada) throws Exception {
		if (identificador == null || identificador.isEmpty()) {
			throw new Exception("Todo processo deve possuir um identificador");
		}

		if (chavePublica == null) {
			throw new Exception("Todo processo deve possuir uma chave pública");
		}

		if (chavePrivada == null) {
			throw new Exception("Todo processo deve possuir uma chave privada");
		}

		this.identificador = identificador;
		this.chavePublica = chavePublica;
		this.chavePrivada = chavePrivada;

		this.situacaoRecursos = new HashMap<EnumResourceId, EnumResourceStatus>();
		this.listasEsperasRecursos = new HashMap<EnumResourceId, Set<String>>();
		this.listaChavesPublicas = new HashMap<String, byte[]>();

		this.situacaoRecursos.put(EnumResourceId.RECURSO_UM, EnumResourceStatus.RELEASED);
		this.situacaoRecursos.put(EnumResourceId.RECURSO_DOIS, EnumResourceStatus.RELEASED);
		this.listasEsperasRecursos.put(EnumResourceId.RECURSO_UM, new HashSet<String>());
		this.listasEsperasRecursos.put(EnumResourceId.RECURSO_DOIS, new HashSet<String>());
		this.inicializado = Boolean.FALSE;
	}

	@Override
	public void run() {
		try {
			// Conecta-se ao multicast
			this.conectar();

			// Anuncia sua entrada
			this.anunciarEntrada();

			// Espera por mensagens
			while (true) {
				this.receberMensagem();
			}
		} catch (Exception e) {
			System.out.println();
			System.out.println(DisplayConstants.COMMAND_PREFIX + "Conexão encerrada");
			System.out.println();
			e.printStackTrace();
		} finally {
			if (this.socket != null) {
				this.socket.close();
			}
		}
	}

	/**
	 * Requisita um recurso
	 * 
	 * @param recurso
	 * @throws Exception
	 */
	public void requisitarRecurso(EnumResourceId recurso) throws Exception {
		if (!this.situacaoRecursos.get(recurso).equals(EnumResourceStatus.RELEASED)) {
			System.out.println();
			System.out.println(DisplayConstants.COMMAND_PREFIX + recurso.toString() + " já foi requisitado");
			System.out.println();
			return;
		}

		// Atualiza a situação do recurso
		this.situacaoRecursos.put(recurso, EnumResourceStatus.WANTED);

		// Envia uma mensagem de requisição do recurso
		Message requisicao = new Message(EnumMessageType.REQUISICAO, recurso, this.identificador);
		this.enviarMensagem(requisicao);

		Long timestampInicio = System.currentTimeMillis();

		Set<Message> respostasRequisicoes = new HashSet<Message>();
		while (respostasRequisicoes.size() != this.listaChavesPublicas.size()) {
			try {
				respostasRequisicoes.add(this.receberRespostaRequisicao(timestampInicio));
			} catch (SocketTimeoutException timeoutException) {
				// Pelo menos um peer esperado não respondeu a tempo
				Set<String> peersQueResponderam = respostasRequisicoes.stream()
						.map(respostaRequisicao -> respostaRequisicao.getRemetente()).collect(Collectors.toSet());

				Map<String, byte[]> peersQuePermanecem = new HashMap<String, byte[]>();
				peersQueResponderam.stream()
						.forEach(peer -> peersQuePermanecem.put(peer, this.listaChavesPublicas.get(peer)));

				Set<String> peersRemovidos = this.listaChavesPublicas.keySet();
				peersRemovidos.removeAll(peersQueResponderam);

				System.out.println();
				System.out.println(DisplayConstants.COMMAND_PREFIX + "Peers removidos: " + peersRemovidos.toString());
				System.out.println();

				this.listaChavesPublicas = peersQuePermanecem;
			}
		}

		// Neste ponto, todos os peers já responderam ou foram removidos
		Set<String> novaListaEspera = respostasRequisicoes.stream().filter(
				respostaRequisicao -> !respostaRequisicao.getSituacaoRecurso().equals(EnumResourceStatus.RELEASED))
				.map(respostaRequisicao -> respostaRequisicao.getRemetente()).collect(Collectors.toSet());

		if (novaListaEspera.size() == 0) {
			// Todos peers responderam que o recurso está RELEASED
			this.situacaoRecursos.put(recurso, EnumResourceStatus.HELD);
			System.out.println();
			System.out.println(DisplayConstants.COMMAND_PREFIX + "Acesso liberado ao " + recurso.toString());
			System.out.println();
		} else {
			// Atualiza a lista de espera pelo recurso
			this.listasEsperasRecursos.put(recurso, novaListaEspera);
			System.out.println();
			System.out.println(DisplayConstants.COMMAND_PREFIX + "Esperando pela liberação dos peers "
					+ novaListaEspera.toString());
			System.out.println();
		}
	}

	/**
	 * Libera um recurso
	 * 
	 * @param recurso
	 * @throws Exception
	 */
	public void liberarRecurso(EnumResourceId recurso) throws Exception {
		EnumResourceStatus situacaoRecurso = this.situacaoRecursos.get(recurso);
		if (!situacaoRecurso.equals(EnumResourceStatus.HELD)) {
			System.out.println();
			System.out.println(DisplayConstants.COMMAND_PREFIX + recurso.toString() + " está " + situacaoRecurso);
			System.out.println();
			return;
		}

		this.situacaoRecursos.put(recurso, EnumResourceStatus.RELEASED);

		Message liberacao = new Message(EnumMessageType.LIBERACAO_RECURSO, recurso, this.identificador);
		this.enviarMensagem(liberacao);
	}

	/**
	 * Envia mensagem de anúncio de entrada no grupo
	 * 
	 * @throws Exception
	 */
	public void anunciarSaida() throws Exception {
		Message anuncioSaida = new Message(EnumMessageType.SAIDA, this.identificador);
		this.enviarMensagem(anuncioSaida);

		this.desconectar();
	}

	/**
	 * Imprime uma lista com os peers conectados
	 */
	public void imprimirPeersConectados() {
		System.out.println();
		System.out.println(
				DisplayConstants.COMMAND_PREFIX + "Peers conectados: " + this.listaChavesPublicas.keySet().toString());
		System.out.println();
	}

	/**
	 * Conecta-se ao grupo
	 * 
	 * @throws Exception
	 */
	private void conectar() throws Exception {
		try {
			this.group = InetAddress.getByName(NetworkConstants.IP);
			this.socket = new MulticastSocket(NetworkConstants.PORT);
			this.socket.joinGroup(group);

			this.socketRespostasRequisicoes = new MulticastSocket(NetworkConstants.PORT);
			this.socketRespostasRequisicoes.joinGroup(group);
			this.socketRespostasRequisicoes.setSoTimeout(NetworkConstants.TIMEOUT_MILLIS);

			System.out.println();
			System.out.println(DisplayConstants.COMMAND_PREFIX + "Conectado a " + NetworkConstants.IP + ":"
					+ NetworkConstants.PORT);
			System.out.println();
		} catch (Exception e) {
			throw new Exception("Erro ao conectar-se ao grupo");
		}
	}

	/**
	 * Desconecta-se do grupo
	 * 
	 * @throws Exception
	 */
	private void desconectar() throws Exception {
		try {
			this.socket.leaveGroup(group);
			this.socket.disconnect();
		} catch (Exception e) {
			throw new Exception("Erro ao desconectar-se do grupo");
		}
	}

	/**
	 * Envia uma mensagem dada ao peer conectado
	 * 
	 * @param mensagem
	 * @throws Exception
	 */
	private void enviarMensagem(Message mensagem) throws Exception {
		try {
			Message mensagemAssinada = mensagem.assinar(this.chavePrivada);

			byte[] mensagemBytes = Message.toBytes(mensagemAssinada);
			DatagramPacket messageOut = new DatagramPacket(mensagemBytes, mensagemBytes.length, this.group, 6789);
			this.socket.send(messageOut);
		} catch (Exception e) {
			throw new Exception("Erro ao enviar mensagem");
		}
	}

	/**
	 * Recebe uma mensagem do peer conectado
	 * 
	 * @throws Exception
	 */
	private void receberMensagem() throws Exception {
		try {
			byte[] buffer = new byte[1000];
			Message mensagem;
			DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

			this.socket.receive(messageIn);

			mensagem = Message.fromBytes(buffer);

			System.out.println(mensagem.toString());

			if (!mensagem.getRemetente().equals(this.identificador)) {
				byte[] chavePublicaRemetenteBytes = this.listaChavesPublicas.get(mensagem.getRemetente());

				if (mensagem.getTipoMensagem().equals(EnumMessageType.ENTRADA)
						|| mensagem.getTipoMensagem().equals(EnumMessageType.RESPOSTA_ENTRADA)) {
					chavePublicaRemetenteBytes = mensagem.getChavePublica();
				}

				mensagem.validar(chavePublicaRemetenteBytes);

				this.tratarMensagem(mensagem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao receber mensagem");
		}
	}

	/**
	 * Recebe uma mensagem de resposta de requisição, chegada após timestampInicio
	 * 
	 * @param timestampInicio
	 * @throws Exception
	 */
	private Message receberRespostaRequisicao(Long timestampInicio) throws Exception {
		try {
			byte[] buffer = new byte[1000];
			Message mensagem;
			DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

			this.socketRespostasRequisicoes.receive(messageIn);

			mensagem = Message.fromBytes(buffer);

			// Limpa mensagens estão no buffer e não interessam
			if (!mensagem.getTipoMensagem().equals(EnumMessageType.RESPOSTA_REQUISICAO)
					|| mensagem.getRemetente().equals(this.identificador)
					|| timestampInicio > mensagem.getTimestamp()) {
				return this.receberRespostaRequisicao(timestampInicio);
			}

			return mensagem;
		} catch (SocketTimeoutException timeoutException) {
			throw new SocketTimeoutException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao receber mensagem");
		}
	}

	/**
	 * Faz o tratamento adequado da mensagem recebida, atualizando listas e
	 * respondendo a mensagem
	 * 
	 * @param mensagem
	 * @throws Exception
	 */
	private void tratarMensagem(Message mensagem) throws Exception {
		switch (mensagem.getTipoMensagem()) {
		case ENTRADA:
			// Atualiza a lista de chaves públicas
			this.listaChavesPublicas.put(mensagem.getRemetente(), mensagem.getChavePublica());
			// Atualiza o estado (inicalizado ou não)
			this.inicializado |= this.listaChavesPublicas.size() + 1 >= NetworkConstants.MINIMUM_PEERS;
			// Responde à mensagens de entrada
			Message respostaEntrada = new Message(EnumMessageType.RESPOSTA_ENTRADA, this.chavePublica,
					this.inicializado, this.identificador);
			this.enviarMensagem(respostaEntrada);
			break;
		case RESPOSTA_ENTRADA:
			// Atualiza a lista de chaves públicas
			this.listaChavesPublicas.put(mensagem.getRemetente(), mensagem.getChavePublica());
			// Atualiza o estado (inicializado ou não)
			this.inicializado |= mensagem.getInicializado();
			break;
		case SAIDA:
			// Atualiza a lista de chaves públicas
			this.listaChavesPublicas.remove(mensagem.getRemetente());
			break;
		case REQUISICAO:
			// Responde à requisição de recursos
			EnumResourceId recursoRequisitado = mensagem.getRecurso();
			EnumResourceStatus situacaoRecursoRequisiado = situacaoRecursos.get(recursoRequisitado);
			Message respostaRequisicao = new Message(EnumMessageType.RESPOSTA_REQUISICAO, recursoRequisitado,
					situacaoRecursoRequisiado, this.identificador);
			this.enviarMensagem(respostaRequisicao);
			break;
		case LIBERACAO_RECURSO:
			// Atualiza a lista de espera do recurso, caso esteja interessado
			EnumResourceId recursoLiberado = mensagem.getRecurso();
			EnumResourceStatus situacaoRecursoLiberado = situacaoRecursos.get(recursoLiberado);

			if (situacaoRecursoLiberado.equals(EnumResourceStatus.WANTED)) {
				Set<String> listaEsperaRecursoLiberado = this.listasEsperasRecursos.get(recursoLiberado);
				listaEsperaRecursoLiberado.remove(mensagem.getRemetente());

				if (listaEsperaRecursoLiberado.size() == 0) {
					this.situacaoRecursos.put(recursoLiberado, EnumResourceStatus.HELD);
					System.out.println();
					System.out.println(
							DisplayConstants.COMMAND_PREFIX + "Acesso liberado ao " + recursoLiberado.toString());
					System.out.println();
				} else {
					System.out.println();
					System.out.println(DisplayConstants.COMMAND_PREFIX + "Esperando pela liberação dos peers "
							+ listaEsperaRecursoLiberado.toString());
					System.out.println();
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Envia mensagem de anúncio de entrada no grupo
	 * 
	 * @throws Exception
	 */
	private void anunciarEntrada() throws Exception {
		Message anuncioEntrada = new Message(EnumMessageType.ENTRADA, this.chavePublica, this.identificador);
		this.enviarMensagem(anuncioEntrada);
	}

	public Boolean getInicializado() {
		return inicializado;
	}

}

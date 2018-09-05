import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import constants.NetworkConstants;
import enums.EnumMessageType;
import enums.EnumResourceId;
import enums.EnumResourceStatus;

public class Process extends Thread {

	private String identificador;
	private byte[] chavePublica;
	private byte[] chavePrivada;

	private Map<EnumResourceId, EnumResourceStatus> situacaoRecursos;
	private Map<String, byte[]> listaChavesPublicas;
	private Boolean inicializado;

	private MulticastSocket socket;
	private InetAddress group;

	private List<Message> bufferMensagens;
	private Boolean requisitandoRecursos;

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
		this.listaChavesPublicas = new HashMap<String, byte[]>();

		this.situacaoRecursos.put(EnumResourceId.RECURSO_UM, EnumResourceStatus.RELEASED);
		this.situacaoRecursos.put(EnumResourceId.RECURSO_DOIS, EnumResourceStatus.RELEASED);
		this.inicializado = Boolean.FALSE;

		this.bufferMensagens = new ArrayList<>();
		this.requisitandoRecursos = Boolean.FALSE;
	}

	@Override
	public void run() {
		try {
			// Conecta-se ao multicast
			this.conectar();

			// Anuncia sua entrada
			this.anunciarEntrada();

			// Espera por mensagens
			while (Boolean.FALSE.equals(this.requisitandoRecursos)) {
				this.receberMensagem();
			}
		} catch (Exception e) {
			System.out.println("Conexão encerrada");
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
		this.requisitandoRecursos = Boolean.TRUE;

		// Atualiza a situação do recurso
		this.situacaoRecursos.put(recurso, EnumResourceStatus.WANTED);

		// Envia uma mensagem de requisição do recurso
		Message requisicao = new Message(EnumMessageType.REQUISICAO, recurso, this.identificador);
		this.enviarMensagem(requisicao);

		// Recebe as mensagens de resposta à requisição, com timeout
		this.socket.setSoTimeout(NetworkConstants.TIMEOUT_MILLIS);

		List<Message> respostasRequisicoes = new ArrayList<>();
		while (respostasRequisicoes.size() != this.listaChavesPublicas.size()) {
			try {
				respostasRequisicoes.add(this.receberRespostaRequisicao());
			} catch (SocketTimeoutException timeoutException) {
				// Pelo menos um peer esperado não respondeu a tempo
				Set<String> peersQueResponderam = respostasRequisicoes.stream()
						.map(respostaRequisicao -> respostaRequisicao.getRemetente()).collect(Collectors.toSet());
				Set<String> peersQueNaoResponderam = this.listaChavesPublicas.keySet();
				peersQueNaoResponderam.removeAll(peersQueResponderam);

				peersQueNaoResponderam.forEach(peer -> this.listaChavesPublicas.remove(peer));
			}
		}

		this.socket.setSoTimeout(0);

		// Neste ponto, todos os peers já responderam ou foram removidos
		Boolean todosReleased = respostasRequisicoes.stream().allMatch(
				respostaRequisicao -> respostaRequisicao.getSituacaoRecurso().equals(EnumResourceStatus.RELEASED));

		if (todosReleased) {
			// Todos peers responderam que o recurso está RELEASED
			this.situacaoRecursos.put(recurso, EnumResourceStatus.HELD);
		} else {
			// TODO
		}

		this.requisitandoRecursos = Boolean.FALSE;
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
	 * Conecta-se ao grupo
	 * 
	 * @throws Exception
	 */
	private void conectar() throws Exception {
		try {
			this.group = InetAddress.getByName(NetworkConstants.IP);
			this.socket = new MulticastSocket(NetworkConstants.PORT);
			this.socket.joinGroup(group);

			System.out.println("Conectado a " + NetworkConstants.IP + ":" + NetworkConstants.PORT);
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
			// Se existirem mensagens no buffer, trata elas antes
			if (!this.bufferMensagens.isEmpty()) {
				Message mensagemNoBuffer = this.bufferMensagens.get(0);
				this.bufferMensagens.remove(mensagemNoBuffer);

				System.out.println(mensagemNoBuffer.toString());

				this.tratarMensagem(mensagemNoBuffer);

				return;
			}

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
	 * Recebe uma mensagem de resposta de requisição
	 * 
	 * @throws Exception
	 */
	private Message receberRespostaRequisicao() throws Exception {
		try {
			byte[] buffer = new byte[1000];
			Message mensagem;
			DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

			this.socket.receive(messageIn);

			mensagem = Message.fromBytes(buffer);

			// Se a mensagem não for uma resposta de requisição, coloca no
			// buffer e espera novamente
			if (!mensagem.getTipoMensagem().equals(EnumMessageType.RESPOSTA_REQUISICAO)) {
				if (mensagem.getRemetente().equals(this.identificador)) {
					System.out.println(mensagem.toString());
				} else {
					this.bufferMensagens.add(mensagem);
				}
				return this.receberRespostaRequisicao();
			}

			System.out.println(mensagem.toString());

			return mensagem;
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

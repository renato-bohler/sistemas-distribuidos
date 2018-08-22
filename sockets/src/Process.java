import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

import constants.NetworkConstants;
import enums.EnumMessageType;
import enums.EnumResourceId;
import enums.EnumResourceStatus;

public class Process extends Thread {

	private String identificador;
	private String chavePublica;
	private String chavePrivada;

	private Map<EnumResourceId, EnumResourceStatus> situacaoRecursos;
	private Map<String, String> listaChavesPublicas;

	private MulticastSocket socket;
	private InetAddress group;

	public Process(String identificador, String chavePublica, String chavePrivada) throws Exception {
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
		this.listaChavesPublicas = new HashMap<String, String>();
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
			System.out.println("Conexão encerrada");
			e.printStackTrace();
		} finally {
			if (this.socket != null) {
				this.socket.close();
			}
		}
	}

	public void requisitarRecurso(EnumResourceId recurso) throws Exception {
		// TODO: TTL. Como vou atualizar a lista caso algum processo morra?
		Message requisicao = new Message(EnumMessageType.REQUISICAO, recurso, this.identificador);
		this.enviarMensagem(requisicao);
	}

	private void conectar() throws Exception {
		try {
			this.group = InetAddress.getByName(NetworkConstants.IP);
			this.socket = new MulticastSocket(NetworkConstants.PORT);
			this.socket.joinGroup(group);
			System.out.println("Conectado a " + NetworkConstants.IP + ":" + NetworkConstants.PORT);
		} catch (Exception e) {
			throw new Exception("Erro ao conectar-se ao grupo");
		}

	}

	private void enviarMensagem(Message mensagem) throws Exception {
		try {
			byte[] mensagemBytes = Message.toBytes(mensagem);
			DatagramPacket messageOut = new DatagramPacket(mensagemBytes, mensagemBytes.length, this.group, 6789);
			this.socket.send(messageOut);
		} catch (Exception e) {
			throw new Exception("Erro ao enviar mensagem");
		}
	}

	private void receberMensagem() throws Exception {
		try {
			byte[] buffer = new byte[1000];
			Message mensagem;
			DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);

			this.socket.receive(messageIn);

			mensagem = Message.fromBytes(buffer);
			System.out.println(mensagem.toString());

			if (!mensagem.getRemetente().equals(this.identificador)) {
				this.tratarMensagem(mensagem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao receber mensagem");
		}
	}

	private void tratarMensagem(Message mensagem) throws Exception {
		switch (mensagem.getTipoMensagem()) {
		case ENTRADA:
			// Responde à mensagens de entrada
			Message respostaEntrada = new Message(EnumMessageType.RESPOSTA_ENTRADA, this.chavePublica,
					this.identificador);
			this.enviarMensagem(respostaEntrada);
			// Atualiza a lista de chaves públicas
			this.listaChavesPublicas.put(mensagem.getRemetente(), mensagem.getChavePublica());
			break;
		case RESPOSTA_ENTRADA:
			// Atualiza a lista de chaves públicas
			this.listaChavesPublicas.put(mensagem.getRemetente(), mensagem.getChavePublica());
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
		// TODO: tratar mensagem
		// - responde a requisições de recursos
	}

	private void anunciarEntrada() throws Exception {
		Message anuncioEntrada = new Message(EnumMessageType.ENTRADA, this.chavePublica, this.identificador);
		this.enviarMensagem(anuncioEntrada);
	}
}

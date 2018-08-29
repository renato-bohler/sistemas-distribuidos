import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

import enums.EnumMessageType;
import enums.EnumResourceId;
import enums.EnumResourceStatus;
import utils.RSAUtils;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String remetente;

	private Long timestamp;

	private byte[] encryptedTimestamp;

	private EnumMessageType tipoMensagem;

	// Deve ser preenchido se tipoMensagem for REQUISICAO ou RESPOSTA_REQUISICAO
	private EnumResourceId recurso;

	// Deve ser preenchido se tipoMensagem for RESPOSTA_REQUISICAO
	private EnumResourceStatus situacaoRecurso;

	// Deve ser preenchido se tipoMensagem for ENTRADA ou RESPOSTA_ENTRADA
	private byte[] chavePublica;

	// Construtor para REQUISICAO
	public Message(EnumMessageType tipoMensagem, EnumResourceId recurso, String remetente) throws Exception {
		if (remetente == null || remetente.isEmpty()) {
			throw new Exception("Toda mensagem deve conter o remetente");
		}

		if (!tipoMensagem.equals(EnumMessageType.REQUISICAO)) {
			throw new Exception("Construtor incompatível com o tipo de mensagem");
		}

		if (recurso == null) {
			throw new Exception("Mensagem de requisição de recurso deve preencher o recurso");
		}

		this.timestamp = System.currentTimeMillis();
		this.tipoMensagem = tipoMensagem;
		this.recurso = recurso;
		this.remetente = remetente;
	}

	// Construtor para RESPOSTA_REQUISICAO
	public Message(EnumMessageType tipoMensagem, EnumResourceId recurso, EnumResourceStatus situacaoRecurso,
			String remetente) throws Exception {
		if (remetente == null || remetente.isEmpty()) {
			throw new Exception("Toda mensagem deve conter o remetente");
		}

		if (!tipoMensagem.equals(EnumMessageType.RESPOSTA_REQUISICAO)) {
			throw new Exception("Construtor incompatível com o tipo de mensagem");
		}

		if (recurso == null) {
			throw new Exception("Mensagem de resposta à requisição de recurso deve preencher o recurso");
		}

		if (situacaoRecurso == null) {
			throw new Exception("Mensagem de resposta à requisição de recurso deve preencher a situação do recurso");
		}

		this.timestamp = System.currentTimeMillis();
		this.tipoMensagem = tipoMensagem;
		this.recurso = recurso;
		this.situacaoRecurso = situacaoRecurso;
		this.remetente = remetente;
	}

	// Construtor para ENTRADA, SAIDA e RESPOSTA_ENTRADA
	public Message(EnumMessageType tipoMensagem, byte[] chavePublica, String remetente) throws Exception {
		if (remetente == null || remetente.isEmpty()) {
			throw new Exception("Toda mensagem deve conter o remetente");
		}

		if (!(tipoMensagem.equals(EnumMessageType.ENTRADA) || tipoMensagem.equals(EnumMessageType.SAIDA)
				|| tipoMensagem.equals(EnumMessageType.RESPOSTA_ENTRADA))) {
			throw new Exception("Construtor incompatível com o tipo de mensagem");
		}

		if ((tipoMensagem.equals(EnumMessageType.ENTRADA) || tipoMensagem.equals(EnumMessageType.RESPOSTA_ENTRADA))
				&& chavePublica == null) {
			throw new Exception("Mensagem de anúncio ou resposta de entrada deve preencher a chave pública");
		}

		if (tipoMensagem.equals(EnumMessageType.SAIDA) && (chavePublica != null)) {
			throw new Exception("Mensagem de anúncio de saída não deve preencher a chave pública");
		}

		this.timestamp = System.currentTimeMillis();
		this.tipoMensagem = tipoMensagem;
		this.chavePublica = chavePublica;
		this.remetente = remetente;
	}

	public Message assinar(byte[] chavePrivadaBytes) throws Exception {
		PrivateKey chavePrivada = RSAUtils.gerarChavePrivadaDeBytes(chavePrivadaBytes);

		this.encryptedTimestamp = RSAUtils.criptografar(timestamp.toString(), chavePrivada);

		return this;
	}

	public void validar(byte[] chavePublicaBytes) throws Exception {
		if (chavePublicaBytes == null) {
			throw new Exception("Impossível validar mensagem sem a chave pública");
		}

		if (this.encryptedTimestamp == null) {
			throw new Exception("Impossível validar mensagem não assinada");
		}

		try {
			PublicKey chavePublica = RSAUtils.gerarChavePublicaDeBytes(chavePublicaBytes);

			String decryptedTimestamp = new String(RSAUtils.descriptografar(this.encryptedTimestamp, chavePublica));

			if (!decryptedTimestamp.equals(this.timestamp.toString())) {
				throw new Exception("A mensagem enviada por " + this.remetente + " não foi enviada por ele!");
			}
		} catch (Exception e) {
			throw new Exception("A mensagem enviada por " + this.remetente + " não foi enviada por ele!");
		}
	}

	@Override
	public String toString() {
		final String PREFIXO = "   >> ";
		StringBuilder sb = new StringBuilder();

		sb.append(PREFIXO);

		sb.append(this.getRemetente());

		switch (this.getTipoMensagem()) {
		case ENTRADA:
			sb.append(" entrou no grupo, informando sua chave pública");
			break;
		case RESPOSTA_ENTRADA:
			sb.append(" respondeu à entrada do colega com a sua chave pública");
			break;
		case SAIDA:
			sb.append(" saiu do grupo");
			break;
		case REQUISICAO:
			sb.append(" está requisitando o recurso " + this.getRecurso().toString());
			break;
		default:
			break;
		}
	
		return sb.toString();
	}

	public static byte[] toBytes(Message mensagem) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(mensagem);
			out.flush();
			return bos.toByteArray();
		} finally {
			try {
				bos.close();
			} catch (Exception e) {
				// Ignora
			}
		}
	}

	public static Message fromBytes(byte[] bytes) throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			return (Message) in.readObject();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				// Ignora
			}
		}
	}

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public EnumMessageType getTipoMensagem() {
		return tipoMensagem;
	}

	public void setTipoMensagem(EnumMessageType tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

	public EnumResourceId getRecurso() {
		return recurso;
	}

	public void setRecurso(EnumResourceId recurso) {
		this.recurso = recurso;
	}

	public EnumResourceStatus getSituacaoRecurso() {
		return situacaoRecurso;
	}

	public void setSituacaoRecurso(EnumResourceStatus situacaoRecurso) {
		this.situacaoRecurso = situacaoRecurso;
	}

	public byte[] getChavePublica() {
		return chavePublica;
	}

	public void setChavePublica(byte[] chavePublica) {
		this.chavePublica = chavePublica;
	}

}

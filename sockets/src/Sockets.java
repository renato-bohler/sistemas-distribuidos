import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

public class Sockets {

	public static void main(String args[]) throws Exception {
		// Gera o identificador automaticamente
		String identificador = UUID.randomUUID().toString().substring(0, 3);

		// Gera as chaves pública e privada automaticamente
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(1024);

		KeyPair keyPair = keyGenerator.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		Base64.Encoder enc = Base64.getEncoder();

		String chavePublica = enc.encodeToString(publicKey.getEncoded());
		String chavePrivada = enc.encodeToString(privateKey.getEncoded());

		// TODO
		// Teste criptografia
		/*
		 * String mensagem = "This is a secret message";
		 * System.out.println("Tamanho da mensagem: " +
		 * mensagem.getBytes().length + " bytes"); KeyPair keyPair2 =
		 * RSAUtils.buildKeyPair(); PublicKey pubKey = keyPair2.getPublic();
		 * PrivateKey privateKey2 = keyPair2.getPrivate();
		 * 
		 * // encrypt the message byte[] encrypted =
		 * RSAUtils.encrypt(privateKey2, mensagem);
		 * System.out.println("Mensagem criptografada: " + new
		 * String(encrypted));
		 * 
		 * // decrypt the message byte[] secret = RSAUtils.decrypt(pubKey,
		 * encrypted); System.out.println("Mensagem descriptografada: " + new
		 * String(secret));
		 * 
		 * System.exit(0);
		 */

		// Inicia o processo
		Process processo;
		try {
			processo = new Process(identificador, chavePublica, chavePrivada);
			processo.start();
		} catch (Exception e) {
			System.out.println("Erro ao criar processo");
		}

		// TODO: interface linha de comando
		while (true)
			;
	}

}
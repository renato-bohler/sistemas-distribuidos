package utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtils {

	private static final String ALGORITHM = "RSA";

	// Tamanho mínimo para RSA
	private static final int KEY_SIZE = 512;

	/**
	 * Gera o keyPair
	 * 
	 * @return KeyPair
	 * @throws Exception
	 */
	public static KeyPair gerarChaves() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(KEY_SIZE);
		return keyPairGenerator.genKeyPair();
	}

	/**
	 * Criptografa uma string com a chave privada informada
	 * 
	 * @param mensagem
	 * @param chavePrivada
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] criptografar(String mensagem, PrivateKey chavePrivada) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, chavePrivada);

		return cipher.doFinal(mensagem.getBytes());
	}

	/**
	 * Descriptografa uma sequência de bytes com a chave pública informada
	 * 
	 * @param encrypted
	 * @param chavePublica
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] descriptografar(byte[] encrypted, PublicKey chavePublica) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, chavePublica);

		return cipher.doFinal(encrypted);
	}

	/**
	 * Transforma PrivateKey numa sequência de bytes
	 * 
	 * @param chavePrivadaBytes
	 * @return PrivateKey
	 * @throws Exception
	 */
	public static PrivateKey gerarChavePrivadaDeBytes(byte[] chavePrivadaBytes) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(chavePrivadaBytes));
	}

	/**
	 * Transforma PublicKey numa sequência de bytes
	 * 
	 * @param chavePublicaBytes
	 * @return PublicKey
	 * @throws Exception
	 */
	public static PublicKey gerarChavePublicaDeBytes(byte[] chavePublicaBytes) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		return keyFactory.generatePublic(new X509EncodedKeySpec(chavePublicaBytes));
	}
}
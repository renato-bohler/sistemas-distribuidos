package utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSAUtils {

	private static final String ALGORITHM = "RSA";

	// Tamanho mínimo para RSA
	private static final int KEY_SIZE = 512;

	public static KeyPair gerarChaves() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(KEY_SIZE);
		return keyPairGenerator.genKeyPair();
	}

	public static byte[] criptografar(String mensagem, PrivateKey chavePrivada) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, chavePrivada);

		return cipher.doFinal(mensagem.getBytes());
	}

	public static byte[] descriptografar(PublicKey publicKey, byte[] encrypted) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(encrypted);
	}
}
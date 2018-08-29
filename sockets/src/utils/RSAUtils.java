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

	public static KeyPair gerarChaves() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(KEY_SIZE);
		return keyPairGenerator.genKeyPair();
	}

	public static byte[] criptografar(String mensagem, PrivateKey chavePrivada) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, chavePrivada);

		return cipher.doFinal(mensagem.getBytes());
	}

	public static byte[] descriptografar(byte[] encrypted, PublicKey chavePublica) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, chavePublica);

		return cipher.doFinal(encrypted);
	}

	public static PrivateKey gerarChavePrivadaDeBytes(byte[] chavePrivadaBytes) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(chavePrivadaBytes));
	}

	public static PublicKey gerarChavePublicaDeBytes(byte[] chavePublicaBytes) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

		return keyFactory.generatePublic(new X509EncodedKeySpec(chavePublicaBytes));
	}
}
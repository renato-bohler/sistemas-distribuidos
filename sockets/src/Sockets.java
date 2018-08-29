import java.security.KeyPair;
import java.util.Scanner;
import java.util.UUID;

import enums.EnumResourceId;
import utils.RSAUtils;

public class Sockets {

	public static void main(String args[]) throws Exception {
		// Gera o identificador automaticamente
		String identificador = UUID.randomUUID().toString().substring(0, 3);

		// Gera as chaves pública e privada automaticamente
		KeyPair keyPair = RSAUtils.gerarChaves();
		byte[] chavePublica = keyPair.getPublic().getEncoded();
		byte[] chavePrivada = keyPair.getPrivate().getEncoded();

		// Inicia o processo
		Process processo = null;
		try {
			processo = new Process(identificador, chavePublica, chavePrivada);
			processo.start();
		} catch (Exception e) {
			System.out.println("Erro ao criar processo");
			e.printStackTrace();
		}

		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("(" + identificador + ") Selecione uma opção abaixo:");
			System.out.println();
			System.out.println("|#\t|Ação\t\t\t|");
			System.out.println("|1\t|Requisitar recurso 1\t|");
			System.out.println("|2\t|Sair\t\t\t|");
			System.out.flush();
			System.out.println();

			switch (scanner.nextInt()) {
			case 1:
				System.out.println("Requisitando recurso 1...");
				processo.requisitarRecurso(EnumResourceId.RECURSO_UM);
				break;
			case 2:
				System.out.println("Saindo do programa...");
				scanner.close();
				System.exit(0);
				break;
			case 3:
				System.out.println("Opção inválida!");
				break;
			}

		}
	}

}
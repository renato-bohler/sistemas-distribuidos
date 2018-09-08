import java.security.KeyPair;
import java.util.Scanner;
import java.util.UUID;

import constants.NetworkConstants;
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

		// Imprime as opções no terminal
		System.out.println("(" + identificador + ") Selecione uma opção abaixo:");
		System.out.println();
		System.out.println("| #\t| Ação\t\t\t\t|");
		System.out.println("| 1\t| Requisitar recurso 1\t\t|");
		System.out.println("| 2\t| Requisitar recurso 2\t\t|");
		System.out.println("| 3\t| Liberar o recurso 1\t\t|");
		System.out.println("| 4\t| Liberar o recurso 2\t\t|");
		System.out.println("| 5\t| Listar peers conectados\t|");
		System.out.println("| 6\t| Sair\t\t\t\t|");
		System.out.flush();
		System.out.println();

		// Responde às ações do usuário no terminal
		Scanner scanner = new Scanner(System.in);
		while (true) {

			int opcao = scanner.nextInt();

			if (processo.getInicializado() || opcao == 5 || opcao == 6) {
				switch (opcao) {
				case 1:
					System.out.println("Requisitando recurso 1...");
					System.out.println();
					processo.requisitarRecurso(EnumResourceId.RECURSO_UM);
					break;
				case 2:
					System.out.println("Requisitando recurso 2...");
					System.out.println();
					processo.requisitarRecurso(EnumResourceId.RECURSO_DOIS);
					break;
				case 3:
					System.out.println("Liberando recurso 1...");
					System.out.println();
					processo.liberarRecurso(EnumResourceId.RECURSO_UM);
					break;
				case 4:
					System.out.println("Liberando recurso 2...");
					System.out.println();
					processo.liberarRecurso(EnumResourceId.RECURSO_DOIS);
					break;
				case 5:
					processo.imprimirPeersConectados();
					break;
				case 6:
					System.out.println("Saindo do programa...");
					System.out.println();
					processo.anunciarSaida();
					scanner.close();
					System.exit(0);
					break;
				default:
					System.out.println("Opção inválida!");
					break;
				}
			} else {
				System.out.println("No mínimo " + NetworkConstants.MINIMUM_PEERS
						+ " peers devem se conectar para iniciar o processo");
			}

		}
	}

}
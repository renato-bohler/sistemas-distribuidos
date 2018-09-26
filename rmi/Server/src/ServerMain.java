
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import constants.Names;
import constants.Network;
import implementation.ServerImplementation;
import rmi.Server;

public class ServerMain {
	public static void main(String[] args) throws RemoteException {
		Registry nameServer = LocateRegistry.createRegistry(Network.PORT);
		Server servidor = null;
		try {
			servidor = new ServerImplementation();
			nameServer.bind(Names.SERVIDOR, servidor);
			System.out.println("Servidor inicializado");
		} catch (Exception e) {
			System.out.println("Erro ao inicializar servidor");
			e.printStackTrace();
		}

		// Responde às ações do usuário no terminal
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				// Imprime as opções no terminal
				System.out.println("Selecione uma opção abaixo:");
				System.out.println();
				System.out.println("| #\t| Ação\t\t\t\t|");
				System.out.println("| 1\t| Consultar passagens\t\t|");
				System.out.println("| 2\t| Cadastrar passagem\t\t|");
				System.out.println("| 3\t| Editar passagem\t\t|");
				System.out.println("| 4\t| Remover passagem\t\t|");
				System.out.println("| 5\t| Consultar hospedagens\t|");
				System.out.println("| 6\t| Cadastrar hospedagem\t\t\t\t|");
				System.out.println("| 7\t| Editar hospedagem\t\t\t\t|");
				System.out.println("| 8\t| Remover hospedagem\t\t\t\t|");
				System.out.println();

				int opcao = scanner.nextInt();

				switch (opcao) {
				case 1:
					// Consultar passagens
					break;
				case 2:
					// Cadastrar passagem
					break;
				case 3:
					// Editar passagem
					break;
				case 4:
					// Remover passagem
					break;
				case 5:
					// Consultar hospedagens
					break;
				case 6:
					// Cadastrar hospedagem
					break;
				case 7:
					// Editar hospedagem
					break;
				case 8:
					// Remover hospedagem
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			scanner.close();
		}
	}
}

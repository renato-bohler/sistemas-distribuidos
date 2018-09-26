import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import constants.Names;
import constants.Network;
import implementation.ClientImplementation;
import rmi.Server;

public class ClientMain {
	public static void main(String[] args) throws AccessException, RemoteException, NotBoundException {
		Registry nameServer = LocateRegistry.getRegistry(Network.IP, Network.PORT);
		Server servidor = (Server) nameServer.lookup(Names.SERVIDOR);

		new ClientImplementation(servidor);

		// Responde às ações do usuário no terminal
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				// Imprime as opções no terminal
				System.out.println("Selecione uma opção abaixo:");
				System.out.println();
				System.out.println("| #\t| Ação\t\t\t\t|");
				System.out.println("| 1\t| Consultar passagens\t\t|");
				System.out.println("| 2\t| Consultar hospedagens\t\t|");
				System.out.println("| 3\t| Consultar pacotes\t\t|");
				System.out.println("| 4\t| Comprar passagens\t\t|");
				System.out.println("| 5\t| Comprar hospedagens\t|");
				System.out.println("| 6\t| Comprar pacotes\t\t\t\t|");
				System.out.println("| 7\t| Consultar interesses\t\t\t\t|");
				System.out.println("| 8\t| Registrar interesse\t\t\t\t|");
				System.out.println("| 9\t| Cancelar interesse\t\t\t\t|");
				System.out.println();

				int opcao = scanner.nextInt();

				switch (opcao) {
				case 1:
					// Consultar passagens
					break;
				case 2:
					// Consultar hospedagens
					break;
				case 3:
					// Consultar pacotes
					break;
				case 4:
					// Comprar passagens
					break;
				case 5:
					// Comprar hospedagens
					break;
				case 6:
					// Comprar pacotes
					break;
				case 7:
					// Consultar interesses
					break;
				case 8:
					// Registrar interesse
					break;
				case 9:
					// Cancelar interesse
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


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import constants.Names;
import constants.Network;
import implementation.ServerImplementation;
import resources.Flight;
import rmi.Server;
import utils.Output;

public class ServerMain {
	public static void main(String[] args) throws RemoteException {
		Registry nameServer = LocateRegistry.createRegistry(Network.PORT);
		Server servidor = null;
		try {
			servidor = new ServerImplementation();
			nameServer.bind(Names.SERVIDOR, servidor);
			Output.imprimir("Servidor inicializado");
		} catch (Exception e) {
			Output.imprimir("Erro ao inicializar servidor");
			e.printStackTrace();
		}

		// Responde às ações do usuário no terminal
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				// Imprime as opções no terminal
				Output.imprimir();
				Output.imprimir("| #\t| Ação\t\t\t\t|");
				Output.imprimir("| 1\t| Consultar vôos\t\t|");
				Output.imprimir("| 2\t| Cadastrar vôo\t\t\t|");
				Output.imprimir("| 3\t| Remover vôo\t\t\t|");
				Output.imprimir("| 4\t| Consultar hospedagens\t\t|");
				Output.imprimir("| 5\t| Cadastrar hospedagem\t\t|");
				Output.imprimir("| 6\t| Remover hospedagem\t\t|");
				Output.imprimir();
				Output.imprimirMesmaLinha("Selecione uma opção: ");

				int opcao = scanner.nextInt();
				scanner.nextLine();

				String origem, destino, data;
				Long numeroVagas, preco;

				switch (opcao) {
				case 1:
					// Consultar vôos
					Output.imprimir();
					Output.imprimirVoos(servidor.consultarVoos());
					break;
				case 2:
					// Cadastrar vôo
					Output.imprimirMesmaLinha("Informe a origem: ");
					origem = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o destino: ");
					destino = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data (dd/mm/aaaa): ");
					data = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o número de vagas: ");
					numeroVagas = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o preço de passagem unitário: ");
					preco = scanner.nextLong();

					Flight vooCadastro = new Flight();
					vooCadastro.setOrigem(origem);
					vooCadastro.setDestino(destino);
					vooCadastro.setData(data);
					vooCadastro.setVagas(numeroVagas);
					vooCadastro.setPrecoUnitario(preco);

					Output.imprimir(servidor.cadastrarVoo(vooCadastro));
					break;
				case 3:
					// Remover vôo
					break;
				case 4:
					// Consultar hospedagens
					break;
				case 5:
					// Cadastrar hospedagem
					break;
				case 6:
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

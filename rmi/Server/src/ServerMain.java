
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import constants.Names;
import constants.Network;
import implementation.ServerImplementation;
import resources.Accommodation;
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

				Integer opcao;
				try {
					opcao = new Integer(scanner.nextLine());
				} catch (NumberFormatException e) {
					Output.imprimir("Opção inválida");
					continue;
				}

				String origem, destino, cidade, data, dataEntrada, dataSaida;
				Long codigo, numeroVagas, numeroQuartos, numeroPessoas, preco, precoPorQuarto, precoPorPessoa;

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
					scanner.nextLine();

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
					Output.imprimirMesmaLinha("Informe o código: ");
					codigo = scanner.nextLong();
					scanner.nextLine();

					Flight vooRemover = new Flight();
					vooRemover.setId(codigo);

					Output.imprimir(servidor.removerVoo(vooRemover));
					break;
				case 4:
					// Consultar hospedagens
					Output.imprimir();
					Output.imprimirHospedagens(servidor.consultarHospedagens());
					break;
				case 5:
					// Cadastrar hospedagem
					Output.imprimirMesmaLinha("Informe a cidade: ");
					cidade = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data de entrada (dd/mm/aaaa): ");
					dataEntrada = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data de saída (dd/mm/aaaa): ");
					dataSaida = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o número de quartos: ");
					numeroQuartos = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o número de pessoas: ");
					numeroPessoas = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o preço por quarto: ");
					precoPorQuarto = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o preço por pessoa: ");
					precoPorPessoa = scanner.nextLong();
					scanner.nextLine();

					Accommodation hospedagemCadastro = new Accommodation();
					hospedagemCadastro.setCidade(cidade);
					hospedagemCadastro.setDataEntrada(dataEntrada);
					hospedagemCadastro.setDataSaida(dataSaida);
					hospedagemCadastro.setNumeroQuartos(numeroQuartos);
					hospedagemCadastro.setNumeroPessoas(numeroPessoas);
					hospedagemCadastro.setPrecoPorQuarto(precoPorQuarto);
					hospedagemCadastro.setPrecoPorPessoa(precoPorPessoa);

					Output.imprimir(servidor.cadastrarHospedagem(hospedagemCadastro));
					break;
				case 6:
					// Remover hospedagem
					Output.imprimirMesmaLinha("Informe o código: ");
					codigo = scanner.nextLong();
					scanner.nextLine();

					Accommodation hospedagemRemover = new Accommodation();
					hospedagemRemover.setId(codigo);

					Output.imprimir(servidor.removerHospedagem(hospedagemRemover));
					break;
				default:
					Output.imprimir("Opção inválida");
					break;
				}
			}
		} catch (Exception e) {
			scanner.close();
		}
	}
}

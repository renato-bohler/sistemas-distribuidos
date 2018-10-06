import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import constants.Names;
import constants.Network;
import enums.EnumDesiredEvent;
import implementation.ClientImplementation;
import resources.Accommodation;
import resources.Airfare;
import resources.Flight;
import resources.Interest;
import resources.Package;
import rmi.Client;
import rmi.Server;
import utils.Output;

public class ClientMain {
	public static void main(String[] args) throws AccessException, RemoteException, NotBoundException {
		Registry nameServer = LocateRegistry.getRegistry(Network.IP, Network.PORT);
		Server servidor = (Server) nameServer.lookup(Names.SERVIDOR);

		Client cliente = new ClientImplementation(servidor);

		// Responde às ações do usuário no terminal
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				// Imprime as opções no terminal
				Output.imprimir();
				Output.imprimir("| #\t| Ação\t\t\t\t|");
				Output.imprimir("| 1\t| Consultar passagens\t\t|");
				Output.imprimir("| 2\t| Comprar passagem\t\t|");
				Output.imprimir("| 3\t| Consultar hospedagens\t\t|");
				Output.imprimir("| 4\t| Comprar hospedagem\t\t|");
				Output.imprimir("| 5\t| Consultar pacotes\t\t|");
				Output.imprimir("| 6\t| Comprar pacote\t\t|");
				Output.imprimir("| 7\t| Consultar interesses\t\t|");
				Output.imprimir("| 8\t| Registrar interesse\t\t|");
				Output.imprimir("| 9\t| Cancelar interesse\t\t|");
				Output.imprimir();
				Output.imprimirMesmaLinha("Selecione uma opção: ");

				int opcao = scanner.nextInt();
				scanner.nextLine();

				String origem, destino, dataIda, dataVolta, dataEntrada, dataSaida;
				Long numeroQuartos, numeroPessoas, codigo, preco;

				switch (opcao) {

				case 1:
					// Consultar passagens
					Output.imprimirMesmaLinha("Ida e volta (sim/não)? ");
					String idaEVoltaString = scanner.nextLine();
					Boolean idaEVolta = null;
					switch (idaEVoltaString) {
					case "sim":
						idaEVolta = Boolean.TRUE;
						break;
					case "não":
						idaEVolta = Boolean.FALSE;
						break;
					default:
						Output.imprimir("Erro: resposta deve ser 'sim' ou 'não'");
						continue;
					}

					Output.imprimirMesmaLinha("Informe a origem: ");
					origem = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o destino: ");
					destino = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data de ida (dd/mm/aaaa): ");
					dataIda = scanner.nextLine();

					if (idaEVolta) {
						Output.imprimirMesmaLinha("Informe a data de volta (dd/mm/aaaa): ");
						dataVolta = scanner.nextLine();
					} else {
						dataVolta = null;
					}

					Output.imprimirMesmaLinha("Informe o número de pessoas: ");
					numeroPessoas = scanner.nextLong();

					Output.imprimir();
					Output.imprimirPassagens(
							servidor.consultarPassagens(origem, destino, dataIda, dataVolta, numeroPessoas));
					Output.imprimir();
					break;
				case 2:
					// Comprar passagem
					Output.imprimirMesmaLinha("Informe o código da passagem: ");
					String codigoPassagem = scanner.nextLine();

					Airfare passagem = new Airfare();
					if (codigoPassagem.contains("-")) {
						Long codigoIda = Long.valueOf(codigoPassagem.split("-")[0]);
						Long codigoVolta = Long.valueOf(codigoPassagem.split("-")[1]);

						Flight vooIda = new Flight();
						vooIda.setId(codigoIda);

						Flight vooVolta = new Flight();
						vooVolta.setId(codigoVolta);

						passagem.setIda(vooIda);
						passagem.setVolta(vooVolta);
					} else {
						Long codigoIda = Long.valueOf(codigoPassagem);

						Flight vooIda = new Flight();
						vooIda.setId(codigoIda);

						passagem.setIda(vooIda);
					}

					Output.imprimirMesmaLinha("Informe o número de pessoas: ");
					numeroPessoas = scanner.nextLong();
					scanner.nextLine();

					passagem.setNumeroPessoas(numeroPessoas);

					Output.imprimir(servidor.comprarPassagem(passagem));

					break;
				case 3:
					// Consultar hospedagens
					Output.imprimirMesmaLinha("Informe o destino: ");
					destino = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data de entrada (dd/mm/aaaa): ");
					dataEntrada = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data de saída (dd/mm/aaaa): ");
					dataSaida = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o número de quartos: ");
					numeroQuartos = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o número de pessoas: ");
					numeroPessoas = scanner.nextLong();

					Output.imprimir();
					Output.imprimirHospedagens(servidor.consultarHospedagens(destino, dataEntrada, dataSaida,
							numeroQuartos, numeroPessoas));
					break;
				case 4:
					// Comprar hospedagem
					Output.imprimirMesmaLinha("Informe o código da passagem: ");
					codigo = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o número de quartos: ");
					numeroQuartos = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o número de pessoas: ");
					numeroPessoas = scanner.nextLong();

					Accommodation hospedagem = new Accommodation();
					hospedagem.setId(codigo);
					hospedagem.setNumeroQuartos(numeroQuartos);
					hospedagem.setNumeroPessoas(numeroPessoas);

					Output.imprimir(servidor.comprarHospedagem(hospedagem));
					break;
				case 5:
					// Consultar pacotes
					Output.imprimirMesmaLinha("Informe a origem: ");
					origem = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o destino: ");
					destino = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data de ida (dd/mm/aaaa): ");
					dataIda = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe a data de volta (dd/mm/aaaa): ");
					dataVolta = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o número de quartos: ");
					numeroQuartos = scanner.nextLong();

					Output.imprimirMesmaLinha("Informe o número de pessoas: ");
					numeroPessoas = scanner.nextLong();

					Output.imprimir();
					// TODO: guardar a lista de pacotes para a chamada de compra
					Output.imprimir(
							servidor.consultarPacotes(origem, destino, dataIda, dataVolta, numeroQuartos, numeroPessoas)
									.toString());
					break;
				case 6:
					// Comprar pacote
					Output.imprimirMesmaLinha("Informe o código do pacote: ");
					codigo = scanner.nextLong();

					// TODO: recuperar os IDs da passagem e hospedagem
					// correspondente
					Package pacote = new Package();

					Output.imprimir(servidor.comprarPacote(pacote));
					break;
				case 7:
					// Consultar interesses
					Output.imprimir(servidor.consultarInteresses(cliente).toString());
					break;
				case 8:
					// Registrar interesse
					Output.imprimir();
					Output.imprimir("| #\t| Evento\t\t\t\t|");
					Output.imprimir("| 1\t| Somente passagem\t\t|");
					Output.imprimir("| 2\t| Somente hospedagem\t\t|");
					Output.imprimir("| 3\t| Passagem e hospedagem\t\t|");
					Output.imprimir();
					Output.imprimir("Selecione um evento desejado: ");

					int eventoInt = scanner.nextInt() - 1;
					scanner.nextLine();

					EnumDesiredEvent eventoDesejado = null;
					switch (eventoInt) {
					case 0:
						eventoDesejado = EnumDesiredEvent.SOMENTE_PASSAGEM;
						break;
					case 1:
						eventoDesejado = EnumDesiredEvent.SOMENTE_HOSPEDAGEM;
						break;
					case 2:
						eventoDesejado = EnumDesiredEvent.PASSAGEM_E_HOSPEDAGEM;
						break;
					default:
						break;
					}

					Output.imprimirMesmaLinha("Informe o destino desejado: ");
					destino = scanner.nextLine();

					Output.imprimirMesmaLinha("Informe o preço máximo: ");
					preco = scanner.nextLong();

					Interest interesseCadastro = new Interest();
					interesseCadastro.setCliente(cliente);
					interesseCadastro.setEventoDesejado(eventoDesejado);
					interesseCadastro.setDestinoDesejado(destino);
					interesseCadastro.setPrecoMaximo(preco);

					Output.imprimir(servidor.registrarInteresse(interesseCadastro));
					break;
				case 9:
					// Cancelar interesse
					Output.imprimirMesmaLinha("Informe o código do interesse: ");
					codigo = scanner.nextLong();

					Interest interesseCancelamento = new Interest();
					interesseCancelamento.setId(codigo);

					Output.imprimir(servidor.removerInteresse(interesseCancelamento));
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

package implementation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import enums.EnumDesiredEvent;
import resources.Accommodation;
import resources.Airfare;
import resources.Flight;
import resources.Interest;
import resources.Package;
import rmi.Client;
import rmi.Server;

public class ServerImplementation extends UnicastRemoteObject implements Server {
	private static final long serialVersionUID = 1L;

	// Persistência
	private List<Flight> voos;
	private List<Accommodation> hospedagens;
	private List<Interest> interesses;

	// Sequência global de IDs para persistência
	private Long sequence;

	public ServerImplementation() throws RemoteException {
		super();

		this.voos = new ArrayList<>();
		this.hospedagens = new ArrayList<>();
		this.interesses = new ArrayList<>();

		this.sequence = 1L;
	}

	@Override
	public List<Airfare> consultarPassagens(String origem, String destino, String dataIda, String dataVolta,
			Long numeroPessoas) throws RemoteException {
		// Busca os vôos compatíveis para ida
		List<Flight> voosCompativeisIda = this.voos.stream()
				.filter(voo -> voo.getOrigem().equals(origem) && voo.getDestino().equals(destino)
						&& (dataIda == null || voo.getData().equals(dataIda))
						&& voo.getVagas().compareTo(numeroPessoas) >= 0)
				.collect(Collectors.toList());

		if (dataVolta == null) {
			// Caso seja somente ida, retorna as passagens de ida correspondentes
			return voosCompativeisIda.stream().map(vooIda -> {
				Airfare passagemIda = new Airfare();
				passagemIda.setIda(vooIda);
				passagemIda.setNumeroPessoas(numeroPessoas);
				passagemIda.setValorTotal(numeroPessoas * vooIda.getPrecoUnitario());
				return passagemIda;
			}).collect(Collectors.toList());
		}

		// Busca os vôos compatíveis para volta
		List<Flight> voosCompativeisVolta = this.voos.stream()
				.filter(voo -> voo.getOrigem().equals(destino) && voo.getDestino().equals(origem)
						&& voo.getData().equals(dataVolta) && voo.getVagas().compareTo(numeroPessoas) >= 0)
				.collect(Collectors.toList());

		// As passagens compatíveis de ida e volta são um cross join entre voosCompativeisIda e voosCompativeisVolta
		return voosCompativeisIda.stream().flatMap(vooIda -> voosCompativeisVolta.stream().map(vooVolta -> {
			Airfare passagemIdaVolta = new Airfare();
			passagemIdaVolta.setIda(vooIda);
			passagemIdaVolta.setVolta(vooVolta);
			passagemIdaVolta.setNumeroPessoas(numeroPessoas);
			passagemIdaVolta.setValorTotal(numeroPessoas * (vooIda.getPrecoUnitario() + vooVolta.getPrecoUnitario()));
			return passagemIdaVolta;
		})).collect(Collectors.toList());
	}

	private Flight pesquisaVoo(Long idVoo) {
		return this.voos.stream().filter(voo -> voo.getId().equals(idVoo)).findFirst().orElse(null);
	}

	private Boolean vagasSuficientesVoo(Flight voo, Long numeroPessoas) {
		return voo.getVagas().compareTo(numeroPessoas) >= 0;
	}

	private void descontaVagasVoo(Flight voo, Long numeroPessoas) {
		voo.setVagas(voo.getVagas() - numeroPessoas);
		if (voo.getVagas().equals(0L)) {
			this.voos.remove(voo);
		}
	}

	@Override
	public String comprarPassagem(Airfare passagem) throws RemoteException {
		Flight vooIda = this.pesquisaVoo(passagem.getIda().getId());

		if (vooIda == null) {
			return "Vôo de ida não encontrado";
		}

		if (!vagasSuficientesVoo(vooIda, passagem.getNumeroPessoas())) {
			return "Vôo de ida não possui vagas suficientes";
		}

		Flight vooVolta = null;
		if (passagem.getVolta() != null) {
			vooVolta = this.pesquisaVoo(passagem.getVolta().getId());

			if (vooVolta == null) {
				return "Vôo de volta não encontrado";
			}

			if (!vagasSuficientesVoo(vooVolta, passagem.getNumeroPessoas())) {
				return "Vôo de volta não possui vagas suficientes";
			}

			this.descontaVagasVoo(vooVolta, passagem.getNumeroPessoas());
		}

		this.descontaVagasVoo(vooIda, passagem.getNumeroPessoas());

		return "Passagem comprada com sucesso";
	}

	@Override
	public List<Accommodation> consultarHospedagens(String destino, String dataEntrada, String dataSaida,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		// Busca as hospedagens compatíveis
		return this.hospedagens.stream()
				.filter(hospedagem -> hospedagem.getCidade().equals(destino)
						&& (dataEntrada == null || hospedagem.getDataEntrada().equals(dataEntrada))
						&& (dataSaida == null || hospedagem.getDataSaida().equals(dataSaida))
						&& hospedagem.getNumeroQuartos().compareTo(numeroQuartos) >= 0
						&& hospedagem.getNumeroPessoas().compareTo(numeroPessoas) >= 0)
				.map(hospedagem -> {
					hospedagem.setNumeroQuartos(numeroQuartos);
					hospedagem.setNumeroPessoas(numeroPessoas);
					hospedagem.setValorTotal(hospedagem.getPrecoPorQuarto() * numeroQuartos
							+ hospedagem.getPrecoPorPessoa() * numeroPessoas);
					return hospedagem;
				}).collect(Collectors.toList());
	}

	private Accommodation pesquisaHospedagem(Long idHospedagem) {
		return this.hospedagens.stream().filter(hospedagem -> hospedagem.getId().equals(idHospedagem)).findFirst()
				.orElse(null);
	}

	private Boolean quartosSuficientesHospedagem(Accommodation hospedagem, Long numeroQuartos) {
		return hospedagem.getNumeroQuartos().compareTo(numeroQuartos) >= 0;
	}

	private Boolean vagasSuficientesHospedagem(Accommodation hospedagem, Long numeroPessoas) {
		return hospedagem.getNumeroPessoas().compareTo(numeroPessoas) >= 0;
	}

	private void descontaVagasHospedagem(Accommodation hospedagem, Long numeroQuartos, Long numeroPessoas) {
		hospedagem.setNumeroQuartos(hospedagem.getNumeroQuartos() - numeroQuartos);
		hospedagem.setNumeroPessoas(hospedagem.getNumeroPessoas() - numeroPessoas);

		if (hospedagem.getNumeroQuartos().equals(0L) || hospedagem.getNumeroPessoas().equals(0L)) {
			this.hospedagens.remove(hospedagem);
		}
	}

	@Override
	public String comprarHospedagem(Accommodation hospedagem) throws RemoteException {
		Accommodation hospedagemCompra = this.pesquisaHospedagem(hospedagem.getId());

		if (hospedagemCompra == null) {
			return "Hospedagem não encontrada";
		}

		if (!this.quartosSuficientesHospedagem(hospedagemCompra, hospedagem.getNumeroQuartos())) {
			return "Não existem quartos suficientes nesta hospedagem";
		}

		if (!this.vagasSuficientesHospedagem(hospedagemCompra, hospedagem.getNumeroPessoas())) {
			return "Não existem vagas suficientes nesta hospedagem";
		}

		this.descontaVagasHospedagem(hospedagemCompra, hospedagem.getNumeroQuartos(), hospedagem.getNumeroPessoas());

		return "Hospedagem comprada com sucesso";
	}

	@Override
	public List<Package> consultarPacotes(String origem, String destino, String dataIda, String dataVolta,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		// Consulta todas as passagens de ida e volta compatíveis
		List<Airfare> passagensIdaEVolta = this.consultarPassagens(origem, destino, dataIda, dataVolta, numeroPessoas);

		// Consulta todas as hospedagens compatíveis
		List<Accommodation> hospedagens = this.consultarHospedagens(destino, dataIda, dataVolta, numeroQuartos,
				numeroPessoas);

		// As passagens compatíveis de ida e volta são um cross join entre passagensIdaEVolta e hospedagens
		return passagensIdaEVolta.stream().flatMap(passagem -> hospedagens.stream().map(hospedagem -> {
			Package pacote = new Package();
			pacote.setPassagem(passagem);
			pacote.setHospedagem(hospedagem);
			pacote.setValorTotal(passagem.getValorTotal() + hospedagem.getValorTotal());
			return pacote;
		})).collect(Collectors.toList());
	}

	@Override
	public String comprarPacote(Package pacote) throws RemoteException {
		Airfare passagem = pacote.getPassagem();
		Accommodation hospedagem = pacote.getHospedagem();

		// Passagem
		Flight vooIda = this.pesquisaVoo(passagem.getIda().getId());
		Flight vooVolta = this.pesquisaVoo(passagem.getVolta().getId());

		if (vooIda == null || vooVolta == null) {
			return "Vôo de ida e/ou volta não encontrados";
		}

		if (!vagasSuficientesVoo(vooIda, passagem.getNumeroPessoas())
				|| !vagasSuficientesVoo(vooVolta, passagem.getNumeroPessoas())) {
			return "Vôo de ida e/ou volta não possuem vagas suficientes";
		}

		// Hospedagem
		Accommodation hospedagemPacote = this.pesquisaHospedagem(hospedagem.getId());

		if (hospedagemPacote == null) {
			return "Hospedagem não encontrada";
		}

		if (!this.quartosSuficientesHospedagem(hospedagemPacote, hospedagem.getNumeroQuartos())
				|| !this.vagasSuficientesHospedagem(hospedagemPacote, hospedagem.getNumeroPessoas())) {
			return "Não existem quartou e/ou vagas suficientes nesta hospedagem";
		}

		this.descontaVagasVoo(vooVolta, passagem.getNumeroPessoas());
		this.descontaVagasVoo(vooIda, passagem.getNumeroPessoas());
		this.descontaVagasHospedagem(hospedagemPacote, hospedagem.getNumeroQuartos(), hospedagem.getNumeroPessoas());

		return "Pacote comprado com sucesso";
	}

	@Override
	public List<Interest> consultarInteresses(Client referencia) throws RemoteException {
		// Busca os interesses de um cliente pela sua referência
		return this.interesses.stream().filter(interesse -> interesse.getCliente().equals(referencia))
				.collect(Collectors.toList());
	}

	@Override
	public String registrarInteresse(Interest interesse) throws RemoteException {
		interesse.setId(sequence++);
		this.interesses.add(interesse);
		return "Interesse registrado com sucesso";
	}

	@Override
	public String removerInteresse(Interest interesseArg) throws RemoteException {
		// Busca o interesse a ser removido pelo seu ID
		Interest interesseCancelar = this.interesses.stream()
				.filter(interesse -> interesse.getId().equals(interesseArg.getId())).findFirst().orElse(null);

		if (interesseCancelar == null) {
			return "Interesse não encontrado";
		}

		this.interesses.remove(interesseCancelar);

		return "Interesse cancelado com sucesso";
	}

	@Override
	public List<Flight> consultarVoos() throws RemoteException {
		return this.voos;
	}

	@Override
	public String cadastrarVoo(Flight voo) throws RemoteException {
		voo.setId(sequence++);
		this.voos.add(voo);
		this.notificarCadastroVoo(voo);
		return "Vôo cadastrado com sucesso";
	}

	@Override
	public String removerVoo(Flight vooArg) throws RemoteException {
		// Busca o vôo a ser removido pelo seu ID
		Flight vooRemover = this.voos.stream().filter(voo -> voo.getId().equals(vooArg.getId())).findFirst()
				.orElse(null);

		if (vooRemover == null) {
			return "Vôo não encontrado";
		}

		this.voos.remove(vooRemover);

		return "Vôo removido com sucesso";
	}

	@Override
	public List<Accommodation> consultarHospedagens() throws RemoteException {
		return this.hospedagens;
	}

	@Override
	public String cadastrarHospedagem(Accommodation hospedagem) throws RemoteException {
		hospedagem.setId(sequence++);
		this.hospedagens.add(hospedagem);
		this.notificarCadastroHospedagem(hospedagem);
		return "Hospedagem cadastrada com sucesso";
	}

	@Override
	public String removerHospedagem(Accommodation hospedagemArg) throws RemoteException {
		// Busca a hospedagem a ser removida pelo seu ID
		Accommodation hospedagemRemover = this.hospedagens.stream()
				.filter(hospedagem -> hospedagem.getId().equals(hospedagemArg.getId())).findFirst().orElse(null);

		if (hospedagemRemover == null) {
			return "Hospedagem não encontrada";
		}

		this.hospedagens.remove(hospedagemRemover);

		return "Hospedagem removida com sucesso";
	}

	private void notificarCadastroVoo(Flight novoVoo) {
		// Para cada interesse cadastrado
		this.interesses.forEach(interesse -> {
			// Se for um interesse em pacotes, trata de maneira específica
			if (interesse.getEventoDesejado().equals(EnumDesiredEvent.PASSAGEM_E_HOSPEDAGEM)) {
				// TODO: pacote
			}

			// Caso o interesse não seja compatível, não notifica
			if (!interesse.getEventoDesejado().equals(EnumDesiredEvent.SOMENTE_PASSAGEM)
					|| !interesse.getOrigem().equals(novoVoo.getOrigem())
					|| !interesse.getDestino().equals(novoVoo.getDestino())
					|| interesse.getNumeroPessoas().compareTo(novoVoo.getVagas()) > 0) {
				return;
			}

			try {
				// Para todos interesses compatíveis
				// 	- consulta todas as passagens cujo valor total é menor ou igual ao preço máximo
				//  - para cada uma destas, notifica o cliente correspondente
				this.consultarPassagens(
						interesse.getOrigem(), interesse.getDestino(), null, null, interesse.getNumeroPessoas())
						.stream()
						.filter(passagem -> (passagem.getIda().getId().equals(novoVoo.getId())
								|| (passagem.getVolta() != null && passagem.getVolta().getId().equals(novoVoo.getId())))
								&& passagem.getValorTotal().compareTo(interesse.getPrecoMaximo()) <= 0)
						.forEach(passagemNotificacao -> {
							try {
								interesse.getCliente().notificar(passagemNotificacao);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						});

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
	}

	private void notificarCadastroHospedagem(Accommodation novaHospedagem) {
		// Para cada interesse cadastrado
		this.interesses.forEach(interesse -> {
			// Se for um interesse em pacotes, trata de maneira específica
			if (interesse.getEventoDesejado().equals(EnumDesiredEvent.PASSAGEM_E_HOSPEDAGEM)) {
				// TODO: pacote
			}

			// Caso o interesse não seja compatível, não notifica
			if (!interesse.getEventoDesejado().equals(EnumDesiredEvent.SOMENTE_HOSPEDAGEM)
					|| !interesse.getDestino().equals(novaHospedagem.getCidade())
					|| interesse.getNumeroQuartos().compareTo(novaHospedagem.getNumeroQuartos()) > 0
					|| interesse.getNumeroPessoas().compareTo(novaHospedagem.getNumeroPessoas()) > 0) {
				return;
			}

			try {
				// Para todos interesses compatíveis
				// 	- consulta todas as hospedagens cujo valor total é menor ou igual ao preço máximo
				//  - para cada uma destas, notifica o cliente correspondente
				this.consultarHospedagens(
						interesse.getDestino(), null, null, interesse.getNumeroQuartos(), interesse.getNumeroPessoas())
						.stream()
						.filter(hospedagem -> hospedagem.getId().equals(novaHospedagem.getId())
								&& hospedagem.getValorTotal().compareTo(interesse.getPrecoMaximo()) <= 0)
						.forEach(hospedagemNotificacao -> {
							try {
								interesse.getCliente().notificar(hospedagemNotificacao);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						});

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
	}
}

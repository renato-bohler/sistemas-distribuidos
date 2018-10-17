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
	private static final String ANY = "any";

	// Persistência
	private List<Flight> voos;
	private List<Accommodation> hospedagens;
	private List<Interest> interesses;

	// Sequência global de IDs para persistência
	private Long sequence;

	/**
	 * Construtor do servidor
	 *
	 * @throws RemoteException
	 */
	public ServerImplementation() throws RemoteException {
		super();

		this.voos = new ArrayList<>();
		this.hospedagens = new ArrayList<>();
		this.interesses = new ArrayList<>();

		this.sequence = 1L;
	}

	/**
	 * Consulta as passagens disponíveis de acordo com os filtros
	 * 
	 * @param origem
	 *            {@link String}
	 * @param destino
	 *            {@link String}
	 * @param dataIda
	 *            {@link String}
	 * @param dataVolta
	 *            {@link String}
	 * @param numeroPessoas
	 *            {@link Long}
	 * @return {@link List}<{@link Airfare}>
	 * @throws RemoteException
	 */
	@Override
	public List<Airfare> consultarPassagens(String origem, String destino, String dataIda, String dataVolta,
			Long numeroPessoas) throws RemoteException {
		// Busca os vôos compatíveis para ida
		List<Flight> voosCompativeisIda = this.voos.stream()
				.filter(voo -> voo.getOrigem().equals(origem) && voo.getDestino().equals(destino)
						&& (dataIda == null || dataIda.equals(ANY) || voo.getData().equals(dataIda))
						&& voo.getVagas().compareTo(numeroPessoas) >= 0)
				.collect(Collectors.toList());

		if (dataVolta == null) {
			// Caso seja somente ida, retorna as passagens de ida
			// correspondentes
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
						&& (dataVolta.equals(ANY) || voo.getData().equals(dataVolta))
						&& voo.getVagas().compareTo(numeroPessoas) >= 0)
				.collect(Collectors.toList());

		// As passagens compatíveis de ida e volta são um cross join entre
		// voosCompativeisIda e voosCompativeisVolta
		return voosCompativeisIda.stream().flatMap(vooIda -> voosCompativeisVolta.stream().map(vooVolta -> {
			Airfare passagemIdaVolta = new Airfare();
			passagemIdaVolta.setIda(vooIda);
			passagemIdaVolta.setVolta(vooVolta);
			passagemIdaVolta.setNumeroPessoas(numeroPessoas);
			passagemIdaVolta.setValorTotal(numeroPessoas * (vooIda.getPrecoUnitario() + vooVolta.getPrecoUnitario()));
			return passagemIdaVolta;
		})).collect(Collectors.toList());
	}

	/**
	 * Compra uma passagem
	 * 
	 * @param passagem
	 * @return {@link String}
	 * @throws RemoteException
	 */
	@Override
	public synchronized String comprarPassagem(Airfare passagem) throws RemoteException {
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

	/**
	 * Consulta as hospedagens disponíveis de acordo com os filtros
	 * 
	 * @param destino
	 *            {@link String}
	 * @param dataEntrada
	 *            {@link String}
	 * @param dataSaida
	 *            {@link String}
	 * @param numeroQuartos
	 *            {@link Long}
	 * @param numeroPessoas
	 *            {@link Long}
	 * @return {@link List}<{@link Accommodation}>
	 * @throws RemoteException
	 */
	@Override
	public List<Accommodation> consultarHospedagens(String destino, String dataEntrada, String dataSaida,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		// Busca as hospedagens compatíveis
		return this.hospedagens.stream().filter(hospedagem -> hospedagem.getCidade().equals(destino)
				&& (dataEntrada == null || dataEntrada.equals(ANY) || hospedagem.getDataEntrada().equals(dataEntrada))
				&& (dataSaida == null || dataSaida.equals(ANY) || hospedagem.getDataSaida().equals(dataSaida))
				&& hospedagem.getNumeroQuartos().compareTo(numeroQuartos) >= 0
				&& hospedagem.getNumeroPessoas().compareTo(numeroPessoas) >= 0).map(h -> {
					Accommodation hospedagem = this.copiar(h);
					hospedagem.setNumeroQuartos(numeroQuartos);
					hospedagem.setNumeroPessoas(numeroPessoas);
					hospedagem.setValorTotal(hospedagem.getPrecoPorQuarto() * numeroQuartos
							+ hospedagem.getPrecoPorPessoa() * numeroPessoas);
					return hospedagem;
				}).collect(Collectors.toList());
	}

	/**
	 * Compra uma hospedagem
	 * 
	 * @param hospedagem
	 *            {@link Accommodation}
	 * @return {@link String}
	 * @throws RemoteException
	 */
	@Override
	public synchronized String comprarHospedagem(Accommodation hospedagem) throws RemoteException {
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

	/**
	 * Consulta os pacotes disponíveis de acordo com os filtros
	 * 
	 * @param origem
	 *            {@link String}
	 * @param destino
	 *            {@link String}
	 * @param dataIda
	 *            {@link String}
	 * @param dataVolta
	 *            {@link String}
	 * @param numeroQuartos
	 *            {@link Long}
	 * @param numeroPessoas
	 *            {@link Long}
	 * @return {@link List}<{@link Package}>
	 * @throws RemoteException
	 */
	@Override
	public List<Package> consultarPacotes(String origem, String destino, String dataIda, String dataVolta,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		// Consulta todas as passagens de ida e volta compatíveis
		List<Airfare> passagensIdaEVolta = this.consultarPassagens(origem, destino, dataIda, dataVolta, numeroPessoas);

		// Consulta todas as hospedagens compatíveis
		List<Accommodation> hospedagens = this.consultarHospedagens(destino, dataIda, dataVolta, numeroQuartos,
				numeroPessoas);

		// As passagens compatíveis de ida e volta são um cross join entre
		// passagensIdaEVolta e hospedagens
		return passagensIdaEVolta.stream().flatMap(passagem -> hospedagens.stream().map(hospedagem -> {
			Package pacote = new Package();
			pacote.setPassagem(this.copiar(passagem));
			pacote.setHospedagem(this.copiar(hospedagem));
			pacote.setValorTotal(passagem.getValorTotal() + hospedagem.getValorTotal());
			return pacote;
		})).collect(Collectors.toList());
	}

	/**
	 * Compra um pacote
	 * 
	 * @param pacote
	 *            {@link Package}
	 * @return {@link String}
	 * @throws RemoteException
	 */
	@Override
	public synchronized String comprarPacote(Package pacote) throws RemoteException {
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

	/**
	 * Consulta os interesses de um dado cliente
	 * 
	 * @param referencia
	 *            {@link Client}
	 * @return {@link List}<{@link Interest}>
	 * @throws RemoteException
	 */
	@Override
	public List<Interest> consultarInteresses(Client referencia) throws RemoteException {
		// Busca os interesses de um cliente pela sua referência
		return this.interesses.stream().filter(interesse -> interesse.getCliente().equals(referencia))
				.collect(Collectors.toList());
	}

	/**
	 * Registra interesse em um evento
	 * 
	 * @param interesse
	 *            {@link Interest}
	 * @return {@link String}
	 * @throws RemoteException
	 */
	@Override
	public String registrarInteresse(Interest interesse) throws RemoteException {
		Interest novoInteresse = this.copiar(interesse);

		this.interesses.add(novoInteresse);
		return "Interesse registrado com sucesso";
	}

	/**
	 * Remove o interesse de um evento
	 * 
	 * @param interesse
	 *            {@link Interest}
	 * @return {@link String}
	 * @throws RemoteException
	 */
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

	/**
	 * Consulta todos os vôos cadastrados
	 *
	 * @return {@link List}<{@link Flight}>
	 * @throws RemoteException
	 */
	@Override
	public List<Flight> consultarVoos() throws RemoteException {
		return this.voos;
	}

	/**
	 * Cadastra um vôo
	 * 
	 * @param voo
	 *            {@link Flight}
	 * @return {@link String}
	 * @throws RemoteException
	 */
	@Override
	public String cadastrarVoo(Flight voo) throws RemoteException {
		voo.setId(sequence++);

		Flight novoVoo = this.copiar(voo);

		this.voos.add(novoVoo);
		this.notificarCadastroVoo(novoVoo);
		return "Vôo cadastrado com sucesso";
	}

	/**
	 * Remove um vôo
	 * 
	 * @param voo
	 *            {@link Flight}
	 * @return {@link String}
	 * @throws RemoteException
	 */
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

	/**
	 * Consulta todas as hospedagens cadastradas
	 * 
	 * @return {@link List}<{@link Accommodation}>
	 * @throws RemoteException
	 */
	@Override
	public List<Accommodation> consultarHospedagens() throws RemoteException {
		return this.hospedagens;
	}

	/**
	 * Cadastra uma hospedagem
	 * 
	 * @param hospedagem
	 *            {@link Accommodation}
	 * @return {@link String}
	 * @throws RemoteException
	 */
	@Override
	public String cadastrarHospedagem(Accommodation hospedagem) throws RemoteException {
		hospedagem.setId(sequence++);

		Accommodation novaHospedagem = this.copiar(hospedagem);

		this.hospedagens.add(novaHospedagem);
		this.notificarCadastroHospedagem(novaHospedagem);
		return "Hospedagem cadastrada com sucesso";
	}

	/**
	 * Remove uma hospedagem
	 * 
	 * @param hospedagem
	 *            {@link Accommodation}
	 * @return {@link String}
	 * @throws RemoteException
	 */
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

	/*
	 * MÉTODOS AUXILIARES
	 */

	/**
	 * Pesquisa um vôo pelo seu ID
	 * 
	 * @param idVoo
	 *            {@link Long}
	 * @return {@link Flight}
	 */
	private Flight pesquisaVoo(Long idVoo) {
		return this.voos.stream().filter(voo -> voo.getId().equals(idVoo)).findFirst().orElse(null);
	}

	/**
	 * Valida se um vôo tem vagas suficientes
	 *
	 * @param voo
	 *            {@link Flight}
	 * @param numeroPessoas
	 *            {@link Long}
	 * @return {@link Boolean}
	 */
	private Boolean vagasSuficientesVoo(Flight voo, Long numeroPessoas) {
		return voo.getVagas().compareTo(numeroPessoas) >= 0;
	}

	/**
	 * Desconta as vagas de um vôo
	 *
	 * @param voo
	 *            {@link Flight}
	 * @param numeroPessoas
	 *            {@link Long}
	 */
	private void descontaVagasVoo(Flight voo, Long numeroPessoas) {
		voo.setVagas(voo.getVagas() - numeroPessoas);
		if (voo.getVagas().equals(0L)) {
			this.voos.remove(voo);
		}
	}

	/**
	 * Pesquisa uma hospedagem pelo seu ID
	 * 
	 * @param idHospedagem
	 *            {@link Long}
	 * @return {@link Accommodation}
	 */
	private Accommodation pesquisaHospedagem(Long idHospedagem) {
		return this.hospedagens.stream().filter(hospedagem -> hospedagem.getId().equals(idHospedagem)).findFirst()
				.orElse(null);
	}

	/**
	 * Valida se uma hospedagem tem quartos suficientes
	 *
	 * @param hospedagem
	 *            {@link Accommodation}
	 * @param numeroQuartos
	 *            {@link Long}
	 * @return {@link Boolean}
	 */
	private Boolean quartosSuficientesHospedagem(Accommodation hospedagem, Long numeroQuartos) {
		return hospedagem.getNumeroQuartos().compareTo(numeroQuartos) >= 0;
	}

	/**
	 * Valida se uma hospedagem tem vagas suficientes
	 *
	 * @param hospedagem
	 *            {@link Accommodation}
	 * @param numeroPessoas
	 *            {@link Long}
	 * @return {@link Boolean}
	 */
	private Boolean vagasSuficientesHospedagem(Accommodation hospedagem, Long numeroPessoas) {
		return hospedagem.getNumeroPessoas().compareTo(numeroPessoas) >= 0;
	}

	/**
	 * Desconta as vagas de uma hospedagem
	 *
	 * @param hospedagem
	 *            {@link Accommodation}
	 * @param numeroQuartos
	 *            {@link Long}
	 * @param numeroPessoas
	 *            {@link Long}
	 */
	private void descontaVagasHospedagem(Accommodation hospedagem, Long numeroQuartos, Long numeroPessoas) {
		hospedagem.setNumeroQuartos(hospedagem.getNumeroQuartos() - numeroQuartos);
		hospedagem.setNumeroPessoas(hospedagem.getNumeroPessoas() - numeroPessoas);

		if (hospedagem.getNumeroQuartos().equals(0L) || hospedagem.getNumeroPessoas().equals(0L)) {
			this.hospedagens.remove(hospedagem);
		}
	}

	/**
	 * Notifica o cadastro de um novo vôo para todos interessados
	 *
	 * @param novoVoo
	 *            {@link Flight}
	 */
	private void notificarCadastroVoo(Flight novoVoo) {
		// Para cada interesse cadastrado
		this.interesses.forEach(interesse -> {
			// Se for um interesse em pacotes, trata de maneira específica
			if (interesse.getEventoDesejado().equals(EnumDesiredEvent.PASSAGEM_E_HOSPEDAGEM)) {
				this.notificarPacote(interesse, novoVoo);
				return;
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
				// - consulta todas as passagens cujo valor total é menor ou
				// igual ao preço
				// máximo
				// - para cada uma destas, notifica o cliente correspondente
				this.consultarPassagens(interesse.getOrigem(), interesse.getDestino(), null, null,
						interesse.getNumeroPessoas())
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

	/**
	 * Notifica o cadastro de uma nova hospedagem para todos interessados
	 *
	 * @param novaHospedagem
	 *            {@link Accommodation}
	 */
	private void notificarCadastroHospedagem(Accommodation novaHospedagem) {
		// Para cada interesse cadastrado
		this.interesses.forEach(interesse -> {
			// Se for um interesse em pacotes, trata de maneira específica
			if (interesse.getEventoDesejado().equals(EnumDesiredEvent.PASSAGEM_E_HOSPEDAGEM)) {
				this.notificarPacote(interesse, novaHospedagem);
				return;
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
				// - consulta todas as hospedagens cujo valor total é menor ou
				// igual ao preço
				// máximo
				// - para cada uma destas, notifica o cliente correspondente
				this.consultarHospedagens(interesse.getDestino(), null, null, interesse.getNumeroQuartos(),
						interesse.getNumeroPessoas())
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

	/**
	 * Notifica um dado interesse (caso ele deva ser notificado) por conta da
	 * criação de um novo vôo
	 * 
	 * @param interesse
	 *            {@link Interest}
	 * @param novoVoo
	 *            {@link Flight}
	 */
	private void notificarPacote(Interest interesse, Flight novoVoo) {
		// Caso o interesse não seja compatível, não notifica
		if (!(interesse.getOrigem().equals(novoVoo.getOrigem()) || interesse.getOrigem().equals(novoVoo.getDestino()))
				|| !(interesse.getDestino().equals(novoVoo.getDestino())
						|| interesse.getDestino().equals(novoVoo.getOrigem()))
				|| interesse.getNumeroPessoas().compareTo(novoVoo.getVagas()) > 0) {
			return;
		}

		try {
			// Para todos interesses compatíveis
			// - consulta todos os pacotes cujo valor total é menor ou igual ao
			// preço
			// máximo
			// - para cada uma destes, notifica o cliente correspondente
			this.consultarPacotes(interesse.getOrigem(), interesse.getDestino(), ANY, ANY, interesse.getNumeroQuartos(),
					interesse.getNumeroPessoas())
					.stream()
					.filter(pacote -> (pacote.getPassagem().getIda().getId().equals(novoVoo.getId())
							|| (pacote.getPassagem().getVolta() != null
									&& pacote.getPassagem().getVolta().getId().equals(novoVoo.getId())))
							&& pacote.getPassagem().getVolta() != null
							&& pacote.getPassagem().getIda().getData().equals(pacote.getHospedagem().getDataEntrada())
							&& pacote.getPassagem().getVolta().getData().equals(pacote.getHospedagem().getDataSaida())
							&& pacote.getValorTotal().compareTo(interesse.getPrecoMaximo()) <= 0)
					.forEach(pacoteNotificacao -> {
						try {
							interesse.getCliente().notificar(pacoteNotificacao);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Notifica um dado interesse (caso ele deva ser notificado) por conta da
	 * criação de uma nova hospedagem
	 * 
	 * @param interesse
	 *            {@link Interest}
	 * @param novaHospedagem
	 *            {@link Accommodation}
	 */
	private void notificarPacote(Interest interesse, Accommodation novaHospedagem) {
		// Caso o interesse não seja compatível, não notifica
		if (!interesse.getDestino().equals(novaHospedagem.getCidade())
				|| interesse.getNumeroQuartos().compareTo(novaHospedagem.getNumeroQuartos()) > 0
				|| interesse.getNumeroPessoas().compareTo(novaHospedagem.getNumeroPessoas()) > 0) {
			return;
		}

		try {
			// Para todos interesses compatíveis
			// - consulta todos os pacotes cujo valor total é menor ou igual ao
			// preço
			// máximo
			// - para cada uma destes, notifica o cliente correspondente
			this.consultarPacotes(interesse.getOrigem(), interesse.getDestino(), ANY, ANY, interesse.getNumeroQuartos(),
					interesse.getNumeroPessoas())
					.stream()
					.filter(pacote -> pacote.getHospedagem().getId().equals(novaHospedagem.getId())
							&& pacote.getPassagem().getVolta() != null
							&& pacote.getPassagem().getIda().getData().equals(pacote.getHospedagem().getDataEntrada())
							&& pacote.getPassagem().getVolta().getData().equals(pacote.getHospedagem().getDataSaida())
							&& pacote.getValorTotal().compareTo(interesse.getPrecoMaximo()) <= 0)
					.forEach(pacoteNotificacao -> {
						try {
							interesse.getCliente().notificar(pacoteNotificacao);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Copia uma instãncia de võo para uma nova.
	 * 
	 * @param voo
	 *            {@link Flight}
	 * @return {@link Flight}
	 */
	private Flight copiar(Flight voo) {
		Flight novaInstancia = new Flight();
		novaInstancia.setId(voo.getId());
		novaInstancia.setData(voo.getData());
		novaInstancia.setDestino(voo.getDestino());
		novaInstancia.setOrigem(voo.getOrigem());
		novaInstancia.setPrecoUnitario(voo.getPrecoUnitario());
		novaInstancia.setVagas(voo.getVagas());

		return novaInstancia;
	}

	/**
	 * Copia uma instãncia de passagem para uma nova.
	 * 
	 * @param passagem
	 *            {@link Airfare}
	 * @return {@link Airfare}
	 */
	private Airfare copiar(Airfare passagem) {
		Airfare novaInstancia = new Airfare();
		novaInstancia.setIda(passagem.getIda());
		novaInstancia.setNumeroPessoas(passagem.getNumeroPessoas());
		novaInstancia.setValorTotal(passagem.getValorTotal());
		novaInstancia.setVolta(passagem.getVolta());

		return novaInstancia;
	}

	/**
	 * Copia uma instãncia de hospedagem para uma nova.
	 * 
	 * @param hospedagem
	 *            {@link Accommodation}
	 * @return {@link Accommodation}
	 */
	private Accommodation copiar(Accommodation hospedagem) {
		Accommodation novaInstancia = new Accommodation();
		novaInstancia.setId(hospedagem.getId());
		novaInstancia.setCidade(hospedagem.getCidade());
		novaInstancia.setDataEntrada(hospedagem.getDataEntrada());
		novaInstancia.setDataSaida(hospedagem.getDataSaida());
		novaInstancia.setNumeroPessoas(hospedagem.getNumeroPessoas());
		novaInstancia.setNumeroQuartos(hospedagem.getNumeroQuartos());
		novaInstancia.setPrecoPorPessoa(hospedagem.getPrecoPorPessoa());
		novaInstancia.setPrecoPorQuarto(hospedagem.getPrecoPorQuarto());
		novaInstancia.setValorTotal(hospedagem.getValorTotal());

		return novaInstancia;
	}

	/**
	 * Copia uma instãncia de interesse para uma nova.
	 * 
	 * @param interesse
	 *            {@link Interest}
	 * @return {@link Interest}
	 */
	private Interest copiar(Interest interesse) {
		Interest novaInstancia = new Interest();
		novaInstancia.setId(interesse.getId());
		novaInstancia.setCliente(interesse.getCliente());
		novaInstancia.setDestino(interesse.getDestino());
		novaInstancia.setEventoDesejado(interesse.getEventoDesejado());
		novaInstancia.setNumeroPessoas(interesse.getNumeroPessoas());
		novaInstancia.setNumeroQuartos(interesse.getNumeroQuartos());
		novaInstancia.setOrigem(interesse.getOrigem());
		novaInstancia.setPrecoMaximo(interesse.getPrecoMaximo());

		return novaInstancia;
	}

}

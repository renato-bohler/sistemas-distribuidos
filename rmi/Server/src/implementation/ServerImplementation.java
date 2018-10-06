package implementation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import resources.Accommodation;
import resources.Airfare;
import resources.Flight;
import resources.Interest;
import resources.Package;
import rmi.Client;
import rmi.Server;

public class ServerImplementation extends UnicastRemoteObject implements Server {
	private static final long serialVersionUID = 1L;

	private List<Flight> voos;
	private List<Accommodation> hospedagens;
	private List<Interest> interesses;

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
		List<Flight> voosCompativeisIda = this.voos.stream()
				.filter(voo -> voo.getOrigem().equals(origem) && voo.getDestino().equals(destino)
						&& voo.getData().equals(dataIda) && voo.getVagas().compareTo(numeroPessoas) >= 0)
				.collect(Collectors.toList());

		if (dataVolta == null) {
			return voosCompativeisIda.stream().map(vooIda -> {
				Airfare passagemIda = new Airfare();
				passagemIda.setIda(vooIda);
				passagemIda.setNumeroPessoas(numeroPessoas);
				passagemIda.setValorTotal(numeroPessoas * vooIda.getPrecoUnitario());
				return passagemIda;
			}).collect(Collectors.toList());
		}

		List<Flight> voosCompativeisVolta = this.voos.stream()
				.filter(voo -> voo.getOrigem().equals(destino) && voo.getDestino().equals(origem)
						&& voo.getData().equals(dataVolta) && voo.getVagas().compareTo(numeroPessoas) >= 0)
				.collect(Collectors.toList());

		// Cross join voosCompativeisIda x voosCompativeisVolta
		return voosCompativeisIda.stream().flatMap(vooIda -> voosCompativeisVolta.stream().map(vooVolta -> {
			Airfare passagemIdaVolta = new Airfare();
			passagemIdaVolta.setIda(vooIda);
			passagemIdaVolta.setVolta(vooVolta);
			passagemIdaVolta.setNumeroPessoas(numeroPessoas);
			passagemIdaVolta.setValorTotal(numeroPessoas * (vooIda.getPrecoUnitario() + vooVolta.getPrecoUnitario()));
			return passagemIdaVolta;
		})).collect(Collectors.toList());
	}

	@Override
	public String comprarPassagem(Airfare passagem) throws RemoteException {
		Flight vooIda = this.voos.stream().filter(voo -> voo.getId().equals(passagem.getIda().getId())).findFirst()
				.orElse(null);

		if (vooIda == null) {
			return "Vôo de ida não encontrado";
		}

		if (vooIda.getVagas().compareTo(passagem.getNumeroPessoas()) < 0) {
			return "Vôo de ida não possui vagas suficientes";
		}

		Flight vooVolta = null;
		if (passagem.getVolta() != null) {
			vooVolta = this.voos.stream().filter(voo -> voo.getId().equals(passagem.getVolta().getId())).findFirst()
					.orElse(null);

			if (vooVolta == null) {
				return "Vôo de volta não encontrado";
			}

			if (vooIda.getVagas().compareTo(passagem.getNumeroPessoas()) < 0) {
				return "Vôo de volta não possui vagas suficientes";
			}

			vooVolta.setVagas(vooVolta.getVagas() - passagem.getNumeroPessoas());
			if (vooVolta.getVagas().equals(0L)) {
				this.voos.remove(vooVolta);
			}
		}

		vooIda.setVagas(vooIda.getVagas() - passagem.getNumeroPessoas());
		if (vooIda.getVagas().equals(0L)) {
			this.voos.remove(vooIda);
		}

		return "Passagem comprada com sucesso";
	}

	@Override
	public List<Accommodation> consultarHospedagens(String destino, String dataEntrada, String dataSaida,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		return this.hospedagens.stream().filter(hospedagem -> hospedagem.getCidade().equals(destino)
				&& hospedagem.getDataEntrada().equals(dataEntrada) && hospedagem.getDataSaida().equals(dataSaida)
				&& hospedagem.getNumeroQuartos().compareTo(numeroQuartos) >= 0
				&& hospedagem.getNumeroPessoas().compareTo(numeroPessoas) >= 0).collect(Collectors.toList());
	}

	@Override
	public String comprarHospedagem(Accommodation hospedagem) throws RemoteException {
		// TODO: implementar
		return "Ainda não implementado";
	}

	@Override
	public List<Package> consultarPacotes(String origem, String destino, String dataIda, String dataVolta,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public String comprarPacote(Package pacote) throws RemoteException {
		// TODO: implementar
		return "Ainda não implementado";
	}

	@Override
	public List<Interest> consultarInteresses(Client referencia) throws RemoteException {
		// TODO: implementar
		return this.interesses;
	}

	@Override
	public String registrarInteresse(Interest interesse) throws RemoteException {
		// TODO: implementar
		return "Ainda não implementado";
	}

	@Override
	public String removerInteresse(Interest interesse) throws RemoteException {
		// TODO: implementar
		return "Ainda não implementado";
	}

	@Override
	public List<Flight> consultarVoos() throws RemoteException {
		return this.voos;
	}

	@Override
	public String cadastrarVoo(Flight voo) throws RemoteException {
		voo.setId(sequence++);
		this.voos.add(voo);
		return "Vôo cadastrado com sucesso";
	}

	@Override
	public String removerVoo(Flight vooArg) throws RemoteException {
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
		return "Hospedagem cadastrada com sucesso";
	}

	@Override
	public String removerHospedagem(Accommodation hospedagemArg) throws RemoteException {
		Accommodation hospedagemRemover = this.hospedagens.stream()
				.filter(hospedagem -> hospedagem.getId().equals(hospedagemArg.getId())).findFirst().orElse(null);

		if (hospedagemRemover == null) {
			return "Hospedagem não encontrada";
		}

		this.hospedagens.remove(hospedagemRemover);

		return "Hospedagem removida com sucesso";
	}

}

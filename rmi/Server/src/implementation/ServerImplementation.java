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
	public Boolean comprarPassagem(Airfare passagem) throws RemoteException {
		Flight vooIda = this.voos.stream().filter(voo -> voo.getId().equals(passagem.getIda().getId())).findFirst()
				.orElse(null);

		if (vooIda == null) {
			// Vôo de ida não encontrado
			return Boolean.FALSE;
		}

		if (vooIda.getVagas().compareTo(passagem.getNumeroPessoas()) < 0) {
			// Vôo de ida não possui vagas suficientes
			return Boolean.FALSE;
		}

		Flight vooVolta = null;
		if (passagem.getVolta() != null) {
			vooVolta = this.voos.stream().filter(voo -> voo.getId().equals(passagem.getVolta().getId())).findFirst().orElse(null);

			if (vooVolta == null) {
				// Vôo de volta não encontrado
				return Boolean.FALSE;
			}

			if (vooIda.getVagas().compareTo(passagem.getNumeroPessoas()) < 0) {
				// Vôo de volta não possui vagas suficientes
				return Boolean.FALSE;
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

		return Boolean.TRUE;
	}

	@Override
	public List<Accommodation> consultarHospedagens(String destino, String dataEntrada, String dataSaida,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		// TODO: implementar
		return this.hospedagens;
	}

	@Override
	public Boolean comprarHospedagem(Accommodation hospedagem) throws RemoteException {
		// TODO: implementar
		return Boolean.TRUE;
	}

	@Override
	public List<Package> consultarPacotes(String origem, String destino, String dataIda, String dataVolta,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean comprarPacote(Package pacote) throws RemoteException {
		// TODO: implementar
		return Boolean.TRUE;
	}

	@Override
	public List<Interest> consultarInteresses(Client referencia) throws RemoteException {
		// TODO: implementar
		return this.interesses;
	}

	@Override
	public Boolean registrarInteresse(Interest interesse) throws RemoteException {
		// TODO: implementar
		return Boolean.TRUE;
	}

	@Override
	public Boolean removerInteresse(Interest interesse) throws RemoteException {
		// TODO: implementar
		return Boolean.TRUE;
	}

	@Override
	public List<Flight> consultarVoos() throws RemoteException {
		return this.voos;
	}

	@Override
	public Boolean cadastrarVoo(Flight voo) throws RemoteException {
		voo.setId(sequence++);
		this.voos.add(voo);
		return Boolean.TRUE;
	}

	@Override
	public Boolean removerVoo(Flight voo) throws RemoteException {
		// TODO: implementar
		return Boolean.TRUE;
	}

	@Override
	public Boolean cadastrarHospedagem(Accommodation hospedagem) throws RemoteException {
		// TODO: implementar
		return Boolean.TRUE;
	}

	@Override
	public Boolean removerHospedagem(Accommodation hospedagem) throws RemoteException {
		// TODO: implementar
		return Boolean.TRUE;
	}

}

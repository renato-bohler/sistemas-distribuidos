package implementation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import resources.Accommodation;
import resources.Airfare;
import resources.Interest;
import rmi.Client;
import rmi.Server;

public class ServerImplementation extends UnicastRemoteObject implements Server {
	private static final long serialVersionUID = 1L;

	private List<Airfare> passagens;
	private List<Accommodation> hospedagens;
	private List<Interest> interesses;

	public ServerImplementation() throws RemoteException {
		super();

		this.passagens = new ArrayList<>();
		this.hospedagens = new ArrayList<>();
		this.interesses = new ArrayList<>();
	}

	@Override
	public List<Airfare> consultarPassagens(String origem, String destino, String dataIda, String dataVolta,
			Long numeroPessoas) throws RemoteException {
		// TODO: implementar
		return this.passagens;
	}

	@Override
	public Boolean comprarPassagem(Airfare passagem) throws RemoteException {
		// TODO: implementar
		return null;
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
		return null;
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
		return null;
	}

	@Override
	public List<Interest> consultarInteresses(Client referencia) throws RemoteException {
		// TODO: implementar
		return this.interesses;
	}

	@Override
	public Boolean registrarInteresse(Interest interesse) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean removerInteresse(Interest interesse) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean cadastrarPassagem(Airfare passagem) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean editarPassagem(Airfare passagem) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean removerPassagem(Airfare passagem) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean cadastrarHospedagem(Accommodation hospedagem) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean editarHospedagem(Accommodation hospedagem) throws RemoteException {
		// TODO: implementar
		return null;
	}

	@Override
	public Boolean removerHospedagem(Accommodation hospedagem) throws RemoteException {
		// TODO: implementar
		return null;
	}

}

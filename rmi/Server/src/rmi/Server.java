package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import resources.Accommodation;
import resources.Airfare;
import resources.Flight;
import resources.Interest;
import resources.Package;

public interface Server extends Remote {
	public List<Airfare> consultarPassagens(String origem, String destino, String dataIda, String dataVolta,
			Long numeroPessoas) throws RemoteException;

	public String comprarPassagem(Airfare passagem) throws RemoteException;

	public List<Accommodation> consultarHospedagens(String destino, String dataEntrada, String dataSaida,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException;

	public String comprarHospedagem(Accommodation hospedagem) throws RemoteException;

	public List<Package> consultarPacotes(String origem, String destino, String dataIda, String dataVolta,
			Long numeroQuartos, Long numeroPessoas) throws RemoteException;

	public String comprarPacote(Package pacote) throws RemoteException;

	public List<Interest> consultarInteresses(Client referencia) throws RemoteException;

	public String registrarInteresse(Interest interesse) throws RemoteException;

	public String removerInteresse(Interest interesse) throws RemoteException;

	public List<Flight> consultarVoos() throws RemoteException;

	public String cadastrarVoo(Flight voo) throws RemoteException;

	public String removerVoo(Flight voo) throws RemoteException;

	public String cadastrarHospedagem(Accommodation hospedagem) throws RemoteException;

	public String removerHospedagem(Accommodation hospedagem) throws RemoteException;

}

package implementation;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import resources.Accommodation;
import resources.Airfare;
import resources.Package;
import rmi.Client;
import rmi.Server;
import utils.Output;

public class ClientImplementation extends UnicastRemoteObject implements Client {

	private static final long serialVersionUID = 1L;

	Server servidor;

	/**
	 * Construtor do cliente
	 *
	 * @param servidor {@link Server}
	 * @throws RemoteException
	 */
	public ClientImplementation(Server servidor) throws AccessException, RemoteException, NotBoundException {
		this.servidor = servidor;
	}

	/**
	 * Callback para notificação de nova passagem
	 * 
	 * @param novaPassagem {@link Airfare}
	 * @throws RemoteException
	 */
	@Override
	public void notificar(Airfare novaPassagem) throws RemoteException {
		Output.imprimir();
		Output.imprimir("*** Nova passagem de seu interesse ***");
		Output.imprimirPassagem(novaPassagem);
		Output.imprimir();
	}

	/**
	 * Callback para notificação de nova hospedagem
	 * 
	 * @param novaHospedagem {@link Accommodation}
	 * @throws RemoteException
	 */
	@Override
	public void notificar(Accommodation novaHospedagem) throws RemoteException {
		Output.imprimir();
		Output.imprimir("*** Nova hospedagem de seu interesse ***");
		Output.imprimirHospedagem(novaHospedagem);
		Output.imprimir();
	}

	/**
	 * Callback para notificação de novo pacote
	 * 
	 * @param novoPacote {@link Package}
	 * @throws RemoteException
	 */
	@Override
	public void notificar(Package novoPacote) throws RemoteException {
		Output.imprimir();
		Output.imprimir("*** Novo pacote de seu interesse ***");
		Output.imprimirPacote(novoPacote);
		Output.imprimir();
	}

}

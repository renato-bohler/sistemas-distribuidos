package implementation;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import enums.EnumDesiredEvent;
import rmi.Client;
import rmi.Server;

public class ClientImplementation extends UnicastRemoteObject implements Client {

	private static final long serialVersionUID = 1L;

	Server servidor;

	public ClientImplementation(Server servidor) throws AccessException, RemoteException, NotBoundException {
		this.servidor = servidor;
	}

	@Override
	public void notificar(EnumDesiredEvent evento, String destino, Long preco) throws RemoteException {
		// TODO: implementação
	}

}

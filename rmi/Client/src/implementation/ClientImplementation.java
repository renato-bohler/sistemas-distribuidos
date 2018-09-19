package implementation;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import constants.Names;
import rmi.Client;
import rmi.Server;

public class ClientImplementation extends UnicastRemoteObject implements Client {

	private static final long serialVersionUID = 1L;

	Registry nameServer;

	public ClientImplementation(Registry nameServer) throws AccessException, RemoteException, NotBoundException {
		this.nameServer = nameServer;

		Server servidor = (Server) nameServer.lookup(Names.SERVIDOR);

		servidor.chamar("mensagem ida", this);
	}

	@Override
	public void echo(String mensagem) throws RemoteException {
		System.out.println(mensagem);
	}

}

package implementation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmi.Client;
import rmi.Server;

public class ServerImplementation extends UnicastRemoteObject implements Server {
	private static final long serialVersionUID = 1L;

	public ServerImplementation() throws RemoteException {
		super();
	}

	@Override
	public void chamar(String mensagem, Client referencia) throws RemoteException {
		System.out.println(mensagem);
		referencia.echo(mensagem);
	}

}

package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
	public void echo(String mensagem) throws RemoteException;
}

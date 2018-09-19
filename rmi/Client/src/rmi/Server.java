package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
	public void chamar(String mensagem, Client referencia) throws RemoteException;
}

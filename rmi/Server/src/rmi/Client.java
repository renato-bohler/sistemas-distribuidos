package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import enums.EnumDesiredEvent;

public interface Client extends Remote {
	public void notificar(EnumDesiredEvent evento, String destino, Long preco) throws RemoteException;
}

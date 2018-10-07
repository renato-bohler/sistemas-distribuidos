package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import resources.Accommodation;
import resources.Airfare;
import resources.Package;

public interface Client extends Remote {
	public void notificar(Airfare novaPassagem) throws RemoteException;

	public void notificar(Accommodation novaHospedagem) throws RemoteException;

	public void notificar(Package novoPacote) throws RemoteException;

}
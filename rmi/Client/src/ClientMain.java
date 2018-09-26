import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import constants.Network;
import implementation.ClientImplementation;

public class ClientMain {
	public static void main(String[] args) throws AccessException, RemoteException, NotBoundException {
		Registry nameServer = LocateRegistry.getRegistry(Network.IP, Network.PORT);

		new ClientImplementation(nameServer);
	}
}

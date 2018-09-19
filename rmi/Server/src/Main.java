
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import constants.Names;
import constants.Network;
import implementation.ServerImplementation;
import rmi.Server;

public class Main {
	public static void main(String[] args) throws RemoteException {
		Registry nameServer = LocateRegistry.createRegistry(Network.PORT);

		try {
			Server servidor = new ServerImplementation();
			nameServer.bind(Names.SERVIDOR, servidor);
			System.out.println("Servidor inicializado");
		} catch (Exception e) {
			System.out.println("Erro ao inicializar servidor");
			e.printStackTrace();
		}
	}
}

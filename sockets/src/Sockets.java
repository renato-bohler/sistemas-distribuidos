import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

public class Sockets {

	public static void main(String args[]) {
		MulticastSocket s = null;
		try {
			InetAddress group = InetAddress.getByName(args[0]);
			s = new MulticastSocket(6789);
			s.joinGroup(group);

			Sender enviador;
			enviador = new Sender(s, group);
			enviador.start();

			Receiver receptor;
			receptor = new Receiver(s);
			receptor.start();

			while (true)
				;
		} catch (SocketException e) {
			System.out.println("Conexão encerrada");
		} catch (IOException e) {
			System.out.println("Conexão encerrada");
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

}

class Sender extends Thread {

	MulticastSocket s;
	InetAddress group;

	public Sender(MulticastSocket s, InetAddress group) {
		this.s = s;
		this.group = group;
	}

	@Override
	public void run() {
		while (true) {
			Scanner scanner = null;
			try {
				scanner = new Scanner(System.in);
				String message = scanner.nextLine();

				if (message.equals("quit")) {
					this.s.leaveGroup(group);
					this.s.close();
					System.out.println("Conexão encerrada");
					break;
				}

				byte[] m = message.getBytes();
				DatagramPacket messageOut = new DatagramPacket(m, m.length,
						this.group, 6789);
				this.s.send(messageOut);
			} catch (IOException e) {
				System.out.println("Conexão encerrada");
			}
		}
	}
}

class Receiver extends Thread {

	MulticastSocket s;

	public Receiver(MulticastSocket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			while (true) {
				byte[] buffer = new byte[1000];
				String message;
				DatagramPacket messageIn = new DatagramPacket(buffer,
						buffer.length);
				this.s.receive(messageIn);
				message = new String(messageIn.getData());
				System.out.println("Amiguinho diz: " + message);
			}
		} catch (IOException e) {
			System.out.println("Conexão encerrada");
		}
	}
}
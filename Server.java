import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Thread {
	private final int LISTENING_PORT;
	
	public Server(int port) {
		LISTENING_PORT = port;
	}
	
	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(LISTENING_PORT);
			System.out.println("[" + new Date().toString() + "] Server started.");
			while(true) {
				try {
					System.out.println("\tWaiting to accept client connection...");
					Socket clientSocket = serverSocket.accept();
					System.out.println("[" + new Date().toString() + "] Accepted connection from client " + clientSocket.toString());
					new ClientThread(clientSocket).start();
				} catch (IOException e) {
					System.out.println("Exception occurred when trying to accept or create client thread.");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
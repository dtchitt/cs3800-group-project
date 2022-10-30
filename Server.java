import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;
	private int playerCount;
	private ServerConnection playerOne;
	private ServerConnection playerTwo;

	public Server() {
		this.playerCount = 0;

		try {
			this.serverSocket = new ServerSocket(60100);
			System.out.println("Server is listening on port 60100");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void acceptConnections() {
		try {
			while (this.playerCount < 2) {
				Socket socket = serverSocket.accept();
				this.playerCount++;
				ServerConnection serverConnection = new ServerConnection(socket, this.playerCount);

				if (this.playerCount == 1) {
					System.out.println("Waiting for 1 more player!");
					playerOne = serverConnection;
				} else {
					playerTwo = serverConnection;
					System.out.println("Max amount of players found, starting game");
				}

				Thread thread = new Thread(serverConnection);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.acceptConnections();
	}
}

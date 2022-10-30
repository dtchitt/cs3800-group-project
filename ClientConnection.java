import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection {
	private Socket socket; 
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private int id;

	public ClientConnection() {
		try {
			this.socket = new Socket("localhost",60100);
			this.inStream = new DataInputStream(socket.getInputStream());
			this.outStream = new DataOutputStream(socket.getOutputStream());
			this.id = inStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getID() {
		return this.id;
	}
}

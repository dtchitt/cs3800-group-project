import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection implements Runnable {
	private Socket socket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private int id;

	public ServerConnection(Socket socket, int id) {
		this.socket = socket;
		this.id = id;
		try {
			this.inStream = new DataInputStream(socket.getInputStream());
			this.outStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			outStream.writeInt(this.id);
			outStream.flush();

			while (true) {

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

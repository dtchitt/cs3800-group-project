public class Player {
	private int id;
	ClientConnection connection;

	public Player() {
		this.connection = new ClientConnection();
		this.id = this.connection.getID();
		System.out.println("Connected to server as Player ID#: " + this.id);
	}

	public static void main (String[] args) {
		Player player = new Player();
	} 
}

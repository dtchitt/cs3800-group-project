import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * A server for a multi-player tic tac toe game. Loosely based on an example in
 * Deitel and Deitels Java How to Program book. For this project I created a
 * new application-level protocol called TTTP (for Tic Tac Toe Protocol), which
 * is entirely plain text. The messages of TTTP are:
 *
 * Client -> Server MOVE <n> QUIT
 *
 * Server -> Client WELCOME <char> VALID_MOVE OTHER_PLAYER_MOVED <n>
 * OTHER_PLAYER_LEFT VICTORY DEFEAT TIE MESSAGE <text>
 */
public class TicTacToeServer {

	public static void main(String[] args) throws Exception {
		try (var listener = new ServerSocket(60111)) {
			System.out.println("Tic Tac Toe Server is Running...");
			var pool = Executors.newFixedThreadPool(200);
			while (true) {
				Game game = new Game();
				pool.execute(game.new Player(listener.accept(), 'X'));
				pool.execute(game.new Player(listener.accept(), 'O'));
			}
		}
	}
}

class Game {

	// Board cells numbered 0-8, top to bottom, left to right; null if empty
	private Player[] board = new Player[9];

	Player currentPlayer;

	/**
	 * Checks for a winner
	 * @return the winning player or null
	 */
	public Player hasWinner() {
		Player winner = null;

		int index = 0;
		if (board[index] != null
				&& (checkMatches(index, 1, 2) || checkMatches(index, 3, 6) || checkMatches(index, 4, 8))) {
			winner = board[index];
		}

		index = 1;
		if (board[index] != null && checkMatches(index, 3, 6)) {
			winner = board[index];
		}

		index = 2;
		if (board[index] != null && (checkMatches(index, 3, 6) || checkMatches(index, 2, 4))) {
			winner = board[index];
		}

		index = 3;
		if (board[index] != null && checkMatches(index, 1, 2)) {
			winner = board[index];
		}

		index = 6;
		if (board[index] != null && checkMatches(index, 1, 2)) {
			winner = board[index];
		}

		return winner;
	}

	/**
	* check if the players match 3 winnng positions
	* @param base the base index
	* @param a base index + a will be checked
	* @param b base index + b will be checked
	* @return true if a player is winning with given indices
	*/
	private boolean checkMatches(int base, int a, int b) {
		return (this.board[base] == this.board[base + a] && this.board[base] == this.board[base + b]);
	}

	/**
	 * Checks if the board is full
	 * @return true if full
	 */
	public boolean isBoardFull() {
		return Arrays.stream(board).allMatch(p -> p != null);
	}

	/**
	 * Blocking function that determines what to do when a player tries to move
	 * If it invalid it will handle it
	 * @param location the loc clicked on the board
	 * @param player the clicking player
	 */
	public synchronized void move(int location, Player player) {
		if (player != currentPlayer) {
			throw new IllegalStateException("Not your turn");
		} else if (player.opponent == null) {
			throw new IllegalStateException("You don't have an opponent yet");
		} else if (board[location] != null) {
			throw new IllegalStateException("Cell already occupied");
		}
		board[location] = currentPlayer;
		currentPlayer = currentPlayer.opponent;
	}

	/**
	 * resets the server board
	 */
	public void resetBoard() {
		for(int i = 0; i < board.length; i++) {
			board[i] = null;
			System.out.println("Clearing the board");
		}
	}

	/**
	 * A Player is identified by a character mark which is either 'X' or 'O'. For
	 * communication with the client the player has a socket and associated Scanner
	 * and PrintWriter.
	 */
	class Player implements Runnable {
		char mark;
		Player opponent;
		Socket socket;
		Scanner input;
		PrintWriter output;
		int wins = 0;
		int loses = 0;

		public Player(Socket socket, char mark) {
			this.socket = socket;
			this.mark = mark;
		}

		/**
		 * This function is ran when the player thread is started (it comes from the Runnable interface)
		 */
		@Override
		public void run() {
			try {
				setup();
				processCommands();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (opponent != null && opponent.output != null) {
					opponent.output.println("OTHER_PLAYER_LEFT");
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}

		/**
		 * Set up the players in/out streams and send needed info
		 * @throws IOException
		 */
		private void setup() throws IOException {
			input = new Scanner(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
			output.println("WELCOME " + mark);
			if (mark == 'X') {
				currentPlayer = this;
				output.println("MESSAGE Waiting for opponent to connect");
			} else {
				opponent = currentPlayer;
				opponent.opponent = this;
				opponent.output.println("MESSAGE Your move");
			}
		}

		/**
		 * Processes the command from the input
		 */
		private void processCommands() {
			while (input.hasNextLine()) {
				var command = input.nextLine();
				if (command.startsWith("QUIT")) {
					return;
				} else if (command.startsWith("MOVE")) {
					processMoveCommand(Integer.parseInt(command.substring(5)));
				}
			}
		}

		/**
		 * Handles player movement on board
		 * @param location loc of place on board being manipulated
		 */
		private void processMoveCommand(int location) {
			try {
				move(location, this);
				output.println("VALID_MOVE");
				opponent.output.println("OPPONENT_MOVED " + location);

				Player winner = hasWinner();

				if (winner == this) {
					this.wins++;
					output.println("VICTORY-" + this.wins + "-" + this.loses);
					opponent.loses++;
					opponent.output.println("DEFEAT-" + opponent.wins + "-" + opponent.loses);
				}

				if (winner == opponent) {
					opponent.wins++;
					opponent.output.println("VICTORY-" + opponent.wins + "-" + opponent.loses);
					this.loses++;
					this.output.println("DEFEAT-" + this.wins + "-" + this.loses);
				}

				if (isBoardFull() && winner == null) {
					this.output.println("TIE");
					opponent.output.println("TIE");
					resetBoard();
				}

				if (winner != null) {
					this.output.println("PLAY_AGAIN");
					opponent.output.println("PLAY_AGAIN");
					resetBoard();
				}

			} catch (IllegalStateException e) {
				this.output.println("MESSAGE " + e.getMessage());
			}
		}

		/**
		* set the number of wins a player has
		* @param wins the wins of a player
		*/
		public void setWins(int wins) {
			this.wins = wins;
		}

		/**
		 * get the number of wins a player has 
		 * @return the wins of a player
		 */
		public int getWins() {
			return wins;
		}

		/**
		 * set the number of loses a player has
		 * @param loses the loses of a player
		 */
		public void setLoses(int loses) {
			this.loses = loses;
		}

		/**
		 * get the number of loses a player has 
		 * @return the loses of a player
		 */
		public int getLoses() {
			return loses;
		}
	}
}

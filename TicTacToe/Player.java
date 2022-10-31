package TicTacToe;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
     * A Player is identified by a character mark which is either 'X' or 'O'. For
     * communication with the client the player has a socket and associated Scanner
     * and PrintWriter.
     */
class Player implements Runnable {
	private char mark;
	private Player opponent;
	private Socket socket;
	private Scanner input;
	private PrintWriter output;

	public Player(Socket socket, char mark) {
		this.socket = socket;
		this.mark = mark;
	}

	@Override
	public void run() {
		// try {
		// 	setup();
		// 	processCommands();
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// } finally {
		// 	if (opponent != null && opponent.output != null) {
		// 		opponent.output.println("OTHER_PLAYER_LEFT");
		// 	}
		// 	try {
		// 		socket.close();
		// 	} catch (IOException e) {
		// 	}
		// }
	}

	private void setup() throws IOException {
		// input = new Scanner(socket.getInputStream());
		// output = new PrintWriter(socket.getOutputStream(), true);
		// output.println("WELCOME " + mark);
		// if (mark == 'X') {
		// 	//currentPlayer = this;
		// 	output.println("MESSAGE Waiting for opponent to connect");
		// } else {
		// 	//opponent = currentPlayer;
		// 	opponent.opponent = this;
		// 	opponent.output.println("MESSAGE Your move");
		// }
	}

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

	private void processMoveCommand(int location) {
		try {
			// move(location, this);
			// output.println("VALID_MOVE");
			// opponent.output.println("OPPONENT_MOVED " + location);
			// if (hasWinner()) {
			//     output.println("VICTORY");
			//     opponent.output.println("DEFEAT");
			// } else if (boardFilledUp()) {
			//     output.println("TIE");
			//     opponent.output.println("TIE");
			// }
		} catch (IllegalStateException e) {
			// output.println("MESSAGE " + e.getMessage());
		}
	}
}
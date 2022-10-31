package TicTacToe;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * GameLogic holds all needed game logic for the Tic Tac Toe game
 */
public class GameLogic {
	private int[] boardArray;

	public GameLogic() {
		this.boardArray = resetBoard();
	}

	public synchronized void step() {
		//TODO code to handle a player attempting to mark a spot
	}

	/**
	 * Creates a player object for incoming connection
	 * @param serverSocket the socket to listen on
	 * @param playerIcon the icon the player will be identified as in game (X or O)
	 * @return a new Runnable which is executed by a thread in the ExecuterService of the server
	 * @throws IOException stack trace will be printed
	 */
	public Runnable addPlayer(ServerSocket serverSocket, char playerIcon) throws IOException {
		return new Player(serverSocket.accept(), playerIcon);
	}

	/**
	 * Checks if the board is full by seeing if there are any spots marked as unused (-1 means unused)
	 * @return a bool, true for full, false for not fill
	 */
	public boolean isBoardFull() {
		boolean result = true;
		
		for (int i = 0; i < boardArray.length; i++) {
			if (this.boardArray[i] == -1) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * Checks to see if a player won in relation to the position they last went
	 * @param position the position the player marked
	 * @return a bool, true if a player has won, false if not
	 */
	public boolean checkVictory(int position) {
		boolean result = false;

		switch (position) {
			case 0:
				result = checkMatches(position, position + 1, position + 2)
						|| checkMatches(position, position + 4, position + 8)
						|| checkMatches(position, position + 3, position + 6);
				break;
			case 1:
				result = checkMatches(position, position - 1, position + 1)
						|| checkMatches(position, position + 3, position + 6);
				break;
			case 2:
				result = checkMatches(position, position - 1, position - 2)
						|| checkMatches(position, position + 3, position + 6)
						|| checkMatches(position, position + 2, position + 4);
				break;
			case 3:
				result = checkMatches(position, position - 3, position + 3)
						|| checkMatches(position, position + 1, position + 2);
				break;
			case 4:
				result = checkMatches(position, position - 1, position + 1)
						|| checkMatches(position, position - 3, position + 3)
						|| checkMatches(position, position - 4, position + 4)
						|| checkMatches(position, position - 2, position + 2);
				break;
			case 5:
				result = checkMatches(position, position - 3, position + 3)
						|| checkMatches(position, position - 2, position - 1);
				break;
			case 6:
				result = checkMatches(position, position + 1, position + 2)
						|| checkMatches(position, position - 3, position - 6)
						|| checkMatches(position, position - 2, position - 4);
				break;
			case 7:
				result = checkMatches(position, position - 1, position + 1)
						|| checkMatches(position, position - 3, position - 6);
				break;
			case 8:
				result = checkMatches(position, position - 4, position - 8)
						|| checkMatches(position, position - 1, position - 2)
						|| checkMatches(position, position - 3, position - 6);
				break;
			default:
				break;
		}

		return result;
	}

	//Helper function to check matches in checkVictory
	private boolean checkMatches(int base, int a, int b) {
		return (this.boardArray[base] == this.boardArray[a] && this.boardArray[base] == this.boardArray[b]);
	}

	//Resets the board by setting all array values to -1
	private int[] resetBoard() {
		return new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	}
}

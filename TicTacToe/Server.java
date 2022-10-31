package TicTacToe;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	public final static int port = 60100;
	public static ExecutorService threadPool;
	public static GameLogic game;

	public static void main(String[] args) throws Exception {
			//Create a Server Socket bound to port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server Listening on port #" + " " + port);
			//Create a ExecutorService to manage threads
            threadPool = Executors.newFixedThreadPool(2);
            while (true) {
				//Set up tic tac toe logic
				game = new GameLogic();
				//Tell the server to listen for connections from serverSocket
				//When a connection is accepted, it will create the Runnable Player
                threadPool.execute(game.addPlayer(serverSocket, 'X'));
                threadPool.execute(game.addPlayer(serverSocket, 'O'));
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
}

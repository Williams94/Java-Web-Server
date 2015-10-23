
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author rbw3 Extended from CS5001 Example code by John Lewis
 */
public class Server {

	private ServerSocket ss;
	ExecutorService executor;

	/**
	 * @param port e.g. 80
	 */
	public Server(int port) {
		try {
			ss = new ServerSocket(port);

			System.out.println("Server started ... listening on port " + port + " ...\n");
			while (true) {

				// Fixed Thread pool to handle threads
				executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

				// Upto 10 threads at once allowed
				for (int i = 0; i <= 10; i++) {
					
					// create a new client handle and pass socket as a parameter
					ClientHandler st = new ClientHandler("Client" + i, ss.accept());

					// Executes client handler in a new thread
					executor.execute(st);
				}

				// Too many threads and it will close them down
				executor.shutdown();

			}
		} catch (IOException ioe) {
			System.out.println("Ooops " + ioe.getMessage());
		}
	}
}

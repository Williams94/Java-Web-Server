import java.net.InetAddress;
import java.net.Socket;

/**
 * @author rbw3
 */
public class ClientHandler implements Runnable {

	private Socket socket;
	private ConnectionHandler ch;
	private String name;

	/**
	 * @param name 	of each thread
	 * @param s		socket to be passed to the thread
	 */
	public ClientHandler(String name, Socket s) {
		this.socket = s;
		this.name = name;
	}

	public void run() {
		// Creates a new connection handler and passes it the socket
		ch = new ConnectionHandler(socket);
		
		// Runs the connection handler
		ch.run();
	}

	/**
	 * @return	e.g. /127.0.0.1
	 */
	public InetAddress getAddress() {
		return socket.getInetAddress();

	}

	/**
	 * @return name of thread
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}

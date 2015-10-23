

/**
 * CS5001 Example code by John Lewis
 */
public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 1){ 
			System.out.println("Usage: ClientMain <hostname>");
			System.exit(1);
		} 
		Client c = new Client(args[0], Configuration.defaultPort);
	}	
}

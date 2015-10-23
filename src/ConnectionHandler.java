
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author rbw3 
 * Extended from CS5001 Example code by John Lewis
 */
public class ConnectionHandler extends Thread {
	// socket representing TCP/IP connection to Client
	private Socket conn;

	// get data from client on this input stream
	private InputStream is;

	// can send data back to the client on this output
	private OutputStream os;

	// use buffered reader to read client data
	BufferedReader in;

	// use print writer to write data to client
	PrintWriter out;

	// file to read
	File f;

	// input stream to read from file
	FileInputStream fis;

	// buffered reader to read from file input stream
	BufferedReader br;

	BufferedOutputStream bos;

	// boolean to check if using Chrome since Chrome wont work went sending
	// content length in header
	private boolean chrome = false;

	// 404 Error File
	File errFile;

	int lines = 0;

	/**
	 * @param conn -- Socket connection passed by ClientHandler class
	 */
	public ConnectionHandler(Socket conn) {
		this.conn = conn;
		try {
			// get data from client on this input stream
			is = conn.getInputStream();

			// to send data back to the client on this stream
			os = conn.getOutputStream();

			// use buffered reader to read client data
			in = new BufferedReader(new InputStreamReader(is));

			// use buffered writer to write data to client
			out = new PrintWriter(os);

			bos = new BufferedOutputStream(os);

			errFile = new File(Configuration.htmlDirectory + "/404.html");

		} catch (IOException ioe) {
			System.out.println("ConnectionHandler: " + ioe.getMessage());
		}
	}

	// run method is invoked when the Thread's start method (ch.start(); in
	// Server class) is invoked
	public void run() {
		System.out.println("ConnectionHandler: new connection handler thread created");
		try {

			// Sends a response to client based on the request received
			sendResponse(getClientRequest());
			cleanup();

			// exit cleanly for any Exception (including IOException,
			// ClientDisconnectedException)
		} catch (Exception e) {
			System.out.println("ConnectionHandler: " + e.getMessage());
			// e.printStackTrace();
			cleanup();
		}
	}

	/**
	 * @return Request
	 * @throws DisconnectedException
	 * @throws IOException
	 * @throws InvalidRequestException
	 */
	private Request getClientRequest() throws DisconnectedException, IOException, InvalidRequestException {

		// enter infinite loop
		for (;;) {

			// Create and initialise a request
			Request req = null;

			// Read in each line from client's request
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() == 0) {
					break;
				} else if (typeOfRequest(line).equals("GET")) {
					
					// if GET request decompose line into the request object
					req = decomposeRequest(line);
					
				} else if (typeOfRequest(line).equals("HEAD")) {
					// Possible extension in here for HEAD requests
					System.out.println("HEAD");
					
				} else if (line.contains("text/html") && line.length() > 17) {
					// If client is looking for html then assign the appropriate request field
					req.setContentType(line.substring(8, 17));
					
				} else if (line.contains("image/*") && line.length() > 26) {
					// If client is looking for images then assign the appropriate request field
					req.setContentType("image/*");
					
				} else if (line.contains("Chrome")) {
					// Checking if Chrome is being used
					chrome = true;
				}

			}

			// Throws exception if the request isn't GET or HEAD
			if (req == null) {
				throw new InvalidRequestException("Not a GET Request");
			}

			return req;
		}
	}

	/**
	 * @param req  needs a request to formulate a response
	 * @throws IOException
	 */
	private void sendResponse(Request req) throws IOException {

		// Create response based on the client request
		Response res = new Response(req.getVersion(), req.getContentType(), req.getURI().trim());

		// Create file from the URI the client requests
		f = getFile(res.getURI());

		// Changes file according to 404 or 200 status code
		handleResponse(res);

		// Sends the header to the client
		sendHeader(res);

		if (res.getContentType().equals("text/html")) {
			// Sends html to the client
			sendData();
		} else if (res.getContentType().equals("image/*")) {
			// Sends image to the client
			sendImage(res);
		}

		// Closing open connections
		out.close();
		bos.close();
		bos.flush();
	}

	/**
	 * @param res 
	 * Takes in a response to determine what data needs to be sent based on the header
	 */
	public void sendImage(Response res) {
		try {
			// Find image and convert it to bytes
			Path filePath = Paths.get(Configuration.htmlDirectory + "/" + res.getURI());
			byte[] data = Files.readAllBytes(filePath);
			
			// Write image to client on BufferedOutputStream
			bos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * sends data from a file using Buffered Reader to the client using Print Writer
	 */
	public void sendData() {
		String data = null;
		try {
			while ((data = br.readLine()) != null) {
				out.print(data);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param res 
	 * Takes response as a parameter to determine what header fields to send to the client
	 */
	public void sendHeader(Response res) {
		// This is the header sent to the client
		out.print("HTTP/" + res.getVersion() + " " + res.getStatusCode() + res.getStatusCodeDescription() + "\r\n");
		out.print("Connection: close\r\n");
		out.print("Date: " + res.getDateString() + "\r\n");
		out.print("Server: " + Configuration.serverName + "\r\n");

		// Explained fully in the README file, but Chrome and Firefoxs file
		// length in the Content-Length header field vary
		if (chrome) {
			// Taking away the number of lines twice from the file length makes
			// this work in Chrome
			out.print("Content-Length: " + ((res.getContentLength() - lines) - lines) + "\r\n");
		} else {
			// Firefox is happy with the file length as it is
			out.print("Content-Length: " + res.getContentLength() + "\r\n");
		}

		out.print("Content-Type: " + res.getContentType() + "\r\n");
		out.print("\r\n");
	}

	/**
	 * @param res 
	 * Tries to create a File Input Stream, but if the file cant be found then send the client a 404 error page instead
	 */
	public void handleResponse(Response res) {
		// If the file is not found it sets response code to 404
		try {
			is = new FileInputStream(f);
			res.setStatusCode(statusCode(f));
			res.setContentLength(f.length());
			
			// Need the number of lines in the file since Chrome and firefox
			// have varying acceptance of Content-Length field in the header
			// Will go into more detail in the README file
			lines = getNumberOfFileLines(f);
			
			// Log the client access
			Log l = new Log(res.getStatusCode(), conn.getInetAddress(), res.getDate(), res.getURI());
			l.store();
		} catch (FileNotFoundException e1) {
			res.setContentLength(errFile.length());
			res.setStatusCode(404);
			f = errFile;
			try {
				is = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			lines = getNumberOfFileLines(errFile);
			
			// log the error
			Log l = new Log(res.getStatusCode(), conn.getInetAddress(), res.getDate(), res.getURI());
			l.store();
		}
		// Buffered Reader to read in data from input stream and html file
		br = new BufferedReader(new InputStreamReader(is));
	}

	
	/**
	 * @param takes in a file as a parameter
	 * @return number of lines in file
	 * For the chrome issue see README.txt
	 */
	private int getNumberOfFileLines(File f) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			int lines = 0;
			while (reader.readLine() != null) {
				lines++;
			}
			reader.close();
			return lines;
		} catch (IOException e) {
			return 0;
		}
	}

	
	/**
	 * @param File
	 * @return status code as int
	 * Checks if the file exists and assigns the appropriate status code
	 */
	private int statusCode(File f) {
		if (f.exists()) {
			return 200;
		} else {
			return 404;
		}
	}


	/**
	 * @param uri e.g. /index.html
	 * @return the file object
	 */
	private File getFile(String uri) {
		File doc = new File(Configuration.htmlDirectory + uri);

		return doc;
	}


	/**
	 * @param client request line
	 * @return request
	 * @throws InvalidRequestException
	 */
	private Request decomposeRequest(String line) throws InvalidRequestException {

		// Find type of request
		String method = line.substring(0, 4).trim();
		
		// Makes sure it is a HTTP request
		int index = line.indexOf("HTTP");
		if (index == -1) {
			throw new InvalidRequestException("Invalid Request Made. HTTP statement not found");
		}

		// Find out what file the client is looking for
		String uri = line.substring(4, index).trim();
		
		// Get the HTTP version
		double version = Double.parseDouble(line.substring(index + 5));
		
		// Create and return the request
		Request r = new Request(method, uri, version);
		return r;
	}

	
	/**
	 * @param line from client
	 * @return type of request e.g. GET
	 */
	private String typeOfRequest(String line) {

		if (line.length() > 3 && line.substring(0, 3).equals("GET")) {
			// System.out.println("GET Request Made.");
			return line.substring(0, 3);
		} else if (line.length() > 3 && line.substring(0, 4).equals("HEAD")) {
			// System.out.println("HEAD Request Made.");
			return line.substring(0, 4);
		} else {
			return "";
		}

	}

	/**
	 * Closes connections
	 */
	private void cleanup() {
		System.out.println("ConnectionHandler: ... cleaning up and exiting ... \n");
		try {
			in.close();
			is.close();
			conn.close();
		} catch (IOException ioe) {
			System.out.println("ConnectionHandler:cleanup " + ioe.getMessage());
		}
	}

}

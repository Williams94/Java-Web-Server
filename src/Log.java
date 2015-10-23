
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Date;

/**
 * @author rbw3
 *
 */
public class Log {

	// Different log files for client access and errors
	File accessLog = new File(Configuration.logDirectory + "/access.txt");
	File errorLog = new File(Configuration.logDirectory + "/errors.txt");

	BufferedWriter bw;
	PrintWriter pw;

	private int type;
	private InetAddress ipAddress;
	private Date date;
	private String page;

	/**
	 * @param statusCode 	e.g. 200
	 * @param inetAddress	e.g. /127.0.0.1
	 * @param date			date of access or error
	 * @param page			page accessed or trying to be accessed
	 */
	public Log(int statusCode, InetAddress inetAddress, Date date, String page) {
		this.setType(statusCode);
		this.setIpAddress(inetAddress);
		this.setDate(date);
		this.setPage(page);
	}

	/**
	 * Stores the log in the appropriate file
	 */
	public void store() {
		try {

			if (getType() == 200) {
				bw = new BufferedWriter(new FileWriter(accessLog, true));
			} else if (getType() == 404) {
				bw = new BufferedWriter(new FileWriter(errorLog, true));
			}

			pw = new PrintWriter(bw);

			pw.println("[" + getDate() + "] [" + getType() + "] [client" + getIpAddress() + "] " + getPage() + "\n");

			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return returns status code
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param i
	 */
	public void setType(int i) {
		this.type = i;
	}

	/**
	 * @return e.g. /127.0.0.1
	 */
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param inetAddress
	 */
	public void setIpAddress(InetAddress inetAddress) {
		this.ipAddress = inetAddress;
	}

	/**
	 * @return date of access or error
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return page trying to be accessed e.g. /index.html
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page
	 */
	public void setPage(String page) {
		this.page = page;
	}

}

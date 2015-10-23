import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author rbw3
 *
 */
public class Response {

	private double version;
	private int statusCode;
	private Date date;
	private long contentLength;
	private String contentType;
	private String uri;

	/**
	 * @param version 		HTTP version e.g 1.1
	 * @param contentType 	e.g. text/html
	 * @param uri			File requested
	 */
	public Response(double version, String contentType, String uri) {
		this.setVersion(version);
		// this.setStatusCode(statusCode);
		this.setDate(new Date());
		// this.setContentLength(contentLength);
		this.setContentType(contentType);
		this.uri = uri;
	}

	public String toString() {

		return "HTTP/" + getVersion() + " " + getStatusCode() + " " + getStatusCodeDescription() + "\n" + "Date: "
				+ getDateString() + "\n" + "Content-Length: " + getContentLength() + "\n" + "Content-Type: "
				+ getContentType() + "\r\n";
	}

	/**
	 * @return Gives status code description
	 */
	public String getStatusCodeDescription() {
		if (getStatusCode() == 200) {
			return "OK";
		} else {
			return "OK";
		}
	}

	/**
	 * @return HTTP version
	 */
	public double getVersion() {
		return version;
	}

	/**
	 * @param version
	 */
	public void setVersion(double version) {
		this.version = version;
	}

	/**
	 * @return status code e.g. 200
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return date of response
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return date in more readable format
	 */
	public String getDateString() {
		SimpleDateFormat ft = new SimpleDateFormat("E',' dd MMM yyyy HH:mm:ss zzz");
		return ft.format(date);
	}

	/**
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return length of file
	 */
	public long getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength
	 */
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @return e.g. text/html
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return file being asked for
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * @param uri
	 */
	public void setURI(String uri) {
		this.uri = uri;
	}

}


/**
 * @author rbw3
 *
 */
public class Request {

	private String method;
	private String uri;
	private double version;
	private String contentType;

	/**
	 * @param method	e.g. HTTP
	 * @param uri		e.g. /index.html
	 * @param version	e.g. 1.1
	 */
	public Request(String method, String uri, double version) {
		this.setMethod(method);
		this.setUri(uri);
		this.setVersion(version);
	}

	public String toString() {
		String s = "Request Made \nMethod: " + getMethod() + "\nURI: " + getURI() + "\nVersion: " + getVersion();
		return s;
	}

	/**
	 * @return	e.g. GET
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return e.g. /index.html
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * @param uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return e.g. 1.1
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

}

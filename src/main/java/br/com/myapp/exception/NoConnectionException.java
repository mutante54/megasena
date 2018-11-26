package  br.com.myapp.exception;

public class NoConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4240478898419304299L;

	public NoConnectionException() {
		// TODO Auto-generated constructor stub
	}

	public NoConnectionException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public NoConnectionException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public NoConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

}

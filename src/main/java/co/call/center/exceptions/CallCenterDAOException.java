package co.call.center.exceptions;

public class CallCenterDAOException extends Exception {

	private static final long serialVersionUID = 1L;

	public CallCenterDAOException() {
		super();
	}

	public CallCenterDAOException( String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace ) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

	public CallCenterDAOException( String message, Throwable cause ) {
		super( message, cause );
	}

	public CallCenterDAOException( String message ) {
		super( message );
	}

	public CallCenterDAOException( Throwable cause ) {
		super( cause );
	}

}

package co.call.center.exceptions;

public class CallCenterServiceException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CallCenterServiceException() {
		super();
	}

	public CallCenterServiceException( String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace ) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

	public CallCenterServiceException( String message, Throwable cause ) {
		super( message, cause );
	}

	public CallCenterServiceException( String message ) {
		super( message );
	}

	public CallCenterServiceException( Throwable cause ) {
		super( cause );
	}

}

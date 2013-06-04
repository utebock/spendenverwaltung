package at.fraubock.spendenverwaltung.interfaces.exceptions;

/**
 * Exception to throw if some input is invalid
 * 
 * @author manuel-bichler
 * 
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = -5618166433512708772L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

}

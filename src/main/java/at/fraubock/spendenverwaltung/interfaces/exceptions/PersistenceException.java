package at.fraubock.spendenverwaltung.interfaces.exceptions;

/**
 * exception thrown by the DAOs. occurs whenever writing to/reading from the
 * underlying persistence API fails.
 * 
 * @author philipp muhoray
 * @author manuel-bichler
 * 
 */
public class PersistenceException extends Exception {
	private static final long serialVersionUID = 6999117012991639225L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(Throwable cause) {
		super(cause);
	}

}

package at.fraubock.spendenverwaltung.interfaces.exceptions;

/**
 * exception thrown by the services. occurs whenever the service layer
 * encounters an error.
 * 
 * @author manuel-bichler
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String failureNotice) {
		super(failureNotice);
	}

	public ServiceException(Throwable e) {
		super(e);
	}

	public ServiceException(String failureNotice, Throwable e) {
		super(failureNotice, e);
	}
}

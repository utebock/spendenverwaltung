package at.fraubock.spendenverwaltung.interfaces.exceptions;

public class ServiceException extends Throwable {

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

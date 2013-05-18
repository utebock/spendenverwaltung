package exceptions;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceException(){
		super();
	}

	public ServiceException(String failureNotice){
		super(failureNotice);
	}

	public ServiceException(Exception e){
        super(e);
	}
}

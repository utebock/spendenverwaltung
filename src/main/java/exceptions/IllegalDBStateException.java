package exceptions;

/**
 * RuntimeException to throw in cases where the database state causing the exception
 * should not have existed in the first place
 */
public class IllegalDBStateException extends RuntimeException {
	
	public IllegalDBStateException(Throwable e) {
		super(e);
	}

	private static final long serialVersionUID = 1L;

}

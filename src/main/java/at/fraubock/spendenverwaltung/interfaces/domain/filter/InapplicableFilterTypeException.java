package at.fraubock.spendenverwaltung.interfaces.domain.filter;

/**
 * thrown whenever a {@link Criterion} is added to a {@link Filter} or another
 * criterion that is not of the same type
 * 
 * @author philipp muhoray
 * 
 */
public class InapplicableFilterTypeException extends RuntimeException {
	private static final long serialVersionUID = -8199287323050669950L;

	public InapplicableFilterTypeException(String msg) {
		super(msg);
	}

}

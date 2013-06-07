package at.fraubock.spendenverwaltung.interfaces.exceptions;

/**
 * thrown when a filter can not be deleted because it is used as a mounted
 * filter
 * 
 * @author philipp muhoray
 * 
 */
public class FilterInUseException extends Exception {
	private static final long serialVersionUID = -4401537403820048015L;

}

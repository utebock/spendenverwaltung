package at.fraubock.spendenverwaltung.gui;

public class InvalidInputException extends Exception {
	private static final long serialVersionUID = -3753778144833335928L;
	
	public InvalidInputException(String msg) {
		super(msg);
	}

}

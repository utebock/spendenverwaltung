package at.fraubock.spendenverwaltung.util;

public enum FilterProperty {
	/* properties of a person to be filtered */
	PERSON_GIVENNAME("givenname"),
	PERSON_EMAIL("email"),
	PERSON_COMPANY("company"),
	
	/* properties of a donation to be filtered */
	DONATION_AMOUNT("amount");
	
	private String type;
	
	private FilterProperty(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
}

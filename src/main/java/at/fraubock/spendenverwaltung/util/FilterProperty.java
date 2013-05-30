package at.fraubock.spendenverwaltung.util;

public enum FilterProperty {
	/* properties of a person to be filtered */
	PERSON_GIVENNAME("givenname"),
	PERSON_EMAIL("email"),
	PERSON_COMPANY("company"),
	
	/* properties of a donation to be filtered */
	DONATION_AMOUNT("amount"),
	
	/* properties of an address to be filtered */
	IS_MAIN_ADDRESS("ismain");
	
	private String type;
	
	private FilterProperty(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
	
	public static FilterProperty getPropertyForString(String value) {
		if("givenname".equals(value)) {
			return PERSON_GIVENNAME;
		} else if("email".equals(value)) {
			return PERSON_EMAIL;
		} else if("company".equals(value)) {
			return PERSON_COMPANY;
		}
		
		return null;
	}
}

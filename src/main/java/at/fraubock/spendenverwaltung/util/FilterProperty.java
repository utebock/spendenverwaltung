package at.fraubock.spendenverwaltung.util;

public enum FilterProperty {
	/* filter properties of a person */
	PERSON_GIVENNAME("givenname"),
	PERSON_EMAIL("email"),
	PERSON_COMPANY("company"),
	
	/* filter properties of a donation */
	DONATION_AMOUNT("amount"),
	DONATION_DATE("donationdate"),
	
	/* filter properties of a mailing */
	MAILING_DATE("date"),
	
	/* filter properties of an address */
	ADDRESS_IS_MAIN("isMain");
	
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

package at.fraubock.spendenverwaltung.util;

public enum FilterProperty {
	/* filter properties of a person */
	PERSON_GIVENNAME("givenname"),
	PERSON_SURNAME("surname"),
	PERSON_SEX("sex"),
	PERSON_EMAIL("email"),
	PERSON_COMPANY("company"),
	PERSON_WANTS_EMAIL("emailnotification"),
	PERSON_WANTS_MAIL("postalnotification"),
	//TODO search in note yes or no?
	
	/* filter properties of a donation */
	DONATION_AMOUNT("amount"),
	DONATION_DATE("donationdate"),
	DONATION_DEDICATION("dedication"),
	DONATION_TYPE("type"),
	//TODO search in note yes or no?
	
	/* filter properties of a mailing */
	MAILING_DATE("mailing_date"),
	MAILING_TYPE("mailing_type"),
	MAILING_MEDIUM("mailing_medium"),
	
	/* filter properties of an address */
	ADDRESS_IS_MAIN("ismain"),
	ADDRESS_STREET("street"),
	ADDRESS_POSTCODE("postcode"),
	ADDRESS_CITY("city"),
	ADDRESS_COUNTRY("country");
	
	private String type;
	
	private FilterProperty(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

	//TODO any properties missing?
	public static FilterProperty getPropertyForString(String value, FilterType filterType) {
		if(filterType.equals(FilterType.PERSON)) {
			if("givenname".equals(value)) {
				return PERSON_GIVENNAME;
			} else if("surname".equals(value)) {
				return PERSON_SURNAME;
			} else if("sex".equals(value)) {
				return PERSON_SEX;
			} else if("email".equals(value)) {
				return PERSON_EMAIL;
			} else if("company".equals(value)) {
				return PERSON_COMPANY;
			} else if("emailnotification".equals(value)) {
				return PERSON_WANTS_EMAIL;
			} else if("postalnotification".equals(value)) {
				return PERSON_WANTS_MAIL;
			} else if("amount".equals(value)) {
				return PERSON_WANTS_MAIL;
			}
		} else if(filterType.equals(FilterType.DONATION)) { 
			if("donationdate".equals(value)) {
				return DONATION_DATE;
			} else if("amount".equals(value)) {
				return DONATION_AMOUNT;
			} else if("dedication".equals(value)) {
				return DONATION_DEDICATION;
			} else if("type".equals(value)) {
				return DONATION_TYPE;
			} 
		} else if(filterType.equals(FilterType.MAILING)) {
			if("type".equals(value)) {
				return MAILING_TYPE;
			} else if("medium".equals(value)) {
				return MAILING_MEDIUM;
			} else if("mailingdate".equals(value)) {
				return MAILING_DATE;
			} 
		} else if(filterType.equals(FilterType.ADDRESS)) {
			if("ismain".equals(value)) {
				return ADDRESS_IS_MAIN;
			} else if ("street".equals(value)) {
				return ADDRESS_STREET;
			} else if ("postcode".equals(value)) {
				return ADDRESS_POSTCODE;
			} else if ("city".equals(value)) {
				return ADDRESS_CITY;
			} else if ("country".equals(value)) {
				return ADDRESS_COUNTRY;
			}   
		}
		
		return null;
	}
}

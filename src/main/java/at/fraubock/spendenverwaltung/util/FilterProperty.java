package at.fraubock.spendenverwaltung.util;

import org.apache.log4j.Logger;

public enum FilterProperty {

	/* filter properties of a person */
	PERSON_GIVENNAME("givenname"), PERSON_SURNAME("surname"), PERSON_SEX("sex"), PERSON_EMAIL(
			"email"), PERSON_COMPANY("company"), PERSON_WANTS_EMAIL(
			"emailnotification"), PERSON_WANTS_MAIL("postalnotification"), PERSON_TITLE(
			"title"), PERSON_NOTE("note"), PERSON_TELEPHONE("telephone"),

	/* filter properties of a donation */
	DONATION_AMOUNT("amount"), DONATION_DATE("donationdate"), DONATION_DEDICATION(
			"dedication"), DONATION_TYPE("type"), DONATION_NOTE("note"),

	/* filter properties of a mailing */
	MAILING_DATE("mailing_date"), MAILING_TYPE("mailing_type"), MAILING_MEDIUM(
			"mailing_medium"),

	/* filter properties of an address */
	ADDRESS_STREET("street"), ADDRESS_POSTCODE("postcode"), ADDRESS_CITY("city"),
	// isMain is not in address table, but will be used like that for the filter
	// data structure. will be resolved in the sql builder
	ADDRESS_IS_MAIN("ismain"), ADDRESS_COUNTRY("country");

	private static final Logger log = Logger.getLogger(FilterProperty.class);

	private String type;

	private FilterProperty(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

	// TODO any properties missing?
	public static FilterProperty getPropertyForString(String value,
			FilterType filterType) {
		if (filterType.equals(FilterType.PERSON)) {
			if ("givenname".equals(value)) {
				return PERSON_GIVENNAME;
			} else if ("surname".equals(value)) {
				return PERSON_SURNAME;
			} else if ("sex".equals(value)) {
				return PERSON_SEX;
			} else if ("email".equals(value)) {
				return PERSON_EMAIL;
			} else if ("company".equals(value)) {
				return PERSON_COMPANY;
			} else if ("emailnotification".equals(value)) {
				return PERSON_WANTS_EMAIL;
			} else if ("postalnotification".equals(value)) {
				return PERSON_WANTS_MAIL;
			} else if ("title".equals(value)) {
				return PERSON_TITLE;
			} else if ("note".equals(value)) {
				return PERSON_NOTE;
			} else if ("telephone".equals(value)) {
				return PERSON_TELEPHONE;
			}

		} else if (filterType.equals(FilterType.DONATION)) {
			if ("donationdate".equals(value)) {
				return DONATION_DATE;
			} else if ("amount".equals(value)) {
				return DONATION_AMOUNT;
			} else if ("dedication".equals(value)) {
				return DONATION_DEDICATION;
			} else if ("type".equals(value)) {
				return DONATION_TYPE;
			} else if ("note".equals(value)) {
				return DONATION_NOTE;
			}

		} else if (filterType.equals(FilterType.MAILING)) {
			if ("mailing_type".equals(value)) {
				return MAILING_TYPE;
			} else if ("mailing_medium".equals(value)) {
				return MAILING_MEDIUM;
			} else if ("mailing_date".equals(value)) {
				return MAILING_DATE;
			}

		} else if (filterType.equals(FilterType.ADDRESS)) {
			if ("ismain".equals(value)) {
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

		log.error("No FilterProperty enum found for string='" + value
				+ "' and filter type='" + filterType + "'");
		return null;
	}
}

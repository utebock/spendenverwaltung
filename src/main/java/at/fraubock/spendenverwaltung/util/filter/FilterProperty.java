package at.fraubock.spendenverwaltung.util.filter;

import org.apache.log4j.Logger;

/**
 * properties to be used for filtering.
 * 
 * @author philipp muhoray
 * 
 */
public enum FilterProperty {

	/* filter properties of a person */
	PERSON_GIVENNAME("givenname"), PERSON_SURNAME("surname"), PERSON_SEX("sex"), PERSON_EMAIL(
			"email"), PERSON_COMPANY("company"), PERSON_WANTS_EMAIL(
			"emailnotification"), PERSON_WANTS_MAIL("postalnotification"), PERSON_TITLE(
			"title"), PERSON_NOTE("note"), PERSON_TELEPHONE("telephone"),

	/* filter properties of a donation */
	DONATION_AMOUNT("amount"), DONATION_DATE("donationdate"), DONATION_DEDICATION(
			"dedication"), DONATION_TYPE("type"), DONATION_NOTE("note"), DONATION_IS_ANON("personid"),

	/* filter properties of a mailing */
	MAILING_DATE("mailing_date"), MAILING_TYPE("mailing_type"), MAILING_MEDIUM(
			"mailing_medium"),

	/* filter properties of an address */
	ADDRESS_STREET("street"), ADDRESS_POSTCODE("postcode"), ADDRESS_CITY("city"),

	// isMain is not in address table, but will be used like that for the filter
	// data structure. will be resolved in the SQL builder
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

	public static FilterProperty getPropertyForString(String value,
			FilterType filterType) {
		// put properties that exist twice here
		if("note".equals(value)) {
			if(filterType==FilterType.PERSON) {
				return PERSON_NOTE;
			} else if(filterType==FilterType.DONATION) {
				return DONATION_NOTE;
			}
		}
		
		FilterProperty result = null;
		for (FilterProperty prop : FilterProperty.values()) {
			if (prop.toString().equals(value)) {
				if (result != null) {
					log.error("There are unchecked FilterProperty enums with the same property-string. string: "
							+ value);
					throw new IllegalStateException(
							"There are unchecked FilterProperty enums with the same property-string. string: "
									+ value);
				}
				result = prop;
			}
		}
		if(result==null) {
			log.error("No FilterProperty enum found for string='" + value
					+ "' and filter type='" + filterType + "'");
		}
		return result;
	}
}

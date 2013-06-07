package at.fraubock.spendenverwaltung.util;

public enum FilterType {
	
	PERSON("persons","Personen"), 
	DONATION("donations","Spenden"), 
	MAILING("mailings","Aussendungen"), 
	ADDRESS("addresses","Adressen");
	
	private String type;
	private String display;
	
	private FilterType(String type,String display) {
		this.display = display;
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
	
	public String getDisplayName() {
		return display;
	}
	
	public static FilterType getTypeForString(String value) {
		if("persons".equals(value)) {
			return PERSON;
		} else if("donations".equals(value)) {
			return DONATION;
		} else if("mailings".equals(value)) {
			return MAILING;
		} else if("addresses".equals(value)) {
			return ADDRESS;
		}
		return null;
	}
}

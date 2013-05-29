package at.fraubock.spendenverwaltung.util;

public enum FilterType {
	
	PERSON("persons"), 
	DONATION("donations"), 
	MAILING("mailings"), 
	ADDRESS("addresses");
	
	private String type;
	
	private FilterType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
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

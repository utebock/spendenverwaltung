package at.fraubock.spendenverwaltung.util;

public enum FilterType {
	
	PERSON("validated_persons"), 
	DONATION("validated_donations"), 
	MAILING("mailings"), 
	ADDRESS("validated_addresses");
	
	private String type;
	
	private FilterType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
	
	public static FilterType getTypeForString(String value) {
		if("validated_persons".equals(value)) {
			return PERSON;
		} else if("validated_donations".equals(value)) {
			return DONATION;
		} else if("validated_mailings".equals(value)) {
			return MAILING;
		} else if("validated_addresses".equals(value)) {
			return ADDRESS;
		}
		return null;
	}
}

package at.fraubock.spendenverwaltung.util;

public enum FilterType {
	
	PERSON("validated_persons","Personen"), 
	DONATION("validated_donations","Spenden"), 
	MAILING("mailings","Aussendungen"), 
	ADDRESS("validated_addresses","Adressen");
	
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

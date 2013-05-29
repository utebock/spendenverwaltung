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
}

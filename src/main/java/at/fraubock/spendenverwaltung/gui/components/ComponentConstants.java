package at.fraubock.spendenverwaltung.gui.components;

public enum ComponentConstants {
	NUMERIC_TEXT(5), SHORT_TEXT(30), MEDIUM_TEXT(120), LONG_TEXT(1024);
	
	private final int value;
	
	ComponentConstants(int value) {
		this.value = value;
	}

	public int getValue(ComponentConstants constant) {
		return this.value;
	}
}

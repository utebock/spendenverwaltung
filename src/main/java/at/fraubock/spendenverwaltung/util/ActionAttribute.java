package at.fraubock.spendenverwaltung.util;

public enum ActionAttribute {
	ACTOR("actor", "Benutzer"), TIME("time", "Datum"), PAYLOAD("payload", "Daten");

	private String name, displayableName;

	private ActionAttribute(String name, String displayableName) {
		this.name = name;
		this.displayableName = displayableName;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return displayableName;
	}

	public ActionAttribute getByName(String name) {
		for (ActionAttribute aa : ActionAttribute.values()) {
			if (aa.getName().equals(name)) {
				return aa;
			}
		}
		return null;
	}

}

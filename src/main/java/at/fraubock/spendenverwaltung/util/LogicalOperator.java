package at.fraubock.spendenverwaltung.util;

public enum LogicalOperator {
	AND("und"), OR("oder"), AND_NOT("und nicht"),OR_NOT("oder nicht"), XOR("entweder - oder");
	
	private String text;

	private LogicalOperator(String text) {
		this.text = text;
	}

//	@Override
//	public String toString() {
//		return text;
//	}
}

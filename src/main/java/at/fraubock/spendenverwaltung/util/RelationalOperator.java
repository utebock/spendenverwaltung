package at.fraubock.spendenverwaltung.util;

public enum RelationalOperator {

	GREATER(">", "gr\u00F6\u00DFer als"), LESS("<", "kleiner als"), EQUALS("=",
			"gleich"), UNEQUAL("<>", "ungleich"), LIKE("LIKE",
			"\u00E4hnlich wie"), GREATER_EQ(">=",
			"gr\u00F6\u00DFer oder gleich als"), LESS_EQ("<=",
			"kleiner oder gleich als"), NOT_NULL("IS NOT NULL", "ist gesetzt"), IS_NULL(
			"IS NULL", "ist nicht gesetzt");

	private String expr;
	private String text;

	private RelationalOperator(String expr, String text) {
		this.expr = expr;
		this.text = text;
	}

	public String toExpression() {
		return expr;
	}

//	@Override
//	public String toString() {
//		return text;
//	}
}

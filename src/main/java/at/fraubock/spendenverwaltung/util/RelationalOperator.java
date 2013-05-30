package at.fraubock.spendenverwaltung.util;

public enum RelationalOperator {
	GREATER(">"), LESS("<"), EQUALS("="), LIKE("LIKE"), 
	GREATER_EQ(">="), LESS_EQ("<="), NOT_NULL("NOT NULL");
	
	private String math;
	
	private RelationalOperator(String math) {
		this.math = math;
	}
	
	public String getExpression() {
		return math;
	}
}

package at.fraubock.spendenverwaltung.util;

public enum RelationalOperator {
	GREATER(">"), LESS("<"), EQUALS("="), LIKE("LIKE"), 
	GREATER_EQ(">="), LESS_EQ("<="), NOT_NULL("NOT NULL"), IS_NULL("IS NULL");
	
	private String math;
	
	private RelationalOperator(String math) {
		this.math = math;
	}
	
	public String getExpression() {
		return math;
	}
}

package at.fraubock.spendenverwaltung.util;

public enum RelationalOperator {

	GREATER(">"), LESS("<"), EQUALS("="), UNEQUAL("<>"), LIKE("LIKE"), 
	GREATER_EQ(">="), LESS_EQ("<="), NOT_NULL("IS NOT NULL"), IS_NULL("IS NULL");
	
	private String math;
	
	private RelationalOperator(String math) {
		this.math = math;
	}
	
	public String getExpression() {
		return math;
	}
}

package at.fraubock.spendenverwaltung.util;

public enum RelationalOperator {
	GREATER(">"), LESS("<"), EQUALS("="), LIKE("~"), 
	GREATER_EQ(">="), LESS_EQ("<=");
	
	private String math;
	
	private RelationalOperator(String math) {
		this.math = math;
	}
	
	public String getMath() {
		return math;
	}
}

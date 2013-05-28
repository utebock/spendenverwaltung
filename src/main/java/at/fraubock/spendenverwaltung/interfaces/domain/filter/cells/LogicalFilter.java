package at.fraubock.spendenverwaltung.interfaces.domain.filter.cells;

import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class LogicalFilter extends FilterCell {
	
	private Integer id;
	private FilterCell operand1, operand2;
	private LogicalOperator logical_operator;

	@Override
	public String createSqlExpression() {
		return "(" + operand1.createSqlExpression() + " " + logical_operator
				+ " " + operand2.createSqlExpression() + ")";
	}

	public FilterCell getOperand1() {
		return operand1;
	}

	public void setOperand1(FilterCell operand1) {
		this.operand1 = operand1;
	}

	public FilterCell getOperand2() {
		return operand2;
	}

	public void setOperand2(FilterCell operand2) {
		this.operand2 = operand2;
	}

	public LogicalOperator getLogicalOperator() {
		return logical_operator;
	}

	public void setLogicalOperator(LogicalOperator operator) {
		this.logical_operator = operator;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

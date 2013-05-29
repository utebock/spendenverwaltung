package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class ConnectedCriterion extends Criterion {

	private Integer id;
	private Criterion operand1, operand2;
	private LogicalOperator logicalOperator;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Criterion getOperand1() {
		return operand1;
	}

	public void setOperand1(Criterion operand1) {
		this.operand1 = operand1;
	}

	public Criterion getOperand2() {
		return operand2;
	}

	public void setOperand2(Criterion operand2) {
		this.operand2 = operand2;
	}

	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(LogicalOperator logical_operator) {
		this.logicalOperator = logical_operator;
	}

}

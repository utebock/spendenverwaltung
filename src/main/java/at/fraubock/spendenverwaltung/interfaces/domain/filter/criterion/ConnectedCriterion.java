package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class ConnectedCriterion extends Criterion {
	
	private Integer id;
	private Criterion operand1, operand2;
	private LogicalOperator logical_operator;
	
	public ConnectedCriterion(FilterType type, LogicalOperator logical_operator, Criterion operand1) {
		this.type = type;
		this.operand1 = operand1;
		this.logical_operator = logical_operator;
	}

	public Criterion getOperand1() {
		return operand1;
	}

	public Criterion getOperand2() {
		return operand2;
	}

	public void setOperand2(Criterion operand2) {
		this.operand2 = operand2;
	}

	public LogicalOperator getLogicalOperator() {
		return logical_operator;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

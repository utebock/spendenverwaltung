package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class ConnectedCriterion extends Criterion {

	private Criterion operand1, operand2;
	private LogicalOperator logicalOperator;
	
	public void connect(Criterion operand1, LogicalOperator logicalOperator, Criterion operand2) {
		this.setOperand1(operand1);
		this.setLogicalOperator(logicalOperator);
		this.setOperand2(operand2);
	}

	public Criterion getOperand1() {
		return operand1;
	}

	public void setOperand1(Criterion operand1) {
		this.operand1 = operand1;
		operand1.setType(this.getType());
	}

	public Criterion getOperand2() {
		return operand2;
	}

	public void setOperand2(Criterion operand2) {
		this.operand2 = operand2;
		operand2.setType(this.getType());
	}

	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(LogicalOperator logical_operator) {
		this.logicalOperator = logical_operator;
	}
	
	@Override
	public void setType(FilterType type) {
		if(operand1!=null) {
			operand1.setType(type);
		}
		if(operand2!=null) {
			operand2.setType(type);
		}
		this.type = type;
	}

}

package at.fraubock.spendenverwaltung.service.to;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class ConnectedCriterionTO extends CriterionTO {

	private Criterion operand1, operand2;
	private LogicalOperator logicalOperator;

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

	public void setLogicalOperator(LogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	@Override
	public Criterion createCriterion() {
		// TODO Auto-generated method stub
		return null;
	}

}

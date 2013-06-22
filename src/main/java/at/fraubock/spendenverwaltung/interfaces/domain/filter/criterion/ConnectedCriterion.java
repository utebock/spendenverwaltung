package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.InapplicableFilterTypeException;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;

/**
 * this class represents a {@link Criterion} that evaluates an entity based on
 * the result of one or more {@link Criterion}s. the results will be connected
 * with a logical operation. conceptually, the operands' input will be the input
 * entity of this criterion.
 * 
 * @NOTE in order to fulfill the definition above, the operands have to be of
 *       the same {@link FilterType} as this criterion's type. if the filter
 *       type is not set when appending operands, it will be set to this filter
 *       type. if it is set and not of this filer type, an exception will be
 *       thrown.
 * 
 * @author philipp muhoray
 * 
 */
public class ConnectedCriterion extends Criterion {

	/*
	 * the operands to be evaluated. operand1 must not be null (NOT takes only
	 * one operand)
	 */
	private Criterion operand1, operand2;

	/* the logical operator of the evaluation */
	private LogicalOperator logicalOperator;

	/**
	 * use this method for defining the logical operation to be evaluation for
	 * two operands.
	 * 
	 * @param operand1
	 * @param logicalOperator
	 * @param operand2
	 * @throws {@link InapplicableFilterTypeException} - when the filter type of
	 *         one of the operands don't match with this one
	 */
	public void connect(Criterion operand1, LogicalOperator logicalOperator,
			Criterion operand2) throws InapplicableFilterTypeException {
		this.setOperand1(operand1);
		this.setLogicalOperator(logicalOperator);
		this.setOperand2(operand2);
	}

	/**
	 * use this method to negate an operand.
	 * 
	 * @NOTE this method does the same as
	 *       <code>#connect(operand, NOT, null)</code>
	 * 
	 * @param operand
	 */
	public void not(Criterion operand) {
		this.connect(operand, LogicalOperator.NOT, null);
	}

	public Criterion getOperand1() {
		return operand1;
	}

	/**
	 * sets the first operand and throws exception if the filter type is not the
	 * same as this.
	 * 
	 * @param operand1
	 * @throws InapplicableFilterTypeException
	 */
	public void setOperand1(Criterion operand1)
			throws InapplicableFilterTypeException {
		checkAndSetOperandType(operand1);
		this.operand1 = operand1;
	}

	public Criterion getOperand2() {
		return operand2;
	}

	/**
	 * sets the second operand and throws exception if the filter type is not
	 * the same as this.
	 * 
	 * @param operand2
	 * @throws InapplicableFilterTypeException
	 */
	public void setOperand2(Criterion operand2)
			throws InapplicableFilterTypeException {
		checkAndSetOperandType(operand2);
		this.operand2 = operand2;
	}

	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(LogicalOperator logical_operator) {
		this.logicalOperator = logical_operator;
	}

	/**
	 * sets this criterion's {@link FilterType} AND of all it's operands.
	 * 
	 * @NOTE the filter type of both operands will be overridden with the given
	 *       filter type!
	 */
	@Override
	public void setType(FilterType type) {
		if (operand1 != null) {
			operand1.setType(type);
		}
		if (operand2 != null) {
			operand2.setType(type);
		}
		this.type = type;
	}

	/*
	 * checks whether the operand's filter type is not of this instance's filter
	 * type (considering both are not null)
	 */
	private void checkAndSetOperandType(Criterion operand) {
		if (operand.getType() != null && this.type != null
				&& operand.getType() != this.getType()) {
			throw new InapplicableFilterTypeException(
					"The filter type of the operand is not applicable with this "
							+ "connected criterion's filter type. Operand was: class='"
							+ operand.getClass() + "', type='"
							+ operand.getType() + "', id='" + operand.getId()
							+ "'");
		}
		operand.setType(this.getType());
	}

}

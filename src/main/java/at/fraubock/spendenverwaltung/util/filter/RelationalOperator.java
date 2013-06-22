package at.fraubock.spendenverwaltung.util.filter;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;

/**
 * a relational operator used for the evaluate function in
 * {@link PropertyCriterion} and {@link MountedFilterCriterion}
 * 
 * @author philipp muhoray
 * 
 */
public enum RelationalOperator {

	GREATER(">"), LESS("<"), EQUALS("="), UNEQUAL("<>"), LIKE("LIKE"), GREATER_EQ(
			">="), LESS_EQ("<="), NOT_NULL("IS NOT NULL"), IS_NULL("IS NULL");

	private String expr;

	private RelationalOperator(String expr) {
		this.expr = expr;
	}

	public String toExpression() {
		return expr;
	}
}

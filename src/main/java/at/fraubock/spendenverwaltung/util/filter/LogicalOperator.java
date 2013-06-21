package at.fraubock.spendenverwaltung.util.filter;

/**
 * a logical operator used for the evaluate function in
 * {@link ConnectedCriterion}
 * 
 * @author philipp muhoray
 * 
 */
public enum LogicalOperator {
	AND, OR, AND_NOT, OR_NOT, XOR, NOT;
}

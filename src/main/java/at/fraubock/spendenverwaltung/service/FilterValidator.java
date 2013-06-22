package at.fraubock.spendenverwaltung.service;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;

/**
 * validates a {@link Filter} instance
 * 
 * @author philipp muhoray
 * 
 */
public class FilterValidator {

	private static final Logger log = Logger.getLogger(FilterValidator.class);

	public void validate(Filter filter) {

		if (filter == null) {
			log.error("Argument was null");
			throw new IllegalArgumentException("Filter must not be null");
		}

		if (filter.getType() == null) {
			log.error("Type was null");
			throw new IllegalArgumentException("Type must not be null");
		}

		if (filter.getCriterion() != null
				&& filter.getCriterion().getType() != filter.getType()) {
			log.error("Criterion had a different type than filter");
			throw new IllegalArgumentException(
					"Criterion must have same type as filter");
		}

	}

	public void validate(ConnectedCriterion criterion) {

		if (criterion == null) {
			log.error("Argument was null");
			throw new IllegalArgumentException("Criterion must not be null");
		}

		if (criterion.getType() == null) {
			log.error("Type was null");
			throw new IllegalArgumentException("Type must not be null");
		}

		if (criterion.getLogicalOperator() == null) {
			log.error("LogicalOperator was null");
			throw new IllegalArgumentException(
					"LogicalOperator must not be null");
		}

		if (criterion.getOperand1() == null) {
			log.error("Operand 1 was null");
			throw new IllegalArgumentException("Operand1 must not be null");
		}

		if (criterion.getOperand1().getType() != criterion.getType()) {
			log.error("Operand1 had a different type than this criterion");
			throw new IllegalArgumentException(
					"Operand1 must have same type as this criterion");
		}

		if (criterion.getOperand2() != null
				&& criterion.getOperand2().getType() != criterion.getType()) {
			log.error("Operand2 had a different type than this criterion");
			throw new IllegalArgumentException(
					"Operand2 must have same type as this criterion");
		}
	}

	public void validate(PropertyCriterion criterion) {

		if (criterion == null) {
			log.error("Argument was null");
			throw new IllegalArgumentException("Criterion must not be null");
		}

		if (criterion.getType() == null) {
			log.error("Type was null");
			throw new IllegalArgumentException("Type must not be null");
		}

		if (criterion.getRelationalOperator() == null) {
			log.error("RelationalOperator was null");
			throw new IllegalArgumentException(
					"RelationalOperator must not be null");
		}

		if (criterion.getProperty() == null) {
			log.error("Property was null");
			throw new IllegalArgumentException("Property must not be null");
		}
	}

	public void validate(MountedFilterCriterion criterion) {

		if (criterion == null) {
			log.error("Argument was null");
			throw new IllegalArgumentException("Criterion must not be null");
		}

		if (criterion.getType() == null) {
			log.error("Type was null");
			throw new IllegalArgumentException("Type must not be null");
		}

		if (criterion.getRelationalOperator() == null) {
			log.error("RelationalOperator was null");
			throw new IllegalArgumentException(
					"RelationalOperator must not be null");
		}

		if (criterion.getCount() != null
				&& (criterion.getProperty() != null
						|| criterion.getSum() != null || criterion.getAvg() != null)) {
			log.error("Count value was set for mounted filter but also other values.");
			throw new IllegalArgumentException(
					"Either the count value or property and (avg or sum) value"
							+ " must be set for a MountedFilterCriterion");
		}

		if (criterion.getProperty() != null
				&& (criterion.getCount() != null || !(criterion.getAvg() != null ^ criterion
						.getSum() != null))) {
			log.error("Property was set for mounted filter but also either count value or both avg and sum value.");
			throw new IllegalArgumentException(
					"Either the count value or property and [avg or sum] value"
							+ " must be set for a MountedFilterCriterion");
		}
	}
}

package at.fraubock.spendenverwaltung.service;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;

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
		
		if (filter.getCriterion().getType() != filter.getType()) {
			log.error("Criterion had a different type than filter");
			throw new IllegalArgumentException("Criterion must have same type as filter");
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
		
		if(criterion.getLogicalOperator() == null) {
			log.error("LogicalOperator was null");
			throw new IllegalArgumentException("LogicalOperator must not be null");
		}
		
		if(criterion.getOperand1() == null) {
			log.error("Operand 1 was null");
			throw new IllegalArgumentException("Operand1 must not be null");
		}
		
		if(criterion.getOperand1().getType() != criterion.getType()) {
			log.error("Operand1 had a different type than this criterion");
			throw new IllegalArgumentException("Operand1 must have same type as this criterion");
		}
		
		if(criterion.getOperand2().getType() != criterion.getType()) {
			log.error("Operand2 had a different type than this criterion");
			throw new IllegalArgumentException("Operand2 must have same type as this criterion");
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

		if (!(criterion.getNumValue() != null ^ criterion.getStrValue() != null
				^ criterion.getDateValue() != null
				^ criterion.getDaysBack() != null
				^ criterion.getBoolValue() != null)) {
			log.error("More or less than one value was set for this PropertyCriterion");
			throw new IllegalArgumentException(
					"Exactly one value must be set for a PropertyCriterion");
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

		if (!((criterion.getCount() != null && criterion.getProperty() == null
				&& criterion.getSum() == null && criterion.getAvg() == null) ^ (criterion
				.getProperty() != null && criterion.getCount() == null && (criterion
				.getSum() != null ^ criterion.getAvg() != null)))) {
			log.error("The combination of set value for this MountedFilterCriterion was invalid");
			throw new IllegalArgumentException(
					"Either the count value or property and (avg or sum) value" +
					" must be set for a MountedFilterCriterion");
		}
	}
}

package at.fraubock.spendenverwaltung.util;

import java.text.SimpleDateFormat;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.service.FilterValidator;

/**
 * creates the SQL statement for a {@link Filter}. executing this statement
 * will return all entries from the filter type's table that fulfill the
 * criterias of the {@link Criterion} provided by the filter.
 * 
 * @author philipp muhoray
 * 
 */
public class FilterToSqlBuilder {

	private FilterValidator validator;

	/**
	 * creates the SQL statement to this filter.
	 * @param filter
	 * @return sql statement
	 */
	public String createSqlStatement(Filter filter) {
		validator.validate(filter);
		
		String stmt = "select * from " + filter.getType() + " as mount0";
		
		if (filter.getCriterion() != null) {
			stmt += " where "
					+ createConditionalStatement(filter.getCriterion(), 1);
		}

		return stmt;
	}

	private String createConditionalStatement(Criterion criterion,
			int mountLevel) {
		// determine the criterion to build the statement
		if (criterion instanceof ConnectedCriterion) {
			return createConnectedCritSqlStmt((ConnectedCriterion) criterion,
					mountLevel);
		} else if (criterion instanceof PropertyCriterion) {
			return createPropertyCritSqlStmt((PropertyCriterion) criterion);
		} else if (criterion instanceof MountedFilterCriterion) {
			return createMountedCritSqlStmt((MountedFilterCriterion) criterion,
					mountLevel);

		}

		return null;
	}

	private String createPropertyCritSqlStmt(PropertyCriterion prop) {
		validator.validate(prop);
		RelationalOperator operator = prop.getRelationalOperator();

		String stmt = prop.getProperty() + " " + operator.getExpression() + " ";

		if (operator == RelationalOperator.IS_NULL
				|| operator == RelationalOperator.NOT_NULL) {
			// no comparison with any value needed
			return stmt;
		}

		// determine the value the property will be compared with
		if (prop.getNumValue() != null) {
			stmt += prop.getNumValue();
		} else if (prop.getStrValue() != null) {
			stmt += "'" + prop.getStrValue() + "'";
		} else if (prop.getDateValue() != null) {
			stmt += "DATE('"
					+ new SimpleDateFormat("yyyy-MM-dd").format(prop
							.getDateValue()) + "')";
		} else if (prop.getBoolValue() != null) {
			stmt += prop.getBoolValue();
		} else if (prop.getDaysBack() != null) {
			stmt += "DATE_SUB(DATE(NOW()),INTERVAL " + prop.getDaysBack()
					+ " DAY)";
		} else {
			throw new IllegalArgumentException(
					"No value for PropertyCriterion provided. Type='"
							+ prop.getType() + "', property='"
							+ prop.getProperty() + "'");
		}

		return stmt;
	}

	private String createConnectedCritSqlStmt(ConnectedCriterion con,
			int mountLevel) {
		validator.validate(con);
		LogicalOperator op = con.getLogicalOperator();

		if (op == LogicalOperator.AND_NOT || op == LogicalOperator.OR_NOT) {
			// operator involves NOT, therefore some modifications are needed
			return "("
					+ createConditionalStatement(con.getOperand1(), mountLevel)
					+ " " + (op == LogicalOperator.AND_NOT ? "AND" : "OR")
					+ " NOT ("
					+ createConditionalStatement(con.getOperand2(), mountLevel)
					+ "))";
		}

		return "(" + createConditionalStatement(con.getOperand1(), mountLevel)
				+ " " + op + " "
				+ createConditionalStatement(con.getOperand2(), mountLevel)
				+ ")";
	}

	private String createMountedCritSqlStmt(MountedFilterCriterion criterion,
			int mountLevel) {
		validator.validate(criterion);
		Filter filter = criterion.getMount();

		String select = "(select ";
		// will be needed at the end, but easier to set it know
		String constraint = criterion.getRelationalOperator().getExpression();

		// set wanted result for comparison
		if (criterion.getCount() != null) {
			select += "count(*) from ";
			constraint += " " + criterion.getCount();
		} else if (criterion.getProperty() != null) {
			if (criterion.getSum() != null) {
				select += "sum(" + criterion.getProperty() + ") from ";
				constraint += criterion.getSum();
			} else if (criterion.getAvg() != null) {
				select += "avg(" + criterion.getProperty() + ") from ";
				constraint += criterion.getAvg();
			}
		}

		// set the filter type (table) and give it the name mountX, where X is
		// the mount level. this number is needed to distinct between all the
		// several sub selects one query might have.
		String statement = select + filter.getType() + " as mount" + mountLevel;

		/*
		 * we now have to make sure that only entries related to the current
		 * entry from the top select will be considered in this sub select.
		 * therefore, we'll have to add a constraint to the where-part of the
		 * sub select, telling it which entries to consider exclusively
		 * (regardless of all other clauses in the where-part). this will be
		 * determined by the combination of the types of both selects. some
		 * combinations are easy to handle (like same types), some will need
		 * special handling (mostly address and person).
		 */

		FilterType mountedType = criterion.getMount().getType();
		FilterType thisType = criterion.getType();
		// will need special treatment
		FilterType personType = FilterType.PERSON;
		FilterType addressType = FilterType.ADDRESS;

		if (thisType == mountedType) {
			// types are the same. only consider entries with same id as top select
			statement += " where mount" + (mountLevel) + ".id=mount"
					+ (mountLevel - 1) + ".id";

		}
		
		else if (thisType != personType && thisType != addressType) {
			// this type is donation or mailing
			if (mountedType != personType) {
				// they can only be combined with same type or person
				throw new IllegalArgumentException(
						"Mounting failed due to invalid combination of types. This type='"
								+ thisType + "', mounted type='" + mountedType
								+ "'");
			}

			// at this point we'll have to mount person to donation or
			// mailing. therefore only select persons with the same id as the
			// donation's/mailing's personid
			statement += " where mount" + (mountLevel) + ".id=mount"
					+ (mountLevel - 1) + ".personid";
		}

		else if (thisType == personType) {
			if (mountedType == addressType) {
				// person mounts address. therefore, we have to join addresses
				// with livesAt on id=aid.
				// from this join we have to keep only the addresses where pid
				// is the id of the top select's id
				// (since top select is type person)
				statement = statement.replaceAll("as mount.+",
						"join livesat on id=aid where pid=mount0.id");
			} else {
				// donation or mailing. only consider those where their personid
				// is the same as the top selects' id
				statement += " where mount" + (mountLevel - 1) + ".id=mount"
						+ (mountLevel) + ".personid";
			}
		}

		else if (thisType == addressType) {
			if (mountedType != personType) {
				// address can only mount person
				throw new IllegalArgumentException(
						"Mounting failed due to invalid combination of types. This type='"
								+ thisType + "', mounted type='" + mountedType
								+ "'");
			}
			// same way as person mounts address, only with switched ids.
			// TODO: persons join livesat on id=pid where aid=mount0.id
		}
		
		/* all necessary default constraints are set now */

		// add the mounted filters criterions to the where clause
		if (filter.getCriterion() != null) {
			statement += " and "
					+ createConditionalStatement(filter.getCriterion(),
							mountLevel + 1);
		}

		// add the constraint to the result set of this sub select
		statement += ") " + constraint;

		return statement;
	}

	public FilterValidator getValidator() {
		return validator;
	}

	public void setValidator(FilterValidator validator) {
		this.validator = validator;
	}

}

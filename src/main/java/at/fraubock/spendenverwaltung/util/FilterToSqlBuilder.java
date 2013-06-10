package at.fraubock.spendenverwaltung.util;

import java.text.SimpleDateFormat;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.service.FilterValidator;

/**
 * interprets a given {@link Filter} to a corresponding SQL statements.
 * executing this statement will return all entries from the filter type's table
 * matching the conditions of the filter's {@link Criterion}.
 * 
 * @author philipp muhoray
 * 
 */
public class FilterToSqlBuilder {

	private FilterValidator validator;

	/**
	 * creates the SQL statement for the given filter
	 * 
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

		String stmt = prop.getProperty() + " " + operator.toExpression() + " ";

		if (operator == RelationalOperator.IS_NULL
				|| operator == RelationalOperator.NOT_NULL) {
			// no comparison with any value needed
			return stmt;
		}

		// determine the value the property will be compared to
		if (prop.getNumValue() != null) {
			stmt += escapeSQL(""+prop.getNumValue());
		} else if (prop.getStrValue() != null) {
			String percent = prop.getRelationalOperator() == RelationalOperator.LIKE ? "%"
					: "";
			stmt += "'" + percent + escapeSQL(prop.getStrValue()) + percent + "'";
		} else if (prop.getDateValue() != null) {
			stmt += "DATE('"
					+ new SimpleDateFormat("yyyy-MM-dd").format(prop
							.getDateValue()) + "')";
		} else if (prop.getBoolValue() != null) {
			stmt += prop.getBoolValue();
		} else if (prop.getDaysBack() != null) {
			// switch relational operator to fulfill the 'days back' condition
			if (operator == RelationalOperator.GREATER) {
				operator = RelationalOperator.LESS;
			} else if (operator == RelationalOperator.GREATER_EQ) {
				operator = RelationalOperator.LESS_EQ;
			} else if (operator == RelationalOperator.LESS_EQ) {
				operator = RelationalOperator.GREATER_EQ;
			} else if (operator == RelationalOperator.LESS) {
				operator = RelationalOperator.GREATER;
			}
			stmt = prop.getProperty() + " " + operator.toExpression()
					+ " DATE_SUB(DATE(NOW()),INTERVAL " + escapeSQL(""+prop.getDaysBack())
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
		String constraint = criterion.getRelationalOperator().toExpression();

		// set wanted result for comparison
		if (criterion.getCount() != null) {
			select += "count(*) from ";
			constraint += " " + escapeSQL(""+criterion.getCount());
		} else if (criterion.getProperty() != null) {
			if (criterion.getSum() != null) {
				select += "sum(" + criterion.getProperty() + ") from ";
				constraint += escapeSQL(""+criterion.getSum());
			} else if (criterion.getAvg() != null) {
				select += "avg(" + criterion.getProperty() + ") from ";
				constraint += escapeSQL(""+criterion.getAvg());
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
		 * (regardless of all other constraints in the where-part). this will be
		 * determined by the combination of the types of both selects. some
		 * combinations are easy to handle (like same types), some will need
		 * special handling (like address and person).
		 */

		FilterType mountedType = criterion.getMount().getType();
		FilterType thisType = criterion.getType();

		if (thisType == mountedType) {
			statement = mountSameTypes(mountLevel, statement);

		} else if (thisType == FilterType.PERSON) {
			
			if (mountedType == FilterType.DONATION) {
				statement = mountDonationToPerson(mountLevel, statement);
			} else if (mountedType == FilterType.MAILING) {
				statement = mountMailingToPerson(mountLevel, statement);
			} else if (mountedType == FilterType.ADDRESS) {
				statement = mountAddressToPerson(mountLevel, statement);
			}
			
		} else if (thisType == FilterType.DONATION) {
			
			if (mountedType == FilterType.PERSON) {
				statement = mountPersonToDonation(mountLevel, statement);
			} else {
				illegalMounting(thisType, mountedType);
			}
		}

		else if (thisType == FilterType.MAILING) {
			
			if (mountedType == FilterType.PERSON) {
				statement = mountPersonToMailing(mountLevel, statement);
			} else {
				illegalMounting(thisType, mountedType);
			}
		}

		/*
		 * NOTE that the GUI doesn't provide mounting into an address filter.
		 * therefore there is no handling for that case
		 */

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

	private String mountSameTypes(int mountLevel, String statement) {
		// types are the same. only consider entries with same id as top
		// select
		return statement += " where mount" + (mountLevel) + ".id=mount" + (mountLevel - 1)
				+ ".id";
	}

	private String mountDonationToPerson(int mountLevel, String statement) {
		// donation. only consider those where their personid
		// is equal to the top selects' id
		return statement += " where mount" + (mountLevel - 1) + ".id=mount" + (mountLevel)
				+ ".personid";
	}

	private String mountMailingToPerson(int mountLevel, String statement) {
		// person mounts mailing. therefore, we have to join addresses
		// with sent_mailings on id=mailing_id.
		// from this join we have to keep only the mailings where person_id
		// is the id of the top select's id
		// (since top select is type person)
		return statement +=
				" join sent_mailings as sent on mount"+mountLevel+".id=sent.mailing_id where sent.person_id=mount"
						+ (mountLevel-1) + ".id";
	}

	private String mountAddressToPerson(int mountLevel, String statement) {
		// person mounts address. therefore, we have to join addresses
		// with livesAt on id=aid.
		// from this join we have to keep only the addresses where pid
		// is the id of the top select's id
		// (since top select is type person)
		return statement += " join livesat on mount"+mountLevel+".id=aid where pid=mount" + (mountLevel-1) + ".id";
	}

	private String mountPersonToDonation(int mountLevel, String statement) {
		// at this point we'll have to mount person to donation.
		// therefore only select persons with the same id as the
		// donation's personid
		return statement += " where mount" + (mountLevel) + ".id=mount"
				+ (mountLevel - 1) + ".personid";
	}

	private String mountPersonToMailing(int mountLevel, String statement) {
		// see mountMailingToPerson (just with switched ids)
		return statement +=	" join sent_mailings as sent on mount"+mountLevel+".id=sent.person_id where sent.mailing_id=mount"
						+ (mountLevel-1) + ".id";
	}
	
	private void illegalMounting(FilterType thisType, FilterType mountedType) {
		throw new IllegalArgumentException(
				"Mounting failed due to invalid combination of types. This type='"
						+ thisType + "', mounted type='" + mountedType
						+ "'");
	}

	public FilterValidator getValidator() {
		return validator;
	}

	public void setValidator(FilterValidator validator) {
		this.validator = validator;
	}
	
	public static String escapeSQL(String value) {
		String escaped = value;
		escaped = value.replace("\\","\\\\"); // replace \ with \\
		escaped = escaped.replace("'","\\'"); // replace ' with \'
		return escaped;
	}

}

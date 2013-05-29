package at.fraubock.spendenverwaltung.util;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;

public class FilterToSqlBuilder {

	public String createSqlStatement(Filter filter) {
		String stmt = "select * from " + filter.getType()
				+ " as "+filter.getType()+" where " + createConditionalStatement(filter.getCriterion());

		return stmt;
	}

	private String createConditionalStatement(Criterion criterion) {

		if (criterion instanceof ConnectedCriterion) {
			ConnectedCriterion log = (ConnectedCriterion) criterion;
			return "(" + createConditionalStatement(log.getOperand1()) + " "
					+ log.getLogicalOperator() + " "
					+ createConditionalStatement(log.getOperand2()) + ")";

		}
		
		if (criterion instanceof PropertyCriterion) {
			PropertyCriterion prop = (PropertyCriterion) criterion;
			String expr = prop.getType() + "." + prop.getProperty()
					+ " " + prop.getRelationalOperator().getMath();

			if (prop.getNumValue() != null) {
				expr += prop.getNumValue();
			} else if (prop.getStrValue() != null) {
				expr += "'" + prop.getStrValue() + "'";
			} else if (prop.getDateValue() != null) {
				expr += "'" + prop.getDateValue() + "'";
			} else if (prop.getBoolValue() != null) {
				expr += prop.getBoolValue();
			} else if (prop.getDaysBack() != null) {
				expr += prop.getDaysBack();
			}

			return expr;
		}
		
		if (criterion instanceof MountedFilterCriterion) {
			MountedFilterCriterion mounted = (MountedFilterCriterion) criterion;
			
			String statement = "("+createSqlStatement(mounted.getMount());
			
			FilterType mountedType = mounted.getMount().getType();
			FilterType thisType = mounted.getType();
			if(mounted.getType()==mountedType) {
				statement += " and "+mountedType+".id="+thisType+".id)";
			} else if(thisType==FilterType.PERSON) {
				statement += " and "+thisType+".id="+mountedType+".personid)";
			} else {
				statement += " and "+mountedType+".id="+thisType+".personid)";
			}
			
			statement+=mounted.getRelationalOperator().getMath();
			
			if(mounted.getCount()!=null) {
				statement = statement.replace("select * from", "select count(*) from");
				statement += mounted.getCount();
			} else if(mounted.getProperty()!=null) {
				if(mounted.getSum()!=null) {
					statement = statement.replace("select * from", "select sum("+mounted.getProperty()+") from");
					statement += mounted.getSum();
				} else if(mounted.getAvg()!=null) {
					statement = statement.replace("select * from", "select avg("+mounted.getProperty()+") from");
					statement += mounted.getAvg();
				}
			}
			return statement;
		}

		return null;
	}

}

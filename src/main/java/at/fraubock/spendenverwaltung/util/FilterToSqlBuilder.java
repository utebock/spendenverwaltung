package at.fraubock.spendenverwaltung.util;

import java.text.SimpleDateFormat;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.service.FilterValidator;

public class FilterToSqlBuilder {
	
	private FilterValidator validator;

	public String createSqlStatement(Filter filter) {
		validator.validate(filter);
		
		String stmt = "select * from " + filter.getType();
		if(filter.getCriterion()!=null) {
			stmt += " where " + createConditionalStatement(filter.getCriterion());
		}
				
		System.out.println(stmt);
		return stmt;
	}

	private String createConditionalStatement(Criterion criterion) {
		
		if (criterion instanceof ConnectedCriterion) {
			ConnectedCriterion con = (ConnectedCriterion) criterion;
			validator.validate(con);
			return "(" + createConditionalStatement(con.getOperand1()) + " "
					+ con.getLogicalOperator() + " "
					+ createConditionalStatement(con.getOperand2()) + ")";

		}
		
		if (criterion instanceof PropertyCriterion) {
			PropertyCriterion prop = (PropertyCriterion) criterion;
			validator.validate(prop);
			String expr = prop.getProperty()
					+ " " + prop.getRelationalOperator().getExpression();

			if (prop.getNumValue() != null) {
				expr += prop.getNumValue();
			} else if (prop.getStrValue() != null) {
				expr += "'" + prop.getStrValue() + "'";
			} else if (prop.getDateValue() != null) {
				expr += "DATE('" + new SimpleDateFormat("yyyy-MM-dd").format(prop.getDateValue()) + "')";
			} else if (prop.getBoolValue() != null) {
				expr += prop.getBoolValue();
			} else if (prop.getDaysBack() != null) {
				expr += "DATE_SUB(DATE(NOW()),INTERVAL "+prop.getDaysBack()+" DAY)";
			}

			return expr;
		}
		
		if (criterion instanceof MountedFilterCriterion) {
			MountedFilterCriterion mounted = (MountedFilterCriterion) criterion;
			validator.validate(mounted);
			String statement = "("+createSqlStatement(mounted.getMount());
			if(statement.contains("where")) {
				statement = statement.replace("where","as mount where");
			} else {
				statement += " as mount";
			}
			FilterType mountedType = mounted.getMount().getType();
			FilterType thisType = mounted.getType();
			if(mounted.getType()==mountedType) {
				statement += " and mount.id=id)";
			} else if(thisType==FilterType.PERSON) {
				statement += " and id=mount.personid)";
			} else {
				statement += " and mount.id=personid)";
			} //TODO address
			
			statement+=mounted.getRelationalOperator().getExpression();
			
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

	public FilterValidator getValidator() {
		return validator;
	}

	public void setValidator(FilterValidator validator) {
		this.validator = validator;
	}

}

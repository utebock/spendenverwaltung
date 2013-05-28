package at.fraubock.spendenverwaltung.interfaces.domain.filter.cells;

import java.sql.Date;

import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class PropertyFilter extends FilterCell {
	
	private Integer id;
	private String type;
	private String property;
	private RelationalOperator relationalOperator;
	private Double numValue;
	private String strValue;
	private Date dateValue;
	private Boolean boolValue;
	private Integer daysBack;
	
	@Override
	public String createSqlExpression() {
		String expr = type+"."+property+relationalOperator.getMath();
		
		if(numValue!=null) {
			expr+=getNumValue();
		} else if(strValue!=null) {
			expr+="'"+strValue+"'";
		} else if(dateValue!=null) {
			expr+="'"+dateValue+"'";
		} else if(boolValue!=null) {
			expr+=boolValue;
		} else if(daysBack!=null) {
			expr+=daysBack;
		}
		
		return expr;
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public RelationalOperator getRelationalOperator() {
		return relationalOperator;
	}
	public void setRelationalOperator(RelationalOperator relationalOperator) {
		this.relationalOperator = relationalOperator;
	}
	public Double getNumValue() {
		return numValue;
	}
	public void setNumValue(Double numValue) {
		this.numValue = numValue;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
	public Boolean getBoolValue() {
		return boolValue;
	}
	public void setBoolValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}
	public int getDaysBack() {
		return daysBack;
	}
	public void setDaysBack(int daysBack) {
		this.daysBack = daysBack;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

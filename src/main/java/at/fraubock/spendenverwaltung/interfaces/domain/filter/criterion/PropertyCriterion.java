package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import java.sql.Date;

import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class PropertyCriterion extends Criterion {
	
	private Integer id;
	private FilterProperty property;
	private RelationalOperator relationalOperator;
	private Double numValue;
	private String strValue;
	private Date dateValue;
	private Boolean boolValue;
	private Integer daysBack;
	
	public PropertyCriterion(FilterType type, FilterProperty property, RelationalOperator relationalOperator) {
		this.type = type;
		this.property = property;
		this.relationalOperator = relationalOperator;
	}

	public FilterProperty getProperty() {
		return property;
	}
	
	public RelationalOperator getRelationalOperator() {
		return relationalOperator;
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
	public Integer getDaysBack() {
		return daysBack;
	}
	public void setDaysBack(int daysBack) {
		this.daysBack = daysBack;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import java.util.Date;

import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class PropertyCriterion extends Criterion {

	private FilterProperty property;
	private RelationalOperator relationalOperator;
	private Double numValue;
	private String strValue;
	private Date dateValue;
	private Boolean boolValue;
	private Integer daysBack;
	
	public void compareNotNull(FilterProperty property) {
		this.setProperty(property);
		this.setRelationalOperator(RelationalOperator.NOT_NULL);
	}
	
	public void compareIsNull(FilterProperty property) {
		this.setProperty(property);
		this.setRelationalOperator(RelationalOperator.IS_NULL);
	}
	
	public void compare(FilterProperty property, RelationalOperator relationalOperator, Double numValue) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setNumValue(numValue);
	}
	
	public void compare(FilterProperty property, RelationalOperator relationalOperator, String strValue) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setStrValue(strValue);
	}
	
	public void compare(FilterProperty property, RelationalOperator relationalOperator, Date dateValue) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setDateValue(dateValue);
	}
	
	public void compare(FilterProperty property, boolean boolValue) {
		this.setProperty(property);
		this.setRelationalOperator(RelationalOperator.EQUALS);
		this.setBoolValue(boolValue);
	}
	
	public void compareDaysBack(FilterProperty property, RelationalOperator relationalOperator, int daysBack) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setDaysBack(daysBack);
	}

	public FilterProperty getProperty() {
		return property;
	}

	public void setProperty(FilterProperty property) {
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

	public Integer getDaysBack() {
		return daysBack;
	}

	public void setDaysBack(Integer daysBack) {
		this.daysBack = daysBack;
	}

}

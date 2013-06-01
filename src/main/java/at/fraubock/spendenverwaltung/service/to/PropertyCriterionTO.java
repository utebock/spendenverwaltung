package at.fraubock.spendenverwaltung.service.to;

import java.util.Date;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class PropertyCriterionTO extends CriterionTO {

	private FilterProperty property;
	private RelationalOperator relationalOperator;
	private Double numValue;
	private String strValue;
	private Date dateValue;
	private Boolean boolValue;
	private Integer daysBack;
	
	@Override
	public PropertyCriterion createCriterion() {
		PropertyCriterion prop = new PropertyCriterion();
		prop.setType(type);
		prop.setProperty(property);
		prop.setRelationalOperator(relationalOperator);
		prop.setBoolValue(boolValue);
		prop.setDateValue(dateValue);
		prop.setDaysBack(daysBack);
		prop.setNumValue(numValue);
		prop.setStrValue(strValue);
		return prop;
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

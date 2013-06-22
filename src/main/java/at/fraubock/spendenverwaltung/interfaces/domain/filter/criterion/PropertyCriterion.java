package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import java.util.Date;

import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

/**
 * this class represents a {@link Criterion} that evaluates an entity based on
 * the value of one of it's properties. it compares a property defined by
 * {@link FilterProperty} with a given value that is set in one of the
 * <code>xyzValue</code> variables. the operator of the comparison is given as a
 * {@link RelationalOperator}.
 * 
 * @author philipp muhoray
 * 
 */
public class PropertyCriterion extends Criterion {

	private FilterProperty property;
	private RelationalOperator relationalOperator;
	private Double numValue;
	private String strValue;
	private Date dateValue;
	private Boolean boolValue;
	private Integer daysBack;

	/**
	 * compares the given {@link FilterProperty} with the given {@link Double}
	 * value based on the given {@link RelationalOperator}. the given
	 * {@link FilterProperty} must correspond to this criterion's
	 * {@link FilterType} AND ALSO be of a numeric data type.
	 * 
	 * @param property
	 * @param relationalOperator
	 * @param numValue
	 */
	public void compare(FilterProperty property,
			RelationalOperator relationalOperator, Double numValue) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setNumValue(numValue);
	}

	/**
	 * compares the given {@link FilterProperty} with the given {@link String}
	 * value based on the given {@link RelationalOperator}. the given
	 * {@link FilterProperty} must correspond to this criterion's
	 * {@link FilterType} AND ALSO be of a textual data type.
	 * 
	 * @param property
	 * @param relationalOperator
	 * @param strValue
	 */
	public void compare(FilterProperty property,
			RelationalOperator relationalOperator, String strValue) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setStrValue(strValue);
	}

	/**
	 * compares the given {@link FilterProperty} with the given {@link Date}
	 * value based on the given {@link RelationalOperator}. the given
	 * {@link FilterProperty} must correspond to this criterion's
	 * {@link FilterType} AND ALSO be of a data type that represents a date.
	 * 
	 * @param property
	 * @param relationalOperator
	 * @param dateValue
	 */
	public void compare(FilterProperty property,
			RelationalOperator relationalOperator, Date dateValue) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setDateValue(dateValue);
	}

	/**
	 * compares the given {@link FilterProperty} with the given {@link Boolean}
	 * value based on the {@link RelationalOperator.EQUALS} operator. the given
	 * {@link FilterProperty} must correspond to this criterion's
	 * {@link FilterType} AND ALSO be of a boolean data type.
	 * 
	 * @param property
	 * @param boolValue
	 */
	public void compare(FilterProperty property, boolean boolValue) {
		this.setProperty(property);
		this.setRelationalOperator(RelationalOperator.EQUALS);
		this.setBoolValue(boolValue);
	}

	/**
	 * compares the given {@link FilterProperty} with a date dating back the
	 * given amount of <code>daysBack</code> based on the given
	 * {@link RelationalOperator}. the given {@link FilterProperty} must
	 * correspond to this criterion's {@link FilterType} AND ALSO be of a data
	 * type that represents a date.
	 * 
	 * @param property
	 * @param relationalOperator
	 * @param daysBack
	 */
	public void compareDaysBack(FilterProperty property,
			RelationalOperator relationalOperator, int daysBack) {
		this.setProperty(property);
		this.setRelationalOperator(relationalOperator);
		this.setDaysBack(daysBack);
	}

	/**
	 * assures that the given {@link FilterProperty} is not <code>NULL</code>.
	 * the given {@link FilterProperty} must correspond to this criterion's
	 * {@link FilterType}.
	 * 
	 * @param property
	 */
	public void compareNotNull(FilterProperty property) {
		this.setProperty(property);
		this.setRelationalOperator(RelationalOperator.NOT_NULL);
	}

	/**
	 * assures that the given {@link FilterProperty} is <code>NULL</code>. the
	 * given {@link FilterProperty} must correspond to this criterion's
	 * {@link FilterType}.
	 * 
	 * @param property
	 */
	void compareIsNull(FilterProperty property) {
		this.setProperty(property);
		this.setRelationalOperator(RelationalOperator.IS_NULL);
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

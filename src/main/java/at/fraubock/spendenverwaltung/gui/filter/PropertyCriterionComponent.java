package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class PropertyCriterionComponent {

	private String text;
	private FilterProperty property;

	private StringComparator strValue;
	private EnumComparator enumValue;
	private NumberComparator numValue;
	private BooleanComparator boolValue;
	private DateComparator dateValue;
	private NumberComparator daysBack;

	private PropertyCriterionComponent(String text, FilterProperty property) {
		this.text = text;
		this.property = property;
	}

	public PropertyCriterionComponent(String text, FilterProperty property,
			StringComparator comp) {
		this(text, property);
		this.strValue = comp;
	}

	public PropertyCriterionComponent(String text, FilterProperty property,
			NumberComparator forNumbers, NumberComparator forDaysBack) {
		this(text, property);
		this.numValue = forNumbers;
		this.daysBack = forDaysBack;
	}

	public PropertyCriterionComponent(String text, FilterProperty property,
			BooleanComparator comp) {
		this(text, property);
		this.boolValue = comp;
	}

	public PropertyCriterionComponent(String text, FilterProperty property,
			DateComparator comp) {
		this(text, property);
		this.dateValue = comp;
	}

	public PropertyCriterionTO toPropertyCriterionTO() {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		RelationalOperator op = null;

		if (strValue != null) {
			op = strValue.getOperator();
			crit.setStrValue(strValue.getString());
		} else if (enumValue != null) {
			op = enumValue.getOperator();
			crit.setStrValue(enumValue.getString());
		} else if (numValue != null) {
			op = numValue.getOperator();
			crit.setNumValue(numValue.getNumber());
		} else if (boolValue != null) {
			op = RelationalOperator.EQUALS;
			crit.setBoolValue(boolValue.getBoolean());
		} else if (dateValue != null) {
			op = dateValue.getOperator();
			crit.setDateValue(dateValue.getDate());
		} else if (daysBack != null) {
			op = daysBack.getOperator();
			// TODO checking
			crit.setDaysBack(daysBack.getNumber().intValue());
		}

		crit.setRelationalOperator(op);

		return crit;
	}

	@Override
	public String toString() {
		return text;
	}

	public JPanel getComparatorComponent() {
		if (strValue != null) {
			return strValue;
		} else if (enumValue != null) {
			return enumValue;
		} else if (numValue != null) {
			return numValue;
		} else if (boolValue != null) {
			return boolValue;
		} else if (dateValue != null) {
			return dateValue;
		} else if (daysBack != null) {
			return daysBack;
		}
		return null;
	}
}

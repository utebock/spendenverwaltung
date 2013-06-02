package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.filter.comparators.BooleanComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.DateComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.EnumComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.NumberComparator;
import at.fraubock.spendenverwaltung.gui.filter.comparators.StringComparator;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class PropertyComponent extends JPanel {
	private static final long serialVersionUID = -7565391688723214454L;
	
	private String text;
	private FilterProperty property;

	private StringComparator strComp;
	private EnumComparator enumComp;
	private NumberComparator numComp;
	private BooleanComparator boolComp;
	private DateComparator dateComp;
	private NumberComparator daysBackComp;

	private PropertyComponent(String text, FilterProperty property) {
		this.text = text;
		this.property = property;
	}

	public PropertyComponent(String text, FilterProperty property,
			StringComparator comp) {
		this(text, property);
		this.strComp = comp;
		this.add(comp);
	}

	/**
	 * either one of the provided components must be set, the other one must be
	 * null
	 * 
	 * @param text
	 * @param property
	 * @param forNumbers
	 * @param forDaysBack
	 */
	public PropertyComponent(String text, FilterProperty property,
			NumberComparator forNumbers, NumberComparator forDaysBack) {
		this(text, property);
		if (forNumbers != null) {
			this.numComp = forNumbers;
			this.add(forNumbers);
		} else {
			this.daysBackComp = forDaysBack;
			this.add(daysBackComp);
		}
	}

	public PropertyComponent(String text, FilterProperty property,
			BooleanComparator comp) {
		this(text, property);
		this.boolComp = comp;
		this.add(comp);
	}

	public PropertyComponent(String text, FilterProperty property,
			DateComparator comp) {
		this(text, property);
		this.dateComp = comp;
		this.add(comp);
	}

	public PropertyComponent(String text, FilterProperty property,
			EnumComparator comp) {
		this(text, property);
		this.enumComp = comp;
		this.add(comp);
	}

	public PropertyCriterionTO toPropertyCriterionTO() {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		RelationalOperator op = null;

		if (strComp != null) {
			op = strComp.getOperator();
			crit.setStrValue(strComp.getString());
		} else if (enumComp != null) {
			op = enumComp.getOperator();
			crit.setStrValue(enumComp.getString());
		} else if (numComp != null) {
			op = numComp.getOperator();
			crit.setNumValue(numComp.getNumber());
		} else if (boolComp != null) {
			op = RelationalOperator.EQUALS;
			crit.setBoolValue(boolComp.getBoolean());
		} else if (dateComp != null) {
			op = dateComp.getOperator();
			crit.setDateValue(dateComp.getDate());
		} else if (daysBackComp != null) {
			op = daysBackComp.getOperator();
			// TODO checking
			crit.setDaysBack(daysBackComp.getNumber().intValue());
		}

		crit.setRelationalOperator(op);

		return crit;
	}

	@Override
	public String toString() {
		return text;
	}
}

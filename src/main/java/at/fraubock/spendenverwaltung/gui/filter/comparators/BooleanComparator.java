package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class BooleanComparator extends JPanel implements ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private JCheckBox checkBox;
	private FilterProperty property;
	private String display;

	public BooleanComparator(FilterProperty property, String display) {
		this.display = display;
		this.property = property;
		add(checkBox = new JCheckBox());
	}

	@Override
	public CriterionTO createCriterion() throws InvalidInputException {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		crit.setRelationalOperator(RelationalOperator.EQUALS);
		crit.setBoolValue(checkBox.isSelected());
		return crit;
	}

	@Override
	public JComponent getConfigComponent() {
		return this;
	}
	
	@Override
	public String toString() {
		return display;
	}
}

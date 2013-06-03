package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class BooleanComparator extends JPanel implements IComparator {
	private static final long serialVersionUID = 5674883209607705490L;

	private JCheckBox checkBox;

	public BooleanComparator() {
		add(checkBox = new JCheckBox());
	}

	@Override
	public PropertyCriterionTO getPropertyCriterionTOForProperty(
			FilterProperty property) {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		crit.setRelationalOperator(RelationalOperator.EQUALS);
		crit.setBoolValue(checkBox.isSelected());
		return crit;
	}
	
	@Override
	public JComponent getPanel() {
		return checkBox;
	}
}

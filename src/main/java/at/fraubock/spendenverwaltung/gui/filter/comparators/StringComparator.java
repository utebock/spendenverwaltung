package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public class StringComparator extends JPanel implements IComparator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private JTextField textField;

	public StringComparator() {
		add(picker = new RelationalOperatorPicker(RelationType.FOR_STRING));
		add(textField = new JTextField(20));
	}

	@Override
	public PropertyCriterionTO getPropertyCriterionTOForProperty(FilterProperty property) {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setStrValue(textField.getText());
		return crit;
	}
	
	@Override
	public JComponent getPanel() {
		JPanel panel = new JPanel();
		panel.add(picker);
		panel.add(textField);
		return panel;
	}
}

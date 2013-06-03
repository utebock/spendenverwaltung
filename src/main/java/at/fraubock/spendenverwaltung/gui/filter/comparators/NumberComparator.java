package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public class NumberComparator extends JPanel implements IComparator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private CustomTextField textField;

	public NumberComparator() {
		add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER_AND_DATE));
		add(textField = new CustomTextField(5));
	}

	private Double getNumber() throws InvalidInputException {
		try {
			return Double.valueOf(textField.getText());
		} catch (NumberFormatException e) {
			textField.invalidateInput();
			throw new InvalidInputException(
					"Bitte geben Sie eine gültige Zahl ein!");
		}
	}

	@Override
	public PropertyCriterionTO getPropertyCriterionTOForProperty(
			FilterProperty property) throws InvalidInputException {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setNumValue(getNumber());
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

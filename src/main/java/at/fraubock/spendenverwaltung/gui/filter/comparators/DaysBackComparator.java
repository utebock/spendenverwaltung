package at.fraubock.spendenverwaltung.gui.filter.comparators;

import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public class DaysBackComparator extends JPanel implements IComparator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private CustomTextField textField;
	private JComboBox<String> timeUnit;

	public DaysBackComparator() {
		add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER_AND_DATE));
		add(textField = new CustomTextField(5));
		add(timeUnit = new JComboBox<String>(new SimpleComboBoxModel<String>(
				Arrays.asList(new String[] { "Tage", "Wochen", "Monate",
						"Jahre" }))));
	}

	private Integer getNumber() throws InvalidInputException {
		Integer num = null;
		try {
			num = Integer.valueOf(textField.getText());
		} catch(NumberFormatException e) {
			textField.invalidateInput();
			throw new InvalidInputException("Bitte geben Sie eine gültige Zahl ein!");
		}
		switch ((String) timeUnit.getModel().getSelectedItem()) {
			case "Wochen":
				return num * 7;
			case "Monate":
				return num * 30;
			case "Jahre":
				return num * 365;
			default:
				return num;
		}
	}

	@Override
	public PropertyCriterionTO getPropertyCriterionTOForProperty(
			FilterProperty property) throws InvalidInputException {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setDaysBack(getNumber());
		return crit;
	}
	
	@Override
	public JComponent getPanel() {
		JPanel panel = new JPanel();
		panel.add(picker);
		panel.add(textField);
		panel.add(timeUnit);
		return panel;
	}
}

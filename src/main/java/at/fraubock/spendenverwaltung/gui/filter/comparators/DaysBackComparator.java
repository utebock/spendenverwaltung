package at.fraubock.spendenverwaltung.gui.filter.comparators;

import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public class DaysBackComparator extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private CustomTextField textField;
	private JComboBox<String> timeUnit;
	private FilterProperty property;
	private String display;

	public DaysBackComparator(FilterProperty property, String display) {
		this.display = display;
		this.property = property;
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
		} catch (NumberFormatException e) {
			textField.invalidateInput();
			throw new InvalidInputException(
					"Bitte geben Sie eine gültige Zahl ein!");
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
	public Criterion createCriterion() throws InvalidInputException {
		PropertyCriterion crit = new PropertyCriterion();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setDaysBack(getNumber());
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

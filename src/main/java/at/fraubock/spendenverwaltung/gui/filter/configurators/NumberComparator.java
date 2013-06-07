package at.fraubock.spendenverwaltung.gui.filter.configurators;

import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;

/**
 * implements {@link ICriterionConfigurator} for {@link PropertyCriterion}s of
 * type {@link Number}.
 * 
 * @author philipp muhoray
 * 
 */
public class NumberComparator extends JPanel implements ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private CustomTextField textField;
	private FilterProperty property;
	private String display;

	public NumberComparator(FilterProperty property, String display) {
		this.display = display;
		this.property = property;
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
					"Bitte geben Sie eine g\u00FCltige Zahl ein!");
		}
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		PropertyCriterion crit = new PropertyCriterion();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setNumValue(getNumber());
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

package at.fraubock.spendenverwaltung.gui.filter;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;

/**
 * this component represents a specific mountable filter in the filter gui
 * 
 * @author philipp muhoray
 * 
 */
public class PersonToDonationFilterConfig extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -7505945596725892100L;

	private JComboBox<Filter> filterBox;
	private String display;
	private RelationalOperatorPicker picker;
	private CustomTextField amount;
	private JComboBox<String> compareWith;

	public PersonToDonationFilterConfig(String display, List<Filter> filters) {
		this.display = display;

		add(filterBox = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				filters)));

		add(new JLabel("insgesamt"));
		add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER_AND_DATE));
		add(amount = new CustomTextField(5));
		add(compareWith = new JComboBox<String>(new SimpleComboBoxModel<String>(
				Arrays.asList(new String[] { "mal", "Euro",
						"Euro durchschnittl." }))));
		add(new JLabel("gespendet"));
	}

	private Double getNumber() throws InvalidInputException {
		try {
			return Double.valueOf(amount.getText());
		} catch (NumberFormatException e) {
			amount.invalidateInput();
			throw new InvalidInputException(
					"Bitte geben Sie eine gültige Zahl ein!");
		}
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		Filter filter = (Filter) filterBox.getModel().getSelectedItem();

		MountedFilterCriterion crit = new MountedFilterCriterion();
		crit.setMount(filter);
		crit.setRelationalOperator(picker.getPickedOperator());
		Double number = getNumber();
		switch ((String) compareWith.getModel().getSelectedItem()) {
		case "mal":
			crit.setCount(number.intValue());
			break;// TODO check if double input
		case "Euro":
			crit.setProperty(FilterProperty.DONATION_AMOUNT);
			crit.setSum(number);
			break;
		case "Euro durchschnittl.":
			crit.setProperty(FilterProperty.DONATION_AMOUNT);
			crit.setAvg(number);
			break;
		// TODO log
		default:
			throw new InvalidInputException(
					"Enum picked that is not available.");
		}

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

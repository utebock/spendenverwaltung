package at.fraubock.spendenverwaltung.gui.filter.comparators;

import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;
import at.fraubock.spendenverwaltung.service.to.MountedFilterCriterionTO;

public class DonationToPersonComp extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -9206863381571374141L;

	private RelationalOperatorPicker picker;
	private CustomTextField amount;
	private JComboBox<String> timeUnit;
	private Filter filter;
	private String display;

	public DonationToPersonComp(Filter filter, String display) {
		this.display = display;
		this.filter = filter;
		add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER_AND_DATE));
		add(new JLabel("insgesamt"));
		add(amount = new CustomTextField(5));
		add(timeUnit = new JComboBox<String>(new SimpleComboBoxModel<String>(
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
	public CriterionTO createCriterion() throws InvalidInputException {
		MountedFilterCriterionTO crit = new MountedFilterCriterionTO();
		crit.setMount(filter);
		crit.setRelationalOperator(picker.getPickedOperator());
		Double number = getNumber();
		switch ((String) timeUnit.getModel().getSelectedItem()) {
		case "mal":
			crit.setCount(number.intValue());
			break;// TODO check if double input
		case "Euro":
			crit.setSum(number);
			break;
		case "Euro durchschnittl.":
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

package at.fraubock.spendenverwaltung.gui.filter.configurators.property;

import java.util.Arrays;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;

/**
 * implements {@link ICriterionConfigurator} for {@link PropertyCriterion}s of
 * type {@link Date}. it let's the user compare this property with a preceding
 * number of days.
 * 
 * @author philipp muhoray
 * 
 */
public class DaysBackComparator extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private CustomTextField textField;
	private JComboBox<String> timeUnit;
	private FilterProperty property;
	private String display;
	private final String DAYS = "Tage";
	private final String WEEKS = "Wochen";
	private final String MONTHS = "Monate";
	private final String YEARS = "Jahre";

	public DaysBackComparator(FilterProperty property, String display) {
		this.display = display;
		this.property = property;

		add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER_AND_DATE));
		add(textField = new CustomTextField(5));
		add(timeUnit = new JComboBox<String>(new SimpleComboBoxModel<String>(Arrays.asList(new String[]{DAYS,WEEKS,MONTHS,YEARS}))));
	}

	private Integer getNumber() throws InvalidInputException {
		Integer num = null;
		try {
			num = Integer.valueOf(textField.getText());
		} catch (NumberFormatException e) {
			textField.invalidateInput();
			throw new InvalidInputException(
					"Bitte geben Sie eine g\u00FCltige Zahl ein!");
		}
		String sel = (String) timeUnit.getModel().getSelectedItem();

		if (sel.equals(WEEKS)) {
			return num * 7;
		} else if (sel.equals(MONTHS)) {
			return num * 30;
		} else if (sel.equals(YEARS)) {
			return num * 365;
		}
		return num;
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

	@Override
	public boolean applyCriterion(Criterion criterion) {
		if (criterion instanceof PropertyCriterion) {
			PropertyCriterion prop = (PropertyCriterion) criterion;
			if (prop.getProperty() == this.property && prop.getDaysBack()!=null) {
				int daysBack = prop.getDaysBack();
				this.picker.setSelectedItem(
						RelationalOperatorGuiWrapper.getForOperator(prop
								.getRelationalOperator()));

				if (daysBack % 365 == 0) {
					this.textField.setText("" + (daysBack / 365));
					timeUnit.setSelectedItem(YEARS);
				} else if (daysBack % 30 == 0) {
					this.textField.setText("" + (daysBack / 30));
					timeUnit.setSelectedItem(MONTHS);
				} else if (daysBack % 7 == 0) {
					this.textField.setText("" + (daysBack / 7));
					timeUnit.setSelectedItem(WEEKS);
				} else {
					this.textField.setText("" + daysBack);
					timeUnit.setSelectedItem(DAYS);
				}
				return true;
			}
		}
		return false;
	}
}

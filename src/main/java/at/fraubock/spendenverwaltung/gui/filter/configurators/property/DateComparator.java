package at.fraubock.spendenverwaltung.gui.filter.configurators.property;

import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;

/**
 * implements {@link ICriterionConfigurator} for {@link PropertyCriterion}s of
 * type {@link Date}.
 * 
 * @author philipp muhoray
 * 
 */
public class DateComparator extends JPanel implements ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private CustomTextField textField;
	private FilterProperty property;
	private String display;

	public DateComparator(FilterProperty property, String display) {
		this.display = display;
		this.property = property;
		add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER_AND_DATE));
		add(textField = new CustomTextField(12));
		JLabel format = new JLabel("(dd.MM.yyyy)");
		format.setFont(new Font("Headline", Font.PLAIN, 11));
		add(format);
	}

	private Date getDate() throws InvalidInputException {
		try {
			return new SimpleDateFormat("dd.MM.yyyy")
					.parse(textField.getText());
		} catch (ParseException e) {
			textField.invalidateInput();
			throw new InvalidInputException(
					"Bitte tragen Sie ein g\u00FCltiges Datum ein!");
		}
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		PropertyCriterion crit = new PropertyCriterion();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setDateValue(getDate());
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
			if (prop.getProperty() == this.property && prop.getDateValue()!=null) {
				this.picker.setSelectedItem(
						RelationalOperatorGuiWrapper.getForOperator(prop
								.getRelationalOperator()));
				this.textField.setText(new SimpleDateFormat("dd.MM.yyyy")
						.format(prop.getDateValue()));
				return true;
			}
		}
		return false;
	}
}

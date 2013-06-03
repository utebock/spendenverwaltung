package at.fraubock.spendenverwaltung.gui.filter.comparators;

import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.CustomTextField;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;

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
					"Bitte tragen Sie ein gültiges Datum ein!");
		}
	}

	@Override
	public CriterionTO createCriterion() throws InvalidInputException {
		PropertyCriterionTO crit = new PropertyCriterionTO();
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
}

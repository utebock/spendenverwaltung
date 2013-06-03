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
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public class DateComparator extends JPanel implements IComparator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private CustomTextField textField;

	public DateComparator() {
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
					"Bitte tragen Sie ein g�ltiges Datum ein!");
		}
	}

	@Override
	public PropertyCriterionTO getPropertyCriterionTOForProperty(
			FilterProperty property) throws InvalidInputException {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setDateValue(getDate());
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

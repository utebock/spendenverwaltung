package at.fraubock.spendenverwaltung.gui.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class DateComparator extends JPanel {
	private static final long serialVersionUID = 5674883209607705490L;
	
	private RelationalOperatorPicker picker;
	private JTextField textField;
	
	public DateComparator() {
		add(picker = new RelationalOperatorPicker(RelationType.FOR_STRING));
		//TODO datepicker
		add(textField = new JTextField(10));
	}
	
	public Date getDate() {
		try {
			return new SimpleDateFormat("dd.MM.yyyy").parse(textField.getText());
		} catch (ParseException e) {
			//TODO error msg
		}
		return null;
	}
	
	public RelationalOperator getOperator() {
		return picker.getPickedOperator();
	}
}

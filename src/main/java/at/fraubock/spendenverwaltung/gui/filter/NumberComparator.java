package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JPanel;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class NumberComparator extends JPanel {
	private static final long serialVersionUID = 5674883209607705490L;
	
	private RelationalOperatorPicker picker;
	private JTextField textField;
	
	public NumberComparator() {
		add(picker = new RelationalOperatorPicker(RelationType.FOR_NUMBER_AND_DATE));
		add(textField = new JTextField(5));
	}
	
	public Double getNumber() {
		//TODO checking
		return Double.valueOf(textField.getText());
	}
	
	public RelationalOperator getOperator() {
		return picker.getPickedOperator();
	}
}

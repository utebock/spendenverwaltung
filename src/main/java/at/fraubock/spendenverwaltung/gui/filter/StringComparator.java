package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JPanel;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class StringComparator extends JPanel {
	private static final long serialVersionUID = 5674883209607705490L;
	
	private RelationalOperatorPicker picker;
	private JTextField textField;
	
	public StringComparator() {
		add(picker = new RelationalOperatorPicker(RelationType.FOR_STRING));
		add(textField = new JTextField(20));
	}
	
	public String getString() {
		return textField.getText();
	}
	
	public RelationalOperator getOperator() {
		return picker.getPickedOperator();
	}
}

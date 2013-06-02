package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JPanel;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class EnumComparator extends JPanel {
	private static final long serialVersionUID = 5674883209607705490L;
	
	private RelationalOperatorPicker picker;
	private JTextField textField;
	
	public EnumComparator() {
		add(picker = new RelationalOperatorPicker(RelationType.FOR_STRING));
		//TODO dd
		add(textField = new JTextField(20));
	}
	
	public String getString() {
		//TODO checking
		return textField.getText();
	}
	
	public RelationalOperator getOperator() {
		return picker.getPickedOperator();
	}
}

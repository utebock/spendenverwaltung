package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class BooleanComparator extends JPanel {
	private static final long serialVersionUID = 5674883209607705490L;

	private JCheckBox checkBox;

	public BooleanComparator() {
		add(checkBox = new JCheckBox());
	}

	public boolean getBoolean() {
		return checkBox.isSelected();
	}
}

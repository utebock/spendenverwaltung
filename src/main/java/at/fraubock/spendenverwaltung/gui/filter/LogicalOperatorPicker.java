package at.fraubock.spendenverwaltung.gui.filter;

import java.util.Arrays;

import javax.swing.JComboBox;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

/**
 * ComboBox for picking a {@link LogicalOperator} in the {@link CreateFilter}
 * view.
 * 
 * @author philipp muhoray
 * 
 */
public class LogicalOperatorPicker extends JComboBox<LogicalOperator> {
	private static final long serialVersionUID = 6985601580458540392L;

	public LogicalOperatorPicker() {

		super(new SimpleComboBoxModel<LogicalOperator>(
				Arrays.asList(LogicalOperator.values())));
	}
	
	/**
	 * @return the selected {@link LogicalOperator}
	 */
	public LogicalOperator getPickedOperator() {
		return (LogicalOperator)getModel().getSelectedItem();
	}

}

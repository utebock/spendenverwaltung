package at.fraubock.spendenverwaltung.gui.filter;

import java.util.Arrays;

import javax.swing.JComboBox;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.LogicalOperatorPicker.LogicalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

/**
 * ComboBox for picking a {@link LogicalOperator} in the {@link CreateFilter}
 * view.
 * 
 * @author philipp muhoray
 * 
 */
public class LogicalOperatorPicker extends JComboBox<LogicalOperatorGuiWrapper> {
	private static final long serialVersionUID = 6985601580458540392L;

	public LogicalOperatorPicker() {

		super(new SimpleComboBoxModel<LogicalOperatorGuiWrapper>(
				Arrays.asList(LogicalOperatorGuiWrapper.values())));
	}

	/**
	 * @return the selected {@link LogicalOperator}
	 */
	public LogicalOperator getPickedOperator() {
		return ((LogicalOperatorGuiWrapper) getModel().getSelectedItem())
				.getOperator();
	}

	/**
	 * used to map viewable text strings to {@link LogicalOperator} for the GUI
	 * 
	 * @author philipp muhoray
	 * 
	 */
	public enum LogicalOperatorGuiWrapper {

		AND(LogicalOperator.AND, "und"), OR(LogicalOperator.OR, "oder"), AND_NOT(
				LogicalOperator.AND_NOT, "und nicht"), OR_NOT(
				LogicalOperator.OR_NOT, "oder nicht"), XOR(LogicalOperator.XOR,
				"entweder - oder");

		private String text;
		private LogicalOperator op;

		private LogicalOperatorGuiWrapper(LogicalOperator op, String text) {
			this.op = op;
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}

		public LogicalOperator getOperator() {
			return op;
		}

	}

}

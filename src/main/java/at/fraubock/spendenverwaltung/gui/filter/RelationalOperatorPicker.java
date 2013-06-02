package at.fraubock.spendenverwaltung.gui.filter;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

/**
 * ComboBox for picking a {@link RelationalOperator} in the {@link CreateFilter}
 * view. use {@link RelationType} to define for which data type operators should
 * be provided.
 * 
 * @author philipp muhoray
 * 
 */
public class RelationalOperatorPicker extends
		JComboBox<RelationalOperatorGuiWrapper> {
	private static final long serialVersionUID = 6985601580458540392L;

	public RelationalOperatorPicker(RelationType type) {
		super(new SimpleComboBoxModel<RelationalOperatorGuiWrapper>(
				type.getOperators()));
	}

	/**
	 * @return the selected {@link RelationalOperator}
	 */
	public RelationalOperator getPickedOperator() {
		return ((RelationalOperatorGuiWrapper) getModel().getSelectedItem())
				.getOperator();
	}

	public enum RelationType {
		FOR_STRING, FOR_NUMBER_AND_DATE;

		public List<RelationalOperatorGuiWrapper> getOperators() {
			if (this == FOR_STRING) {
				return Arrays.asList(new RelationalOperatorGuiWrapper[] {
						RelationalOperatorGuiWrapper.EQUALS,
						RelationalOperatorGuiWrapper.UNEQUAL,
						RelationalOperatorGuiWrapper.LIKE });
			} else if (this == FOR_NUMBER_AND_DATE) {
				List<RelationalOperatorGuiWrapper> ops = Arrays
						.asList(RelationalOperatorGuiWrapper.values());
				ops.remove(RelationalOperatorGuiWrapper.LIKE);
				// TODO make this work
				ops.remove(RelationalOperatorGuiWrapper.NOT_NULL);
				ops.remove(RelationalOperatorGuiWrapper.IS_NULL);
				return ops;
			}
			return null;
		}
	}

	/**
	 * used to map viewable text strings to {@link RelationalOperator} for the
	 * GUI
	 * 
	 * @author philipp muhoray
	 * 
	 */
	public enum RelationalOperatorGuiWrapper {

		GREATER(RelationalOperator.GREATER, "gr\u00F6\u00DFer als"), LESS(
				RelationalOperator.LESS, "kleiner als"), EQUALS(
				RelationalOperator.EQUALS, "gleich"), UNEQUAL(
				RelationalOperator.UNEQUAL, "ungleich"), LIKE(
				RelationalOperator.LIKE, "\u00E4hnlich wie"), GREATER_EQ(
				RelationalOperator.GREATER_EQ,
				"gr\u00F6\u00DFer oder gleich als"), LESS_EQ(
				RelationalOperator.LESS_EQ, "kleiner oder gleich als"), NOT_NULL(
				RelationalOperator.NOT_NULL, "ist gesetzt"), IS_NULL(
				RelationalOperator.IS_NULL, "ist nicht gesetzt");

		private String text;
		private RelationalOperator op;

		private RelationalOperatorGuiWrapper(RelationalOperator op, String text) {
			this.op = op;
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}

		public RelationalOperator getOperator() {
			return op;
		}

	}
}

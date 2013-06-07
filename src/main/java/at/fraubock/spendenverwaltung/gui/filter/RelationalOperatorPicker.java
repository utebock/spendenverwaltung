package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

/**
 * GUI component for picking a {@link RelationalOperator} in the
 * {@link CreateFilter} view. use {@link RelationType} to define for what data
 * type operators should be provided.
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
		FOR_STRING, FOR_NUMBER_AND_DATE, FOR_ENUM;

		public List<RelationalOperatorGuiWrapper> getOperators() {
			if (this == FOR_STRING) {
				return Arrays.asList(new RelationalOperatorGuiWrapper[] {
						RelationalOperatorGuiWrapper.EQUALS,
						RelationalOperatorGuiWrapper.UNEQUAL,
						RelationalOperatorGuiWrapper.LIKE });
			} else if (this == FOR_NUMBER_AND_DATE) {
				List<RelationalOperatorGuiWrapper> ops = new ArrayList<RelationalOperatorGuiWrapper>();
				ops.addAll(Arrays.asList(RelationalOperatorGuiWrapper.values()));
				ops.remove(RelationalOperatorGuiWrapper.LIKE);
				// TODO make this work
				ops.remove(RelationalOperatorGuiWrapper.NOT_NULL);
				ops.remove(RelationalOperatorGuiWrapper.IS_NULL);
				return ops;
			} else if (this == FOR_ENUM) {
				return Arrays.asList(new RelationalOperatorGuiWrapper[] {
						RelationalOperatorGuiWrapper.EQUALS,
						RelationalOperatorGuiWrapper.UNEQUAL });
			}
			return null;
		}
	}

	/**
	 * used to map representative labels to {@link RelationalOperator}s
	 * 
	 * @author philipp muhoray
	 * 
	 */
	public enum RelationalOperatorGuiWrapper {
		LESS(RelationalOperator.LESS, "weniger als"), LESS_EQ(
				RelationalOperator.LESS_EQ, "weniger oder gleich als"), EQUALS(
				RelationalOperator.EQUALS, "gleich"), UNEQUAL(
				RelationalOperator.UNEQUAL, "ungleich"), LIKE(
				RelationalOperator.LIKE, "enth\u00E4lt"), GREATER_EQ(
				RelationalOperator.GREATER_EQ, "mehr oder gleich als"), GREATER(
				RelationalOperator.GREATER, "mehr als"), NOT_NULL(
				RelationalOperator.NOT_NULL, "ist vorhanden"), IS_NULL(
				RelationalOperator.IS_NULL, "ist nicht vorhanden");

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

		public static RelationalOperatorGuiWrapper getForOperator(
				RelationalOperator op) {
			for (RelationalOperatorGuiWrapper wrapper : RelationalOperatorGuiWrapper
					.values()) {
				if (wrapper.getOperator() == op) {
					return wrapper;
				}
			}
			return null;
		}

	}
}

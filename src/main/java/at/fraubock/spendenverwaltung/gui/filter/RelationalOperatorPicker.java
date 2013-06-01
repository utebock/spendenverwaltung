package at.fraubock.spendenverwaltung.gui.filter;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;

import at.fraubock.spendenverwaltung.gui.CreateFilter;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

/**
 * ComboBox for picking a {@link RelationalOperator} in the {@link CreateFilter}
 * view. use {@link RelationType} to define for which data type operators should
 * be provided.
 * 
 * @author philipp muhoray
 * 
 */
public class RelationalOperatorPicker extends JComboBox<RelationalOperator> {
	private static final long serialVersionUID = 6985601580458540392L;

	public RelationalOperatorPicker(RelationType type) {
		super(new SimpleComboBoxModel<RelationalOperator>(type.getOperators()));
	}

	/**
	 * @return the selected {@link RelationalOperator}
	 */
	public RelationalOperator getPickedOperator() {
		return (RelationalOperator) getModel().getSelectedItem();
	}

	public enum RelationType {
		FOR_STRING, FOR_NUMBER_AND_DATE;

		public List<RelationalOperator> getOperators() {
			if (this == FOR_STRING) {
				return Arrays.asList(new RelationalOperator[] {
						RelationalOperator.EQUALS, RelationalOperator.UNEQUAL,
						RelationalOperator.LIKE });
			} else if (this == FOR_NUMBER_AND_DATE) {
				List<RelationalOperator> ops = Arrays.asList(RelationalOperator
						.values());
				ops.remove(RelationalOperator.LIKE);
				ops.remove(RelationalOperator.NOT_NULL);
				ops.remove(RelationalOperator.IS_NULL);
				return ops;
			}
			return null;
		}
	}
}

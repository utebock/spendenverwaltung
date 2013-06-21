package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

/**
 * GUI component for picking a {@link RelationalOperator} in the
 * {@link CreateFilter} view. use {@link RelationType} to define for what data
 * type operators should be provided.
 * 
 * @author philipp muhoray
 * 
 */
public class RelationalOperatorPicker extends JComboBox<RelationalOperatorGuiWrapper> {
	private static final long serialVersionUID = 6985601580458540392L;

	public RelationalOperatorPicker(RelationType type) {
		super(new SimpleComboBoxModel<RelationalOperatorGuiWrapper>(type.getOperators()));
	}

	/**
	 * @return the selected {@link RelationalOperator}
	 */
	public RelationalOperator getPickedOperator() {
		return ((RelationalOperatorGuiWrapper) getModel().getSelectedItem()).getOperator();
	}

	public enum RelationType {
		FOR_STRING, FOR_NUMBER, FOR_ENUM, FOR_DATE;

		public List<RelationalOperatorGuiWrapper> getOperators() {
			if (this == FOR_STRING) {
				return getStringOperators();
			} else if (this == FOR_NUMBER) {
				return getNumberOperators();
			} else if (this == FOR_DATE) {
				return getDateOperators();
			} else if (this == FOR_ENUM) {
				return getEnumOperators();
			}
			return null;
		}
	}

	private static List<RelationalOperatorGuiWrapper> getNumberOperators() {
		List<RelationalOperatorGuiWrapper> list = new ArrayList<RelationalOperatorGuiWrapper>();

		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.LESS, "weniger als"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.LESS_EQ,
				"weniger als oder gleich"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.EQUALS, "gleich"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.UNEQUAL, "ungleich"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.GREATER_EQ,
				"mehr als oder gleich"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.GREATER, "mehr als"));

		return list;
	}

	private static List<RelationalOperatorGuiWrapper> getDateOperators() {
		List<RelationalOperatorGuiWrapper> list = new ArrayList<RelationalOperatorGuiWrapper>();

		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.LESS, "liegt vor"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.LESS_EQ, "liegt vor oder am"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.EQUALS, "liegt am"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.UNEQUAL, "liegt nicht am"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.GREATER_EQ,
				"liegt nach oder am"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.GREATER, "liegt nach"));

		return list;
	}

	private static List<RelationalOperatorGuiWrapper> getStringOperators() {
		List<RelationalOperatorGuiWrapper> list = new ArrayList<RelationalOperatorGuiWrapper>();

		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.EQUALS, "gleich"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.UNEQUAL, "ungleich"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.LIKE, "enth\u00E4lt"));

		return list;
	}

	private static List<RelationalOperatorGuiWrapper> getEnumOperators() {
		List<RelationalOperatorGuiWrapper> list = new ArrayList<RelationalOperatorGuiWrapper>();

		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.EQUALS, "gleich"));
		list.add(new RelationalOperatorGuiWrapper(RelationalOperator.UNEQUAL, "ungleich"));

		return list;
	}

	public static class RelationalOperatorGuiWrapper {

		private String text;
		private RelationalOperator operator;

		public RelationalOperatorGuiWrapper(RelationalOperator operator, String text) {
			this.text = text;
			this.operator = operator;
		}

		@Override
		public String toString() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public RelationalOperator getOperator() {
			return operator;
		}

		public void setOperator(RelationalOperator operator) {
			this.operator = operator;
		}

		public static RelationalOperatorGuiWrapper getForOperator(RelationalOperator op) {
			
			for (RelationalOperatorGuiWrapper wrapper : getNumberOperators()) {
				if (wrapper.getOperator() == op) {
					return wrapper;
				}
			}

			for (RelationalOperatorGuiWrapper wrapper : getStringOperators()) {
				if (wrapper.getOperator() == op) {
					return wrapper;
				}
			}
			return null;
		}
		
		public static RelationalOperatorGuiWrapper getForDateOperator(RelationalOperator op) {
			
			for (RelationalOperatorGuiWrapper wrapper : getDateOperators()) {
				if (wrapper.getOperator() == op) {
					return wrapper;
				}
			}
			return null;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other==this) return true;
			if(other==null) return false;
			if(other.getClass()!=RelationalOperatorGuiWrapper.class) return false;
			
			RelationalOperatorGuiWrapper wrap = (RelationalOperatorGuiWrapper)other;
			if(this.getOperator()==wrap.getOperator() && this.toString().equals(wrap.toString())) {
				return true;
			}
			return false;
		}

	}
}

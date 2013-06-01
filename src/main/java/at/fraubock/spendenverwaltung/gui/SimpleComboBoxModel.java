package at.fraubock.spendenverwaltung.gui;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;

/**
 * implementation of {@link ComboBoxModel}. takes a {@link List} in the
 * constructor which holds the model objects to be represented in a
 * {@link JComboBox}. the methods of the interface mostly delegates to the given
 * list.
 * @NOTE the first element of the list (index 0) will be set as the default value of this model.
 * 
 * @author philipp muhoray
 * 
 */
public class SimpleComboBoxModel<T> implements ComboBoxModel<T> {

	private List<T> selectors;
	private T selected;

	public SimpleComboBoxModel(List<T> selectors) {
		this.selectors = selectors;
		this.selected = selectors.get(0);
	}

	@Override
	public T getElementAt(int index) {
		return selectors.get(index);
	}

	@Override
	public int getSize() {
		return selectors.size();
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setSelectedItem(Object anItem) {
		selected = (T) anItem;

	}

	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub

	}
}

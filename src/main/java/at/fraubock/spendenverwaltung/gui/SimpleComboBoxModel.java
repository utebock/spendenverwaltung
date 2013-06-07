package at.fraubock.spendenverwaltung.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;

/**
 * implementation of {@link ComboBoxModel}. takes a {@link List} which holds the
 * objects to be represented in a {@link JComboBox} via their #toString()
 * method. the selected item can then be retrieved by calling
 * #getSelectedItem().
 * 
 * @NOTE the first element of the list (index 0) will be set as the default
 *       value of this model.
 * 
 * @author philipp muhoray
 * 
 */
public class SimpleComboBoxModel<T> implements ComboBoxModel<T> {

	private List<T> items;
	private T selected;

	public SimpleComboBoxModel(List<T> items) {
		this.items = items;
		if(!items.isEmpty()) {
			this.selected = items.get(0);
		}
	}
	
	public SimpleComboBoxModel(@SuppressWarnings("unchecked") T... items) {
		this.items = new ArrayList<T>();
		for(T item: items) {
			this.items.add(item);
		}
		this.selected = items[0];
	}

	@Override
	public T getElementAt(int index) {
		return items.get(index);
	}

	@Override
	public int getSize() {
		return items.size();
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

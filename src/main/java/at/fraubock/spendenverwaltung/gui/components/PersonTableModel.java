package at.fraubock.spendenverwaltung.gui.components;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;

public class PersonTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = new String[] { "Vorname",
			"Nachname", "Adresse" };
	private Vector<Person> persons = new Vector<Person>();

	public void addPerson(Person person) {
		persons.add(person);
		fireTableDataChanged();
	}

	public void removePerson(int row) {
		persons.remove(row);
		fireTableDataChanged();
	}

	public Person getPersonRow(int rowIndex) {
		return persons.get(rowIndex);
	}
	
	/**
	 * removes all items from this model
	 */
	public void clear() {
		persons.clear();
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return persons.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person person = (Person)persons.get(rowIndex);
		
		switch(columnIndex){
			case 0: return person.getGivenName();
			case 1: return person.getSurname();
			case 2: return person.getAddresses().isEmpty()?"keine Adresse vorhanden":
				person.getAddresses().get(0).getStreet();
			case 3: return person.getId();
			default: return null;
		}
	}

	public Class<?> getColumnClass(int col) {

		switch (col) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return Integer.class;
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	public List<Person> getPersons() {
		return persons;
	}
}

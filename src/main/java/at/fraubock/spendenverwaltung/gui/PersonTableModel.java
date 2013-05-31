package at.fraubock.spendenverwaltung.gui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;

public class PersonTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Id", "Vorname", "Nachname", "Adresse"};
	private Vector<Person> persons = new Vector<Person>();

	public void addPerson (Person person){
		persons.add(person);
	}
	
	public void removePerson (int row){
		persons.remove(row);
		fireTableDataChanged();
	}
	
	public Person getPersonRow(int rowIndex){
		return persons.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return persons.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person person = (Person)persons.get(rowIndex);
		
		switch(columnIndex){
			case 0: return person.getId();
			case 1: return person.getSurname();
			case 2: return person.getGivenName();
			case 3: return "";
			default: return null;
		}
	}
	
	public Class<?> getColumnClass(int col) {
		
		switch (col) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3: 
			return String.class;
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
}


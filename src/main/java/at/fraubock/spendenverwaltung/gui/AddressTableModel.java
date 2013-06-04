package at.fraubock.spendenverwaltung.gui;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;


public class AddressTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Stra\u00DFe", "PLZ", "Stadt", "Land"};
	private Vector<Address> addresses = new Vector<Address>();

	public void addAddress (Address address){
		addresses.add(address);
	}
	
	public void removeAddress (int row){
		addresses.remove(row);
		fireTableDataChanged();
	}
	
	public void removeAll(){
		addresses = new Vector<Address>();
		fireTableDataChanged();
	}
	
	public void insertList(List<Address> insertAddresses){
		for(Address a : insertAddresses){
			addresses.add(a);
		}
		fireTableDataChanged();
	}
	
	public Address getAddressRow(int rowIndex){
		return addresses.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return addresses.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Address address = (Address)addresses.get(rowIndex);
		
		switch(columnIndex){
			case 0: return address.getStreet();
			case 1: return address.getPostalCode();
			case 2: return address.getCity();
			case 3: return address.getCountry();
			case 4: return address.getId();
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
			return String.class;
		case 4: 
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

}


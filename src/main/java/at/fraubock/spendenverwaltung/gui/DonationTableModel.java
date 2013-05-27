package at.fraubock.spendenverwaltung.gui;

import java.util.Date;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;

public class DonationTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Id", "Betrag", "Datum", "Widmung", "Typ", "Notiz"};
	private Vector<Donation> donations = new Vector<Donation>();

	public void addDonation (Donation donation){
		donations.add(donation);
	}
	
	public void removeDonation (int row){
		donations.remove(row);
		fireTableDataChanged();
	}
	
	public Donation getDonationRow(int rowIndex){
		return donations.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return donations.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Donation donation = (Donation)donations.get(rowIndex);
		
		switch(columnIndex){
			case 0: return donation.getId();
			case 1: return donation.getAmount();
			case 2: return donation.getDate();
			case 3: return donation.getDedication();
			case 4: return donation.getType();
			case 5: return donation.getNote();
			default: return null;
		}
	}
	
	public Class<?> getColumnClass(int col) {
		
		switch (col) {
		case 0:
			return Integer.class;
		case 1:
			return Long.class;
		case 2:
			return Date.class;
		case 3: 
			return String.class;
		case 4: 
			return Donation.DonationType.class;
		case 5: 
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


/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.gui;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;

public class DonationTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Betrag", "Datum", "Widmung", "Typ", "Notiz"};
	private Vector<Donation> donations = new Vector<Donation>();

	public void addDonation (Donation donation){
		donations.add(donation);
		fireTableDataChanged();
	}
	
	public void removeDonation (int row){
		donations.remove(row);
		fireTableDataChanged();
	}
	
	public void addDonations(List<Donation> donations) {
		donations.addAll(donations);
		fireTableDataChanged();
	}
	
	public void clear() {
		donations = new Vector<Donation>();
		fireTableDataChanged();
	}
	
	public List<Donation> getDonations() {
		return donations;
	}
	
	public Donation getDonationRow(int rowIndex){
		return donations.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return donations.size();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Donation donation = (Donation)donations.get(rowIndex);
		
		switch(columnIndex){
			case 0: return (donation.getAmount()/100D);
			case 1: return donation.getDate();
			case 2: return donation.getDedication();
			case 3: return donation.getType();
			case 4: return donation.getNote();
			default: return null;
		}
	}
	
	public Class<?> getColumnClass(int col) {
		
		switch (col) {
		case 0:
			return Long.class;
		case 1:
			return Date.class;
		case 2: 
			return String.class;
		case 3: 
			return Donation.DonationType.class;
		case 4: 
			return String.class;
		}
		return null;
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


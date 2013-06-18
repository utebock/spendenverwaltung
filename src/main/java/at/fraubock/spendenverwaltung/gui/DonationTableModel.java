package at.fraubock.spendenverwaltung.gui;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;

public class DonationTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Betrag", "Datum", "Widmung", "Typ", "Notiz"};
	private Vector<Donation> donations = new Vector<Donation>();
	private IDonationService donationService;
	private JPanel parent;

	public DonationTableModel(JPanel parent, IDonationService donationService){
		this.parent = parent;
		this.donationService = donationService;
	}
	
	public void addDonation (Donation donation){
		donations.add(donation);
	}
	
	public void removeDonation (int row){
		donations.remove(row);
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
	public Object getValueAt(int rowIndex, int columnIndex) {
		Donation donation = (Donation)donations.get(rowIndex);
		
		switch(columnIndex){
			case 0: return (donation.getAmount()/100);
			case 1: return donation.getDate();
			case 2: return donation.getDedication();
			case 3: return donation.getType();
			case 4: return donation.getNote();
			default: return null;
		}
	}
	
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Donation updateDonation = donations.get(rowIndex);
        
        switch(columnIndex){
        	case 0: updateDonation.setAmount((Long) value);
        			break;
        	case 1: updateDonation.setDate((Date) value);
					break;
        	case 2: updateDonation.setDedication((String) value);
					break;
        	case 3: updateDonation.setType((Donation.DonationType) value);
					break;
        	case 4: updateDonation.setNote((String) value);
					break;
        }
        
			try {
				updateDonation = donationService.update(updateDonation);
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(parent, "Updating Donation failed", "Error", JOptionPane.ERROR_MESSAGE);
		        e.printStackTrace();
		        return;
			}
		
		JOptionPane.showMessageDialog(parent, "Successfully updated donation", "Information", JOptionPane.INFORMATION_MESSAGE);
	    
		donations.set(rowIndex, updateDonation);
        fireTableCellUpdated(rowIndex, columnIndex);
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
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		switch(col){
			case 0: return false;
			default: return true;
		}
	}

	public void clear() {
		donations = new Vector<Donation>();
	}
}


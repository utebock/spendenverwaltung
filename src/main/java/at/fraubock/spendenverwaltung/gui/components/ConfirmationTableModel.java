package at.fraubock.spendenverwaltung.gui.components;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;

public class ConfirmationTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = new String[] { "Vorname",
			"Nachname", "Firma", "Spendenbetrag", "SpendenDatum", "Vorlage", "Erstellungsdatum" };
	private Vector<Confirmation> confirmations = new Vector<Confirmation>();

	public void addConfirmation(Confirmation confirmation) {
		confirmations.add(confirmation);
		fireTableDataChanged();
	}

	public void removeConfirmation(int row) {
		confirmations.remove(row);
		fireTableDataChanged();
	}

	public Confirmation getConfirmationRow(int rowIndex) {
		return confirmations.get(rowIndex);
	}
	
	/**
	 * removes all items from this model
	 */
	public void clear() {
		confirmations.clear();
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return confirmations.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		SimpleDateFormat f = new SimpleDateFormat("MM.dd.yyyy");
		Confirmation confirmation = (Confirmation)confirmations.get(rowIndex);
		switch(columnIndex){
			case 0: return confirmation.getPerson().getGivenName();
			case 1: return confirmation.getPerson().getSurname();
			case 2: if(!(confirmation.getPerson().getCompany() == null)) {
						if(!confirmation.getPerson().getCompany().isEmpty()) {
							return confirmation.getPerson().getCompany();
						}
					}
					return null;
			case 3: if(confirmation.getDonation()!=null){
						return confirmation.getDonation().getAmount()/(double)100;
					}
					else{
						return "Mehrere Spenden";
					}
			case 4: if(confirmation.getDonation()!=null)return f.format(confirmation.getDonation().getDate());
					else{
						return f.format(confirmation.getFromDate())+" bis "+f.format(confirmation.getToDate());
					}
			case 5: if(confirmation.getTemplate()!=null)return confirmation.getTemplate().getName();
					else return null;
			case 6: return confirmation.getDate();
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
			return String.class;
		case 5:
			return String.class;
		case 6:
			return java.util.Date.class;
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
	
	public List<Confirmation> getConfirmations() {
		return confirmations;
	}

	public void addAll(List<Confirmation> results) {
		confirmations = new Vector<Confirmation>();
		confirmations.addAll(results);
		fireTableDataChanged();
	}
}

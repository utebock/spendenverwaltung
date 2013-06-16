package at.fraubock.spendenverwaltung.gui.components;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;

/**
 * TableModel for unconfirmed mailings using the UnconfirmedMailing DTO
 * 
 * @author Chris
 */

public class UnconfirmedMailingTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Vector<UnconfirmedMailing> unconfirmedMailings = new Vector<UnconfirmedMailing>();
	
	private String[] columnNames = new String[] { "Datum",
			"Aussendungstyp", "Medium", "Ersteller" };
	
	public void addUnconfirmedMailing(UnconfirmedMailing m) {
		unconfirmedMailings.add(m);
		fireTableDataChanged();
	}
	
	public void removeUnconfirmedMailing(UnconfirmedMailing m) {
		unconfirmedMailings.remove(m);
		fireTableDataChanged();
	}
	
	public void addUnconfirmedMailings(List<UnconfirmedMailing> unconfirmedMailings) {
		this.unconfirmedMailings.addAll(unconfirmedMailings);
		fireTableDataChanged();
	}
	
	public void clear() {
		this.unconfirmedMailings = new Vector<UnconfirmedMailing>();
	}
	
	@Override
	public int getRowCount() {
		return unconfirmedMailings.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}
	
	public Class<?> getColumnClass(int col) {

		switch (col) {
		case 0:
			return java.util.Date.class;
		case 1:
			return Mailing.MailingType.class;
		case 2:
			return Mailing.Medium.class;
		case 3:
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
	
	public UnconfirmedMailing getRow(int rowIndex) {
		if(rowIndex < 0 || rowIndex >= getRowCount()) {
			return null;
		}
		
		return unconfirmedMailings.get(rowIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0:
				return unconfirmedMailings.get(rowIndex).getMailing().getDate();
			case 1:
				return unconfirmedMailings.get(rowIndex).getMailing().getType();
			case 2:
				return unconfirmedMailings.get(rowIndex).getMailing().getMedium();
			case 3:
				return unconfirmedMailings.get(rowIndex).getCreator();
			default:
			return null;
		}
	}

	public List<UnconfirmedMailing> getUnconfirmedMailings() {
		return unconfirmedMailings;
	}

}

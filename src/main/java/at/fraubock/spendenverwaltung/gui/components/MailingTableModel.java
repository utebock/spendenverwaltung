package at.fraubock.spendenverwaltung.gui.components;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;

public class MailingTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Vector<Mailing> mailings = new Vector<Mailing>();
	
	private String[] columnNames = new String[] { "Datum",
			"Aussendungstyp", "Medium", "Vorlage" };
	
	public void addMailing(Mailing m) {
		mailings.add(m);
		fireTableDataChanged();
	}
	
	public void removeMailing(Mailing m) {
		mailings.remove(m);
		fireTableDataChanged();
	}
	
	public void addMailings(List<Mailing> mailings) {
		this.mailings.addAll(mailings);
		fireTableDataChanged();
	}
	
	public void clear() {
		this.mailings = new Vector<Mailing>();
		fireTableDataChanged();
	}
	
	@Override
	public int getRowCount() {
		return mailings.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public Class<?> getColumnClass(int col) {

		switch (col) {
		case 0:
			return java.util.Date.class;
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
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0:
				return mailings.get(rowIndex).getDate();
			case 1:
				return mailings.get(rowIndex).getType().getName();
			case 2:
				return mailings.get(rowIndex).getMedium().getName();
			case 3:
				if(mailings.get(rowIndex).getTemplate() != null) {
					return mailings.get(rowIndex).getTemplate().getFileName();
				} else {
					return "Keine Vorlage vorhanden";
				}
			default:
			return null;
		}
	}

	public Mailing getRow(int rowIndex) {
		if(rowIndex < 0 || rowIndex >= getRowCount()) {
			return null;
		}
		
		return mailings.get(rowIndex);
	}

}

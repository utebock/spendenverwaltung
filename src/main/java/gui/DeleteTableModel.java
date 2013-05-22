package gui;

import javax.swing.table.DefaultTableModel;

public class DeleteTableModel extends DefaultTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Loeschen", "Id", "Vorname", "Nachname", "Adresse"};

	public Class<?> getColumnClass(int col) {
		
		switch (col) {
		case 0:
			return Boolean.class;
		case 1:
			return Integer.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
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
		return true;
	}
}


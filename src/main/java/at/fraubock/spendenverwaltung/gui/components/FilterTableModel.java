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
package at.fraubock.spendenverwaltung.gui.components;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;

public class FilterTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Name", "Typ"};
	private Vector<Filter> filters = new Vector<Filter>();

	public void addFilter (Filter filter){
		filters.add(filter);
	}
	
	public void removeFilter (int row){
		filters.remove(row);
		fireTableDataChanged();
	}
	
	public Filter getFilterRow(int rowIndex){
		return filters.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return filters.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Filter filter = (Filter)filters.get(rowIndex);
		
		switch(columnIndex){
			case 0: return filter.getName();
			case 1: return filter.getType().getDisplayName();
			default: return null;
		}
	}
	
	public Class<?> getColumnClass(int col) {
		
		switch (col) {
		case 0:
			return String.class;
		case 1:
			return Enum.class;
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


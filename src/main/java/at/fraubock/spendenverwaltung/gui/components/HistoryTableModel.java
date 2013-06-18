package at.fraubock.spendenverwaltung.gui.components;

import java.util.Date;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.util.Pager;

public class HistoryTableModel extends AbstractTableModel implements
		PageableTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = new String[] { "Benutzer", "Aktion",
			"Objekt", "Datum", "Daten" };
	private Vector<Action> actions = new Vector<Action>();
	private Pager<Action> pager;

	public HistoryTableModel(Pager<Action> pager) {
		this.pager = pager;
	}

	@Override
	public int getRowCount() {
		return actions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Action action = (Action) actions.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return action.getActor();
		case 1:
			return action.getType().toString();
		case 2:
			return action.getEntity().toString();
		case 3:
			return action.getTime();
		case 4:
			return action.getPayload();
		default:
			return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {

		switch (col) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return Date.class;
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
		return false;
	}

	@Override
	public long getPageCount() throws ServiceException {
		return pager.getNumberOfPages();
	}

	@Override
	public int getPosition() {
		return pager.getCurrentPosition();

	}

	@Override
	public void showPage(int index) throws ServiceException {
		actions.clear();
		actions.addAll(pager.getPage(index));
		fireTableDataChanged();
	}

	public void refreshPage() throws ServiceException {
		showPage(getPosition());
	}
	
	public void setPager(Pager<Action> pager) {
		this.pager = pager;
	}
}

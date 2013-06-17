package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface PageableTableModel {
	
	public long getPageCount() throws ServiceException;
	
	public int getPosition();
	
	public void showPage(int index) throws ServiceException;
	
}

package at.fraubock.spendenverwaltung.util;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface Pager<T> {

	public List<T> getPage(int index) throws ServiceException;

	public List<T> getPreviousPage() throws ServiceException;
	
	public List<T> getNextPage() throws ServiceException;
	
	public int getCurrentPosition();
	
	public long getNumberOfPages() throws ServiceException;
	
	public int getPageSize();
	
	
}

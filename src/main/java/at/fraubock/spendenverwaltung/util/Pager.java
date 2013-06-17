package at.fraubock.spendenverwaltung.util;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * this interface provides functionality for accessing DAOs in a paging manner.
 * 
 * @author philipp muhoray
 * 
 * @param <T>
 *            - the object type to be retrieved from the DAO
 */
public interface Pager<T> {

	/**
	 * returns a list of objects of this pager's type. the index specifies the
	 * page to be returned.
	 * 
	 * @param index
	 * @return
	 * @throws ServiceException
	 */
	public List<T> getPage(int index) throws ServiceException;

	/**
	 * returns the index of the page that was requested at last.
	 * 
	 * @return
	 */
	public int getCurrentPosition();

	/**
	 * returns the amount of pages for this pager
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public long getNumberOfPages() throws ServiceException;

}

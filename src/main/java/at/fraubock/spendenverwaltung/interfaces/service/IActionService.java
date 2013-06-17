package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.util.Pager;

/**
 * service providing functionality for {@link Action}
 * 
 * @author philipp muhoray
 * 
 */
public interface IActionService {

	/**
	 * Creates a new action
	 * 
	 * @param a
	 *            Action to be created
	 * @return Fully defined action
	 */
	public Action create(Action a) throws ServiceException;

	/**
	 * Deletes an existing action
	 * 
	 * @param a
	 *            Action to be deleted
	 */
	public void delete(Action a) throws ServiceException;

	/**
	 * Retrieves entire Action table
	 * 
	 * @return List of all actions
	 */
	public List<Action> getAll() throws ServiceException;

	public Pager<Action> getAllAsPager(final int pageSizeParam) throws ServiceException;
}

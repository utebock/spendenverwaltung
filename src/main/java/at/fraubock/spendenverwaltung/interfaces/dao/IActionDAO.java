package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * interface to the data source for {@link Action} entities.
 * 
 * @author philipp muhoray
 * 
 */
public interface IActionDAO {

	/**
	 * Inserts a new action to the persistence layer and sets it's id.
	 * 
	 * @param a
	 *            Action to be inserted
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insert(Action a) throws PersistenceException;

	/**
	 * Deletes an action from the underlying persistence layer.
	 * 
	 * @param a
	 *            Action to be deleted. Its id must be set, i.e. it must be
	 *            persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Action a) throws PersistenceException;

	/**
	 * Retrieves all actions stored in the underlying persistence layer.
	 * 
	 * @return List of all actions, sorted by date descending.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Action> getAll() throws PersistenceException;
	
	/**
	 * Retrieves the number of actions given in <code>count</code> starting from
	 * the given <code>offset</code>.
	 * 
	 * @param offset - starting from this result
	 * @param count - retrieving this amount of results
	 * @return List of actions from offset plus count, sorted by date descending.
	 * @throws PersistenceException
	 */
	public List<Action> getAllLimitResult(int offset, int count) throws PersistenceException;
	
	public long countResultsOfAll() throws PersistenceException;
}

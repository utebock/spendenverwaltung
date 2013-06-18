package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.ActionAttribute;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;

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
	 * @param offset
	 *            - starting from this result
	 * @param count
	 *            - retrieving this amount of results
	 * @return List of actions from offset plus count, sorted by date
	 *         descending.
	 * @throws PersistenceException
	 */
	public List<Action> getAllWithLimitedResult(int offset, int count)
			throws PersistenceException;

	/**
	 * returns the amount of all persisted actions
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public long countResultsOfAll() throws PersistenceException;

	/**
	 * Retrieves the number of actions given in <code>count</code> starting from
	 * <code>offset</code>, where the attribute given in the
	 * {@link ActionAttribute} contains <code>value</code>
	 * 
	 * @param attribute
	 *            - the attribute to be compared
	 * @param value
	 *            - the value the attribute will be compared with (LIKE)
	 * @param offset
	 *            - starting from this result
	 * @param count
	 *            - retrieving this amount of results
	 * @return List of actions from offset plus count, sorted by date descending
	 *         where attribute LIKE value
	 * @throws PersistenceException
	 */
	public List<Action> getLimitedResultByAttributesLike(
			ActionAttribute attribute, String value, int offset, int count)
			throws PersistenceException;

	/**
	 * returns the amount of all persisted actions where the attribute given in
	 * the {@link ActionAttribute} contains the given <code>value</code>
	 * 
	 * @param attribute
	 *            - the attribute to be compared
	 * @param value
	 *            - the value the attribute will be compared with (LIKE)
	 * @return - amount of persisted actions
	 * @throws PersistenceException
	 */
	public long countResultsOfAttributeLike(ActionAttribute attribute,
			String value) throws PersistenceException;

	public List<Action> getLimitedResultByAttributesLike(
			ActionSearchVO searchVO, int offset, int count)
			throws PersistenceException;

	public long countResultsOfAttributesLike(ActionSearchVO searchVO)
			throws PersistenceException;
}

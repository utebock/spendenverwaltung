package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;

/**
 * interface to the data source for {@link Action} entities.
 * 
 * @author philipp muhoray
 * 
 */
public interface IActionDAO {

	/**
	 * Retrieves the number of actions given in <code>count</code> starting from
	 * <code>offset</code>, whose attributes match the values given in the
	 * {@link ActionSearchVO}
	 * 
	 * @param searchVO
	 *            - a value object containing the values to be searched for
	 * @param offset
	 *            - starting from this result
	 * @param count
	 *            - retrieving this amount of results
	 * @return List of actions from offset plus count, sorted by date descending
	 *         where attributes match given values
	 * @throws PersistenceException
	 */

	public List<Action> getLimitedResultByAttributes(ActionSearchVO searchVO,
			int offset, int count) throws PersistenceException;

	/**
	 * returns the amount of all persisted actions whose attributes match the
	 * values given in the {@link ActionSearchVO}
	 * 
	 * @param searchVO
	 *            - a value object containing the values to be searched for
	 * @return - amount of persisted actions
	 * @throws PersistenceException
	 */
	public long getNumberOfResultsByAttributes(ActionSearchVO searchVO)
			throws PersistenceException;
}

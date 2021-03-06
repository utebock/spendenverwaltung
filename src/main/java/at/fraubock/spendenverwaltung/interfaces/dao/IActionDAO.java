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
	 * Retrieves actions whose attributes match the values given in the
	 * {@link ActionSearchVO}. retrieves <code>count</code> actions starting
	 * from <code>offset</code>
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
	 * Retrieves actions whose attributes match the values given in the
	 * {@link ActionSearchVO} and whose actor attribute does not match the
	 * current user's name. retrieves <code>count</code> actions starting from
	 * <code>offset</code>
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
	public List<Action> getByAttributesFromOthers(ActionSearchVO searchVO,
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

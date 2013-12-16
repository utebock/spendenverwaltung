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
package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.Date;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;
import at.fraubock.spendenverwaltung.util.Pager;

/**
 * service providing functionality for {@link Action}
 * 
 * @author philipp muhoray
 * 
 */
public interface IActionService {

	/**
	 * returns a {@link Pager} instance that pages over all actions whose
	 * attributes match the values given in the {@link ActionSearchVO}. each
	 * page has the size of <code>pageSizeParam</code>
	 * 
	 * @param searchVO
	 *            - a value object containing the values to be searched for
	 * @param pageSizeParam
	 *            - size of a single page
	 * @return pager
	 * @throws ServiceException
	 */
	public Pager<Action> searchActions(final ActionSearchVO searchVOParam,
			final int pageSizeParam) throws ServiceException;

	/**
	 * returns the first <code>amount</code> of actions that occurred since the
	 * specified <code>date</code>.
	 * 
	 * @param date
	 * @param amount
	 * @return
	 * @throws ServiceException
	 */
	public List<Action> pollForActionSince(Date date, int amount)
			throws ServiceException;
}

package at.fraubock.spendenverwaltung.interfaces.service;

import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.util.ActionAttribute;
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
	 * returns a {@link Pager} instance that pages over all existent actions.
	 * each page has the size of <code>pageSizeParam</code>
	 * 
	 * @param pageSizeParam
	 *            - size of a single page
	 * @return pager
	 * @throws ServiceException
	 */
	public Pager<Action> getAllAsPager(final int pageSizeParam)
			throws ServiceException;

	/**
	 * returns a {@link Pager} instance that pages over all actions where the
	 * attribute given in the {@link ActionAttribute} contains the given
	 * <code>valueParam</code> each page has the size of
	 * <code>pageSizeParam</code>
	 * 
	 * @param attribute
	 *            - the attribute to be compared
	 * @param value
	 *            - the value the attribute will be compared with (LIKE)
	 * @param pageSizeParam
	 *            - size of a single page
	 * @return pager
	 * @throws ServiceException
	 */
	public Pager<Action> getAttributeLikeAsPager(
			final ActionAttribute attributeParam, final String valueParam,
			final int pageSizeParam) throws ServiceException;
}

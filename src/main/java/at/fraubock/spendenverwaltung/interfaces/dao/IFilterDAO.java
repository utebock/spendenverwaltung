package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter.FilterPrivacyStatus;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * interface to the data source for {@link Filter} entities.
 * 
 * @author philipp muhoray
 * 
 */
public interface IFilterDAO {

	/**
	 * Inserts a new filter to the persistence layer (if its id is null or not
	 * yet existent). Its id will be set and its owner will be set to the
	 * current database user.
	 * 
	 * @param f
	 *            Filter to be inserted
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insert(Filter f) throws PersistenceException;

	/**
	 * Deletes a filter from the underlying persistence layer.
	 * 
	 * @param f
	 *            the filter to be deleted. Its id must be set, i.e. it must be
	 *            persisted or retrieved by this DAO. Its owner must either be
	 *            the current database user or its privacy status must be set to
	 *            {@link FilterPrivacyStatus#READ_UPDATE_DELETE}. Those two
	 *            values will not be read from the filter object committed, but
	 *            from the underlying persistence system.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Filter f) throws PersistenceException;

	/**
	 * Retrieves all filters stored in the underlying persistence layer for
	 * which the current database user has read rights.
	 * 
	 * @return List of all filters, sorted by id descending
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Filter> getAll() throws PersistenceException;

	/**
	 * Retrieves Filter by ID
	 * 
	 * @param id
	 *            unique filter identification number
	 * @return the filter stored with the given id, or null if no such filter
	 *         exists or if the current database user has no read rights on the
	 *         filter
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public Filter getById(int id) throws PersistenceException;

	/**
	 * 
	 * @return name of the user currently connected to the underlying
	 *         persistence system. Must be equal to {@link Filter#getOwner()}
	 *         for filters that are owned by the invoking user
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public String getCurrentUserName() throws PersistenceException;

}

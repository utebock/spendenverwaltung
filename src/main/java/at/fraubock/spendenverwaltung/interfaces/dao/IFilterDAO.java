package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

public interface IFilterDAO {

	/**
	 * Inserts a new filter to the persistence layer (if its id is null or not
	 * yet existent) If the filter is inserted, its id will be set.
	 * 
	 * @param p
	 *            Filter to be inserted or updates
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insert(Filter f) throws PersistenceException;

	/**
	 * Deletes a filter from the underlying persistence layer.
	 * 
	 * @param p
	 *            the filter to be deleted. Its id must be set, i.e. it must be
	 *            persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Filter f) throws PersistenceException;

	/**
	 * Retrieves all filters stored in the underlying persistence layer
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
	 *         exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public Filter getById(int id) throws PersistenceException;

}

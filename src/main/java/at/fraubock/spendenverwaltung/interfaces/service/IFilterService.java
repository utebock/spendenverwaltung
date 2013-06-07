package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.FilterInUseException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.util.FilterType;

/**
 * service providing functionality for {@link Filter}
 * 
 * @author philipp muhoray
 * 
 */
public interface IFilterService {

	/**
	 * Creates a new filter
	 * 
	 * @param d
	 *            Filter to be created
	 * @return Fully defined filter
	 */
	public Filter create(FilterTO f) throws ServiceException;

	/**
	 * Updates an existing filter
	 * 
	 * @param d
	 *            Filter to be updated
	 * @return Updated filter
	 */
	public Filter update(Filter f, FilterTO to) throws ServiceException;

	/**
	 * Deletes an existing filter
	 * 
	 * @param d
	 *            Filter to be deleted
	 * @throws FilterInUseException
	 *             when filter can not be deleted because it is used as mounted
	 *             filter
	 */
	public void delete(Filter f) throws ServiceException, FilterInUseException;

	/**
	 * Retrieves entire {@link Filter} table
	 * 
	 * @return List of all filters
	 */
	public List<Filter> getAll() throws ServiceException;

	/**
	 * Retrieves all {@link Filter} of the given {@link FilterType}
	 * 
	 * @return List of all filters of given type
	 */
	public List<Filter> getAll(FilterType type) throws ServiceException;

	/**
	 * Retrieves Filter by ID
	 * 
	 * @param id
	 *            unique filter identification number
	 * @return Filter based on given id or NULL if id non existent
	 */
	public Filter getByID(int id) throws ServiceException;

	/**
	 * Puts all Filter Types in a String Array
	 * 
	 * @return Returns a String Array including all Filter Types
	 */
	// public String[] getFilterTypes();
}

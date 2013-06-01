package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.service.to.FilterTO;


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
	public FilterTO create(FilterTO f) throws ServiceException;

	/**
	 * Updates an existing filter
	 * 
	 * @param d
	 *            Filter to be updated
	 * @return Updated filter
	 */
	public Filter update(Filter f) throws ServiceException;

	/**
	 * Deletes an existing filter
	 * 
	 * @param d
	 *            Filter to be deleted
	 */
	public void delete(Filter f) throws ServiceException;
	
	/**
	 * Retrieves entire {@link Filter} table
	 * @return List of all filters
	 */
	public List<Filter> getAll() throws ServiceException;

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
//	public String[] getFilterTypes();
}

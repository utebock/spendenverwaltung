package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;
import java.io.IOException;
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
	 * Updates an existing filter by deleting the given filter and creating a
	 * new one from the given FilterTO.
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
	 * Retrieves all <b>non-anonymous</b> {@link Filter} of the given
	 * {@link FilterType}
	 * 
	 * @return List of all filters of given type that are not anonymous
	 */
	public List<Filter> getAllByFilter(FilterType type) throws ServiceException;

	/**
	 * Retrieves all {@link Filter} depending on their anonymous state
	 * 
	 * @return List of all filters depending on their anonymous state
	 */
	public List<Filter> getAllByAnonymous(boolean anonymous)
			throws ServiceException;

	/**
	 * Retrieves Filter by ID
	 * 
	 * @param id
	 *            unique filter identification number
	 * @return Filter based on given id or NULL if id non existent
	 */
	public Filter getByID(int id) throws ServiceException;

	/**
	 * Takes the id of a filter and converts the list of those entities matching
	 * this filter to a CSV as specified by
	 * {@link IAddressService#convertToCSV(List)},
	 * {@link IPersonService#convertToCSV(List)},
	 * {@link IDonationService#convertToCSV(List)},
	 * {@link IMailingService#convertToCSV(List)}, depending on which type the
	 * filter is of.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param id
	 *            the id of a filter
	 * @return CSV string of the filter's results, or null if there is no filter
	 *         with such an id.
	 * @throws ServiceException
	 */
	public String convertResultsToCSVById(int id) throws ServiceException;

	/**
	 * Takes the id of a filter and saves the list of those entities matching
	 * this filter as a CSV file as specified by
	 * {@link IAddressService#saveAsCSV(List, java.io.File)},
	 * {@link IPersonService#saveAsCSV(List, java.io.File)},
	 * {@link IDonationService#saveAsCSV(List, java.io.File)},
	 * {@link IMailingService#saveAsCSV(List, java.io.File)}, depending on which
	 * type the filter is of.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param id
	 *            the id of a filter
	 * @param csvFile
	 *            the file the CSV should be saved to. Not null.
	 * @return true if everything went fine, false if there is no filter with
	 *         the given id.
	 * @throws IOException
	 *             if writing to the file failed
	 * @throws ServiceException
	 */
	public boolean saveResultsAsCSVById(int id, File csvFile)
			throws ServiceException, IOException;
}

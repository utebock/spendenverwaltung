package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * service providing functionality for {@link Address}
 * 
 * @author philipp muhoray
 * 
 */
public interface IAddressService {

	/**
	 * Creates a new address
	 * 
	 * @param a
	 *            Address to be created
	 * @return Fully defined address
	 */
	public Address create(Address a) throws ServiceException;

	/**
	 * Updates an existing address
	 * 
	 * @param a
	 *            Address to be updated
	 * @return Updated address
	 */
	public Address update(Address a) throws ServiceException;

	/**
	 * Deletes an existing address
	 * 
	 * @param a
	 *            Address to be deleted
	 */
	public void delete(Address a) throws ServiceException;

	/**
	 * Retrieves entire Address table
	 * 
	 * @return List of all addresses
	 */
	public List<Address> getAll() throws ServiceException;

	/**
	 * Retrieves Address by ID
	 * 
	 * @param id
	 *            unique address identification number
	 * @return Address based on given id or NULL if id non existent
	 */
	public Address getByID(int id) throws ServiceException;

	/**
	 * converts a list of addresses to a CSV string.
	 * 
	 * @author manuel-bichler
	 * @param addresses
	 *            a list of addresses. not null.
	 * @return CSV representation of the addresses
	 */
	public String convertToCSV(List<Address> addresses);

	/**
	 * Converts a list of addresses to a CSV and saves it as the given file.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param addresses
	 *            a list of addresses. not null.
	 * @param csvFile
	 *            the file the CSV should be saved to. Not null.
	 * @throws IOException
	 *             if writing the file failed
	 */
	public void saveAsCSV(List<Address> addresses, File csvFile)
			throws IOException;
}

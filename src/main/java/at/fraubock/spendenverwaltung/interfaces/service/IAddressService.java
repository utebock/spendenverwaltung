package at.fraubock.spendenverwaltung.interfaces.service;

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
}

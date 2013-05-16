package dao;

import java.util.List;

import domain.Address;
import exceptions.PersistenceException;

/**
 * interface to the data source for {@link Address} entities.
 * 
 * @author philipp muhoray
 * 
 */
public interface IAddressDAO {

	/**
	 * Creates a new address
	 * 
	 * @param a
	 *            Address to be created
	 * @return Fully defined address
	 */
	public Address create(Address a) throws PersistenceException;

	/**
	 * Updates an existing address
	 * 
	 * @param a
	 *            Address to be updated
	 * @return Updated address
	 */
	public Address update(Address a) throws PersistenceException;

	/**
	 * Deletes an existing address
	 * 
	 * @param a
	 *            Address to be deleted
	 */
	public void delete(Address a) throws PersistenceException;

	/**
	 * Retrieves entire Address table
	 * 
	 * @return List of all addresses
	 */
	public List<Address> getAll() throws PersistenceException;

	/**
	 * Retrieves Address by ID
	 * 
	 * @param id
	 *            unique address identification number
	 * @return Address based on given id
	 */
	public Address getByID(int id) throws PersistenceException;
}

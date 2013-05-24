package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;


/**
 * interface to the data source for {@link Address} entities.
 * 
 * @author philipp muhoray
 * @author manuel-bichler
 * 
 */
public interface IAddressDAO {

	/**
	 * Inserts a new address to the persistence layer (if its id is null or not
	 * yet existent) or updates the address with the already existent id. If the
	 * address is inserted, its id will and other fields may be set.
	 * 
	 * Note that updating an address may affect several persons.
	 * 
	 * @param a
	 *            Address to be inserted or updated
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insertOrUpdate(Address a) throws PersistenceException;

	/**
	 * Deletes an address from the underlying persistence layer.
	 * 
	 * Note that deleting an address may affect several persons.
	 * 
	 * Note that deleting an address may result in persons having several
	 * addresses, but no main address specified.
	 * 
	 * @param a
	 *            Address to be deleted. Its id must be set, i.e. it must be
	 *            persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Address a) throws PersistenceException;

	/**
	 * Retrieves all addresses stored in the underlying persistence layer
	 * 
	 * @return List of all addresses, sorted by id descending.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Address> getAll() throws PersistenceException;

	/**
	 * Retrieves Address by ID
	 * 
	 * @param id
	 *            unique address identification number
	 * @return the address stored with the given id, or null if no such address
	 *         exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public Address getByID(int id) throws PersistenceException;

	/**
	 * Retrieves the main address of a given person stored in the underlying
	 * persistence layer
	 * 
	 * @deprecated persons retrieved by {@link IPersonDAO} already have their
	 *             main address set
	 * @param person
	 *            the person to retrieve the main address from, must have been
	 *            persisted or retrieved using an {@link IPersonDAO} prior to
	 *            calling this method
	 * @return the main address of the given person, or null if the given person
	 *         has no main address
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	@Deprecated
	public Address getMainAddressByPerson(Person person)
			throws PersistenceException;
}
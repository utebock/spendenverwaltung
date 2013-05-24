package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.PersonFilter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;


/**
 * interface to a data source for {@link Person} entities
 * 
 * @author manuel-bichler
 * 
 */
public interface IPersonDAO {

	/**
	 * Inserts a new person to the persistence layer (if its id is null or not
	 * yet existent) or updates the person with the already existent id. If the
	 * person is inserted, its id will and other fields may be set.
	 * 
	 * Addresses held by this person must have been persisted or retrieved using
	 * an {@link IAddressDAO} prior to calling this method.
	 * 
	 * @param p
	 *            Person to be inserted or updates
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insertOrUpdate(Person p) throws PersistenceException;

	/**
	 * Deletes a person from the underlying persistence layer.
	 * 
	 * Does not delete addresses held by this person.
	 * 
	 * @param p
	 *            the person to be deleted. Its id must be set, i.e. it must be
	 *            persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Person p) throws PersistenceException;

	/**
	 * Retrieves all persons stored in the underlying persistence layer
	 * 
	 * @return List of all persons, sorted by id descending, including their
	 *         addresses.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Person> getAll() throws PersistenceException;

	/**
	 * Retrieves Person by ID
	 * 
	 * @param id
	 *            unique person identification number
	 * @return the person stored with the given id (including its addresses), or
	 *         null if no such person exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public Person getById(int id) throws PersistenceException;

	/**
	 * Retrieves all persons having the given address
	 * 
	 * @param address
	 *            the address the desired persons should have. This address must
	 *            have an id, i.e. must have been persisted or retrieved by an
	 *            {@link IAddressDAO} prior to calling this method.
	 * @return a list of persons sharing the given address, sorted by id
	 *         descending, including their addresses
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Person> getByAddress(Address address)
			throws PersistenceException;

	/**
	 * Retrieves those persons matching the given filter
	 * 
	 * @param filter
	 *            the filter to be used
	 * @return a list of all those persons matching the given filter, sorted by
	 *         id descending, including their addresses
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Person> getByFilter(PersonFilter filter)
			throws PersistenceException;
}
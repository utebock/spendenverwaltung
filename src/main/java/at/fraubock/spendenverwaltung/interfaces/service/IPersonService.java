package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;


public interface IPersonService {

	/**
	 * Creates a new person
	 * @param p Person to be created
	 * @return Fully defined person
	 */
	public Person create(Person p) throws ServiceException;
	
	/**
	 * Updates an existing person
	 * @param p Person to be updated
	 * @return Updated person
	 */
	public Person update(Person p) throws ServiceException;
	
	/**
	 * Deletes an existing person
	 * @param p Person to be deleted
	 */
	public void delete(Person p) throws ServiceException;

	/**
	 * Deletes an existing address
	 * @param a
	 * @throws ServiceException
	 */
	public void deleteAddressAndUpdatePerson(Address a, Person p) throws ServiceException;
	
	/**
	 * Retrieves entire Person table
	 * @return List of all persons
	 */
	public List<Person> getAll() throws ServiceException;
	
	/**
	 * Retrieves {@link Person}s matching the given {@link Filter}
	 * @return List of all persons matching the given filter
	 */
	public List<Person> getByFilter(Filter filter) throws ServiceException;
	
	/**
	 * Retrieves Person by ID
	 * @param id unique person identification number
	 * @return Person based on retrieved id
	 */
	public Person getById(int id) throws ServiceException;
	
}

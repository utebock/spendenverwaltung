package dao;

import java.util.List;

import domain.Person;
import domain.PersonFilter;
import exceptions.PersistenceException;

public interface IPersonDAO {

	/**
	 * Creates a new person
	 * 
	 * @param p
	 *            Person to be created
	 * @return Fully defined person
	 */
	public Person create(Person p) throws PersistenceException;

	/**
	 * Updates an existing person
	 * 
	 * @param p
	 *            Person to be updated
	 * @return Updated person
	 */
	public Person update(Person p) throws PersistenceException;

	/**
	 * Deletes an existing person
	 * 
	 * @param p
	 *            Person to be deleted
	 */
	public void delete(Person p) throws PersistenceException;

	/**
	 * Retrieves entire Person table
	 * 
	 * @return List of all persons
	 */
	public List<Person> getAll() throws PersistenceException;

	/**
	 * Retrieves Person by ID
	 * 
	 * @param id
	 *            unique person identification number
	 * @return Person based on retrieved id
	 */
	public Person getById(int id) throws PersistenceException;

	/**
	 * Retrieves those persons matching the given filter
	 * 
	 * @param filter
	 *            the filter to be used
	 * @return a list of all those persons matching the given filter
	 * @throws PersistenceException
	 */
	public List<Person> getByFilter(PersonFilter filter)
			throws PersistenceException;
}

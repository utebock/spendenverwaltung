package service;

import java.util.List;

import domain.Person;
import exceptions.ServiceException;

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
	 * Retrieves entire Person table
	 * @return List of all persons
	 */
	public List<Person> getAll() throws ServiceException;
	
	/**
	 * Retrieves Person by ID
	 * @param id unique person identification number
	 * @return Person based on retrieved id
	 */
	public Person getById(int id) throws ServiceException;
	
}

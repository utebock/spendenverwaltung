package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface IPersonService {

	/**
	 * Creates a new person
	 * 
	 * @param p
	 *            Person to be created
	 * @return Fully defined person
	 */
	public Person create(Person p) throws ServiceException;

	/**
	 * Updates an existing person
	 * 
	 * @param p
	 *            Person to be updated
	 * @return Updated person
	 */
	public Person update(Person p) throws ServiceException;

	/**
	 * Deletes an existing person
	 * 
	 * @param p
	 *            Person to be deleted
	 */
	public void delete(Person p) throws ServiceException;

	/**
	 * Deletes an existing address
	 * 
	 * @param a
	 * @throws ServiceException
	 */
	public void deleteAddressAndUpdatePerson(Address a, Person p)
			throws ServiceException;

	/**
	 * Retrieves entire Person table
	 * 
	 * @return List of all persons
	 */
	public List<Person> getAll() throws ServiceException;

	/**
	 * Retrieves all confirmed Persons
	 * 
	 * @return List of all confirmed persons
	 */
	public List<Person> getConfirmed() throws ServiceException;

	/**
	 * Retrieves confirmed {@link Person}s matching the given {@link Filter}
	 * 
	 * @return List of all confirmed persons matching the given filter
	 */
	public List<Person> getByFilter(Filter filter) throws ServiceException;

	/**
	 * Retrieves Person by ID
	 * 
	 * @param id
	 *            unique person identification number
	 * @return Person based on retrieved id
	 */
	public Person getById(int id) throws ServiceException;

	/**
	 * Retrieves Person by it's attributes
	 * 
	 * @param person
	 *            with parameters
	 * @return List of persons where the persons attributes matches
	 */
	public List<Person> getByAttributes(Person p) throws ServiceException;

	/**
	 * converts a list of persons to a CSV string.
	 * 
	 * @param persons
	 * @return CSV representation of the persons
	 */
	public String convertToCSV(List<Person> persons);

	/**
	 * Converts a list of persons to a CSV and saves it as the given file.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param persons
	 *            a list of persons
	 * @param csvFile
	 *            the file the CSV should be saved to
	 * @throws IOException
	 *             if writing the file failed
	 */
	public void saveAsCSV(List<Person> persons, File csvFile)
			throws IOException;

	/**
	 * returns all persons associated with a specific mailing
	 * 
	 * @param mailing
	 * @return
	 * @throws ServiceException
	 */
	public List<Person> getPersonsByMailing(Mailing mailing)
			throws ServiceException;

}

package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface IMailingService {
	
	/**
	 * Persists a new Mailing by calling the Persistence layer
	 * 
	 * @param m Mailing to be inserted or updated
	 * @return inserted mailing
	 * @throws ServiceException
	 *             if communication to the underlying persistence system failed
	 */
	public Mailing insertOrUpdate(Mailing m) throws ServiceException;

	/**
	 * Calls upon the persistence layer to delete a mailing
	 *
	 * @param m
	 *            mailing to be deleted. Its id must be set, i.e. it must be
	 *            persisted and retrievable by this DAO.
	 * @throws ServiceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Mailing m) throws ServiceException;

	/**
	 * Retrieves all mailings stored in the underlying persistence layer
	 * 
	 * @return List of all mailings, sorted by id descending.
	 * @throws ServiceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Mailing> getAll() throws ServiceException;

	/**
	 * Retrieves mailing by ID
	 * 
	 * @param id unique mailing identification number
	 * 
	 * @return the mailing stored with the given id, or null if no such mailing
	 *         exists
	 * @throws ServiceException
	 *             if communication to the underlying persistence system failed
	 */
	public Mailing getById(int id) throws ServiceException;

	/**
	 * Retrieves all mailings associated with a specific Person
	 *
	 * @param person
	 *           
	 * @return all mailings returned by the DAO method call
	 * 
	 * @throws ServiceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Mailing> getMailingsByPerson(Person person)
				throws ServiceException;

	
	/**
	 * converts a list of mailings to a CSV string.
	 * 
	 * @param mailings
	 * @return CSV representation of the mailings
	 */
	public String convertToCSV(List<Mailing> mailings);
	
}

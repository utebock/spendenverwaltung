package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface IMailingService {

	/**
	 * Persists a new Mailing by calling the Persistence layer
	 * 
	 * @param m
	 *            Mailing to be inserted or updated
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
	 * @param id
	 *            unique mailing identification number
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
	public List<Mailing> getConfirmedMailingsByPerson(Person person)
			throws ServiceException;

	/**
	 * converts a list of mailings to a CSV string.
	 * 
	 * @param mailings
	 * @return CSV representation of the mailings
	 */
	public String convertToCSV(List<Mailing> mailings);

	/**
	 * reproduces the document that was created by the given mailing. the
	 * resulting file will be identical to the one that was produced when the
	 * given mailing was initially created.
	 * 
	 * @param destination
	 * @param mailing
	 */
	public void reproduceDocument(Mailing mailing) throws ServiceException;

	/**
	 * returns all mailings that have been confirmed by a user
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<Mailing> getAllConfirmed() throws ServiceException;

	/**
	 * returns all mailings that have yet to be confirmed by a user
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<Mailing> getAllUnconfirmed() throws ServiceException;

	/**
	 * confirms a mailing
	 * 
	 * @param mailing
	 * @throws ServiceException
	 */
	public void confirmMailing(Mailing mailing) throws ServiceException;

	/**
	 * returns a list of unconfirmed mailings
	 * @return
	 * @throws ServiceException 
	 */
	List<UnconfirmedMailing> getUnconfirmedMailingsWithCreator() throws ServiceException;

	/**
	 * Exports the email addresses of all persons in the mailing to MailChimp
	 * @param mailing
	 * @param mailChimpListId
	 * 			ID of the list at MailChimp
	 * @return
	 * 			returns the amount of errors during import at MailChimp
	 * @throws ServiceException
	 */
	public int exportEMailsToMailChimp(Mailing mailing, String mailChimpListId) throws ServiceException;
}

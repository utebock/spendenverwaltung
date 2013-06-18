package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
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
	 * takes the id of a mailing and converts the list of its receivers to a CSV
	 * string.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param mailingId
	 *            the ID of a mailing
	 * @return CSV representation of the mailing's list of receivers (as
	 *         specified in {@link IPersonService#convertToCSV(List)}), or null
	 *         if there is no mailing with the given id
	 */
	public String convertReceiversToCSVById(int mailingId)
			throws ServiceException;

	/**
	 * Converts a list of mailings to a CSV and saves it as the given file.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param mailings
	 *            a list of mailings
	 * @param csvFile
	 *            the file the CSV should be saved to. Not null.
	 * @throws IOException
	 *             if writing the file failed
	 */
	public void saveAsCSV(List<Mailing> mailings, File csvFile)
			throws IOException;

	/**
	 * takes the id of a mailing, converts the list of its receivers to a CSV
	 * and saves it as the given file as specified by
	 * {@link IPersonService#saveAsCSV(List, File)}.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param mailingId
	 *            the ID of a mailing
	 * @param csvFile
	 *            the file the CSV should be saved to. Not null.
	 * @return true if everything went fine, false if there is no mailing with
	 *         the given id.
	 * @throws IOException
	 *             if writing the file failed
	 * @throws ServiceException
	 */
	public boolean saveReceiversAsCSVById(int mailingId, File csvFile)
			throws IOException, ServiceException;

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
	 * 
	 * @return
	 * @throws ServiceException
	 */
	List<UnconfirmedMailing> getUnconfirmedMailingsWithCreator()
			throws ServiceException;

	/**
	 * returns all mailings that match a specific mailing filter
	 * 
	 * @throws ServiceException
	 * 
	 */
	public List<Mailing> getByFilter(Filter filter) throws ServiceException;

	/**
	 * returns mailings that happened before or on the same day as date
	 * 
	 * @param date
	 * @return
	 * @throws ServiceException
	 */
	public List<Mailing> getBeforeDate(Date date) throws ServiceException;

	/**
	 * returns mailings that happened after or on the same day as date
	 * 
	 * @param date
	 * @return
	 * @throws ServiceException
	 */
	public List<Mailing> getAfterDate(Date date) throws ServiceException;

	/**
	 * returns mailings created between (including the specific days) before and
	 * after
	 * 
	 * @param before
	 * @param after
	 * @return
	 * @throws ServiceException
	 */
	public List<Mailing> getBetweenDates(Date before, Date after)
			throws ServiceException;

	/**
	 * deletes one person from a mailing
	 * 
	 * @param person
	 * @param mailing
	 * @throws ServiceException
	 */
	void deletePersonFromMailing(Person person, Mailing mailing)
			throws ServiceException;
	
	/**
	 * returns the number of persons who got this mailing
	 * 
	 * @param mailing
	 * @throws ServiceException
	 */
	public int getSize(Mailing mailing) throws ServiceException;
	
}

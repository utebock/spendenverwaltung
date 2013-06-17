package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * interface to the data source for {@link Mailing} entities.
 * 
 * @author Chris Steele
 * 
 */

public interface IMailingDAO {

	/**
	 * Persists a new Mailing (if its id is null or not yet existent) 
	 * or updates the Mailing with the already existent id. If the
	 * Mailing is inserted, the id value will be set.
	 * 
	 * @param m
	 *            Mailing to be inserted or updated
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insertOrUpdate(Mailing m) throws PersistenceException;

	/**
	 * Deletes a mailing from the underlying persistence layer.
	 *
	 * @param m
	 *            mailing to be deleted. Its id must be set, i.e. it must be
	 *            persisted and retrievable by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Mailing m) throws PersistenceException;

	/**
	 * Retrieves all mailings stored in the underlying persistence layer
	 * 
	 * @return List of all mailinges, sorted by id descending.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Mailing> getAll() throws PersistenceException;

	/**
	 * Retrieves mailing by ID
	 * 
	 * @param id
	 *            unique mailing identification number
	 * @return the mailing stored with the given id, or null if no such mailing
	 *         exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public Mailing getById(int id) throws PersistenceException;

	/**
	 * Retrieves all mailings associated with a specific Person
	 *
	 * @param person
	 *            the person to retrieve the main mailing from, must have been
	 *            persisted or retrieved using an {@link IPersonDAO} prior to
	 *            calling this method
	 * @return all mailings that have been sent to the specified person or null
	 * 			if no mailings were found
	 * 
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Mailing> getConfirmedMailingsByPerson(Person person)
				throws PersistenceException;

	/**
	 * returns all unconfirmed mailings
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	List<Mailing> getAllUnconfirmed() throws PersistenceException;

	/**
	 * returns all confirmed mailings
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	List<Mailing> getAllConfirmed() throws PersistenceException;
	
	/**
	 * confirms a mailing
	 * 
	 * @throws PersistenceException
	 */
	
	void confirmMailing(Mailing mailing) throws PersistenceException;

	/**
	 * returns the creator of a mailing as a string
	 * 
	 * @param m
	 * @return
	 * @throws PersistenceException
	 */
	String getCreatorOfUnconfirmedMailing(Mailing m)
			throws PersistenceException;

	/**
	 * returns a list of unconfirmed mailings
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	List<UnconfirmedMailing> getUnconfirmedMailingsWithCreator()
			throws PersistenceException;

	/**
	 * removes a specific recipient (person) from an unsent mailing
	 * @param p
	 * @param m
	 * @throws PersistenceException
	 */
	void removePersonFromUnsentMailing(Person p, Mailing m)
			throws PersistenceException;

	/**
	 * returns all mailings matched by this filter
	 * @param filter
	 * @return
	 * @throws PersistenceException 
	 */
	List<Mailing> getMailingsByFilter(Filter filter) throws PersistenceException;

}

package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
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
	public Mailing getByID(int id) throws PersistenceException;

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
	public List<Mailing> getMailingsByPerson(Person person)
				throws PersistenceException;
}

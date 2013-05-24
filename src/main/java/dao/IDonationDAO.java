package dao;

import java.util.List;

import domain.Donation;
import domain.DonationFilter;
import domain.Person;
import exceptions.PersistenceException;

/**
 * Interface to the data source for {@link Donation} entities
 * 
 * @author Thomas
 * @author manuel-bichler
 * 
 */
public interface IDonationDAO {

	/**
	 * Inserts a new donation to the persistence layer (if its id is null or not
	 * yet existent) or updates the donation with the already existent id. If
	 * the donation is inserted, its id will and other fields may be set.
	 * 
	 * The donator of this donation must have been persisted or retrieved using
	 * an {@link IPersonDAO} prior to calling this method.
	 * 
	 * @param d
	 *            Donation to be inserted or updated
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insertOrUpdate(Donation d) throws PersistenceException;

	/**
	 * Deletes a donation from the underlying persistence layer.
	 * 
	 * @param d
	 *            donation to be deleted. Its id must be set, i.e. it must be
	 *            persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Donation d) throws PersistenceException;

	/**
	 * Retrieves all donations stored in the underlying persistence layer
	 * 
	 * @return List of all donations, sorted by id descending, including their
	 *         donators.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Donation> getAll() throws PersistenceException;

	/**
	 * Retrieves donation by ID
	 * 
	 * @param id
	 *            unique donation identification number
	 * @return the donation stored with the given id (including its donator), or
	 *         null if no such donation exists
	 */
	public Donation getByID(int id) throws PersistenceException;

	/**
	 * Retrieves donations by the given person
	 * 
	 * @param donator
	 *            the person having commissioned the desired donations. This
	 *            person must have an id, i.e. must have been persisted or
	 *            retrieved by an {@link IPersonDAO} prior to calling this
	 *            method.
	 * @return a list of all donations by the given person, sorted by id
	 *         descending, including their donator
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Donation> getByPerson(Person donator)
			throws PersistenceException;

	/**
	 * Retrieves donations matching the given filter
	 * 
	 * @param filter
	 *            the filter to be used
	 * @return a list of all those donations matching the given filter, sorted
	 *         by id descending, including their donators
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Donation> getByFilter(DonationFilter filter)
			throws PersistenceException;

	/**
	 * Calculates the sum of the donations matching the given filter.
	 * 
	 * Using this method might be more efficient than first retrieving all
	 * donation objects by {@link #getByFilter(DonationFilter)} (including their
	 * donators and their addresses) and then summing up their amounts,
	 * especially for non-local persistence systems.
	 * 
	 * @param filter
	 *            the filter to be used
	 * @return the sum of all those donations matching the given filter, in
	 *         EUR-cents
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public long sumByFilter(DonationFilter filter) throws PersistenceException;

}

package service;

import java.util.List;

import domain.Address;
import domain.Donation;
import domain.Person;
import exceptions.ServiceException;

/**
 * service providing functionality for {@link Address}
 * 
 * @author Thomas
 *
 */
public interface IDonationService {

	/**
	 * Creates a new donation
	 * 
	 * @param d
	 *            Donation to be created
	 * @return Fully defined donation
	 */
	public Donation create(Donation d) throws ServiceException;

	/**
	 * Updates an existing donation
	 * 
	 * @param d
	 *            Donation to be updated
	 * @return Updated donation
	 */
	public Donation update(Donation d) throws ServiceException;

	/**
	 * Deletes an existing donation
	 * 
	 * @param d
	 *            Donation to be deleted
	 */
	public void delete(Donation d) throws ServiceException;

	/**
	 * Retrieves Donation by ID
	 * 
	 * @param id
	 *            unique donation identification number
	 * @return Donation based on given id or NULL if id non existent
	 */
	public Donation getByID(int id) throws ServiceException;

	/**
	 * Retrieves entire Address table
	 * 
	 * @return List of all addresses
	 */
	public List<Donation> getByPerson(Person p) throws ServiceException;
}

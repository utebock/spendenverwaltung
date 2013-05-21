package dao;

import java.util.List;

import domain.Address;
import domain.Donation;
import domain.Person;
import exceptions.PersistenceException;

/**
 * 
 * @author Thomas
 *
 */
public interface IDonationDAO {

	/**
	 * Creates a new Donation
	 * @param d
	 * 		   Donation to be created
	 * @return Fully defined Donation
	 * @throws PersistenceException
	 */
	public Donation create(Donation d) throws PersistenceException;
	
	/**
	 * Updates an existing donation
	 * 
	 * @param d
	 *            Donation to be updated
	 * @return Updated donation
	 */
	public Donation update(Donation d) throws PersistenceException;

	/**
	 * Deletes an existing donation
	 * 
	 * @param d
	 *            Donatino to be deleted
	 */
	public void delete(Donation d) throws PersistenceException;

	/**
	 * Retrieves donation by ID
	 * 
	 * @param id
	 *            unique donation identification number
	 * @return Donation based on given id or NULL if id non existent
	 */
	public Donation getByID(int id) throws PersistenceException;

	/**
	 * Retrieves donations by PersonID
	 * 
	 * @param p
	 *            person which provides id for filtering
	 * @return List of donations for a single person with id (p.id) NULL if id non existent
	 */
	public List<Donation> getByPerson(Person p) throws PersistenceException;
	
	
}

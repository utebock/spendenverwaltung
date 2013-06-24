package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * 
 * @author romanvoglhuber
 *
 */
public interface IConfirmationDAO {
	
	/**
	 * Persists a new confirmation (if its id is null or not yet existent)
	 * or updates the confirmation with the already existing id. If the
	 * confirmation is inserted, the id value will be set
	 * @param c
	 * 			confirmation to be inserted or updated
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insertOrUpdate(Confirmation c) throws PersistenceException;
	
	/**
	 * Deletes a confirmation from the underlying persistence layer
	 * @param c
	 * 			confirmation to be deleted
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Confirmation c) throws PersistenceException;
	
	/**
	 * Retrieves all confirmations stored in the underlying persistence layer
	 * @return 
	 * 			List of all confirmations, sorted by id descending
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Confirmation> getAll() throws PersistenceException;
	
	/**
	 * Retrieves confirmation by ID
	 * @param id
	 * 			unique confirmation identification number
	 * @return
	 * 			the confirmation stored with the given id, or null if no such confirmation exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public Confirmation getById(int id) throws PersistenceException;
	
	/**
	 * Retrieves all confirmations with the given template stored in the underlying persistence layer 
	 * @param template
	 * 			ConfirmationTemplate with valid id
	 * @return
	 */
	public List<Confirmation> getByConfirmationTemplate(ConfirmationTemplate template) throws PersistenceException;
	
	/**
	 * Retrieves all confirmaitons where givenname or surname match the searchstring in an like query
	 * @param searchString
	 * @return
	 * @throws PersistenceException
	 */
	List<Confirmation> getByPersonNameLike(String searchString)
			throws PersistenceException;
}

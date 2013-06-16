package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

public interface IConfirmationTemplateDAO {

	/**
	 * Inserts a new {@link ConfirmationTemplate} to the persistence layer (if its 
	 * id is null or not yet existent)
	 * @param template
	 * 			ConfirmationTemplate to be inserted
	 * @throws PersistenceException
	 * 			if communication to the underlying persistence system failed
	 */
	public void insert(ConfirmationTemplate template) throws PersistenceException;
	
	/**
	 * Deletes an ConfirmationTemplate from the underlying persistence layer.
	 * @param template
	 * 			ConfirmationTemplate to be deleted. Its id must be set, i.e. it
	 * 			must be persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 * 			if communication to the underlying persistence system failed
	 */
	public void delete(ConfirmationTemplate template) throws PersistenceException;
	
	/**
	 * Retrieves all ConfirmationTemplates stored in the underlying persistence
	 * layer.
	 * 
	 * @return List of all ConfirmationTemplates, sorted by id descending.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<ConfirmationTemplate> getAll() throws PersistenceException;
	
	/**
	 * Retrieves ConfirmationTemplate by ID
	 * 
	 * @param id
	 *            unique ConfirmationTemplate identification number
	 * @return the ConfirmationTemplate stored with the given id, or null if no such
	 *         ConfirmationTemplate exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public ConfirmationTemplate getByID(int id) throws PersistenceException;
}

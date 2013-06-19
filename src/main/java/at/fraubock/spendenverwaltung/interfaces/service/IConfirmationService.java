package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * 
 * @author romanvoglhuber
 *
 */
public interface IConfirmationService {
	
	/**
	 * Inserts a new confirmation if its id is null or updates the confirmation
	 * @param confirmation
	 * 			Confirmation to be inserted or updated
	 * @return
	 * 			Confirmation with valid id
	 * @throws ServiceException
	 */
	public Confirmation insertOrUpdate(Confirmation confirmation) throws ServiceException;
	
	/**
	 * Retrieves a confirmation by ID
	 * @param id
	 * 			ID of confirmation
	 * @return
	 * 			Returns the confirmation with the given id or null if the id is not valid
	 * @throws ServiceException
	 */
	public Confirmation getById(int id) throws ServiceException;
	
	/**
	 * Deletes a confirmation
	 * @param confirmation
	 * 			Confirmation to be deleted
	 * @throws ServiceException
	 */
	public void delete(Confirmation confirmation) throws ServiceException;
	
	/**
	 * Retrieves all saved confirmations
	 * @return
	 * 			List of all confirmations, sorted by id descending
	 * @throws ServiceException
	 */
	public List<Confirmation> getAll() throws ServiceException;
	
	/**
	 * Inserts or updates a ConfirmationTemplate
	 * @param template
	 * 			ConfirmationTemplate with unique name
	 */
	public ConfirmationTemplate insertOrUpdateConfirmationTemplate(ConfirmationTemplate template) throws ServiceException;
	
	/**
	 * Deletes a confirmationTemplate if no confirmation uses this template
	 * @param template
	 * 			ConfirmationTemplate with valid id
	 * @throws ServiceException
	 */
	public void delete(ConfirmationTemplate template) throws ServiceException;
	
	/**
	 * Returns all saved ConfirmationTemplates
	 * @return List of ConfirmationTemplates
	 */
	public List<ConfirmationTemplate> getAllConfirmationTempaltes() throws ServiceException;
	
	/**
	 * Reproduces the created document by given confirmation
	 * @param confirmation
	 * 			Confirmation
	 */
	public void reproduceDocument(Confirmation confirmation) throws ServiceException;
	
}

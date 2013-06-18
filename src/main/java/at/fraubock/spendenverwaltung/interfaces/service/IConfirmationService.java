package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface IConfirmationService {
	
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

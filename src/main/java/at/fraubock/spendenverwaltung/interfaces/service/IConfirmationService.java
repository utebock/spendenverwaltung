/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;
import java.io.IOException;
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
	 * 
	 * @param confirmation
	 *            Confirmation to be inserted or updated
	 * @return Confirmation with valid id
	 * @throws ServiceException
	 */
	public Confirmation insertOrUpdate(Confirmation confirmation)
			throws ServiceException;

	/**
	 * Retrieves a confirmation by ID
	 * 
	 * @param id
	 *            ID of confirmation
	 * @return Returns the confirmation with the given id or null if the id is
	 *         not valid
	 * @throws ServiceException
	 */
	public Confirmation getById(int id) throws ServiceException;

	/**
	 * Deletes a confirmation
	 * 
	 * @param confirmation
	 *            Confirmation to be deleted
	 * @throws ServiceException
	 */
	public void delete(Confirmation confirmation) throws ServiceException;

	/**
	 * Retrieves all saved confirmations
	 * 
	 * @return List of all confirmations, sorted by id descending
	 * @throws ServiceException
	 */
	public List<Confirmation> getAll() throws ServiceException;

	/**
	 * Inserts or updates a ConfirmationTemplate
	 * 
	 * @param template
	 *            ConfirmationTemplate with unique name
	 */
	public ConfirmationTemplate insertOrUpdateConfirmationTemplate(
			ConfirmationTemplate template) throws ServiceException;

	/**
	 * Deletes a confirmationTemplate if no confirmation uses this template
	 * 
	 * @param template
	 *            ConfirmationTemplate with valid id
	 * @throws ServiceException
	 */
	public void delete(ConfirmationTemplate template) throws ServiceException;

	/**
	 * Returns all saved ConfirmationTemplates
	 * 
	 * @return List of ConfirmationTemplates
	 */
	public List<ConfirmationTemplate> getAllConfirmationTempaltes()
			throws ServiceException;

	/**
	 * Reproduces the created document by given confirmation
	 * 
	 * @param confirmation
	 *            Confirmation
	 * @param outputName
	 *            Name of output file
	 * @return
	 */
	public File reproduceDocument(Confirmation confirmation, String outputName)
			throws ServiceException;

	/**
	 * Returns a list where the givenname or surename of the donator matches the
	 * searchString
	 * 
	 * @param searchString
	 * @return
	 * @throws ServiceException
	 */
	List<Confirmation> getByPersonNameLike(String searchString)
			throws ServiceException;

	/**
	 * Reproduces the created document from the confirmation with the given id
	 * and saves the resulting PDF file.
	 * 
	 * @param id
	 *            the id of the confirmation to be reproduces
	 * @param outputFileName
	 *            the name of the file to save the generated file to
	 * @throws IOException
	 *             if reading/writing to/from the file failed
	 * @throws ServiceException
	 */
	public void reproduceById(int id, String outputFileName)
			throws ServiceException, IOException;

}

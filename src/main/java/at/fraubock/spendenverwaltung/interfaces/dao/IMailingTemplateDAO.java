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
package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * interface to the data source for {@link MailingTemplate} entities.
 * 
 * @author philipp muhoray
 * 
 */
public interface IMailingTemplateDAO {

	/**
	 * Inserts a new {@link MailingTemplate} to the persistence layer (if its id
	 * is null or not yet existent)
	 * 
	 * @param mt
	 *            MailingTemplate to be inserted
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insert(MailingTemplate mt) throws PersistenceException;

	/**
	 * Deletes an MailingTemplate from the underlying persistence layer.
	 * 
	 * @param mt
	 *            MailingTemplate to be deleted. Its id must be set, i.e. it
	 *            must be persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(MailingTemplate mt) throws PersistenceException;

	/**
	 * Retrieves all MailingTemplates stored in the underlying persistence
	 * layer, including unconfirmed ones.
	 * 
	 * @return List of all MailingTemplates, sorted by id descending.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<MailingTemplate> getAll() throws PersistenceException;

	/**
	 * Retrieves MailingTemplate by ID
	 * 
	 * @param id
	 *            unique MailingTemplate identification number
	 * @return the MailingTemplate stored with the given id, or null if no such
	 *         MailingTemplate exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public MailingTemplate getByID(int id) throws PersistenceException;
}

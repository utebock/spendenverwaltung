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

import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * Interface to the data source for {@link Import} entities
 * 
 * @author manuel-bichler
 * 
 */
public interface IImportDAO {
	/**
	 * Inserts a new import to the persistence layer (if its id is null or not
	 * yet existent) or updates the import with the already existent id. If the
	 * import is inserted, its id will be set. In both cases the creator will be
	 * ignored. When inserting, creator will be set to the current user. When
	 * updating, creator will not be changed.
	 * 
	 * @param i
	 *            import to be inserted or updated
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insertOrUpdate(Import i) throws PersistenceException;

	/**
	 * Deletes an import from the underlying persistence layer. Any donations
	 * contained in this import will be considered validated afterwards.
	 * 
	 * @param i
	 *            import to be deleted. Its id must be set, i.e. it must be
	 *            persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(Import i) throws PersistenceException;

	/**
	 * Retrieves all imports stored in the underlying persistence layer
	 * 
	 * @return List of all imports, sorted by id descending.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Import> getAll() throws PersistenceException;

	/**
	 * Retrieves all imports stored in the underlying persistence layer which are not confirmed yes
	 * @return List of all unconfirmed imports, sorted by id descending.
	 * @throws PersistenceException
	 * 				if communication to the underlying persistence system failed
	 */
	public List<Import> getAllUnconfirmed() throws PersistenceException;
	
	/**
	 * Retrieves import by ID
	 * 
	 * @param id
	 *            unique import identification number
	 * @return the import stored with the given id, or null if no such import
	 *         exists
	 */
	public Import getByID(int id) throws PersistenceException;
}

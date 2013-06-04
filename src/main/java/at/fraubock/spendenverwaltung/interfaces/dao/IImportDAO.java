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
	 * import is inserted, its id will and other fields may be set.
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
	 * Retrieves import by ID
	 * 
	 * @param id
	 *            unique import identification number
	 * @return the import stored with the given id, or null if no such import
	 *         exists
	 */
	public Import getByID(int id) throws PersistenceException;
}

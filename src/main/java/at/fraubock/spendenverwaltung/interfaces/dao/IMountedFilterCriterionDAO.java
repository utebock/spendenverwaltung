package at.fraubock.spendenverwaltung.interfaces.dao;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * DAO for {@link MountedCriterion} entities.
 * 
 * @author philipp muhoray
 * 
 */
public interface IMountedFilterCriterionDAO {

	/**
	 * Inserts a new {@link MountedFilterCriterion} to the persistence layer and
	 * sets the id.
	 * 
	 * @param f
	 *            MountedFilterCriterion to be inserted
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void insert(MountedFilterCriterion crit) throws PersistenceException;

	/**
	 * Retrieves MountedFilterCriterion by ID
	 * 
	 * @param id
	 *            unique MountedFilterCriterion identification number
	 * @return the criterion stored with the given id, or null if no such filter
	 *         exists
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public MountedFilterCriterion getById(int id) throws PersistenceException;

	/**
	 * Returns the ids of all existing MountedFilterCriterions.
	 * 
	 * @return list of all ids
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Integer> getAllMountedFilterIds() throws PersistenceException;

	/**
	 * replaces the given mountId of every existing MountedFilterCriterion with
	 * the id given in <code>replaceWith</code>
	 * 
	 * @param mountId
	 * @param replaceWith
	 * @throws PersistenceException
	 */
	public void replaceMountId(int mountId, int replaceWith)
			throws PersistenceException;

	/**
	 * Deletes a MountedFilterCriterion from the underlying persistence layer.
	 * 
	 * @param crit
	 *            the MountedFilterCriterion to be deleted. Its id must be set,
	 *            i.e. it must be persisted or retrieved by this DAO.
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public void delete(MountedFilterCriterion crit) throws PersistenceException;

}

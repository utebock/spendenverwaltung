package at.fraubock.spendenverwaltung.dao.criterion;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

public interface IMountedFilterCriterionDAO {

	public void insert(MountedFilterCriterion crit) throws PersistenceException;
	public MountedFilterCriterion getById(int id) throws PersistenceException;
	public List<Integer> getAllMountedFilterIds() throws PersistenceException;
	public void delete(MountedFilterCriterion crit) throws PersistenceException;
	
}

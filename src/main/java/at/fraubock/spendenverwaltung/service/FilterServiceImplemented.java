package at.fraubock.spendenverwaltung.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;

public class FilterServiceImplemented implements IFilterService {

	private IFilterDAO filterDAO;

	public IFilterDAO getFilterDAO() {

		return filterDAO;
	}

	public void setFilterDAO(IFilterDAO filterDAO) {
		this.filterDAO = filterDAO;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Filter create(Filter f) throws ServiceException {
		try {
			filterDAO.insertOrUpdate(f);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return f;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Filter update(Filter f) throws ServiceException {
		try {
			filterDAO.insertOrUpdate(f);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return f;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void delete(Filter f) throws ServiceException {
		try {
			filterDAO.delete(f);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<Filter> getAll() throws ServiceException {
		List<Filter> list = null;
		try {
			list = filterDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Filter getByID(int id) throws ServiceException {
		Filter filter = null;
		try {
			filter = filterDAO.getById(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return filter;
	}

}

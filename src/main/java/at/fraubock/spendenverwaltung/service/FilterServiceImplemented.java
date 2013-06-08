package at.fraubock.spendenverwaltung.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.dao.criterion.IMountedFilterCriterionDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.FilterInUseException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class FilterServiceImplemented implements IFilterService {

	private IFilterDAO filterDAO;
	private IMountedFilterCriterionDAO mountedCritDAO;

	public IFilterDAO getFilterDAO() {

		return filterDAO;
	}

	public void setFilterDAO(IFilterDAO filterDAO) {
		this.filterDAO = filterDAO;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Filter create(FilterTO f) throws ServiceException {
		try {
			Filter filter = createFilterFromTransferObject(f);
			filterDAO.insertOrUpdate(filter);
			return filter;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Filter update(Filter f, FilterTO to) throws ServiceException {
		try {
			Filter filter = create(to);
			mountedCritDAO.replaceMountId(f.getId(), filter.getId());
			delete(f);
			return filter;
		} catch (FilterInUseException e) {
			throw new ServiceException();
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void delete(Filter f) throws ServiceException, FilterInUseException {
		try {
			for (Integer id : mountedCritDAO.getAllMountedFilterIds()) {
				if (f.getId().equals(id)) {
					throw new FilterInUseException();
				}
			}

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
	public List<Filter> getAllByFilter(FilterType type) throws ServiceException {
		List<Filter> list = new ArrayList<Filter>();
		try {
			for (Filter filter : filterDAO.getAll()) {
				if (filter.getType() == type) {
					list.add(filter);
				}
			}
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Filter> getAllByAnonymous(boolean anonymous)
			throws ServiceException {
		List<Filter> list = new ArrayList<Filter>();
		for (Filter f : getAll()) {
			if (f.isAnonymous() == anonymous) {
				list.add(f);
			}
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

	private Filter createFilterFromTransferObject(FilterTO filterTO)
			throws ServiceException {
		Filter filter = new Filter();
		filter.setType(filterTO.getType());
		filter.setName(filterTO.getName());
		filter.setAnonymous(filterTO.isAnonymous());

		LogicalOperator operator = filterTO.getOperator();
		List<Criterion> crits = filterTO.getCriterions();

		if (crits.isEmpty()) {
			return filter;
		} else if (crits.size() == 1) {
			filter.setCriterion(crits.get(0));
			return filter;
		}

		ConnectedCriterion current = new ConnectedCriterion();
		current.connect(crits.get(0), operator, crits.get(1));

		for (int index = 2; index < crits.size(); index++) {
			ConnectedCriterion con = new ConnectedCriterion();
			con.connect(current, operator, crits.get(index));
			current = con;
		}
		filter.setCriterion(current);
		return filter;
	}

	public IMountedFilterCriterionDAO getMountedCritDAO() {
		return mountedCritDAO;
	}

	public void setMountedCritDAO(IMountedFilterCriterionDAO mountedCritDAO) {
		this.mountedCritDAO = mountedCritDAO;
	}

}

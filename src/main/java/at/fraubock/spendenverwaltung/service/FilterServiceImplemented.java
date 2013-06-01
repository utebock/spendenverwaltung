package at.fraubock.spendenverwaltung.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;
import at.fraubock.spendenverwaltung.service.to.FilterTO;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

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
	public FilterTO create(FilterTO f) throws ServiceException {
		try {
			filterDAO.insertOrUpdate(createFilterFromTransferObject(f));
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

	private Filter createFilterFromTransferObject(FilterTO filterTO)
			throws ServiceException {
		List<CriterionTO> crits = filterTO.getCriterions();
		List<LogicalOperator> ops = filterTO.getOperators();

		// if no or only one criterion was provided
		if (ops.isEmpty()) {
			if (crits.size() > 1) {
				// TODO log
				throw new ServiceException(
						"Illegal state of FilterTO: No logical oprator given "
								+ "but more than one criterion");
			}
			Criterion crit = null;
			if (crits.size() == 1) {
				crit = crits.get(0).createCriterion();
			}

			return new Filter(filterTO.getType(), crit);
		}

		// if more than one criterion was provided
		if (ops.size() != crits.size() - 1) {
			// TODO log
			throw new ServiceException(
					"Illegal state of FilterTO: Too many or few operators "
							+ "for the given amount of criterions");
		}

		/*
		 * iterate through all operators and build the filter tree NOTE: this
		 * will create a linear tree. so far, we don't provide prioritisation of
		 * operators ((a or b) and c) WITHOUT using mounted filters. therefore i
		 * don't care at this point how the tree is created, since it will end
		 * up in the same sql query anyway. (though this is not the exact
		 * interpretation of the filter tree, it is the fastest and most stable
		 * solution for now).
		 */
		ConnectedCriterion current = null;
		for (LogicalOperator op : ops) {
			int index = ops.indexOf(op);
			Criterion operand1 = null;
			if (current == null) {
				operand1 = crits.get(index).createCriterion();
			} else {
				operand1 = current;
			}
			ConnectedCriterion con = new ConnectedCriterion();
			con.connect(operand1, op, crits.get(index + 1).createCriterion());
			current = con;
		}

		return new Filter(filterTO.getType(), current);
	}

}

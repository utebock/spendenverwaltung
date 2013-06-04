package at.fraubock.spendenverwaltung.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class FilterServiceImplemented implements IFilterService {

	private static final Logger log = Logger
			.getLogger(FilterServiceImplemented.class);

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
	public List<Filter> getAll(FilterType type) throws ServiceException {
		List<Filter> list = new ArrayList<Filter>();
		try {
			for(Filter filter: filterDAO.getAll()) {
				if(filter.getType()==type) {
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

		List<Criterion> crits = filterTO.getCriterions();
		List<LogicalOperator> ops = filterTO.getOperators();
		
		// check if criterions and operators match in number
		if (ops.size() != crits.size() - 1 || crits.isEmpty() && !ops.isEmpty()) {
			log.error("Error building query from filterTO: n criterions"
					+ "must be connected with n-1 operators");
			throw new ServiceException(
					"Illegal state of FilterTO: Too many or few operators "
							+ "for the given amount of criterions");
		}
		
		if (ops.isEmpty()) {
			// one or no criterion was provided
			Criterion crit = null;
			if (crits.size() == 1) {
				crit = crits.get(0);
			}
			filter.setCriterion(crit);
			return filter;
		}

		/*
		 * iterate through all operators and build the filter tree. NOTE: this
		 * will create a linear tree. so far, we don't provide priorisation of
		 * operators ((a or b) and c) WITHOUT using mounted filters. therefore i
		 * don't care how the tree is created at this point, since it will end
		 * up in the same sql query anyway. though this is not the exact
		 * interpretation of the filter tree, it is the fastest and most stable
		 * solution for now. if this will be fixed, FilterToSqlBuilder needs to
		 * be fixed too (see comments in ConnectedCriterion part).
		 */
		ConnectedCriterion current = null;
		int index = 0;
		for (LogicalOperator op : ops) {
			Criterion operand1 = null;
			if (current == null) {
				// starting point, set first criterion as left child
				operand1 = crits.get(index);
			} else {
				// set the prior ConnectedCriterion to this one's left child
				operand1 = current;
			}
			ConnectedCriterion con = new ConnectedCriterion();
			// set the next criterion to this one's right child
			con.connect(operand1, op, crits.get(index + 1));
			current = con;
			index++;
		}
		filter.setCriterion(current);
		return filter;
	}
}

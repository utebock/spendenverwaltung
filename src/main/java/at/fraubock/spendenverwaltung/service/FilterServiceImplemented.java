package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMountedFilterCriterionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.FilterInUseException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.Pair;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;

import com.sun.xml.internal.txw2.IllegalSignatureException;

/**
 * implementation of {@link IFilterService}
 * 
 * @author philipp muhoray
 * 
 */
@SuppressWarnings("restriction")
public class FilterServiceImplemented implements IFilterService {

	private static final Logger log = Logger
			.getLogger(FilterServiceImplemented.class);

	private IFilterDAO filterDAO;
	private IMountedFilterCriterionDAO mountedCritDAO;

	private IAddressService addressService;
	private IPersonService personService;
	private IDonationService donationService;
	private IMailingService mailingService;

	/**
	 * @return the addressService
	 */
	public IAddressService getAddressService() {
		return addressService;
	}

	/**
	 * @param addressService
	 *            the addressService to set
	 */
	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	/**
	 * @return the personService
	 */
	public IPersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService
	 *            the personService to set
	 */
	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	/**
	 * @return the donationService
	 */
	public IDonationService getDonationService() {
		return donationService;
	}

	/**
	 * @param donationService
	 *            the donationService to set
	 */
	public void setDonationService(IDonationService donationService) {
		this.donationService = donationService;
	}

	/**
	 * @return the mailingService
	 */
	public IMailingService getMailingService() {
		return mailingService;
	}

	/**
	 * @param mailingService
	 *            the mailingService to set
	 */
	public void setMailingService(IMailingService mailingService) {
		this.mailingService = mailingService;
	}

	public IFilterDAO getFilterDAO() {

		return filterDAO;
	}

	public void setFilterDAO(IFilterDAO filterDAO) {
		this.filterDAO = filterDAO;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Filter create(FilterTO f) throws ServiceException {
		try {
			Filter filter = createFilterFromTransferObject(f);
			filterDAO.insert(filter);
			return filter;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
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
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public void delete(Filter f) throws ServiceException, FilterInUseException {
		if (f == null) {
			log.error("Filter was null in delete.");
			throw new ServiceException();
		}

		if (f.getId() == null) {
			log.error("Filter had no ID in delete");
			throw new ServiceException();
		}

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
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Pair<List<Filter>, String> getAll() throws ServiceException {
		List<Filter> list = null;
		String uName = null;
		try {
			list = filterDAO.getAll();
			uName = filterDAO.getCurrentUserName();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return new Pair<List<Filter>, String>(list, uName);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Pair<List<Filter>, String> getAllByFilter(FilterType type)
			throws ServiceException {

		if (type == null) {
			log.error("FilterType was null in getAllByFilter.");
			throw new ServiceException();
		}

		List<Filter> list = new ArrayList<Filter>();
		String uName;
		try {
			for (Filter filter : filterDAO.getAll()) {
				if (filter.getType() == type && !filter.isAnonymous()) {
					list.add(filter);
				}
			}
			uName = filterDAO.getCurrentUserName();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}

		return new Pair<List<Filter>, String>(list, uName);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Pair<List<Filter>, String> getAllByAnonymous(boolean anonymous)
			throws ServiceException {
		Pair<List<Filter>, String> allPair = getAll();
		Iterator<Filter> i = allPair.a.iterator();
		while (i.hasNext()) {
			Filter f = i.next();
			if (f.isAnonymous() != anonymous) {
				i.remove();
			}
		}
		return allPair;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
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

		if (filterTO == null) {
			log.error("FilterTO was null in create");
			throw new ServiceException();
		}

		if (filterTO.getOperator() == null
				|| filterTO.getPrivacyStatus() == null
				|| filterTO.getType() == null) {
			log.error("FilterTO had invalid null values in create: "
					+ filterTO.toString());
			throw new ServiceException();
		}

		Filter filter = new Filter();
		filter.setType(filterTO.getType());
		filter.setName(filterTO.getName());
		filter.setAnonymous(filterTO.isAnonymous());
		filter.setPrivacyStatus(filterTO.getPrivacyStatus());

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

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public String convertResultsToCSVById(int id) throws ServiceException {
		Filter f = getByID(id);
		if (f == null)
			return null;
		switch (f.getType()) {
		case ADDRESS:
			return addressService.convertToCSV(addressService.getByFilter(f));
		case DONATION:
			return donationService.convertToCSV(donationService.getByFilter(f));
		case MAILING:
			return mailingService.convertToCSV(mailingService.getByFilter(f));
		case PERSON:
			return personService.convertToCSV(personService.getByFilter(f));
		}
		// we should never get here
		String msg = "Programming error: This code should not be reached. The filter "
				+ f + " is of an illegal type.";
		Log.error(msg);
		throw new IllegalSignatureException(msg);
	}

	@Override
	public boolean saveResultsAsCSVById(int id, File csvFile)
			throws ServiceException, IOException {
		Filter f = getByID(id);
		if (f == null)
			return false;
		switch (f.getType()) {
		case ADDRESS:
			addressService.saveAsCSV(addressService.getByFilter(f), csvFile);
			return true;
		case DONATION:
			donationService.saveAsCSV(donationService.getByFilter(f), csvFile);
			return true;
		case MAILING:
			mailingService.saveAsCSV(mailingService.getByFilter(f), csvFile);
			return true;
		case PERSON:
			personService.saveAsCSV(personService.getByFilter(f), csvFile);
			return true;
		}
		// we should never get here
		String msg = "Programming error: This code should not be reached. The filter "
				+ f + " is of an illegal type.";
		Log.error(msg);
		throw new IllegalSignatureException(msg);
	}

}

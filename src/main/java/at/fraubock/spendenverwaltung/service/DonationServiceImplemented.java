package at.fraubock.spendenverwaltung.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;


/**
 * implementation of {@link IAddressService}
 * 
 * @author Thomas
 * 
 */
public class DonationServiceImplemented implements IDonationService {

	private IDonationDAO donationDAO;

	public IDonationDAO getDonationDAO() {
		return donationDAO;
	}

	public void setDonationDAO(IDonationDAO donationDAO) {
		this.donationDAO = donationDAO;
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Donation create(Donation d) throws ServiceException {
		try {
			donationDAO.insertOrUpdate(d);
			return d;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Donation update(Donation d) throws ServiceException {
		try {
			donationDAO.insertOrUpdate(d);
			return d;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void delete(Donation d) throws ServiceException {
		try {
			donationDAO.delete(d);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Donation getByID(int id) throws ServiceException {
		try {
			return donationDAO.getByID(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Donation> getByPerson(Person p) throws ServiceException {
		try {
			return donationDAO.getByPerson(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String[] getDonationTypes() {
		DonationType[] donationTypes = Donation.DonationType.values();
		String[] stringDonationTypes = new String[donationTypes.length];
		int counter = 0;
		
		for(DonationType type : donationTypes)
			stringDonationTypes[counter++] = type.toString();
		
		return stringDonationTypes;
	}

	@Override
	public DonationType getDonationTypeByIndex(int index) {
		return Donation.DonationType.values()[index];
	}
}

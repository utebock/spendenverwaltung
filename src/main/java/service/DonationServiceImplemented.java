package service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import dao.IAddressDAO;
import dao.IDonationDAO;
import domain.Address;
import domain.Donation;
import domain.Donation.DonationType;
import domain.Person;
import exceptions.PersistenceException;
import exceptions.ServiceException;

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
			return donationDAO.create(d);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Donation update(Donation d) throws ServiceException {
		try {
			return donationDAO.update(d);
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

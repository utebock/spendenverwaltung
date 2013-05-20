package service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import dao.IAddressDAO;
import domain.Address;
import exceptions.PersistenceException;
import exceptions.ServiceException;

/**
 * implementation of {@link IAddressService}
 * 
 * @author philipp muhoray
 * 
 */
public class AddressServiceImplemented implements IAddressService {

	private IAddressDAO addressDAO;

	public IAddressDAO getIAddressDAO() {
		return addressDAO;
	}

	public void setIAddressDAO(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Address create(Address a) throws ServiceException {
		try {
			return addressDAO.create(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Address update(Address a) throws ServiceException {
		try {
			return addressDAO.update(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void delete(Address a) throws ServiceException {
		try {
			addressDAO.delete(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Address> getAll() throws ServiceException {
		try {
			return addressDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Address getByID(int id) throws ServiceException {
		try {
			return addressDAO.getByID(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}
}

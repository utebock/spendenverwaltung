package at.fraubock.spendenverwaltung.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;


public class PersonServiceImplemented implements IPersonService {

	private IPersonDAO personDAO;
	private IAddressDAO addressDAO;

	public IPersonDAO getPersonDAO() {

		return personDAO;
	}

	public void setPersonDAO(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public IAddressDAO getAddressDAO() {

		return addressDAO;
	}

	public void setAddressDAO(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Person create(Person p) throws ServiceException {
		try {
			personDAO.insertOrUpdate(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return p;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Person update(Person p) throws ServiceException {
		try {
			personDAO.insertOrUpdate(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return p;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void delete(Person p) throws ServiceException {
		try {
			personDAO.delete(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteAddressAndUpdatePerson(Address a, Person p) throws ServiceException {
		update(p);
		try {
			addressDAO.delete(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Person> getAll() throws ServiceException {
		List<Person> list = null;
		try {
			list = personDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public Person getById(int id) throws ServiceException {
		Person person = null;
		try {
			person = personDAO.getById(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return person;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Person> getByAttributes(Person p) throws ServiceException {
		List<Person> persons = new ArrayList<Person>();
		try {
			persons = personDAO.getByAttributes(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return persons;
	}
	@Override
	public List<Person> getByFilter(Filter filter) throws ServiceException {
		List<Person> list = null;
		try {
			list = personDAO.getByFilter(filter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return list;
	}

}
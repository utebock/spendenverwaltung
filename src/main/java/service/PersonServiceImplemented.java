package service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import dao.IPersonDAO;
import domain.Person;
import exceptions.PersistenceException;
import exceptions.ServiceException;

public class PersonServiceImplemented implements IPersonService{

	private IPersonDAO personDAO;
	
	//FIXME accessors should be named after the property, 
	// not the property type (IPersonDAO -> PersonDAO)
	// (will make problems at bean creation) - pm
	public IPersonDAO getIPersonDAO(){
		return personDAO;
	}
	
	public void setIPersonDAO(IPersonDAO personDAO){
		this.personDAO = personDAO;
	}
	
	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Person create(Person p) throws ServiceException {
		Person person = null;
		try{
			person = personDAO.create(p);
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
		return person;
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Person update(Person p) throws ServiceException {
		Person person = null;
		try{
			person = personDAO.update(p);
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
		return person;
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void delete(Person p) throws ServiceException {
		try{
			personDAO.delete(p);
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<Person> getAll() throws ServiceException {
		List<Person> list = null;
		try{
			list = personDAO.getAll();
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public Person getByID(int id) throws ServiceException {
		Person person = null;
		try{
			person = personDAO.getByID(id);
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
		return person;
	}

}

package service;

import java.util.List;

import dao.IPersonDAO;
import domain.Person;
import exceptions.PersistenceException;
import exceptions.ServiceException;

public class PersonServiceImplemented implements IPersonService{

	private IPersonDAO personDAO;
	
	public IPersonDAO getIPersonDAO(){
		return personDAO;
	}
	
	public void setIPersonDAO(IPersonDAO personDAO){
		this.personDAO = personDAO;
	}
	
	@Override
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
	public void delete(Person p) throws ServiceException {
		try{
			personDAO.delete(p);
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
		
	}

	@Override
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

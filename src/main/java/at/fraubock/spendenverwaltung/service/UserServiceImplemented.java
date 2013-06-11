package at.fraubock.spendenverwaltung.service;

import at.fraubock.spendenverwaltung.interfaces.dao.IUserDAO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IUserService;

/**
 * 
 * @author thomas
 *
 */
public class UserServiceImplemented implements IUserService {

	private IUserDAO userDAO;
	
	public IUserDAO getUserDAO() {
		return userDAO;
	}
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public String isUserValid(String user, String pwd) throws ServiceException {
		String userName;
		
		try {
			userName = userDAO.isUserValid(user, pwd);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return userName;
	}

}

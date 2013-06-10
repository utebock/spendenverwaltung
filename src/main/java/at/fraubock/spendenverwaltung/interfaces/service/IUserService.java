package at.fraubock.spendenverwaltung.interfaces.service;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * 
 * @author thomas
 *
 */
public interface IUserService {
	
	/**
	 * looks if user exists, and checks password
	 * 
	 * @param user
	 * @param pwd
	 * @return true, if user is valid
	 * 			false if password, username or both is wrong
	 */
	public boolean isUserValid(String user, String pwd) throws ServiceException;
}

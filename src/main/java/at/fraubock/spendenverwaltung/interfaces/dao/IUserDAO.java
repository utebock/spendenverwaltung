package at.fraubock.spendenverwaltung.interfaces.dao;

import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * 
 * @author thomas
 *
 */
public interface IUserDAO {
	
	/**
	 * looks if user exists, and checks password
	 * 
	 * @param user
	 * @param pwd
	 * @return true, if user is valid
	 * 			false if password, username or both is wrong
	 * @throws PersistenceException 
	 */
	public boolean isUserValid(String user, String pwd) throws PersistenceException;
}

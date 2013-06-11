package at.fraubock.spendenverwaltung.dao;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import at.fraubock.spendenverwaltung.interfaces.dao.IUserDAO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * 
 * @author thomas
 *
 */
public class UserDAOImplemented implements IUserDAO{

	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public String isUserValid(String user, String pwd) throws PersistenceException{
		if(user.equals("") || pwd.equals(""))
			return null;
		
		String selectStmt = "SELECT user FROM mysql.user where user=? AND password=password(?)";
		
		try{
			return jdbcTemplate.queryForObject(selectStmt, new Object[] { user, pwd }, String.class);
		} catch(EmptyResultDataAccessException e){
			return null;
		}
	}

}

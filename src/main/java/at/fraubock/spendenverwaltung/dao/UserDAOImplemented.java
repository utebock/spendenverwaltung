package at.fraubock.spendenverwaltung.dao;

import java.util.List;

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
	public boolean isUserValid(String user, String pwd) throws PersistenceException{
		String selectStmt = "SELECT count(*) FROM ubusers where name='"+user+"' AND pass=md5('"+pwd+"')";
		
		int total = jdbcTemplate.queryForInt(selectStmt);
		
		return (total >= 1);
	}

}

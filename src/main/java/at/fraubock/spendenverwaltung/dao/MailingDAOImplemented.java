package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.MailingValidator;

public class MailingDAOImplemented implements IMailingDAO {
	
	private static final Logger log = Logger
			.getLogger(MailingDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;
	private MailingValidator mailingValidator;
	private IFilterDAO filterDAO;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setMailingValidator(MailingValidator mailingValidator) {
		this.mailingValidator = mailingValidator;
	}
	
	public void setFilterDAO(IFilterDAO filterDAO) {
		this.filterDAO = filterDAO;
	}
	
	private class CreateMailingStatementCreator implements PreparedStatementCreator {

		private Mailing mailing;

		CreateMailingStatementCreator(Mailing mailing) {
			this.mailing = mailing;
		}

		private String createPersons = "insert into mailings (mailingdate, type, medium, filterid) values (?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
			throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createPersons,
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setTimestamp(1, new Timestamp(mailing.getDate().getTime()));
			ps.setString(2, mailing.getType().getName());
			ps.setString(3, mailing.getMedium().getName());
			if(mailing.getFilter() == null) {
				ps.setNull(4, Types.NULL);
			} else {
				ps.setInt(4, mailing.getFilter().getId());
			}
			
			return ps;
		}
	}
	

	/**
	 * 
	 */
	@Override
	public void insertOrUpdate(Mailing mailing) throws PersistenceException {
		log.debug("Entering insertOrUpdate with param "+mailing);
		
		mailingValidator.validate(mailing);

		if(mailing.getId() == null) {
			//create
			
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateMailingStatementCreator(mailing),
					keyHolder);

			mailing.setId(keyHolder.getKey().intValue());
			
			//now apply personfilter and insert relevant entries into sentmailings
			
		} else {
			//update
		}
		
		log.debug("Returning from insertOrUpdate");
	}

	@Override
	public void delete(Mailing mailing) throws PersistenceException {
		log.debug("Entering delete with param "+mailing);
		
		mailingValidator.validate(mailing);
		
		if(mailing.getId() == null) {
			throw new IllegalArgumentException("Mailing's id was null in delete");
		}
			
		jdbcTemplate.update("DELETE FROM mailings where id=?", 
				new Object[] { mailing.getId() });
		
		log.debug("Returning from delete");
	}

	@Override
	public List<Mailing> getAll() throws PersistenceException {
		log.debug("Entering getAll");
		
		List<Mailing> mailings = jdbcTemplate.query(
				"SELECT * FROM mailings", new MailingMapper());
		
		log.debug("Returning from getAll");
		return mailings;
	}

	@Override
	public Mailing getById(int id) throws PersistenceException {
		log.debug("Entering getById with param "+id);
		
		Mailing mailing = jdbcTemplate.queryForObject(
				"SELECT * FROM mailings WHERE id=?", new Object[] {
				id }, new MailingMapper());
		
		
		log.debug("Returning from getById with result "+mailing);
		return mailing;
	}

	@Override
	public List<Mailing> getMailingsByPerson(Person person)
			throws PersistenceException {
		log.debug("Entering getMailingsByPerson with param "+person);

		List<Mailing> mailings = jdbcTemplate.query(
				"SELECT * FROM mailings, sentmailings AS mailings " +
				"WHERE mailings.id=sentmailings.mailingid AND sentmailings.personid = ?", 
				new Object[] { person.getId() }, new MailingMapper());

		log.debug("Returning from getMailingsByPerson");
		return mailings;
	}

	private class MailingMapper implements RowMapper<Mailing> {

		@Override
		public Mailing mapRow(ResultSet rs, int rowNum) throws SQLException {
			Mailing mailing = new Mailing();
			
			mailing.setDate(new Date(rs.getTimestamp("mailingdate").getTime()));
			mailing.setType(Mailing.MailingType.getByName(rs.getString("type")));
			mailing.setMedium(Mailing.Medium.getByName(rs.getString("medium")));
			if(rs.getInt("filterid") == 0) {
				mailing.setFilter(null);
			} else {
				try {
					mailing.setFilter(filterDAO.getById(rs.getInt("filterid")));
				} catch (PersistenceException e) {
					//TODO
				}
			}
			
			return mailing;
		}
		
	}
}

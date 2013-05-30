package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.MailingValidator;
import at.fraubock.spendenverwaltung.service.PersonValidator;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterToSqlBuilder;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class MailingDAOImplemented implements IMailingDAO {
	
	private static final Logger log = Logger
			.getLogger(MailingDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;
	private MailingValidator mailingValidator;
	private PersonValidator personValidator;
	private FilterToSqlBuilder filterToSqlBuilder;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setMailingValidator(MailingValidator mailingValidator) {
		this.mailingValidator = mailingValidator;
	}
	
	public void setFilterToSqlBuilder(FilterToSqlBuilder filterToSqlBuilder) {
		this.filterToSqlBuilder = filterToSqlBuilder;
	}

	public void setPersonValidator(PersonValidator personValidator) {
		this.personValidator = personValidator;
	}
	
	private class CreateMailingStatementCreator implements PreparedStatementCreator {

		private Mailing mailing;

		CreateMailingStatementCreator(Mailing mailing) {
			this.mailing = mailing;
		}

		private String createMailings = "insert into mailings (date, type, medium) values (?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
			throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createMailings,
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setTimestamp(1, new Timestamp(mailing.getDate().getTime()));
			ps.setString(2, mailing.getType().getName());
			ps.setString(3, mailing.getMedium().getName());
			
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
			
			/** apply correct properties to filter to exclude people who
			*	-don't want to receive mailings of the mailingtype
			*	-don't have the relevant mailing address (email or postal)
			*/
			
			if(mailing.getMedium() == Mailing.Medium.EMAIL) {
				PropertyCriterion hasEmail = new PropertyCriterion();
				hasEmail.compareNotNull(FilterProperty.PERSON_EMAIL);
				
				ConnectedCriterion andCriterion = new ConnectedCriterion();
				andCriterion.connect(hasEmail, LogicalOperator.AND, mailing.getFilter().getCriterion());
				
				mailing.getFilter().setCriterion(andCriterion);
				
			} else if(mailing.getMedium() == Mailing.Medium.POSTAL) {
				PropertyCriterion hasMainAddress = new PropertyCriterion();
				hasMainAddress.compare(FilterProperty.ADDRESS_IS_MAIN, true);

				Filter addressFilter = new Filter(FilterType.ADDRESS, hasMainAddress);

				MountedFilterCriterion mountedCompositeFilter = new MountedFilterCriterion();
				mountedCompositeFilter.mountAndCompareCount(addressFilter,RelationalOperator.EQUALS,1);

				ConnectedCriterion andCriterion = new ConnectedCriterion();
				andCriterion.connect(mailing.getFilter().getCriterion(),LogicalOperator.AND,mountedCompositeFilter);

				mailing.getFilter().setCriterion(andCriterion);
			}

			mailing.setId(keyHolder.getKey().intValue());
			
			//now apply personfilter and insert relevant entries into sentmailings
			
			String filterStmt = filterToSqlBuilder.createSqlStatement(mailing.getFilter());
			
			jdbcTemplate.query(filterStmt, new PersonMailingMapper(mailing));
			
		} else {
			jdbcTemplate.update("UPDATE mailings SET date=?, type=?, medium=? WHERE id=?",
					new Object[] { new Timestamp(mailing.getDate().getTime()), mailing.getType().getName()
					, mailing.getMedium().getName(), mailing.getId() });
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
		
		personValidator.validate(person);
		
		List<Mailing> mailings = jdbcTemplate.query(
				"SELECT ma FROM mailings ma, sentmailings se AS mailings " +
				"WHERE ma.id=se.mailingid AND se.personid = ?", 
				new Object[] { person.getId() }, new MailingMapper());

		log.debug("Returning from getMailingsByPerson");
		return mailings;
	}

	private class MailingMapper implements RowMapper<Mailing> {

		@Override
		public Mailing mapRow(ResultSet rs, int rowNum) throws SQLException {
			Mailing mailing = new Mailing();
			
			mailing.setDate(new Date(rs.getTimestamp("date").getTime()));
			mailing.setType(Mailing.MailingType.getByName(rs.getString("type")));
			mailing.setMedium(Mailing.Medium.getByName(rs.getString("medium")));
			
			return mailing;
		}
		
	}
	
	/**
	 * @author Chris
	 *
	 * processes resultset of person filter and applies inserts to
	 * sentmailings table
	 */
	private class PersonMailingMapper implements RowMapper<Void> {

		private Mailing mailing;
		
		private String query = "INSERT INTO sentmailings (mailingid, personid) VALUES (?, ?)";
		
		public PersonMailingMapper(Mailing mailing) {
			this.mailing = mailing;
		}
		
		@Override
		public Void mapRow(ResultSet rs, int rowNum) throws SQLException {
			jdbcTemplate.update(query, new Object[] { mailing.getId(), rs.getInt("id")});
			
			return null;
		}
		
	}
}

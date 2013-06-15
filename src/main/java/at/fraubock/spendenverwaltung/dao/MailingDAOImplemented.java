package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterToSqlBuilder;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

/**
 * 
 * @author Chris Steele
 * 
 */

public class MailingDAOImplemented implements IMailingDAO {

	private static final Logger log = Logger
			.getLogger(MailingDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;
	private FilterToSqlBuilder filterToSqlBuilder;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setFilterToSqlBuilder(FilterToSqlBuilder filterToSqlBuilder) {
		this.filterToSqlBuilder = filterToSqlBuilder;
	}

	public static void validate(Mailing mailing) throws ValidationException {
		if (mailing == null) {
			throw new ValidationException("Mailing was null");
		}

		if (mailing.getMedium() == null) {
			throw new ValidationException("Medium was null");
		}

		if (mailing.getId() != null) {
			if (mailing.getId() < 0) {
				throw new ValidationException("Id was negative");
			}
		}

		if (mailing.getDate() == null) {
			throw new ValidationException("Date was null");
		}

		if (mailing.getType() == null) {
			throw new ValidationException("Type was null");
		}

		/**
		 * fails if the mailing filter was not set, or if the type of the
		 * mailing filter was not set, or if the type of the mailing filter was
		 * not equal to "Person"
		 */
		if (mailing.getFilter() != null) {
			if (mailing.getFilter().getType() == null) {
				throw new ValidationException("Type of filter was null");
			} else {
				if (!mailing.getFilter().getType().equals(FilterType.PERSON)) {
					throw new ValidationException(
							"Type of filter was not equal to Person");
				}
			}
		} else {
			throw new ValidationException("Filter was null");
		}
	}

	private class CreateMailingStatementCreator implements
			PreparedStatementCreator {

		private Mailing mailing;
		private int unconfirmedId;
		
		CreateMailingStatementCreator(Mailing mailing, int unconfirmedId) {
			this.mailing = mailing;
			this.unconfirmedId = unconfirmedId;
		}

		private String createMailings = "insert into mailings (mailing_date, mailing_type, mailing_medium, unconfirmed) values (?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createMailings,
					Statement.RETURN_GENERATED_KEYS);

			ps.setDate(1, new java.sql.Date(mailing.getDate().getTime()));
			ps.setString(2, mailing.getType().getName());
			ps.setString(3, mailing.getMedium().getName());
			ps.setInt(4, unconfirmedId);

			return ps;
		}
	}
	
	private class CreateUnsentMailingStatementCreator implements
		PreparedStatementCreator {
		
		private String user;
		
		public CreateUnsentMailingStatementCreator(String user) {
			this.user = user;
		}
		
		private String insertUnsentMailings = "INSERT INTO unsent_mailings (creator) VALUES (?)";
		
		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(insertUnsentMailings,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user);
			return ps;
}
}

	/**
	 * 
	 */
	@Override
	public void insertOrUpdate(Mailing mailing) throws PersistenceException {
		try {
			log.debug("Entering insertOrUpdate with param " + mailing);

			try {
				validate(mailing);
			} catch (ValidationException e) {
				throw new PersistenceException(e);
			}

			if (mailing.getId() == null) {
				// create

				KeyHolder keyHolder = new GeneratedKeyHolder();

				String user = jdbcTemplate.queryForObject("SELECT current_user()", String.class);
				
				log.debug("inserting user "+user+ "as creator");
				jdbcTemplate.update(new CreateUnsentMailingStatementCreator(user), keyHolder);
				int unconfirmedId = keyHolder.getKey().intValue();

				log.debug("unconfirmed id returned as "+unconfirmedId);
				
				jdbcTemplate.update(new CreateMailingStatementCreator(mailing, unconfirmedId),
						keyHolder);

				/**
				 * apply correct properties to filter to exclude people who
				 * -don't want to receive mailings of the mailingtype -don't
				 * have the relevant mailing address (email or postal)
				 */

				if (mailing.getMedium() == Mailing.Medium.EMAIL) {
					Filter andEmail = new Filter();
					andEmail.setType(FilterType.PERSON);

					PropertyCriterion hasEmail = new PropertyCriterion();
					hasEmail.compareNotNull(FilterProperty.PERSON_EMAIL);

					ConnectedCriterion andCriterion = new ConnectedCriterion();
					andCriterion.connect(hasEmail, LogicalOperator.AND, mailing
							.getFilter().getCriterion());

					andEmail.setCriterion(andCriterion);
					mailing.setFilter(andEmail);

				} else if (mailing.getMedium() == Mailing.Medium.POSTAL) {
					Filter andMainAddressFilter = new Filter();
					andMainAddressFilter.setType(FilterType.PERSON);

					PropertyCriterion hasMainAddress = new PropertyCriterion();
					hasMainAddress
							.compare(FilterProperty.ADDRESS_IS_MAIN, true);

					Filter addressFilter = new Filter(FilterType.ADDRESS,
							hasMainAddress);

					MountedFilterCriterion mountedCompositeFilter = new MountedFilterCriterion();
					mountedCompositeFilter.mountAndCompareCount(addressFilter,
							RelationalOperator.EQUALS, 1);

					ConnectedCriterion andCriterion = new ConnectedCriterion();
					andCriterion.connect(mailing.getFilter().getCriterion(),
							LogicalOperator.AND, mountedCompositeFilter);

					andMainAddressFilter.setCriterion(andCriterion);
					mailing.setFilter(andMainAddressFilter);
				}

				mailing.setId(keyHolder.getKey().intValue());

				// now apply personfilter and insert relevant entries into
				// sent_mailings

				String filterStmt = filterToSqlBuilder
						.createSqlStatement(mailing.getFilter());
				List<Person> persons = jdbcTemplate.query(filterStmt,
						new PersonIdMapper());

				for (Person person : persons) {
					jdbcTemplate
							.update("INSERT INTO sent_mailings(person_id, mailing_id) VALUES (?, ?)",
									new Object[] { person.getId(),
											mailing.getId() });
				}

			} else {
				jdbcTemplate
						.update("UPDATE mailings SET mailing_date=?, mailing_type=?, mailing_medium=? WHERE id=?",
								new Object[] {
										new Timestamp(mailing.getDate()
												.getTime()),
										mailing.getType().getName(),
										mailing.getMedium().getName(),
										mailing.getId() });
			}

			log.debug("Returning from insertOrUpdate");
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}

	}

	@Override
	public void delete(Mailing mailing) throws PersistenceException {
		try {
			log.debug("Entering delete with param " + mailing);

			try {
				validate(mailing);
			} catch (ValidationException e) {
				throw new PersistenceException(e);
			}

			if (mailing.getId() == null) {
				throw new IllegalArgumentException(
						"Mailing's id was null in delete");
			}
			
			Integer unconfirmedId;
			
			try {
				unconfirmedId = jdbcTemplate.queryForObject(
						"SELECT m.unconfirmed FROM mailings m WHERE m.id=? AND m.unconfirmed IS NOT NULL",
						new Object[] {mailing.getId()}, Integer.class);

				log.debug("Returning from getById with result " + mailing);
		
			} catch (EmptyResultDataAccessException e) {
				log.debug("unconfirmedMailing query had 0 results");
				unconfirmedId = null;
			}
			
			jdbcTemplate.update("DELETE FROM mailings where id=?",
					new Object[] { mailing.getId() });

			if(unconfirmedId != null) {
				jdbcTemplate.update("DELETE FROM unsent_mailings WHERE id=?", new Object[] {unconfirmedId});
			}
			
			log.debug("Returning from delete");
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}

	}

	public String getCreatorOfUnconfirmedMailing(Mailing m) throws PersistenceException {
		try {
			String creator = jdbcTemplate.queryForObject("SELECT u.creator FROM mailings m,unsent_mailings u WHERE m.unconfirmed = u.id AND m.id=?", new Object[] {m.getId()}, String.class);
			return creator;
		} catch (EmptyResultDataAccessException e) {
			//only unconfirmed mailings should be passed in here
			throw new PersistenceException(e);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}
	
	/**
	 * adds all unconfirmed mailings to a hashmap, where each creator (key)
	 * maps to a corresponding list of created mails.
	 * 
	 * @returns Map of creators and mailings
	 */
	@Override
	public List<UnconfirmedMailing> getUnconfirmedMailingsWithCreator() throws PersistenceException {
		
		List<Mailing> unconfirmedMailings = getAllUnconfirmed();
		List<UnconfirmedMailing> results = new ArrayList<UnconfirmedMailing>();
		
		for(Mailing m : unconfirmedMailings) {
			String creator = getCreatorOfUnconfirmedMailing(m);
			
			results.add(new UnconfirmedMailing(m, creator));
		}
		
		return results;
	}
	
	/**
	 * @return all mailings
	 */
	@Override
	public List<Mailing> getAll() throws PersistenceException {
		try {
			log.debug("Entering getAll");

			List<Mailing> mailings = jdbcTemplate.query(
					"SELECT * FROM mailings", new MailingMapper());

			log.debug("Returning from getAll");
			return mailings;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}

	}
	
	@Override
	public List<Mailing> getAllConfirmed() throws PersistenceException {
		try {
			log.debug("Entering getAll");
			
			List<Mailing> confirmedMailings = jdbcTemplate.query("SELECT * FROM mailings m WHERE m.unconfirmed IS NULL"
					, new MailingMapper());
			
			return confirmedMailings;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Mailing> getAllUnconfirmed() throws PersistenceException {
		try {
			log.debug("Entering getAll");
			
			List<Mailing> unconfirmedMailings = jdbcTemplate.query("SELECT * FROM mailings m WHERE m.unconfirmed IS NOT NULL"
					, new MailingMapper());
			
			return unconfirmedMailings;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public Mailing getById(int id) throws PersistenceException {
		try {
			log.debug("Entering getById with param " + id);

			try {
				Mailing mailing = jdbcTemplate.queryForObject(
						"SELECT * FROM mailings WHERE id=?",
						new Object[] { id }, new MailingMapper());

				log.debug("Returning from getById with result " + mailing);
				return mailing;
			} catch (EmptyResultDataAccessException e) {
				// return null if query returns 0 rows
				return null;
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}

	}
	
	@Override
	public void confirmMailing(Mailing mailing) throws PersistenceException  {
		log.debug("Entering confirmMailing with param "+mailing);
		
		try {
			validate(mailing);
		} catch (ValidationException e) {
			log.warn("Validation of mailing object failed in confirmMailing");
		}
		
		try {
			if(mailing.getId() == null) {
				log.warn("Mailing with unset ID passed to confirmMailing");
			} else {
				int unconfirmed_id = jdbcTemplate.queryForObject("SELECT m.unconfirmed FROM mailings m WHERE m.id=?", new Object[] {mailing.getId()}, Integer.class);
				log.debug("unconfirmed id is "+ unconfirmed_id);
				jdbcTemplate.update("DELETE FROM unsent_mailings WHERE id=?", new Object[] {unconfirmed_id});
				jdbcTemplate.update("UPDATE mailings ma SET ma.unconfirmed=NULL WHERE ma.id=?", new Object[] {mailing.getId()});
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Mailing> getConfirmedMailingsByPerson(Person person)
			throws PersistenceException {
		try {
			log.debug("Entering getConfirmedMailingsByPerson with param " + person);

			try {
				PersonDAOImplemented.validate(person);
			} catch (ValidationException e) {
				throw new PersistenceException(e);
			}
			
			try {

			List<Mailing> mailings = jdbcTemplate.query(
					"SELECT ma.* FROM mailings ma, sent_mailings se "
							+ "WHERE ma.unconfirmed IS NULL AND ma.id=se.mailing_id AND se.person_id=?",
					new Object[] { person.getId() },
					new int[] { Types.INTEGER }, new MailingMapper());

			log.debug("Returning from getMailingsByPerson");

			if(mailings.isEmpty())
				log.debug("Returning empty list from getConfirmedMailingsByPerson");
			
			return mailings;
			
			} catch (EmptyResultDataAccessException e) {
				log.debug("Returning null from getConfirmedMailingsByPerson");
				return null;
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}

	}

	private class MailingMapper implements RowMapper<Mailing> {

		@Override
		public Mailing mapRow(ResultSet rs, int rowNum) throws SQLException {
			Mailing mailing = new Mailing();

			mailing.setId(rs.getInt("id"));
			mailing.setDate(new java.util.Date(rs.getDate("mailing_date")
					.getTime()));
			mailing.setType(Mailing.MailingType.getByName(rs
					.getString("mailing_type")));
			mailing.setMedium(Mailing.Medium.getByName(rs
					.getString("mailing_medium")));

			return mailing;
		}

	}

	/**
	 * @author Chris Steele maps a row of type person into a person object, used
	 *         to insert entries into sent_mailings
	 */
	private class PersonIdMapper implements RowMapper<Person> {

		public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person person = new Person();
			person.setId(rs.getInt("id"));
			person.setGivenName(rs.getString("givenname"));
			person.setSurname(rs.getString("surname"));

			log.debug("returning from mapRow with param " + person);
			return person;
		}
	}
}

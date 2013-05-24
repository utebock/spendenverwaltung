package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;

import service.PersonValidator;

import domain.Address;
import domain.Person;
import domain.PersonFilter;
import exceptions.IllegalDBStateException;
import exceptions.PersistenceException;

public class PersonDAOImplemented implements IPersonDAO {

	private IAddressDAO addressDAO;
	private JdbcTemplate jdbcTemplate;
	private PersonValidator personValidator;

	private static final Logger log = Logger
			.getLogger(PersonDAOImplemented.class);

	private String addressQuery = "select * from livesat where pid = ?";

	public void setPersonValidator(PersonValidator personValidator) {
		this.personValidator = personValidator;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setAddressDao(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	private class CreatePersonStatementCreator implements
			PreparedStatementCreator {

		private Person person;

		CreatePersonStatementCreator(Person person) {
			this.person = person;
		}

		private String createPersons = "insert into persons (givenname, surname, email, sex, title, "
				+ "company, telephone, emailnotification, postalnotification, note) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createPersons,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, person.getGivenName());
			ps.setString(2, person.getSurname());
			ps.setString(3, person.getEmail());
			ps.setString(4, person.getSex().toString());
			ps.setString(5, person.getTitle());
			ps.setString(6, person.getCompany());
			ps.setString(7, person.getTelephone());
			ps.setBoolean(8, person.isEmailNotification());
			ps.setBoolean(9, person.isPostalNotification());
			ps.setString(10, person.getNote());

			return ps;
		}
	}

	@Override
	public void insertOrUpdate(Person person) throws PersistenceException {

		personValidator.validate(person);

		if (person.getId() == null) {
			// new person to be inserted
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreatePersonStatementCreator(person),
					keyHolder);

			person.setId(keyHolder.getKey().intValue());

			String insertLivesAt = "insert into livesat (pid, aid, ismain) values (?, ?, ?)";

			List<Address> addresses = person.getAddresses();

			/*
			 * inserting relevant livesat entries
			 */
			for (Address address : addresses) {
				boolean isMain = false;
				if (address.equals(person.getMainAddress())) {
					isMain = true;
				}
				jdbcTemplate
						.update(insertLivesAt, new Object[] { person.getId(),
								address.getId(), isMain }, new int[] {
								Types.INTEGER, Types.INTEGER, Types.BOOLEAN });
			}
		} else {
			// person to be updated

			String updatePersons = "update persons set givenname = ?, surname = ?, email = ?, sex = ?, title = ?, "
					+ "company = ?, telephone = ?, emailnotification = ?, postalnotification = ?, note = ? where id = ?;";

			Object[] params = new Object[] { person.getGivenName(),
					person.getSurname(), person.getEmail(), person.getSex(),
					person.getTitle(), person.getCompany(),
					person.getTelephone(), person.isEmailNotification(),
					person.isPostalNotification(), person.getNote(),
					person.getId() };

			int[] types = new int[] { Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.VARCHAR,
					Types.INTEGER };

			jdbcTemplate.update(updatePersons, params, types);

			/*
			 * ismain must only be true once per person
			 */
			String updateLivesAt = "insert into livesat (pid, aid, ismain) values (?, ?, ?)";

			List<Address> addresses = person.getAddresses();

			/*
			 * inserting relevant livesat entries
			 */
			for (Address address : addresses) {
				boolean isMain = false;

				if (address.equals(person.getMainAddress())) {
					isMain = true;
				}
				jdbcTemplate
						.update(updateLivesAt, new Object[] { person.getId(),
								address.getId(), isMain }, new int[] {
								Types.INTEGER, Types.INTEGER, Types.BOOLEAN });
			}
		}
	}

	@Override
	public void delete(Person person) throws PersistenceException {

		personValidator.validate(person);

		String deletePersons = "delete from persons where id = ?;";
		String removeLivesAt = "delete from livesat where pid = ?";

		Object[] params = new Object[] { person.getId() };

		int[] types = new int[] { Types.INTEGER };

		jdbcTemplate.update(removeLivesAt, params, types);

		jdbcTemplate.update(deletePersons, params, types);
	}

	@Override
	public List<Person> getAll() throws PersistenceException {

		String select = "select * from persons";
		List<Person> personList = jdbcTemplate
				.query(select, new PersonMapper());
		log.info(personList.size() + " list size");

		for (Person entry : personList) {

			List<Address> addresses = jdbcTemplate.query(addressQuery,
					new Object[] { entry.getId() }, new AddressMapper());
			entry.setAddresses(addresses);
		}

		return personList;
	}

	@Override
	public Person getById(int id) throws PersistenceException {

		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		Person person;

		String select = "select * from persons where id = ?;";

		person = jdbcTemplate.queryForObject(select, new Object[] { id },
				new PersonMapper());

		List<Address> addresses = jdbcTemplate.query(addressQuery,
				new Object[] { person.getId() }, new AddressMapper());
		person.setAddresses(addresses);

		return person;
	}

	@Override
	public List<Person> getByFilter(PersonFilter filter)
			throws PersistenceException {
		if (filter == null || filter.isEmpty())
			return getAll();

		String select = "SELECT * FROM persons WHERE";
		ArrayList<Object> args = new ArrayList<Object>();

		if (filter.getGivenNamePart() != null) {
			select += " givenname LIKE %?% AND";
			args.add(filter.getGivenNamePart());
		}
		if (filter.getSurnamePart() != null) {
			select += " surname LIKE %?% AND";
			args.add(filter.getSurnamePart());
		}
		if (filter.getTitlePart() != null) {
			select += " title LIKE %?% AND";
			args.add(filter.getTitlePart());
		}
		if (filter.getAddressSet() != null)
			;// TODO address-person mapping has to be redefined first!
		if (filter.getEmailSet() != null) {
			if (filter.getEmailSet())
				select += " email IS NOT NULL AND";
			else
				select += " email IS NULL AND";
		}
		if (filter.getTelephoneSet() != null) {
			if (filter.getTelephoneSet())
				select += " telephone IS NOT NULL AND";
			else
				select += " telephone IS NULL AND";
		}
		if (filter.getTelephonePart() != null) {
			select += " telephone LIKE %?% AND";
			args.add(filter.getTelephonePart());
		}
		if (filter.getNotePart() != null) {
			select += " note LIKE %?% AND";
			args.add(filter.getNotePart());
		}
		if (filter.getWantsPostalNotification() != null)
			; // TODO way of saving information must be redefined first!
		if (filter.getWantsEmailNotification() != null)
			; // TODO way of saving information must be redefined first!
		if (filter.getSex() != null) {
			select += " sex = ? AND";
			args.add(filter.getSex());
		}

		// omit last "AND":
		select = select.substring(0, select.length() - 3);

		List<Person> persons = jdbcTemplate.query(select, args.toArray(),
				new PersonMapper());

		log.info(persons.size() + " list size");

		for (Person entry : persons) {

			List<Address> addresses = jdbcTemplate.query(addressQuery,
					new Object[] { entry.getId() }, new AddressMapper());
			entry.setAddresses(addresses);
		}

		return persons;
	}

	private class AddressMapper implements RowMapper<Address> {

		@Override
		public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				return addressDAO.getByID(rs.getInt("aid"));
			} catch (PersistenceException e) {
				/**
				 * this should NEVER happen, so we rethrow an unchecked
				 * exception
				 */
				throw new IllegalDBStateException(e);
			}
		}
	}

	private class PersonMapper implements RowMapper<Person> {

		/**
		 * TODO: Map Addresses to Person after calling this, otherwise the
		 * person objects have no address lists. Will need another RowMapper to
		 * process a query on "livesat" table in order to obtain the Addresses
		 * (do this in another method that is called by findPerson & findAll
		 * methods).
		 */
		public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person person = new Person();
			person.setId(rs.getInt("id"));
			person.setMainAddress(addressDAO.getMainAddressByPerson(person));
			person.setGivenName(rs.getString("givenname"));
			person.setSurname(rs.getString("surname"));
			person.setEmail(rs.getString("email"));
			person.setSex(Person.Sex.getByName(rs.getString("sex")));
			person.setTitle(rs.getString("title"));
			person.setCompany(rs.getString("company"));
			person.setTelephone(rs.getString("telephone"));

			/**
			 * these values default to true
			 */
			if (!rs.getBoolean("emailnotification")) {
				person.setEmailNotification(false);
			}
			if (!rs.getBoolean("postalnotification")) {
				person.setEmailNotification(false);
			}

			person.setNote(rs.getString("note"));

			return person;
		}
	}
}

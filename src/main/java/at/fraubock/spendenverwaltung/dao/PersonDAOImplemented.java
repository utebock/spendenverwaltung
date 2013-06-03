package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.PersonValidator;
import at.fraubock.spendenverwaltung.util.FilterToSqlBuilder;

public class PersonDAOImplemented implements IPersonDAO {

	private IAddressDAO addressDAO;
	private JdbcTemplate jdbcTemplate;
	private PersonValidator personValidator;
	private FilterToSqlBuilder filterToSqlBuilder;
	
	private static final Logger log = Logger
			.getLogger(PersonDAOImplemented.class);

	public void setPersonValidator(PersonValidator personValidator) {
		this.personValidator = personValidator;
	}
	
	public void setFilterToSqlBuilder(FilterToSqlBuilder filterToSqlBuilder) {
		this.filterToSqlBuilder = filterToSqlBuilder;
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
			ps.setString(4, person.getSex().getName());
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

			insertAddresses(person);

		} else {
			// person to be updated

			String updatePersons = "update persons set givenname = ?, surname = ?, email = ?, sex = ?, title = ?, "
					+ "company = ?, telephone = ?, emailnotification = ?, postalnotification = ?, note = ? where id = ?";

			Object[] params = new Object[] { person.getGivenName(),
					person.getSurname(), person.getEmail(),
					person.getSex().getName(), person.getTitle(),
					person.getCompany(), person.getTelephone(),
					person.isEmailNotification(),
					person.isPostalNotification(), person.getNote(),
					person.getId() };

			int[] types = new int[] { Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.VARCHAR,
					Types.INTEGER };

			jdbcTemplate.update(updatePersons, params, types);

			// to update address relationhips, simply delete them and then
			// insert the new ones

			Person temp = new Person();
			temp.setId(person.getId());
			fetchAddresses(temp);

			List<Address> oldAddresses = temp.getAddresses();
			Address oldMainAddress = temp.getMainAddress();

			// first, check changes to all addresses:
			for (Address oldAddress : oldAddresses) {
				if (!person.getAddresses().contains(oldAddress) && !person.getMainAddress().equals(oldAddress)) {
					jdbcTemplate
							.update("DELETE FROM livesat WHERE pid = ? AND aid = ?",
									new Object[] { person.getId(),
											oldAddress.getId() }, new int[] {
											Types.INTEGER, Types.INTEGER, });
				}
			}
			for (Address newAddress : person.getAddresses()) {
				if (!oldAddresses.contains(newAddress)) {
					jdbcTemplate
							.update("INSERT INTO livesat(pid, aid, ismain) VALUES (?,?,?) ",
									new Object[] {
											person.getId(),
											newAddress.getId(),
											newAddress.equals(person
													.getMainAddress()) },
									new int[] { Types.INTEGER, Types.INTEGER,
											Types.BOOLEAN });
				}
			}

			// now, check changes to mainAddress:
			if (oldMainAddress == null && person.getMainAddress() != null
					&& oldAddresses.contains(person.getMainAddress())) {
				// main address set, was not set before, but address was already
				// set to the person
				jdbcTemplate
						.update("UPDATE livesat SET ismain = TRUE WHERE pid = ? AND aid = ?",
								new Object[] { person.getId(),
										person.getMainAddress().getId() },
								new int[] { Types.INTEGER, Types.INTEGER });
			}
			if (person.getMainAddress() == null && oldMainAddress != null
					&& person.getAddresses().contains(oldMainAddress)) {
				// if there is no new main address but one was set previously and the 
				// previously set address is still contained in the address list
				jdbcTemplate
						.update("UPDATE livesat SET ismain = FALSE WHERE pid = ? AND aid = ?",
								new Object[] { person.getId(),
										oldMainAddress.getId() }, new int[] {
										Types.INTEGER, Types.INTEGER });
			}
			if (person.getMainAddress() != null && oldMainAddress != null
					&& !person.getMainAddress().equals(oldMainAddress) && person.getAddresses().contains(oldMainAddress)
					&& oldAddresses.contains(person.getMainAddress())) {
				// if there is a change in the main address but both addresses are still
				// set to the person
				jdbcTemplate
						.update("UPDATE livesat SET ismain = FALSE WHERE pid = ? AND aid = ?",
								new Object[] { person.getId(),
										oldMainAddress.getId() }, new int[] {
										Types.INTEGER, Types.INTEGER });
				jdbcTemplate
						.update("UPDATE livesat SET ismain = TRUE WHERE pid = ? AND aid = ?",
								new Object[] { person.getId(),
										person.getMainAddress().getId() }, new int[] {
										Types.INTEGER, Types.INTEGER });
			}
		}
	}

	/**
	 * inserts the relevant entries to 'livesat' table
	 * 
	 * @param person
	 *            the person whose addresses should be linked to the person
	 * @throws PersistenceException
	 */
	private void insertAddresses(Person person) throws PersistenceException {
		String insertLivesAt = "insert into livesat (pid, aid, ismain) values (?, ?, ?)";

		List<Address> addresses = person.getAddresses();

		/*
		 * inserting relevant livesat entries
		 */
		
		if(addresses != null) {
			for (Address address : addresses) {
				boolean isMain = false;
				if (address.equals(person.getMainAddress())) {
					isMain = true;
				}
				jdbcTemplate.update(insertLivesAt, new Object[] { person.getId(),
						address.getId(), isMain }, new int[] { Types.INTEGER,
						Types.INTEGER, Types.BOOLEAN });
			}
		}
	}

	@Override
	public void delete(Person person) throws PersistenceException {

		// we don't need to delete references in 'livesat', since ON DELETE is
		// set to CASCADE

		personValidator.validate(person);

		String deletePersons = "delete from persons where id = ?";

		Object[] params = new Object[] { person.getId() };

		int[] types = new int[] { Types.INTEGER };

		jdbcTemplate.update(deletePersons, params, types);
	}

	/**
	 * fetches the person's addresses into the person object and sets the main
	 * address
	 * 
	 * @param person
	 *            the person whose addresses to fetch
	 * @throws PersistenceException
	 */
	private void fetchAddresses(Person person) throws PersistenceException {
		List<Address> addresses = jdbcTemplate.query(
				"SELECT * FROM livesat l WHERE l.pid = ? ORDER BY aid DESC",
				new Object[] { person.getId() }, new AddressMapper());
		person.setAddresses(addresses);
		try {
			Address mainAddress = jdbcTemplate
					.queryForObject(
							"SELECT * FROM livesat l WHERE l.pid = ? AND l.ismain = TRUE",
							new Object[] { person.getId() },
							new AddressMapper());
			person.setMainAddress(mainAddress);
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() != 0)
				throw new PersistenceException(e);
			// otherwise, just leave the main field blank
		}
	}

	@Override
	public List<Person> getAll() throws PersistenceException {

		String select = "SELECT * FROM persons ORDER BY id DESC";
		List<Person> personList = jdbcTemplate
				.query(select, new PersonMapper());
		log.info(personList.size() + " list size");

		// now, load their addresses
		for (Person entry : personList) {
			fetchAddresses(entry);
		}

		return personList;
	}

	@Override
	public Person getById(int id) throws PersistenceException {

		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from persons where id = ?;";

		Person person = jdbcTemplate.queryForObject(select,
				new Object[] { id }, new PersonMapper());

		fetchAddresses(person);

		return person;
	}


	@Override
	public List<Person> getByAttributes(Person p) throws PersistenceException {
		if (p == null) {
			throw new IllegalArgumentException("Person must not be null");
		}
		
		List<Person> selectedPersons;
		
		String select = "select * from persons p, addresses a, livesat l WHERE (l.pid = p.id AND l.aid = a.id) AND ((p.surname LIKE ? AND p.givenname LIKE ?) AND (p.email LIKE ? OR p.telephone LIKE ? OR (a.street LIKE ? AND a.postcode LIKE ? AND a.city LIKE ?)))";
		selectedPersons = jdbcTemplate.query(select,
				new Object[] { p.getSurname(), p.getGivenName(), p.getEmail(), p.getTelephone(), p.getMainAddress().getStreet(), p.getMainAddress().getPostalCode(), p.getMainAddress().getCity() }, new PersonMapper());
		
		log.info("found " + selectedPersons.size() + " persons by given attributes");

		// now, load their addresses
		for (Person entry : selectedPersons) {
			fetchAddresses(entry);
		}
		
		return selectedPersons;
	}
	
	@Override
	public List<Person> getByAddress(Address address)
			throws PersistenceException {

		String select = "SELECT p.* FROM persons p JOIN livesat l ON p.id = l.pid WHERE l.aid = ? ORDER BY p.id DESC";
		List<Person> personList = jdbcTemplate.query(select,
				new Object[] { address.getId() }, new PersonMapper());
		log.info(personList.size() + " list size");

		// now, load their addresses
		for (Person entry : personList) {
			fetchAddresses(entry);
		}

		return personList;
	}

//	@Override
//	public List<Person> getByFilter(PersonFilter filter)
//			throws PersistenceException {
//		if (filter == null || filter.isEmpty())
//			return getAll();
//
//		String select = "SELECT * FROM persons WHERE";
//		ArrayList<Object> args = new ArrayList<Object>();
//
//		if (filter.getGivenNamePart() != null) {
//			select += " givenname LIKE %?% AND";
//			args.add(filter.getGivenNamePart());
//		}
//		if (filter.getSurnamePart() != null) {
//			select += " surname LIKE %?% AND";
//			args.add(filter.getSurnamePart());
//		}
//		if (filter.getTitlePart() != null) {
//			select += " title LIKE %?% AND";
//			args.add(filter.getTitlePart());
//		}
//		if (filter.getAddressSet() != null)
//			;// TODO address-person mapping has to be redefined first!
//		if (filter.getEmailSet() != null) {
//			if (filter.getEmailSet())
//				select += " email IS NOT NULL AND";
//			else
//				select += " email IS NULL AND";
//		}
//		if (filter.getTelephoneSet() != null) {
//			if (filter.getTelephoneSet())
//				select += " telephone IS NOT NULL AND";
//			else
//				select += " telephone IS NULL AND";
//		}
//		if (filter.getTelephonePart() != null) {
//			select += " telephone LIKE %?% AND";
//			args.add(filter.getTelephonePart());
//		}
//		if (filter.getNotePart() != null) {
//			select += " note LIKE %?% AND";
//			args.add(filter.getNotePart());
//		}
//		if (filter.getWantsPostalNotification() != null)
//			; // TODO way of saving information must be redefined first!
//		if (filter.getWantsEmailNotification() != null)
//			; // TODO way of saving information must be redefined first!
//		if (filter.getSex() != null) {
//			select += " sex = ? AND";
//			args.add(filter.getSex());
//		}
//
//		// omit last "AND":
//		select = select.substring(0, select.length() - 3);
//
//		select += " ORDER BY id DESC";
//
//		List<Person> persons = jdbcTemplate.query(select, args.toArray(),
//				new PersonMapper());
//
//		log.info(persons.size() + " list size");
//
//		for (Person entry : persons) {
//
//			fetchAddresses(entry);
//		}
//
//		return persons;
//	}
	
	@Override
	public List<Person> getByFilter(Filter filter) throws PersistenceException {
		String select = filterToSqlBuilder.createSqlStatement(filter);
		List<Person> personList = jdbcTemplate
				.query(select, new PersonMapper());
		log.info(personList.size() + " list size");

		// now, load their addresses
		for (Person entry : personList) {
			fetchAddresses(entry);
		}

		return personList;
	}


	private class AddressMapper implements RowMapper<Address> {

		@Override
		public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				return addressDAO.getByID(rs.getInt("aid"));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
		}
	}

	/**
	 * Class helping mapping a result set to a person object.
	 * 
	 * Note that no address information whatsoever will be stored in the person
	 * object.
	 * 
	 * @author manuel-bichler
	 * 
	 */
	private class PersonMapper implements RowMapper<Person> {

		public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person person = new Person();
			person.setId(rs.getInt("id"));
			person.setGivenName(rs.getString("givenname"));
			person.setSurname(rs.getString("surname"));
			person.setEmail(rs.getString("email"));
			person.setSex(Person.Sex.getByName(rs.getString("sex")));
			person.setTitle(rs.getString("title"));
			person.setCompany(rs.getString("company"));
			person.setTelephone(rs.getString("telephone"));
			person.setEmailNotification(rs.getBoolean("emailnotification"));
			person.setPostalNotification(rs.getBoolean("postalnotification"));
			person.setNote(rs.getString("note"));

			return person;
		}
	}

}

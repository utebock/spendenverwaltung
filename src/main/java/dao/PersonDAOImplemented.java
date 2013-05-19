package dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import service.PersonValidator;

import domain.Address;
import domain.Person;
import exceptions.IllegalDBStateException;
import exceptions.PersistenceException;

public class PersonDAOImplemented implements IPersonDAO{
	
	private IAddressDAO addressDAO;
	private JdbcTemplate jdbcTemplate;
	private PersonValidator personValidator;
	
	private static final Logger log = Logger.getLogger(PersonDAOImplemented.class);
	
	private String addressQuery = "select * from lives_at where person_id = ?";
	
	public void setPersonValidator(PersonValidator personValidator) {
		this.personValidator = personValidator;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setAddressDao(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}
	
	@Override
	public Person create(Person person) {
		
		personValidator.validate(person);
		
		String createPersons = "insert into persons (given_name, surname, mailing_address, email, salutation, title, " + 
		"company, telephone, notification_type, note) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Object[] params = new Object[] {person.getGivenName(), person.getSurname(), person.getMailing_address().getId(),
				person.getEmail(), person.getSalutation(), person.getTitle(), person.getCompany(), person.getTelephone(), 
				person.getNotificationType(), person.getNote()
				};
		
		int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR
				, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR 
				};
		
		/**
		 * set person id to update result
		 */
		person.setId(jdbcTemplate.update(createPersons, params, types));
		
		String insertLivesAt = "insert into lives_at (person_id, address_id) values (?, ?)";
		
		List<Address> addresses = person.getAddresses();
		
		/**
		 * inserting relevant lives_at entries
		 */
		for(Address address : addresses) {
			jdbcTemplate.update(insertLivesAt, new Object[] {person.getId(), address.getId()}, new int[] {Types.INTEGER, Types.INTEGER});
		}
		
		return person;
	}

	@Override
	public Person update(Person person) {
		
		personValidator.validate(person);

		String updatePersons = "update persons set given_name = ?, surname = ?, mailing_address = ?, email = ?, salutation = ?, title = ?, " + 
		"company = ?, telephone = ?, notification_type = ?, note = ? where id = ?;";
		
		Object[] params = new Object[] {person.getGivenName(), person.getSurname(), person.getMailing_address().getId(),
				person.getEmail(), person.getSalutation(), person.getTitle(), person.getCompany(), person.getTelephone(), 
				person.getNotificationType(), person.getNote(), person.getId()
				};
		
		int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR
				, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR 
				};
		
		/**
		 * set person id to update result
		 */
		person.setId(jdbcTemplate.update(updatePersons, params, types));
		
		String updateLivesAt = "insert into lives_at (person_id, address_id) values (?, ?)";
		
		List<Address> addresses = person.getAddresses();
		
		/**
		 * inserting relevant lives_at entries
		 */
		for(Address address : addresses) {
			jdbcTemplate.update(updateLivesAt, new Object[] {person.getId(), address.getId()}, new int[] {Types.INTEGER, Types.INTEGER});
		}
		
		return person;
	}

	@Override
	public void delete(Person person) {
		//TODO: fix constraint violation 
		
//		personValidator.validate(person);
//		
//		String deletePersons = "delete from persons where id = ?;";
//		String removeLivesAt = "delete from lives_at where person_id = ?";
//		
//		Object[] params = new Object[] {person.getId()};
//		
//		int[] types = new int[] {Types.INTEGER};
//		
//		jdbcTemplate.update(removeLivesAt, params, types);
//		
//		jdbcTemplate.update(deletePersons, params, types);
	}

	@Override
	public List<Person> getAll() {
		
		String select = "select * from persons";
		List<Person> personList = jdbcTemplate.query(select, new PersonMapper());
		log.info(personList.size() + " list size");
		
		for(Person entry : personList) {
			
			List<Address> addresses = jdbcTemplate.query(addressQuery, new Object[] {entry.getId()}, new AddressMapper());
			entry.setAddresses(addresses);
		}
		
		return personList;
	}

	@Override
	public Person getByID(int id) {
		
		if(id < 0){
			throw new IllegalArgumentException("Id must not be less than 0");
		}
		
		Person person;
		
		String select = "select * from persons where id = ?;";
		
		person = jdbcTemplate.queryForObject(select, new Object[]{id}, new PersonMapper());
	
		List<Address> addresses = jdbcTemplate.query(addressQuery, new Object[] {person.getId()}, new AddressMapper());
		person.setAddresses(addresses);
		
		return person;
	}
	
	private class AddressMapper implements RowMapper<Address> {

		@Override
		public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				return addressDAO.getByID(rs.getInt("address_id"));
			} catch (PersistenceException e) {
				/**
				 * this should NEVER happen, so we rethrow an unchecked exception
				 */
				throw new IllegalDBStateException(e);
			}
		}
	}
	
	private class PersonMapper implements RowMapper<Person> {
		
		/**
		 * TODO: Map Addresses to Person after calling this, otherwise the person objects have no
		 * address lists. Will need another RowMapper to process a query on "lives_at" table in order
		 * to obtain the Addresses (do this in another method that is called by findPerson & findAll 
		 * methods).
		 */
		public Person mapRow(ResultSet rs, int rowNum) throws SQLException{
			Person person = new Person();
			person.setId(rs.getInt("id"));
			try {
				person.setMailing_address(addressDAO.getByID(rs.getInt("mailing_address")));
			} catch (PersistenceException e) {
				/**
				 * this should NEVER happen, so we rethrow an unchecked exception
				 */
				throw new IllegalDBStateException(e);
			}
			person.setGivenName(rs.getString("given_name"));
			person.setSurname(rs.getString("surname"));
			person.setEmail(rs.getString("email"));
			person.setSalutation(Person.Salutation.valueOf(rs.getString("salutation")));
			person.setTitle(rs.getString("title"));
			person.setCompany(rs.getString("company"));
			person.setTelephone(rs.getString("telephone"));
			person.setNotificationType(Person.NotificationType.valueOf(rs.getString("notification_type")));
			person.setNote(rs.getString("note"));
			
			return person;
		}
	}
}

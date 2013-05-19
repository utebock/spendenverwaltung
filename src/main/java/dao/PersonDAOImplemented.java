package dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import domain.Address;
import domain.Person;

public class PersonDAOImplemented implements IPersonDAO{
	
	private IAddressDAO addressDAO;
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger log = Logger.getLogger(PersonDAOImplemented.class);
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setAddressDao(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}
	
	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Person create(Person person) {
		
		validate(person);
		
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
		
		validate(person);

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
		
		validate(person);
		
		/**
		 * TODO: fix constraint-errors
		String deletePersons = "delete from persons where id = ?;";
		
		Object[] params = new Object[] {person.getId()};
		
		int[] types = new int[] {Types.VARCHAR};
		
		jdbcTemplate.update(deletePersons, params, types);
		*/
	}

	@Override
	public List<Person> getAll() {
		
		String select = "select * from persons";
		List<Person> list = jdbcTemplate.query(select, new PersonMapper());
		log.info(list.size() + " list size");
		return list;
	}

	@Override
	public Person getByID(int id) {
		
		if(id < 0){
			throw new IllegalArgumentException("Id must not be less than 0");
		}
		String select = "select * from persons where id = ?;";
		return jdbcTemplate.queryForObject(select, new Object[]{id}, new PersonMapper());
		
	}
	
	public void validate(Person person){
		
		if(person == null){
			throw new IllegalArgumentException("Person must not be null");
		}
		if(person.getId() < 0){
			throw new IllegalArgumentException("Id must not be less than 0");
		}
		/**
		 * TODO: define all constraints
		 *
		 */
	}
	
	private class PersonMapper implements RowMapper<Person> {
		
		public Person mapRow(ResultSet rs, int rowNum) throws SQLException{
			Person person = new Person();
			Address addr = person.getMailing_address();
			person.setId(rs.getInt("id"));
			person.setGivenName(rs.getString("given_name"));
			person.setSurname(rs.getString("surname"));
			person.setMailing_address(addr);
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

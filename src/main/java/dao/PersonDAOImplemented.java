package dao;


import java.sql.Types;
import java.util.List;

import mapper.PersonMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import domain.Address;
import domain.Person;

public class PersonDAOImplemented implements IPersonDAO{
	
	private IAddressDAO addressDao;
	
	public void setAddressDao(IAddressDAO addressDao) {
		this.addressDao = addressDao;
	}
	
	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public Person create(Person person) {
		
		if(person == null){
			throw new IllegalArgumentException("Person must not be null");
		}
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		
		String createPersons = "insert into persons (given_name, surname, email, salutation, title, " + 
		"company, telephone, notification_type, note) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Object[] params = new Object[] {person.getGivenName(), person.getSurname(), person.getMailing_address(),
				person.getEmail(), person.getSalutation(), person.getTitle(), person.getCompany(), person.getTelephone(), 
				person.getNotificationType(), person.getNote()
				};
		
		int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR
				, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR 
				};
		
		/**
		 * set person id to update result
		 */
		person.setID(jdbcTemplate.update(createPersons, params, types));
		
		String insertLivesAt = "insert into lives_at (person_id, address_id) values (?, ?)";
		
		List<Address> addresses = person.getAddresses();
		
		/**
		 * inserting relevant lives_at entries
		 */
		for(Address address : addresses) {
			jdbcTemplate.update(insertLivesAt, new Object[] {person.getID(), address.getId()}, new int[] {Types.INTEGER, Types.INTEGER});
		}
		
		return person;
	}

	@Override
	public Person update(Person p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Person> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person getByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}


}

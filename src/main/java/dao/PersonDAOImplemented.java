package dao;


import java.util.List;

import mapper.PersonMapper;

import org.springframework.jdbc.core.JdbcTemplate;

import domain.Address;
import domain.Person;

public class PersonDAOImplemented implements IPersonDAO{
	
	private JdbcTemplate jdbcTemplate;
	private IAddressDAO addressDao;
	
	public void setAddressDao(IAddressDAO addressDao) {
		this.addressDao = addressDao;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Person create(Person p) {
		
		if(p == null){
			throw new IllegalArgumentException("Person must not be null");
		}
		
		String createPersons = "insert into persons (given_name, surname, email, salutation, title, " +
						"company, telephone, notification_type, note) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Person person = jdbcTemplate.queryForObject(createPersons, new Object[]{p}, new PersonMapper());
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

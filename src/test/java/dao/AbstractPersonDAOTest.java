package dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import domain.Address;
import domain.Person;
import exceptions.PersistenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("DBPersonDAOTest-context.xml")
@TransactionConfiguration(defaultRollback=true)
public abstract class AbstractPersonDAOTest {

	protected static IPersonDAO personDAO;
	protected static IAddressDAO addressDAO;
	private static final Logger log = Logger.getLogger(AbstractPersonDAOTest.class);
	
	public static void setAddressDao(IAddressDAO addressDAO) {
		AbstractPersonDAOTest.addressDAO = addressDAO;
	}
	
	public static void setPersonDao(IPersonDAO personDAO) {
		AbstractPersonDAOTest.personDAO = personDAO;
	}
	
	@Test
	@Transactional
	public void createShouldCreatePersonWithValidParameters() throws PersistenceException{
		Person person = new Person();
		
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		address = addressDAO.create(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		
		person.setAddresses(addresses);
		person.setMailingAddress(address);
		
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		personDAO.create(person);
		log.info("Created person: "+ person.getGivenName() +" "+ person.getSurname());
		/**
		 * TODO: dieser test testet eigentlich nur ob exceptions geworfen werden,
		 * da die folgenden zeilen immer wahr sein werden egal was sonst passiert.
		 */
		assertThat(person == null, is(false));
		assertThat(person.getGivenName(), is("Heinz"));
	
	}
	
	@Test (expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidParametersShouldThrowException() throws PersistenceException{
		personDAO.create(null);
	}
	
	@Test
	@Transactional
	public void updateShouldUpdatePerson() throws PersistenceException{
		
		Person person = new Person();
		
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		address = addressDAO.create(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		
		person.setAddresses(addresses);
		person.setMailingAddress(address);
		
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		personDAO.create(person);

		person.setSurname("New Surname");
		personDAO.update(person);
		assertThat(person.getSurname(), is("New Surname"));	
	}
	
	@Test (expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidParametersShouldThrowException() throws PersistenceException{
		Person person = null;
		personDAO.update(person);
	}
	
	@Test (expected = EmptyResultDataAccessException.class) 
	@Transactional
	public void updateNonExistentPersonShouldThrowException() throws PersistenceException{
		Person person = personDAO.getByID(1000000);
		person.setSurname("XXX");
		personDAO.update(person);
	}
	
	@Test
	@Transactional
	public void getByIdShouldGetPersonByID() throws PersistenceException{
		Person person = new Person();
		
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		address = addressDAO.create(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		
		person.setAddresses(addresses);
		person.setMailingAddress(address);
		
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		personDAO.create(person);
		
		Person p = personDAO.getByID(person.getId());
		assertThat(p.getGivenName(), is("Heinz"));
	}
	
	@Test (expected = EmptyResultDataAccessException.class)
	@Transactional(readOnly=true)
	public void getByIdOfNonExistentPersonShouldThrowException() throws PersistenceException{
		@SuppressWarnings("unused")
		Person person = personDAO.getByID(100);
	}
	
	@Test
	@Transactional
	public void deleteShouldDeletePerson() throws PersistenceException{
		Person person = new Person();
		
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		address = addressDAO.create(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		
		person.setAddresses(addresses);
		person.setMailingAddress(address);
		
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Arne");
		person.setSurname("Zank");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		personDAO.create(person);
		personDAO.delete(person);
		
		assertNull(personDAO.getByID(person.getId()));
	}
	
	@Test (expected = EmptyResultDataAccessException.class)
	@Transactional
	public void deleteNonExistentPersonShouldThrowException() throws PersistenceException{
		Person person = personDAO.getByID(1000000);
		personDAO.delete(person);
	}
	
	@Test (expected = IllegalArgumentException.class)
	@Transactional
	public void deleteNullShouldThrowException() throws PersistenceException{
		personDAO.delete(null);
	}
	
	@Test
	@Transactional
	public void getAllShouldGetAllPersons() throws PersistenceException{
		
		Person person = new Person();
		
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		address = addressDAO.create(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		
		person.setAddresses(addresses);
		person.setMailingAddress(address);
		
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		personDAO.create(person);
		
		Person person2 = new Person();
		Address address2 = new Address();
		address2.setStreet("Teststreet 1/1");
		address2.setPostalCode(00000);
		address2.setCity("Testcity");
		address2.setCountry("Testcountry");

		address2 = addressDAO.create(address);
		List<Address> addresses2 = new ArrayList<Address>();
		addresses2.add(address2);
		
		person2.setAddresses(addresses2);
		person2.setMailingAddress(address2);
		
		person2.setSalutation(Person.Salutation.HERR);
		person2.setCompany("IBM");
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Heinz");
		person2.setSurname("Oberhummer");
		person2.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person2.setTelephone("01234567889");
		person2.setNotificationType(Person.NotificationType.MAIL);
		person2.setNote("");
		personDAO.create(person2);
		
		List<Person> list = personDAO.getAll();
		assertThat(list.isEmpty(), is(false));
		
		// assertThat(list.size(), is(2)); should return 2 after rollback
		
	}
	
	@Test (expected = EmptyResultDataAccessException.class)
	@Transactional
	public void getAllShouldReturnFalse() throws PersistenceException{
		Person person = new Person();
		
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		address = addressDAO.create(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);
		
		person.setAddresses(addresses);
		person.setMailingAddress(address);
		
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		personDAO.create(person);
		
		Person person2 = personDAO.getByID(100000);
		List<Person> list = personDAO.getAll();
		assertThat(list.contains(person2), is(false));
	}
}

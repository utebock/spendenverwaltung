package at.fraubock.spendenverwaltung.dao;

import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractPersonDAOTest {

	protected static IPersonDAO personDAO;
	protected static IAddressDAO addressDAO;

	// private static final Logger log =
	// Logger.getLogger(AbstractPersonDAOTest.class);
	// TODO use logger?

	public static void setAddressDao(IAddressDAO addressDAO) {
		AbstractPersonDAOTest.addressDAO = addressDAO;
	}

	public static void setPersonDao(IPersonDAO personDAO) {
		AbstractPersonDAOTest.personDAO = personDAO;
	}

	@Test
	@Transactional
	public void createShouldCreatePersonWithValidParameters()
			throws PersistenceException {
		Person person = new Person();
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);

		person.setAddresses(addresses);
		person.setMainAddress(address);

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("");

		personDAO.insertOrUpdate(person);
		Person other = personDAO.getById(person.getId());

		assertEquals(other, person);

	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidParametersShouldThrowException()
			throws PersistenceException {
		personDAO.insertOrUpdate(null);
	}

	@Test
	@Transactional
	public void updateShouldUpdatePerson() throws PersistenceException {
		Person person = new Person();
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);

		person.setAddresses(addresses);
		person.setMainAddress(address);

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("");

		personDAO.insertOrUpdate(person);
		person.setSurname("New Surname");

		personDAO.insertOrUpdate(person);
		Person updated = personDAO.getById(person.getId());

		assert (!updated.getSurname().equals(person.getSurname()));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidParametersShouldThrowException()
			throws PersistenceException {
		Person person = null;
		personDAO.insertOrUpdate(person);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	public void updateNonExistentPersonShouldThrowException()
			throws PersistenceException {
		Person person = personDAO.getById(1000000);
		person.setSurname("XXX");
		personDAO.insertOrUpdate(person);
	}

	@Test
	@Transactional
	public void getByIdShouldGetPersonById() throws PersistenceException {
		Person person = new Person();
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);

		person.setAddresses(addresses);
		person.setMainAddress(address);

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("");

		personDAO.insertOrUpdate(person);

		Person p = personDAO.getById(person.getId());
		assert (p.getGivenName().equals(person.getGivenName()));
		assert (p.getTelephone().equals(person.getTelephone()));
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional(readOnly = true)
	public void getByIdOfNonExistentPersonShouldThrowException()
			throws PersistenceException {
		@SuppressWarnings("unused")
		Person person = personDAO.getById(10000);
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	public void deleteShouldDeletePerson() throws PersistenceException {
		Person person = new Person();
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);

		person.setAddresses(addresses);
		person.setMainAddress(address);

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("");

		personDAO.insertOrUpdate(person);
		assert (person != null);

		personDAO.delete(person);

		assert (person == null);
		Person deleted = personDAO.getById(person.getId());
		assert (deleted == null);

	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	public void deleteNonExistentPersonShouldThrowException()
			throws PersistenceException {
		Person person = personDAO.getById(1000000);
		personDAO.delete(person);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteNullShouldThrowException() throws PersistenceException {
		personDAO.delete(null);
	}

	@Test
	@Transactional
	public void getAllShouldGetAllPersons() throws PersistenceException {

		Person person = new Person();

		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);

		person.setAddresses(addresses);
		person.setMainAddress(address);

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNote("");
		personDAO.insertOrUpdate(person);

		Person person2 = new Person();
		Address address2 = new Address();
		address2.setStreet("Teststreet 1/1");
		address2.setPostalCode("00000");
		address2.setCity("Testcity");
		address2.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address2);
		List<Address> addresses2 = new ArrayList<Address>();
		addresses2.add(address2);

		person2.setAddresses(addresses2);
		person2.setMainAddress(address2);

		person2.setSex(Person.Sex.MALE);
		person2.setCompany("IBM");
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Heinz");
		person2.setSurname("Oberhummer");
		person2.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person2.setTelephone("01234567889");
		person2.setNote("");
		personDAO.insertOrUpdate(person2);

		List<Person> list = personDAO.getAll();
		System.out.println(list.size() + " inhalt: " + list.toString() + "\n");
		assertThat(list.isEmpty(), is(false));
		assertThat(list.size(), is(2)); // should return 2 after rollback

	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	public void getAllShouldReturnFalse() throws PersistenceException {
		Person person = new Person();

		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);

		person.setAddresses(addresses);
		person.setMainAddress(address);

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNote("");
		personDAO.insertOrUpdate(person);

		Person person2 = personDAO.getById(100000);
		List<Person> list = personDAO.getAll();
		assertThat(list.contains(person2), is(false));
	}
}

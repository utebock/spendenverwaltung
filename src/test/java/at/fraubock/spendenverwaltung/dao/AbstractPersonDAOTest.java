package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Person.Sex;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

//import org.apache.log4j.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractPersonDAOTest {

	protected static IPersonDAO personDAO;
	protected static IAddressDAO addressDAO;

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

	@Test(expected = PersistenceException.class)
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

	@Test(expected = PersistenceException.class)
	@Transactional
	public void updateWithInvalidParametersShouldThrowException()
			throws PersistenceException {
		Person person = null;
		personDAO.insertOrUpdate(person);
	}

	@Test
	@Transactional
	public void getNonExistentPersonShouldReturnNull()
			throws PersistenceException {
		Person person = personDAO.getById(1000000);
		assertNull(person);
	}

	/**
	 * Sets up a person (Manuel Bichler), with two addresses, one of which is
	 * his main address, into the database.
	 * 
	 * @return Manuel Bichler
	 * @throws PersistenceException
	 */
	private Person setupManuelBichler() throws PersistenceException {
		Person person = new Person();
		Address address1 = new Address();
		address1.setStreet("Neustiftgasse 68/9");
		address1.setPostalCode("1070");
		address1.setCity("Wien");
		address1.setCountry("Österreich");
		Address address2 = new Address();
		address2.setStreet("Heldenstraße 27");
		address2.setPostalCode("4452");
		address2.setCity("Ternberg");
		address2.setCountry("Österreich");

		addressDAO.insertOrUpdate(address1);
		addressDAO.insertOrUpdate(address2);

		person.setSex(Person.Sex.MALE);
		person.setGivenName("Manuel");
		person.setSurname("Bichler");
		person.setEmail("manuelbichler@aim.com");
		person.setTelephone("+43 (650) 3006/99-1");
		person.getAddresses().add(address1);
		person.getAddresses().add(address2);
		person.setMainAddress(address1);

		personDAO.insertOrUpdate(person);

		return person;
	}

	/**
	 * @param addresses1
	 *            a list of addresses
	 * @param addresses2
	 *            a list of addresses
	 * @return true if the first list contains all of the addresses in the
	 *         second list and vice versa, false else.
	 */
	private boolean addressesEqual(List<Address> addresses1,
			List<Address> addresses2) {
		if (addresses1.size() != addresses2.size())
			return false;
		for (Address a : addresses1) {
			if (!addresses2.contains(a))
				return false;
		}
		for (Address a : addresses2) {
			if (!addresses1.contains(a))
				return false;
		}
		return true;
	}

	@Test
	@Transactional
	public void updateDeleteSecondaryAddress() throws PersistenceException {
		Person manuel = setupManuelBichler();
		Address secAddress = null;
		for (Address a : manuel.getAddresses()) {
			if (a.getPostalCode().equals("4452"))
				secAddress = a;
		}
		assertNotNull(secAddress);
		manuel.getAddresses().remove(secAddress);

		personDAO.insertOrUpdate(manuel);

		assertTrue(addressesEqual(manuel.getAddresses(),
				personDAO.getById(manuel.getId()).getAddresses()));
	}

	@Test
	@Transactional
	public void updateDeleteMainAddress() throws PersistenceException {
		Person manuel = setupManuelBichler();
		manuel.getAddresses().remove(manuel.getMainAddress());
		manuel.setMainAddress(null);

		personDAO.insertOrUpdate(manuel);

		assertTrue(addressesEqual(manuel.getAddresses(),
				personDAO.getById(manuel.getId()).getAddresses()));
		assertNull(personDAO.getById(manuel.getId()).getMainAddress());
	}

	@Test
	@Transactional
	public void updateDeleteMainAddressWithoutDeletingFromAddresses()
			throws PersistenceException {
		Person manuel = setupManuelBichler();
		manuel.setMainAddress(null);

		personDAO.insertOrUpdate(manuel);
		assertTrue(addressesEqual(manuel.getAddresses(),
				personDAO.getById(manuel.getId()).getAddresses()));
		assertNull(personDAO.getById(manuel.getId()).getMainAddress());
	}

	@Test
	@Transactional
	public void updateSetMainAddress() throws PersistenceException {
		Person manuel = setupManuelBichler();
		manuel.setMainAddress(null);
		personDAO.insertOrUpdate(manuel);

		manuel.setMainAddress(manuel.getAddresses().get(0));
		personDAO.insertOrUpdate(manuel);
		assertTrue(addressesEqual(manuel.getAddresses(),
				personDAO.getById(manuel.getId()).getAddresses()));
		assertEquals(personDAO.getById(manuel.getId()).getMainAddress(),
				manuel.getMainAddress());
	}

	@Test
	@Transactional
	public void updateSwitchMainAddress() throws PersistenceException {
		Person manuel = setupManuelBichler();
		Address secAddress = null;
		for (Address a : manuel.getAddresses()) {
			if (a.getPostalCode().equals("4452"))
				secAddress = a;
		}
		assertNotNull(secAddress);
		manuel.setMainAddress(secAddress);
		personDAO.insertOrUpdate(manuel);

		assertTrue(addressesEqual(manuel.getAddresses(),
				personDAO.getById(manuel.getId()).getAddresses()));
		assertEquals(personDAO.getById(manuel.getId()).getMainAddress(),
				manuel.getMainAddress());
	}

	@Test
	@Transactional
	public void updateAddAddress() throws PersistenceException {
		Person manuel = setupManuelBichler();
		Address a = new Address();
		a.setCity("Linz");
		a.setCountry("Österreich");
		a.setPostalCode("4020");
		a.setStreet("Hauptplatz 13");
		addressDAO.insertOrUpdate(a);
		manuel.getAddresses().add(a);

		personDAO.insertOrUpdate(manuel);

		assertTrue(addressesEqual(manuel.getAddresses(),
				personDAO.getById(manuel.getId()).getAddresses()));
	}

	@Test
	@Transactional
	public void getByAddress() throws PersistenceException {
		Person manuel = setupManuelBichler();
		Person julia = new Person();
		julia.setSex(Sex.FEMALE);
		julia.setGivenName("Julia");
		julia.setSurname("Zeilermayr");
		julia.setMainAddress(manuel.getMainAddress());
		julia.getAddresses().add(julia.getMainAddress());
		personDAO.insertOrUpdate(julia);
		Person max = new Person();
		max.setSex(Sex.MALE);
		max.setGivenName("Max");
		max.setSurname("Mustemann");
		personDAO.insertOrUpdate(max);

		List<Person> wg = personDAO.getByAddress(manuel.getMainAddress());
		assertTrue(wg.contains(manuel));
		assertTrue(wg.contains(julia));
		assertEquals(2, wg.size());
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

	@Test
	@Transactional(readOnly = true)
	public void getByIdOfNonExistentPersonShouldReturnNull()
			throws PersistenceException {
		assertNull(personDAO.getById(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getByIdLessThanNull_shouldThrow() throws PersistenceException {
		personDAO.getById(-6);
	}

	@Test
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

		personDAO.delete(person);

		Person deleted = personDAO.getById(person.getId());
		assertNull(deleted);

	}

	@Test(expected = PersistenceException.class)
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
		assertFalse(list.isEmpty());
		assertEquals(list.size(), 2); // should return 2 after rollback

	}

	@Test
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
		assertFalse(list.contains(person2));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void getByFilterWithNullParam_ThrowsException() throws PersistenceException {
		personDAO.getByFilter(null);
	}
	
	@Test
	@Transactional
	public void getByFilterWithPropertyCriterions_ReturnsOnePerson() throws PersistenceException {
		Person person = new Person();
		person.setGivenName("GNTest");
		person.setSurname("SNTest");
		person.setEmail("email@test.com");
		person.setSex(Person.Sex.FAMILY);
		person.setTitle("Testtitle");
		person.setCompany("NotTestCompany");
		person.setTelephone("0123456");
		person.setEmailNotification(true);
		person.setPostalNotification(false);
		person.setNote("Testnote");
		
		personDAO.insertOrUpdate(person);
		
		
		PropertyCriterion givenname = new PropertyCriterion();
		givenname.compare(FilterProperty.PERSON_GIVENNAME, RelationalOperator.EQUALS, "GNTest");
		PropertyCriterion surname = new PropertyCriterion();
		surname.compare(FilterProperty.PERSON_SURNAME, RelationalOperator.EQUALS, "SNTest");
		PropertyCriterion email = new PropertyCriterion();
		email.compare(FilterProperty.PERSON_EMAIL, RelationalOperator.EQUALS, "email@test.com");
		PropertyCriterion sex = new PropertyCriterion();
		sex.compare(FilterProperty.PERSON_SEX, RelationalOperator.EQUALS, "family");
		PropertyCriterion title = new PropertyCriterion();
		title.compare(FilterProperty.PERSON_TITLE, RelationalOperator.EQUALS, "Testtitle");
		PropertyCriterion company = new PropertyCriterion();
		company.compare(FilterProperty.PERSON_COMPANY, RelationalOperator.UNEQUAL, "TestCompany");
		PropertyCriterion phone = new PropertyCriterion();
		phone.compare(FilterProperty.PERSON_TELEPHONE, RelationalOperator.LIKE, "123");
		PropertyCriterion emailNot = new PropertyCriterion();
		emailNot.compare(FilterProperty.PERSON_WANTS_EMAIL, true);
		PropertyCriterion mailNot = new PropertyCriterion();
		mailNot.compare(FilterProperty.PERSON_WANTS_MAIL, false);
		PropertyCriterion note = new PropertyCriterion();
		note.compare(FilterProperty.PERSON_NOTE,RelationalOperator.EQUALS, "Testnote");

		ConnectedCriterion con1 = new ConnectedCriterion();
		con1.connect(givenname,LogicalOperator.AND,surname);
		ConnectedCriterion con2 = new ConnectedCriterion();
		con2.connect(con1,LogicalOperator.AND,email);
		ConnectedCriterion con3 = new ConnectedCriterion();
		con3.connect(con2,LogicalOperator.AND,sex);
		ConnectedCriterion con4 = new ConnectedCriterion();
		con4.connect(con3,LogicalOperator.AND,title);
		ConnectedCriterion con5 = new ConnectedCriterion();
		con5.connect(con4,LogicalOperator.AND,company);
		ConnectedCriterion con6 = new ConnectedCriterion();
		con6.connect(con5,LogicalOperator.AND,phone);
		ConnectedCriterion con7 = new ConnectedCriterion();
		con7.connect(con6,LogicalOperator.AND,emailNot);
		ConnectedCriterion con8 = new ConnectedCriterion();
		con8.connect(con7,LogicalOperator.AND,mailNot);
		ConnectedCriterion con9 = new ConnectedCriterion();
		con9.connect(con8,LogicalOperator.AND,note);
		
		Filter filter = new Filter(FilterType.PERSON,con9);
		
		List<Person> persons = personDAO.getByFilter(filter);
		
		assert(persons.size()==1 && persons.contains(person));
	}
	
}

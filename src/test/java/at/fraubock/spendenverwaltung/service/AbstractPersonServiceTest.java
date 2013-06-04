package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.List;
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
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback=true)

public abstract class AbstractPersonServiceTest {

	protected static IAddressService addressService;
	protected static IPersonService personService;
	
	protected static IPersonDAO personDAO;
	protected static Person person;
	protected static Person person2;
	protected static Person nullPerson;
	protected static Person personCreated;
	
	protected static IAddressDAO addressDAO;
	protected static Address testAddress;
	protected static Address testAddress2;
	protected static Address nullAddress;
	protected static Address testAddressCreated;
	
	public static void setAddressService(IAddressService addressService) {
		AbstractAddressServiceTest.addressService = addressService;
	}
	
	public static void setPersonService(IPersonService personService) {
		AbstractPersonServiceTest.personService = personService;
	}
	
	@Test(expected = ServiceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(personDAO).insertOrUpdate(null);
			personService.create(null);
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test(expected = ServiceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(personDAO).insertOrUpdate(nullPerson);
			personService.create(nullPerson); // all values are null
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedPerson() {
		try {
			person.setId(10);
			Person returned = personService.create(person);
			
			assertEquals(returned,personCreated);
			
			verify(personDAO).insertOrUpdate(person);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = ServiceException.class)
	@Transactional
	public void updateWithNullParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(personDAO).insertOrUpdate(null);
			personService.update(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	@Transactional
	public void updateWithInvalidStateParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(personDAO).insertOrUpdate(nullPerson);
			personService.update(nullPerson);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedPerson() {
		try {
			Person returned = personService.update(personCreated);
			
			assert (returned.equals(personCreated));
			verify(personDAO).insertOrUpdate(personCreated);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = ServiceException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(personDAO).delete(null);
			
			personService.delete(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {
		try {
			personService.delete(person);
			verify(personDAO).delete(person);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAll_ReturnsAllEntities() {
		try {
			List<Person> all = new ArrayList<Person>();
			all.add(person);
			all.add(person2);
			when(personDAO.getAll()).thenReturn(all);

			List<Person> list = personService.getAll();
			assert (list != null && list.size() == 2);
			assert (list.get(0).equals(person) && list
					.get(1).equals(person2));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional(readOnly = true)
	public void getWithInvalidId_ThrowsException() {
		try {
			when(personDAO.getById(10000)).thenThrow(
					new EmptyResultDataAccessException(0));

			personService.getById(10000);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() throws ServiceException {
		try {
			when(personDAO.getById(-1)).thenThrow(
					new PersistenceException());

			personService.getById(-1);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() {
		try {
			when(personDAO.getById(personCreated.getId())).thenReturn(
					personCreated);

			Person found = personService.getById(personCreated
					.getId());

			assertThat(personCreated, is(found));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	
	
	
	protected static void init() throws PersistenceException {
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode("00000");
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");
		
		//testAddress = addressDAO.create(testAddress);
		List<Address> listTest = new ArrayList<Address>();
		listTest.add(testAddress);
		
		testAddressCreated = testAddress;
		testAddressCreated.setId(1);

		testAddress2 = new Address();
		testAddress2.setStreet("Teststreet2 1/1");
		testAddress2.setPostalCode("00001");
		testAddress2.setCity("Testcity2");
		testAddress2.setCountry("Testcountry2");
		
		//testAddress2 = addressDAO.create(testAddress2);
		List<Address> listTest2 = new ArrayList<Address>();
		listTest2.add(testAddress2);
		
		person = new Person();
		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("");
		//person.setAddresses(listTest);
		//person.setMailingAddress(testAddress);
		
		person2 = new Person();
		person2.setSex(Person.Sex.MALE);
		person2.setCompany("IBM");
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Test2");
		person2.setSurname("Test2");
		person2.setEmail("test2@test2.at");
		person2.setTelephone("02234567889");
		person2.setNote("");
		//person2.setAddresses(listTest2);
		//person2.setMailingAddress(testAddress2);
		
		nullPerson = null;
		
		personCreated = person;
		personCreated.setId(1);
		
	}
	
}
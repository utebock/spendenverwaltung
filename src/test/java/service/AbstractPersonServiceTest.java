package service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import service.IAddressService;
import service.IPersonService;
import dao.IAddressDAO;
import dao.IPersonDAO;
import domain.Address;
import domain.Person;
import exceptions.PersistenceException;
import exceptions.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../testspring.xml")
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
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() {
		try {
			when(personDAO.create(null)).thenThrow(
					new IllegalArgumentException());
			
			personService.create(null);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() {
		try {
			when(personDAO.create(nullPerson)).thenThrow(
					new IllegalArgumentException());

			personService.create(nullPerson); // all values are null
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedPerson() {
		try {
			when(personDAO.create(person)).thenReturn(personCreated);

			Person returned = personService.create(person);
			assert (returned.equals(personCreated));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithNullParameter_ThrowsException() {
		try {
			when(personDAO.update(null)).thenThrow(
					new IllegalArgumentException());
			
			personService.update(null);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidStateParameter_ThrowsException() {
		try {
			when(personDAO.update(nullPerson)).thenThrow(
					new IllegalArgumentException());

			personService.update(nullPerson);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedPerson() {
		try {
			when(personDAO.update(personCreated)).thenReturn(
					personCreated);

			Person returned = personService.update(personCreated);
			assert (returned.equals(personCreated));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException() {
		try {
			doThrow(new IllegalArgumentException()).when(personDAO).delete(null);
			
			personService.delete(null);
		} catch (ServiceException e) {
			fail();
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

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() {
		try {
			when(personDAO.getById(-1)).thenThrow(
					new IllegalArgumentException());

			personService.getById(-1);
		} catch (ServiceException e) {
			fail();
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

			assert (personCreated.equals(found));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	
	
	
	protected static void init() throws PersistenceException {
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode(00000);
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");
		
		//testAddress = addressDAO.create(testAddress);
		List<Address> listTest = new ArrayList<Address>();
		listTest.add(testAddress);
		
		testAddressCreated = testAddress;
		testAddressCreated.setId(1);

		testAddress2 = new Address();
		testAddress2.setStreet("Teststreet2 1/1");
		testAddress2.setPostalCode(00001);
		testAddress2.setCity("Testcity2");
		testAddress2.setCountry("Testcountry2");
		
		//testAddress2 = addressDAO.create(testAddress2);
		List<Address> listTest2 = new ArrayList<Address>();
		listTest2.add(testAddress2);
		
		person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		//person.setAddresses(listTest);
		//person.setMailingAddress(testAddress);
		
		person2 = new Person();
		person2.setSalutation(Person.Salutation.HERR);
		person2.setCompany("IBM");
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Test2");
		person2.setSurname("Test2");
		person2.setEmail("test2@test2.at");
		person2.setTelephone("02234567889");
		person2.setNotificationType(Person.NotificationType.MAIL);
		person2.setNote("");
		//person2.setAddresses(listTest2);
		//person2.setMailingAddress(testAddress2);
		
		nullPerson = null;
		
		personCreated = person;
		personCreated.setId(1);
		
	}
	
}
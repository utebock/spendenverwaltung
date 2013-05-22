package service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
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

import service.IAddressService;
import service.IPersonService;
import dao.IAddressDAO;
import dao.IPersonDAO;
import domain.Address;
import domain.Person;
import exceptions.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../testspring.xml")
@TransactionConfiguration(defaultRollback=true)

public abstract class AbstractPersonServiceTest {
	
	private static final Logger log = Logger.getLogger(AbstractPersonServiceTest.class);
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
	
	protected static void init() {
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode(00000);
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");
		
		testAddressCreated = testAddress;
		testAddressCreated.setId(1);

		testAddress2 = new Address();
		testAddress2.setStreet("Teststreet2 1/1");
		testAddress2.setPostalCode(00001);
		testAddress2.setCity("Testcity2");
		testAddress2.setCountry("Testcountry2");

		nullAddress = new Address();
		
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
		
		personCreated = person;
		personCreated.setId(1);
		
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
		
		nullPerson = new Person();
		
	}
	
}
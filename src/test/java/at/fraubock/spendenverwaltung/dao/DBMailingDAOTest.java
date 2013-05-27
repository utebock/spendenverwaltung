package at.fraubock.spendenverwaltung.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

public class DBMailingDAOTest extends AbstractMailingDAOTest {

	private static ApplicationContext context;
	/**
	 * set DAOs, fill DB with testdata that must be deleted afterwards
	 * @throws PersistenceException if initialization fails due to DAOs not creating test objects
	 */
	@BeforeClass
	public void initialize() throws PersistenceException {
		DBMailingDAOTest.context = new ClassPathXmlApplicationContext("testspring.xml");

		AbstractMailingDAOTest.setAddressDAO(context.getBean("addressDAO", IAddressDAO.class));
		AbstractMailingDAOTest.setMailingDAO(context.getBean("mailingDAO", IMailingDAO.class));
		AbstractMailingDAOTest.setPersonDAO(context.getBean("personDAO", IPersonDAO.class));
		
		validAddressOne.setStreet("Nussdorferstrasse 12");
		validAddressOne.setCity("Wien");
		validAddressOne.setCountry("Oesterreich");
		validAddressOne.setPostalCode("1090");

		validAddressTwo.setStreet("Lobaustrasse 1");
		validAddressTwo.setCity("Grossenzersdorf");
		validAddressTwo.setCountry("Oesterreich");
		validAddressTwo.setPostalCode("2301");
		
		validAddressThree.setStreet("Bienerstrasse 20");
		validAddressThree.setCity("Innsbruck");
		validAddressThree.setCountry("Oesterreich");
		validAddressThree.setPostalCode("6020");
		
		addressDAO.insertOrUpdate(validAddressOne);
		addressDAO.insertOrUpdate(validAddressTwo);
		addressDAO.insertOrUpdate(validAddressThree);
		
		validPersonOne.setGivenName("Ralf");
		validPersonOne.setSurname("Mueller");
		List<Address> ralfAddresses = new ArrayList<Address>();
		ralfAddresses.add(validAddressOne);
		validPersonOne.setAddresses(ralfAddresses);
		validPersonOne.setMainAddress(validAddressOne);
		validPersonOne.setSex(Person.Sex.MALE);
		validPersonOne.setEmail("ralfm1379@hotmail.com");
		
		validPersonTwo_hasNoEmail.setGivenName("Daisy");
		validPersonTwo_hasNoEmail.setSurname("Duck");
		List<Address> daisyAddresses = new ArrayList<Address>();
		daisyAddresses.add(validAddressTwo);
		daisyAddresses.add(validAddressThree);
		validPersonTwo_hasNoEmail.setAddresses(daisyAddresses);
		validPersonTwo_hasNoEmail.setMainAddress(validAddressTwo);
		validPersonTwo_hasNoEmail.setSex(Person.Sex.FEMALE);
		
		validPersonThree_hasNoPostalAddress.setGivenName("Donald");
		validPersonThree_hasNoPostalAddress.setEmail("donald@duckburg.net");
		validPersonThree_hasNoPostalAddress.setSex(Person.Sex.MALE);
		
		personDAO.insertOrUpdate(validPersonOne);
		personDAO.insertOrUpdate(validPersonTwo_hasNoEmail);
		personDAO.insertOrUpdate(validPersonThree_hasNoPostalAddress);
	}
	
	/**
	 * delete testpersons and addresses
	 * @throws PersistenceException if something goes wrong
	 */
	@AfterClass 
	static void deleteData() throws PersistenceException {
		personDAO.delete(validPersonOne);
		personDAO.delete(validPersonTwo_hasNoEmail);
		personDAO.delete(validPersonThree_hasNoPostalAddress);
		
		addressDAO.delete(validAddressOne);
		addressDAO.delete(validAddressTwo);
		addressDAO.delete(validAddressThree);
	}
}

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
		
		addressOne.setStreet("Nussdorferstrasse 12");
		addressOne.setCity("Wien");
		addressOne.setCountry("Oesterreich");
		addressOne.setPostalCode("1090");

		addressTwo.setStreet("Lobaustrasse 1");
		addressTwo.setCity("Grossenzersdorf");
		addressTwo.setCountry("Oesterreich");
		addressTwo.setPostalCode("2301");
		
		addressThree.setStreet("Bienerstrasse 20");
		addressThree.setCity("Innsbruck");
		addressThree.setCountry("Oesterreich");
		addressThree.setPostalCode("6020");
		
		addressDAO.insertOrUpdate(addressOne);
		addressDAO.insertOrUpdate(addressTwo);
		addressDAO.insertOrUpdate(addressThree);
		
		personOne.setGivenName("Ralf");
		personOne.setSurname("Mueller");
		List<Address> ralfAddresses = new ArrayList<Address>();
		ralfAddresses.add(addressOne);
		personOne.setAddresses(ralfAddresses);
		personOne.setMainAddress(addressOne);
		personOne.setSex(Person.Sex.MALE);
		personOne.setEmail("ralfm1379@hotmail.com");

		List<Address> daisyAddresses = new ArrayList<Address>();
		daisyAddresses.add(addressTwo);
		daisyAddresses.add(addressThree);

		
		personThree.setGivenName("Donald");
		personThree.setEmail("donald@duckburg.net");
		personThree.setSex(Person.Sex.MALE);
		
		personDAO.insertOrUpdate(personOne);
		personDAO.insertOrUpdate(personTwo);
		personDAO.insertOrUpdate(personThree);
	}
	
	/**
	 * delete testpersons and addresses
	 * @throws PersistenceException if something goes wrong
	 */
	@AfterClass 
	static void deleteData() throws PersistenceException {
		personDAO.delete(personOne);
		personDAO.delete(personTwo);
		personDAO.delete(personThree);
		
		addressDAO.delete(addressOne);
		addressDAO.delete(addressTwo);
		addressDAO.delete(addressThree);
	}
}

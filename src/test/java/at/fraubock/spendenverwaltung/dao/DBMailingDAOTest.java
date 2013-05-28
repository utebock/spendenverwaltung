package at.fraubock.spendenverwaltung.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.cells.LogicalFilter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.cells.PropertyFilter;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class DBMailingDAOTest extends AbstractMailingDAOTest {

	private static ApplicationContext context;
	/**
	 * set DAOs, fill DB with testdata that must be deleted afterwards
	 * @throws PersistenceException if initialization fails due to DAOs not creating test objects
	 */
	@BeforeClass
	public static void initialize() throws PersistenceException {
		DBMailingDAOTest.context = new ClassPathXmlApplicationContext("testspring.xml");

		AbstractMailingDAOTest.setAddressDAO(context.getBean("addressDAO", IAddressDAO.class));
		AbstractMailingDAOTest.setMailingDAO(context.getBean("mailingDAO", IMailingDAO.class));
		AbstractMailingDAOTest.setPersonDAO(context.getBean("personDAO", IPersonDAO.class));
		AbstractMailingDAOTest.setFilterDAO(context.getBean("filterDAO", IFilterDAO.class));
		
		// initialize addresses
		
		addressOne.setStreet("Nussdorferstrasse 12");
		addressOne.setCity("Wien");
		addressOne.setCountry("Österreich");
		addressOne.setPostalCode("1090");

		addressTwo.setStreet("Lobaustrasse 1");
		addressTwo.setCity("Grossenzersdorf");
		addressTwo.setCountry("Österreich");
		addressTwo.setPostalCode("2301");
		
		addressThree.setStreet("Bienerstrasse 20");
		addressThree.setCity("Innsbruck");
		addressThree.setCountry("Österreich");
		addressThree.setPostalCode("6020");
		
		addressDAO.insertOrUpdate(addressOne);
		addressDAO.insertOrUpdate(addressTwo);
		addressDAO.insertOrUpdate(addressThree);
		
		//test person with one address and an email
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
		
		//test person with multiple addresses
		personTwo.setGivenName("Daisy");
		personTwo.setSurname("Duck");
		personTwo.setSex(Person.Sex.FEMALE);
		personTwo.setAddresses(daisyAddresses);

		//test person with no address of either kind
		personThree.setGivenName("Donald");
		personThree.setSurname("Duck");
		personThree.setSex(Person.Sex.MALE);
		
		personDAO.insertOrUpdate(personOne);
		personDAO.insertOrUpdate(personTwo);
		personDAO.insertOrUpdate(personThree);
		
		// initialize filters
		
		PropertyFilter prop1 = new PropertyFilter();
		prop1.setProperty("givenname");
		prop1.setRelationalOperator(RelationalOperator.EQUALS);
		prop1.setStrValue("Ralf");
		
		filterOnePerson.setHead(prop1);
		
		PropertyFilter prop2 = new PropertyFilter();
		prop2.setProperty("givenname");
		prop2.setRelationalOperator(RelationalOperator.EQUALS);
		prop2.setStrValue("Daisy");
		
		PropertyFilter prop3 = new PropertyFilter();
		prop3.setProperty("givenname");
		prop3.setRelationalOperator(RelationalOperator.EQUALS);
		prop3.setStrValue("Ralf");
		
		LogicalFilter log1 = new LogicalFilter();
		log1.setLogicalOperator(LogicalOperator.OR);
		log1.setOperand1(prop2);
		log1.setOperand2(prop3);
		
		//this filter should return daisy and ralf
		filterTwoPeople.setHead(log1);
		
		PropertyFilter prop4 = new PropertyFilter();
		prop4.setProperty("givenname");
		prop4.setRelationalOperator(RelationalOperator.EQUALS);
		prop4.setStrValue("doesntexist");
		
		filterNoPeople.setHead(prop4);
		
		filterDAO.insertOrUpdate(filterOnePerson);
		filterDAO.insertOrUpdate(filterTwoPeople);
		filterDAO.insertOrUpdate(filterNoPeople);
	}
	
	/**
	 * delete testpersons and addresses
	 * @throws PersistenceException if something goes wrong
	 */
	@AfterClass 
	public static void deleteData() throws PersistenceException {
		personDAO.delete(personOne);
		personDAO.delete(personTwo);
		personDAO.delete(personThree);
		
		addressDAO.delete(addressOne);
		addressDAO.delete(addressTwo);
		addressDAO.delete(addressThree);
		
		filterDAO.delete(filterOnePerson);
		filterDAO.delete(filterTwoPeople);
		filterDAO.delete(filterNoPeople);
	}
}

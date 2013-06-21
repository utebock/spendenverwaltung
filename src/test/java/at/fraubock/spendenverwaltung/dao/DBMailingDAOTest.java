package at.fraubock.spendenverwaltung.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

/**
 * 
 * @author Chris Steele
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class DBMailingDAOTest extends AbstractMailingDAOTest {

	private static ApplicationContext context;
		
	/**
	 * set DAOs, fill DB with testdata that must be deleted afterwards
	 * @throws PersistenceException if initialization fails due to DAOs not creating test objects
	 */
	@BeforeClass
	public static void initialize() throws PersistenceException {
		DBMailingDAOTest.context = new ClassPathXmlApplicationContext("testspring.xml");

		AbstractMailingDAOTest.setAddressDAO(context.getBean("addressDao", IAddressDAO.class));
		AbstractMailingDAOTest.setMailingDAO(context.getBean("mailingDao", IMailingDAO.class));
		AbstractMailingDAOTest.setPersonDAO(context.getBean("personDao", IPersonDAO.class));		
	}
	
	@Before
	@Transactional
	public void initdata() throws PersistenceException {
		
		/* set up a new transaction for our initialization and set propagation
		 * to require other transactions called (our unit tests) to run on a new
		 * transaction in order for the desired rollback behaviour to take place
		 */
		
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
		
		//initialize filters
		
		PropertyCriterion onlyRalf = new PropertyCriterion();
		onlyRalf.compare(FilterProperty.PERSON_GIVENNAME, RelationalOperator.EQUALS, "Ralf");
		
		filterOnePerson.setType(FilterType.PERSON);
		filterOnePerson.setCriterion(onlyRalf);
		
		PropertyCriterion onlyDaisy = new PropertyCriterion();
		onlyDaisy.compare(FilterProperty.PERSON_GIVENNAME, RelationalOperator.EQUALS, "Daisy");
		
		ConnectedCriterion ralfAndDaisy = new ConnectedCriterion();
		ralfAndDaisy.connect(onlyDaisy, LogicalOperator.OR, onlyRalf);
	
		filterTwoPeople.setType(FilterType.PERSON);
		filterTwoPeople.setCriterion(ralfAndDaisy);
		
		PropertyCriterion noPeople = new PropertyCriterion();
		noPeople.compare(FilterProperty.PERSON_GIVENNAME,  RelationalOperator.EQUALS, "doesntexist");
		
		filterNoPeople.setType(FilterType.PERSON);
		filterNoPeople.setCriterion(noPeople);
	}
	
	/**
	 * delete testpersons and addresses
	 * @throws PersistenceException if something goes wrong
	 */
	@After
	public void deleteData() throws PersistenceException {
	}
}

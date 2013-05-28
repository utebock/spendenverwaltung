package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class AbstractMailingDAOTest {

	private static final Logger log = Logger
			.getLogger(AbstractMailingDAOTest.class);

	/**
	 * PersonDAO is needed to create people for the tests
	 * AddressDAO is needed to create addresses for the people used in the tests
	 */
	protected static IPersonDAO personDAO;
	protected static IAddressDAO addressDAO;
	protected static IMailingDAO mailingDAO;
	protected static IFilterDAO filterDAO;
	
	/**
	 * defining some valid and invalid entities
	 */
	protected static Address addressOne = new Address();
	protected static Address addressTwo = new Address();
	protected static Address addressThree = new Address();
	
	protected static Person personOne = new Person();
	protected static Person personTwo = new Person();
	protected static Person personThree = new Person();
	
	//mailing instance used by tests
	protected static Mailing mailing = new Mailing();
	
	protected static Filter filterOnePerson = new Filter();
	protected static Filter filterTwoPeople = new Filter();
	protected static Filter filterNoPeople = new Filter();
	
	public static void setAddressDAO(IAddressDAO addressDAO) {
		AbstractMailingDAOTest.addressDAO = addressDAO;
	}
	
	public static void setPersonDAO(IPersonDAO personDAO) {
		AbstractMailingDAOTest.personDAO = personDAO;
	}
	
	public static void setMailingDAO(IMailingDAO MailingDAO) {
		AbstractMailingDAOTest.mailingDAO = MailingDAO;
	}
	
	public static void setFilterDAO(IFilterDAO bean) {
		AbstractMailingDAOTest.filterDAO = filterDAO;
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() {
		try {
			mailingDAO.insertOrUpdate(null);
		} catch (PersistenceException e) {
			log.error("PersistenceException caught in test createWithNullParameter_ThrowsException");
			fail();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() {
		try {
			mailingDAO.insertOrUpdate(new Mailing()); // all values are null
		} catch (PersistenceException e) {

			log.error("PersistenceException caught in test createWithInvalidStateParameter_ThrowsException");
			fail();
		}
	}
	
	@Test
	@Transactional
	public void createWithValidParameters() {

		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType(Mailing.MailingType.DANKESBRIEF);
		mailing.setFilter(filterOnePerson);
		
		Date currentDate = new Date(System.currentTimeMillis());
		
		mailing.setDate(currentDate);
				
		try {
			mailingDAO.insertOrUpdate(mailing);
		} catch (PersistenceException e) {
			fail();
		}
		
		try {
			Mailing result = mailingDAO.getById(mailing.getId());
			
			assertEquals(result, mailing);
		
			//TODO getMailingByPerson call to check if the right
			// people got the right mails
		} catch (PersistenceException e) {
			fail();
		}
		
		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.POSTAL);
		mailing.setType(Mailing.MailingType.DANKESBRIEF);
		mailing.setFilter(filterTwoPeople);
		
		currentDate = new Date(System.currentTimeMillis());
		mailing.setDate(currentDate);
				
		try {
			mailingDAO.insertOrUpdate(mailing);
		} catch (PersistenceException e) {
			fail();
		}
		
		try {
			Mailing result = mailingDAO.getById(mailing.getId());
			
			assertEquals(result, mailing);
			
			//TODO getMailingByPerson call to check if the right
			// people got the right mails
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void updateWithValidParameters() {
		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType(Mailing.MailingType.DANKESBRIEF);
		//TODO: mailing.setPersonFilter

		
		Date currentDate = new Date(System.currentTimeMillis());
		
		mailing.setDate(currentDate);
				
		try {
			mailingDAO.insertOrUpdate(mailing);
		} catch (PersistenceException e) {
			fail();
		}
		
		mailing.setType(Mailing.MailingType.DAUERSPENDER_DANKESBRIEF);
		
		try {
			mailingDAO.insertOrUpdate(mailing);
			
			Mailing result = mailingDAO.getById(mailing.getId());

			assertEquals(mailing, result);
		} catch (PersistenceException e) {
			fail();
		}
		
	}
		
	@Test
	@Transactional
	public void getAll_shouldReturnList() {
		
		//TODO initialize mailings with valid data
		
		List<Mailing> createdMailings = new ArrayList<Mailing>();
		List<Mailing> results;
		
		
		try {
			results = mailingDAO.getAll();
		
			assertEquals(createdMailings, results);
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void getAll_shouldReturnEmptyList() {
		try {
			List<Mailing> results = mailingDAO.getAll();
			assertTrue(results.isEmpty());
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void findByValidId() {
		//TODO create mailing
		
		Mailing initial = new Mailing();
		
		try {
			mailingDAO.insertOrUpdate(initial);
		} catch (PersistenceException e1) {
			fail();
		}
		
		try {
			Mailing result = mailingDAO.getById(mailing.getId());
			assertEquals(result, mailing);
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Transactional
	public void findByInvalidId_shouldReturnNull() {
		try {
			assertNull(mailingDAO.getById(-1));
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void deleteWithCreatedMailing() {
		
		//todo create valid mailing
		
		Mailing mailing = new Mailing();
		Integer oldId = null;
		
		try {
			mailingDAO.insertOrUpdate(mailing);
			oldId = mailing.getId();
		} catch (PersistenceException e) {
			fail();
		}
		
		try {
			mailingDAO.delete(mailing);
		} catch (PersistenceException e) {
			fail();
		}
		
		Mailing result;
		
		try {
			result = mailingDAO.getById(oldId);
			assertNull(result);
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Transactional
	public void deleteWithInvalidId_shouldReturnNull() {
		
		
	}
	
	@Test
	@Transactional	
	public void getMailingsByValidPerson() {
		//TODO insertAndUpdate mailings with filter that would return personOne
		
		Mailing mailingOne = new Mailing();
		Mailing mailingTwo = new Mailing();
		Mailing mailingThree = new Mailing();
		
		List<Mailing> mailingList = new ArrayList<Mailing>();
		mailingList.add(mailingOne);
		mailingList.add(mailingTwo);
		mailingList.add(mailingThree);
		
		try {
			mailingDAO.insertOrUpdate(mailingOne);
			mailingDAO.insertOrUpdate(mailingTwo);
			mailingDAO.insertOrUpdate(mailingThree);
		} catch (PersistenceException e1) {
			fail();
		}
		
		try {
			List<Mailing> results = mailingDAO.getMailingsByPerson(personOne);
			
			assertEquals(results, mailingList);
		} catch (PersistenceException e) {
			fail();
		}	
	}
	
	@Test
	@Transactional	
	public void getMailingsByValidPersonWithNoMailings_shouldReturnNull() {
		try {
			assertNull(mailingDAO.getMailingsByPerson(personOne));
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional	
	public void getMailingsByInvalidPerson_throwsException() {
		try {
			mailingDAO.getMailingsByPerson(new Person());
		} catch (PersistenceException e) {
			fail();
		}
	}


}

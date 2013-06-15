package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * 
 * @author Chris Steele
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class AbstractMailingDAOTest {

//	private static final Logger log = Logger.getLogger(AbstractMailingDAOTest.class);

	/**
	 * PersonDAO is needed to create people for the tests
	 * AddressDAO is needed to create addresses for the people used in the tests
	 */
	protected static IPersonDAO personDAO;
	protected static IAddressDAO addressDAO;
	protected static IMailingDAO mailingDAO;
	
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
	protected static Mailing mailing;
	
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
	
	public void initData() throws PersistenceException {
		addressOne.setId(null);
		addressTwo.setId(null);
		addressThree.setId(null);
		
		addressDAO.insertOrUpdate(addressOne);
		addressDAO.insertOrUpdate(addressTwo);
		addressDAO.insertOrUpdate(addressThree);
		
		personOne.setId(null);
		personTwo.setId(null);
		personThree.setId(null);
		
		personDAO.insertOrUpdate(personOne);
		personDAO.insertOrUpdate(personTwo);
		personDAO.insertOrUpdate(personThree);
	}
	
	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() throws PersistenceException {
		mailingDAO.insertOrUpdate(null);
	}
	
	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() throws PersistenceException {
		mailingDAO.insertOrUpdate(new Mailing()); // all values are null
	}
	
	@Test
	@Transactional
	public void createWithValidParameters() throws PersistenceException {
		
		initData();
		
		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType(Mailing.MailingType.DANKESBRIEF);
		mailing.setFilter(filterOnePerson);
		
		Date currentDate = new Date(System.currentTimeMillis());
		
		mailing.setDate(currentDate);
				
		try {
			assertTrue(mailingDAO != null);
			mailingDAO.insertOrUpdate(mailing);
		} catch (PersistenceException e) {
			fail();
		}
		
		try {
			Mailing result = mailingDAO.getById(mailing.getId());

			assertEquals(result, mailing);

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
			
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void updateWithValidParameters() throws PersistenceException {
		initData();
		
		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType(Mailing.MailingType.DANKESBRIEF);
		mailing.setFilter(filterTwoPeople);
		
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
	public void getAll_shouldReturnList() throws PersistenceException {
		initData();		
		
		List<Mailing> createdMailings = new ArrayList<Mailing>();
		
		Mailing mailingOne = new Mailing();
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.EINZELSPENDEN_DANKESBRIEF);
		mailingOne.setFilter(filterOnePerson);
		
		Mailing mailingTwo = new Mailing();
		mailingTwo.setDate(new Date(System.currentTimeMillis()));
		mailingTwo.setMedium(Mailing.Medium.EMAIL);
		mailingTwo.setType(Mailing.MailingType.EINZELSPENDEN_DANKESBRIEF);
		mailingTwo.setFilter(filterOnePerson);
		
		Mailing mailingThree = new Mailing();
		mailingThree.setDate(new Date(System.currentTimeMillis()));
		mailingThree.setMedium(Mailing.Medium.EMAIL);
		mailingThree.setType(Mailing.MailingType.EINZELSPENDEN_DANKESBRIEF);
		mailingThree.setFilter(filterOnePerson);
		
		mailingDAO.insertOrUpdate(mailingOne);
		mailingDAO.insertOrUpdate(mailingTwo);
		mailingDAO.insertOrUpdate(mailingThree);
		
		createdMailings.add(mailingOne);
		createdMailings.add(mailingTwo);
		createdMailings.add(mailingThree);
		
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
	public void findByValidId() throws PersistenceException {
		initData();
		
		Mailing mailing = new Mailing();
		mailing.setDate(new Date(System.currentTimeMillis()));
		mailing.setFilter(filterTwoPeople);
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType(Mailing.MailingType.ERLAGSCHEINVERSAND);
		
		try {
			mailingDAO.insertOrUpdate(mailing);
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
	public void deleteWithCreatedMailing() throws PersistenceException {
		initData();
		
		Mailing mailing = new Mailing();
		mailing.setDate(new Date(System.currentTimeMillis()));
		mailing.setFilter(filterOnePerson);
		mailing.setMedium(Mailing.Medium.POSTAL);
		mailing.setType(Mailing.MailingType.DAUERSPENDER_DANKESBRIEF);
		
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
	
	@Test
	@Transactional
	public void deleteWithConfirmedMailing() throws PersistenceException {
		initData();
		
		Mailing mailing = new Mailing();
		mailing.setDate(new Date(System.currentTimeMillis()));
		mailing.setFilter(filterOnePerson);
		mailing.setMedium(Mailing.Medium.POSTAL);
		mailing.setType(Mailing.MailingType.DAUERSPENDER_DANKESBRIEF);
		
		Integer oldId = null;
		
		try {
			mailingDAO.insertOrUpdate(mailing);
			mailingDAO.confirmMailing(mailing);
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
			
			List<Mailing> results = mailingDAO.getAllConfirmed();
			assertTrue(results.isEmpty());
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void deleteWithUnconfirmedMailing() throws PersistenceException {
		initData();
		
		Mailing mailing = new Mailing();
		mailing.setDate(new Date(System.currentTimeMillis()));
		mailing.setFilter(filterOnePerson);
		mailing.setMedium(Mailing.Medium.POSTAL);
		mailing.setType(Mailing.MailingType.DAUERSPENDER_DANKESBRIEF);
		
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
			List<Mailing> results = mailingDAO.getAllUnconfirmed();
			assertTrue(results.isEmpty());
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	
	@Test
	@Transactional	
	public void getMailingsByValidPerson() throws PersistenceException {
		initData();
		
		Mailing mailingOne = new Mailing();
		Mailing mailingTwo = new Mailing();
		
		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);
		
		mailingTwo.setMedium(Mailing.Medium.POSTAL);
		mailingTwo.setType(Mailing.MailingType.DANKESBRIEF);
		mailingTwo.setDate(new Date(System.currentTimeMillis()));
		mailingTwo.setFilter(filterOnePerson);
		
		List<Mailing> mailingList = new ArrayList<Mailing>();

		mailingList.add(mailingOne);
		mailingList.add(mailingTwo);
		
		mailingDAO.insertOrUpdate(mailingOne);
		mailingDAO.confirmMailing(mailingOne);
		mailingDAO.insertOrUpdate(mailingTwo);
		mailingDAO.confirmMailing(mailingTwo);
			
		List<Mailing> results = mailingDAO.getConfirmedMailingsByPerson(personOne);
			
		assertEquals(mailingList, results);
	}
	
	@Test
	@Transactional	
	public void getMailingsByValidPersonWithNoMailings_shouldReturnNull() throws PersistenceException {
		
			initData();
			assertTrue(mailingDAO.getConfirmedMailingsByPerson(personOne).isEmpty());
		
	}
	
	@Test(expected = PersistenceException.class)
	@Transactional	
	public void getMailingsByInvalidPerson_throwsException() throws PersistenceException {
		mailingDAO.getConfirmedMailingsByPerson(new Person());
	}
	
	@Test
	@Transactional
	public void getAllConfirmed_shouldReturnEmptyList() {
		try {
			initData();
			
			Mailing mailingOne = new Mailing();
			
			mailingOne.setMedium(Mailing.Medium.EMAIL);
			mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
			mailingOne.setDate(new Date(System.currentTimeMillis()));
			mailingOne.setFilter(filterOnePerson);
			
			mailingDAO.insertOrUpdate(mailingOne);
			List<Mailing> results = mailingDAO.getAll();
			assertFalse(results.isEmpty());
			results = mailingDAO.getAllConfirmed();
			assertTrue(results.isEmpty());
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void getAllConfirmed_shouldReturnOneMailing() {
		try {
			initData();
			
			Mailing mailingOne = new Mailing();
			
			mailingOne.setMedium(Mailing.Medium.EMAIL);
			mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
			mailingOne.setDate(new Date(System.currentTimeMillis()));
			mailingOne.setFilter(filterOnePerson);
			
			Mailing mailingTwo = new Mailing();
			
			mailingTwo.setMedium(Mailing.Medium.POSTAL);
			mailingTwo.setType(Mailing.MailingType.DANKESBRIEF);
			mailingTwo.setDate(new Date(System.currentTimeMillis()));
			mailingTwo.setFilter(filterOnePerson);
			
			mailingDAO.insertOrUpdate(mailingOne);
			mailingDAO.insertOrUpdate(mailingTwo);
			
			mailingDAO.confirmMailing(mailingTwo);
			
			List<Mailing> results = mailingDAO.getAllConfirmed();
			
			List<Mailing> expectedResults = new ArrayList<Mailing>();
			expectedResults.add(mailingTwo);
			
			results = mailingDAO.getAllConfirmed();
			assertEquals(expectedResults, results);
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional
	public void getAllUnConfirmed_shouldReturnEmptyList() throws PersistenceException {
			initData();
			
			Mailing mailingOne = new Mailing();
			
			mailingOne.setMedium(Mailing.Medium.EMAIL);
			mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
			mailingOne.setDate(new Date(System.currentTimeMillis()));
			mailingOne.setFilter(filterOnePerson);
			
			mailingDAO.insertOrUpdate(mailingOne);
			
			mailingDAO.confirmMailing(mailingOne);
			
			List<Mailing> results = mailingDAO.getAllUnconfirmed();
			assertTrue(results.isEmpty());
	}
	
	@Test
	@Transactional
	public void getAllUnConfirmed_shouldReturnOneMailing() {
		try {
			initData();
			
			Mailing mailingOne = new Mailing();
			
			mailingOne.setMedium(Mailing.Medium.EMAIL);
			mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
			mailingOne.setDate(new Date(System.currentTimeMillis()));
			mailingOne.setFilter(filterOnePerson);
			
			Mailing mailingTwo = new Mailing();
			
			mailingTwo.setMedium(Mailing.Medium.POSTAL);
			mailingTwo.setType(Mailing.MailingType.DANKESBRIEF);
			mailingTwo.setDate(new Date(System.currentTimeMillis()));
			mailingTwo.setFilter(filterOnePerson);
			
			mailingDAO.insertOrUpdate(mailingOne);
			mailingDAO.insertOrUpdate(mailingTwo);
			
			mailingDAO.confirmMailing(mailingTwo);
			
			List<Mailing> results = mailingDAO.getAllUnconfirmed();
			List<Mailing> expectedResults = new ArrayList<Mailing>();
			
			expectedResults.add(mailingOne);
			
			results = mailingDAO.getAllUnconfirmed();
			
			assertEquals(expectedResults, results);
		} catch (PersistenceException e) {
			fail();
		}
	}
}

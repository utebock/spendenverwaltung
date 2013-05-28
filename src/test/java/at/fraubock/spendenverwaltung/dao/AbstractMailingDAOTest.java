package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.Date;

import org.apache.log4j.Logger;
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
	
	public static void setAddressDAO(IAddressDAO addressDAO) {
		AbstractMailingDAOTest.addressDAO = addressDAO;
	}
	
	public static void setPersonDAO(IPersonDAO personDAO) {
		AbstractMailingDAOTest.personDAO = personDAO;
	}
	
	public static void setMailingDAO(IMailingDAO MailingDAO) {
		AbstractMailingDAOTest.mailingDAO = MailingDAO;
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
		mailing.setType("Dankesschreiben");
		//TODO: mailing.setPersonFilter
		
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
		} catch (PersistenceException e) {
			fail();
		}

		//TODO define behaviour when mailing is created without
		//the relevant address being set, create mailing with more people
	}
	
	@Test
	@Transactional
	public void updateWithValidParameters() {
		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType("Tippfehler");
		
		Date currentDate = new Date(System.currentTimeMillis());
		
		mailing.setDate(currentDate);
				
		try {
			mailingDAO.insertOrUpdate(mailing);
		} catch (PersistenceException e) {
			fail();
		}
		
		mailing.setType("Dankesschreiben");
		
		try {
			mailingDAO.insertOrUpdate(mailing);
		} catch (PersistenceException e) {
			fail();
		}
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidParameters_ThrowsException() {
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidParameters_ThrowsException() {
		
	}
	
	@Test
	@Transactional
	public void getAll_shouldReturnList() {
		
	}
	
	@Test
	@Transactional
	public void getAll_shouldReturnEmptyList() {
		
	}
	
	@Test
	@Transactional
	public void findByValidId() {
		
	}
	
	@Transactional
	public void findByInvalidId_shouldReturnNull() {
		
	}
	
	@Test
	@Transactional
	public void deleteWithValidId() {
		
	}
	
	@Transactional
	public void deleteWithInvalidId_shouldReturnNull() {
		
	}
	
	@Test
	@Transactional	
	public void getMailingsByValidPerson() {
		
	}
	
	@Test
	@Transactional	
	public void getMailingsByValidPersonWithNoMailings_shouldReturnNull() {
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional	
	public void getMailingsByInvalidPerson_throwsException() {
		
	}
	
	
}

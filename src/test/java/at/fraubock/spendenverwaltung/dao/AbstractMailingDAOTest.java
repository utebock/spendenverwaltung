package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
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
	protected static Address validAddressOne = new Address();
	protected static Address validAddressTwo = new Address();
	protected static Address validAddressThree = new Address();
	
	protected static Address invalidAddressOne = new Address();

	protected static Person validPersonOne = new Person();
	protected static Person validPersonTwo_hasNoEmail = new Person();
	protected static Person validPersonThree_hasNoPostalAddress = new Person();
	
	protected static Person invalidPersonOne = new Person();
	
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
		
	}
	
	@Test
	@Transactional
	public void updateWithValidParameters() {
		
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

/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
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
public abstract class AbstractMailingDAOTest {

	// private static final Logger log =
	// Logger.getLogger(AbstractMailingDAOTest.class);

	/**
	 * PersonDAO is needed to create people for the tests AddressDAO is needed
	 * to create addresses for the people used in the tests
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

	// mailing instance used by tests
	protected static Mailing mailing;

	protected static Filter filterOnePerson = new Filter();
	protected static Filter filterTwoPeople = new Filter();
	protected static Filter filterNoPeople = new Filter();

	protected static MailingTemplate mt;
	protected static MailingTemplate mt2;

	protected static Date currentDate;

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

		String fs = File.separator;
		mt = new MailingTemplate();
		File f = new File("src" + fs + "test" + fs + "resources" + fs
				+ "examplemailing2.docx");
		mt.setFile(f);
		mt.setFileName(f.getName());

		mt2 = new MailingTemplate();
		File f2 = new File("src" + fs + "test" + fs + "resources" + fs
				+ "examplemailing2.docx");
		mt2.setFile(f2);
		mt2.setFileName(f2.getName());

		try {
			currentDate = new SimpleDateFormat("yyyy-MM-dd")
					.parse("2013-06-10");
		} catch (ParseException e) {
			currentDate = new Date();
		}
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException()
			throws PersistenceException {
		mailingDAO.insertOrUpdate(null);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException()
			throws PersistenceException {
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

		// Date currentDate = new Date(System.currentTimeMillis());

		mailing.setDate(currentDate);

		assertTrue(mailingDAO != null);
		mailingDAO.insertOrUpdate(mailing);

		Mailing result = mailingDAO.getById(mailing.getId());

		assertEquals(result, mailing);

		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.POSTAL);
		mailing.setType(Mailing.MailingType.DANKESBRIEF);
		mailing.setFilter(filterTwoPeople);
		mailing.setTemplate(mt);

		// currentDate = new Date(System.currentTimeMillis());
		mailing.setDate(currentDate);

		mailingDAO.insertOrUpdate(mailing);

		result = mailingDAO.getById(mailing.getId());

		assertEquals(result, mailing);

	}

	@Test
	@Transactional
	public void updateWithValidParameters() throws PersistenceException {
		initData();

		mailing = new Mailing();
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType(Mailing.MailingType.DANKESBRIEF);
		mailing.setFilter(filterTwoPeople);

		// Date currentDate = new Date(System.currentTimeMillis());

		mailing.setDate(currentDate);

		mailingDAO.insertOrUpdate(mailing);

		mailing.setType(Mailing.MailingType.DAUERSPENDER_DANKESBRIEF);

		mailingDAO.insertOrUpdate(mailing);

		Mailing result = mailingDAO.getById(mailing.getId());

		assertEquals(mailing, result);

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

		results = mailingDAO.getAll();

		assertEquals(createdMailings, results);

	}

	@Test
	@Transactional
	public void getAll_shouldReturnEmptyList() throws PersistenceException {

		List<Mailing> results = mailingDAO.getAll();
		assertTrue(results.isEmpty());

	}

	@Test
	@Transactional
	public void findByValidId() throws PersistenceException {
		initData();

		Mailing mailing = new Mailing();
		mailing.setDate(currentDate);
		mailing.setFilter(filterTwoPeople);
		mailing.setMedium(Mailing.Medium.EMAIL);
		mailing.setType(Mailing.MailingType.ALLGEMEINER_DANKESBRIEF);

		mailingDAO.insertOrUpdate(mailing);

		Mailing result = mailingDAO.getById(mailing.getId());
		assertEquals(result, mailing);

	}

	@Transactional
	public void findByInvalidId_shouldReturnNull() throws PersistenceException {

		assertNull(mailingDAO.getById(-1));

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
		mailing.setTemplate(mt2);

		Integer oldId = null;

		mailingDAO.insertOrUpdate(mailing);
		oldId = mailing.getId();

		mailingDAO.delete(mailing);

		Mailing result;

		result = mailingDAO.getById(oldId);
		assertNull(result);

	}

	@Test
	@Transactional
	public void deleteWithConfirmedMailing() throws PersistenceException {
		initData();

		Mailing mailing = new Mailing();
		mailing.setDate(new Date(System.currentTimeMillis()));
		mailing.setFilter(filterOnePerson);
		mailing.setMedium(Mailing.Medium.POSTAL);
		mailing.setTemplate(mt);
		mailing.setType(Mailing.MailingType.DAUERSPENDER_DANKESBRIEF);

		Integer oldId = null;

		mailingDAO.insertOrUpdate(mailing);
		mailingDAO.confirmMailing(mailing);
		oldId = mailing.getId();

		mailingDAO.delete(mailing);

		Mailing result;

		result = mailingDAO.getById(oldId);
		assertNull(result);

		List<Mailing> results = mailingDAO.getAllConfirmed();
		assertTrue(results.isEmpty());

	}

	@Test
	@Transactional
	public void deleteWithUnconfirmedMailing() throws PersistenceException {
		initData();

		Mailing mailing = new Mailing();
		mailing.setDate(new Date(System.currentTimeMillis()));
		mailing.setFilter(filterOnePerson);
		mailing.setMedium(Mailing.Medium.POSTAL);
		mailing.setTemplate(mt);
		mailing.setType(Mailing.MailingType.DAUERSPENDER_DANKESBRIEF);

		Integer oldId = null;

		mailingDAO.insertOrUpdate(mailing);
		oldId = mailing.getId();

		mailingDAO.delete(mailing);

		Mailing result;

		result = mailingDAO.getById(oldId);
		assertNull(result);
		List<Mailing> results = mailingDAO.getAllUnconfirmed();
		assertTrue(results.isEmpty());

	}

	@Test
	@Transactional
	public void getMailingsByValidPerson() throws PersistenceException {
		initData();

		Mailing mailingOne = new Mailing();
		Mailing mailingTwo = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(currentDate);
		mailingOne.setFilter(filterOnePerson);

		mailingTwo.setMedium(Mailing.Medium.POSTAL);
		mailingTwo.setType(Mailing.MailingType.DANKESBRIEF);
		mailingTwo.setDate(new Date(System.currentTimeMillis()));
		mailingTwo.setFilter(filterOnePerson);
		mailingTwo.setTemplate(mt2);

		List<Mailing> mailingList = new ArrayList<Mailing>();

		mailingList.add(mailingOne);
		mailingList.add(mailingTwo);

		mailingDAO.insertOrUpdate(mailingOne);
		mailingDAO.confirmMailing(mailingOne);
		mailingDAO.insertOrUpdate(mailingTwo);
		mailingDAO.confirmMailing(mailingTwo);

		List<Mailing> results = mailingDAO
				.getConfirmedMailingsByPerson(personOne);

		assertEquals(mailingList, results);
	}

	@Test
	@Transactional
	public void getMailingsByValidPersonWithNoMailings_shouldReturnNull()
			throws PersistenceException {

		initData();
		assertTrue(mailingDAO.getConfirmedMailingsByPerson(personOne).isEmpty());

	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void getMailingsByInvalidPerson_throwsException()
			throws PersistenceException {
		mailingDAO.getConfirmedMailingsByPerson(new Person());
	}

	@Test
	@Transactional
	public void getAllConfirmed_shouldReturnEmptyList()
			throws PersistenceException {
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

	}

	@Test
	@Transactional
	public void getAllConfirmed_shouldReturnOneMailing()
			throws PersistenceException {

		initData();

		Mailing mailingOne = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);

		Mailing mailingTwo = new Mailing();

		mailingTwo.setMedium(Mailing.Medium.POSTAL);
		mailingTwo.setTemplate(mt2);
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

	}

	@Test
	@Transactional
	public void getAllUnConfirmed_shouldReturnEmptyList()
			throws PersistenceException {
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
	public void getAllUnConfirmed_shouldReturnOneMailing()
			throws PersistenceException {
		initData();

		Mailing mailingOne = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);

		Mailing mailingTwo = new Mailing();

		mailingTwo.setMedium(Mailing.Medium.POSTAL);
		mailingTwo.setTemplate(mt2);
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

	}

	@Test
	@Transactional
	public void getCreatorOfMailing() throws PersistenceException {
		initData();

		Mailing mailingOne = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);

		mailingDAO.insertOrUpdate(mailingOne);

		String result = mailingDAO.getCreatorOfUnconfirmedMailing(mailingOne);
		assertEquals("ubadministrative", result);
	}

	// test depends on current user, should be ubadministrative@localhost, will
	// fail otherwise
	@Test(expected = PersistenceException.class)
	@Transactional
	public void getCreatorOfConfirmedMailingShouldThrowException()
			throws PersistenceException {
		initData();

		Mailing mailingOne = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);

		mailingDAO.insertOrUpdate(mailingOne);

		mailingDAO.confirmMailing(mailingOne);

		String result = mailingDAO.getCreatorOfUnconfirmedMailing(mailingOne);
		assertEquals("ubadministrative@localhost", result);
	}

	// again, depends on user, would need a jdbcTemplate in order to get the
	// user name dynamically
	@Test
	@Transactional
	public void getCreatorAndMailingsMap() throws PersistenceException {
		initData();

		String user = "ubadministrative";

		Mailing mailingOne = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);

		Mailing mailingTwo = new Mailing();

		mailingTwo.setMedium(Mailing.Medium.POSTAL);
		mailingTwo.setTemplate(mt2);
		mailingTwo.setType(Mailing.MailingType.DANKESBRIEF);
		mailingTwo.setDate(new Date(System.currentTimeMillis()));
		mailingTwo.setFilter(filterOnePerson);

		mailingDAO.insertOrUpdate(mailingOne);
		mailingDAO.insertOrUpdate(mailingTwo);

		List<UnconfirmedMailing> expectedList = new ArrayList<UnconfirmedMailing>();
		expectedList.add(new UnconfirmedMailing(mailingOne, user));
		expectedList.add(new UnconfirmedMailing(mailingTwo, user));

		List<UnconfirmedMailing> unconfirmedMailings = mailingDAO
				.getUnconfirmedMailingsWithCreator();

		assertEquals(expectedList, unconfirmedMailings);
	}

	@Test
	@Transactional
	public void getCreatorAndMailingsMapShouldBeEmpty()
			throws PersistenceException {
		initData();

		Mailing mailingOne = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);

		mailingDAO.insertOrUpdate(mailingOne);
		mailingDAO.confirmMailing(mailingOne);

		List<UnconfirmedMailing> unconfirmedMailings = mailingDAO
				.getUnconfirmedMailingsWithCreator();

		assertTrue(unconfirmedMailings.isEmpty());
	}

	@Test
	@Transactional
	public void getCreatorAndMailingsMapShouldBeEmpty2()
			throws PersistenceException {
		initData();

		List<UnconfirmedMailing> unconfirmedMailings = mailingDAO
				.getUnconfirmedMailingsWithCreator();

		assertTrue(unconfirmedMailings.isEmpty());
	}

	@Test
	@Transactional
	public void getCreatorAndMailingsMapShouldBeEmpty3()
			throws PersistenceException {
		initData();

		Mailing mailingOne = new Mailing();

		mailingOne.setMedium(Mailing.Medium.EMAIL);
		mailingOne.setType(Mailing.MailingType.DANKESBRIEF);
		mailingOne.setDate(new Date(System.currentTimeMillis()));
		mailingOne.setFilter(filterOnePerson);

		mailingDAO.insertOrUpdate(mailingOne);

		mailingDAO.delete(mailingOne);

		List<UnconfirmedMailing> unconfirmedMailings = mailingDAO
				.getUnconfirmedMailingsWithCreator();

		assertTrue(unconfirmedMailings.isEmpty());
	}
}

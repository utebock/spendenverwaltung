package at.fraubock.spendenverwaltung.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
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
import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractDonationDAOTest {
	private static final Logger log = Logger
			.getLogger(AbstractDonationDAOTest.class);

	protected static IPersonDAO personDAO;
	protected static IDonationDAO donationDAO;
	protected static IAddressDAO addressDAO;

	public static void setDonationDao(IDonationDAO donationDAO) {
		AbstractDonationDAOTest.donationDAO = donationDAO;
	}

	public static void setPersonDao(IPersonDAO personDAO) {
		AbstractDonationDAOTest.personDAO = personDAO;
	}

	public static void setAddressDao(IAddressDAO addressDAO) {
		AbstractDonationDAOTest.addressDAO = addressDAO;
	}

	/*
	 * testing create
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullParameterShouldThrowException()
			throws PersistenceException {
		donationDAO.insertOrUpdate(null);
	}

	@Test
	@Transactional
	public void createWithValidParameterShouldReturnDonation() {
		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person.setSex(Person.Sex.MALE);

		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person.setAddresses(addresses);
			person.setMainAddress(address);
			personDAO.insertOrUpdate(person);
		} catch (PersistenceException e) {
			fail();
		}

		log.info("Created person: " + person.getGivenName() + " "
				+ person.getSurname());

		donation.setDonator(person);
		donation.setAmount(100L);
		donation.setDate(new Date());
		donation.setDedication("Test dedication");
		donation.setNote("Test note");
		donation.setType(Donation.DonationType.SMS);

		try {
			donationDAO.insertOrUpdate(donation);
			Donation savedDonation = donationDAO.getByID(donation.getId());
			assertEquals(donation.getAmount(), savedDonation.getAmount());
			assertEquals(donation.getDedication(),
					savedDonation.getDedication());
			assertEquals(donation.getNote(), savedDonation.getNote());
			assertEquals(donation.getType(), savedDonation.getType());

		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void updateWithNullParameterShouldThrowException()
			throws PersistenceException {
		donationDAO.insertOrUpdate(null);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void updateWithInvalidParameterShouldThrowException()
			throws PersistenceException {
		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");

		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person.setAddresses(addresses);
			person.setMainAddress(address);
			personDAO.insertOrUpdate(person);
		} catch (PersistenceException e) {
			fail();
		}

		donation.setDonator(person);
		donation.setAmount(9999L);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("lkj");
		donation.setType(Donation.DonationType.BANK_TRANSFER);

		try {
			donationDAO.insertOrUpdate(donation);
			donation.setDonator(null);
		} catch (PersistenceException e) {
			fail();
		}

		donationDAO.insertOrUpdate(donation);
	}

	/*
	 * testing delete
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void deleteWithNullParameterShouldThrowException()
			throws PersistenceException {
		donationDAO.delete(null);
	}

	@Test
	@Transactional
	public void deleteWithValidParameterRemovesEntity() {

		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");

		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person.setAddresses(addresses);
			person.setMainAddress(address);
			personDAO.insertOrUpdate(person);
		} catch (PersistenceException e) {
			fail();
		}

		donation.setDonator(person);
		donation.setAmount(9999L);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("bla");
		donation.setType(Donation.DonationType.BANK_TRANSFER);

		try {
			donationDAO.insertOrUpdate(donation);
			Donation createdDonation = donationDAO.getByID(donation.getId());
			assertThat(createdDonation != null
					&& createdDonation.getId() == donation.getId(), is(true));

			donationDAO.delete(donation);
			assertNull(donationDAO.getByID(donation.getId()));
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAllByPersonReturnsAllEntitiesForPerson() {
		Person person1 = new Person();
		Person person2 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSex(Person.Sex.MALE);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("01234567889");
		person1.setNote("Test Note");

		person2.setSex(Person.Sex.FEMALE);
		person2.setCompany("Mustercompany");
		person2.setTitle("MSc");
		person2.setGivenName("Maxi");
		person2.setSurname("Musti");
		person2.setEmail("maximusti@maximusti.at");
		person2.setTelephone("01234567889");
		person2.setNote("Musternotiz");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person1.setAddresses(addresses);
			person1.setMainAddress(address);
			person2.setAddresses(addresses);
			person2.setMainAddress(address);
			personDAO.insertOrUpdate(person1);
			personDAO.insertOrUpdate(person2);
		} catch (PersistenceException e) {
			fail();
		}

		donation1.setDonator(person1);
		donation1.setAmount(9999L);
		donation1.setDate(new Date());
		donation1.setDedication("test");
		donation1.setNote("bla");
		donation1.setType(Donation.DonationType.BANK_TRANSFER);

		donation2.setDonator(person1);
		donation2.setAmount(1L);
		donation2.setDate(new Date());
		donation2.setDedication("test2");
		donation2.setNote("bla2");
		donation2.setType(Donation.DonationType.SMS);

		donation3.setDonator(person2);
		donation3.setAmount(50L);
		donation3.setDate(new Date());
		donation3.setDedication("test3");
		donation3.setNote("bla3");
		donation3.setType(Donation.DonationType.BANK_TRANSFER);

		try {
			donationDAO.insertOrUpdate(donation1);
			donationDAO.insertOrUpdate(donation2);
			donationDAO.insertOrUpdate(donation3);

			List<Donation> donationList1 = donationDAO.getByPerson(person1);
			List<Donation> donationList2 = donationDAO.getByPerson(person2);

			assertNotNull(donationList1);
			assertEquals(donationList1.size(), 2);
			// reverse order (descending id sort)
			assertEquals(donationList1.get(1).getId(), donation1.getId());
			assertEquals(donationList1.get(0).getId(), donation2.getId());

			assertNotNull(donationList2);
			assertEquals(donationList2.size(), 1);
			assertEquals(donationList2.get(0).getId(), donation3.getId());

		} catch (PersistenceException e) {
			fail();
		}

	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getByPersonWithNullThrowsException()
			throws PersistenceException {
		donationDAO.getByPerson(null);
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithInvalidIdReturnsNull() {
		try {
			assertNull(donationDAO.getByID(999999));
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeIdThrowsException() throws PersistenceException {
		assertNull(donationDAO.getByID(-1));
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidIdReturnsEntity() {
		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");

		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person.setAddresses(addresses);
			person.setMainAddress(address);
			personDAO.insertOrUpdate(person);
		} catch (PersistenceException e) {
			fail();
		}

		donation.setDonator(person);
		donation.setAmount(9999L);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("bla");
		donation.setType(Donation.DonationType.BANK_TRANSFER);

		try {
			donationDAO.insertOrUpdate(donation);
			Donation createdDonation = donationDAO.getByID(donation.getId());

			assertThat(createdDonation != null
					&& createdDonation.getId() == donation.getId(), is(true));

		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getByFilterWithDateReturnsOneEntity() {
		Person person1 = new Person();
		Person person2 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSex(Person.Sex.MALE);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("01234567889");
		person1.setNote("Test Note");

		person2.setSex(Person.Sex.FEMALE);
		person2.setCompany("Mustercompany");
		person2.setTitle("MSc");
		person2.setGivenName("Maxi");
		person2.setSurname("Musti");
		person2.setEmail("maximusti@maximusti.at");
		person2.setTelephone("01234567889");
		person2.setNote("Musternotiz");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person1.setAddresses(addresses);
			person1.setMainAddress(address);
			person2.setAddresses(addresses);
			person2.setMainAddress(address);
			personDAO.insertOrUpdate(person1);
			personDAO.insertOrUpdate(person2);
		} catch (PersistenceException e) {
			fail();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.set(2012, Calendar.JUNE, 20);
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2013, Calendar.JANUARY, 3);
		Calendar cal3 = Calendar.getInstance();
		cal3.set(2013, Calendar.MAY, 20);

		donation1.setDonator(person1);
		donation1.setAmount(9999L);
		donation1.setDate(cal1.getTime());
		donation1.setDedication("test");
		donation1.setNote("bla");
		donation1.setType(Donation.DonationType.BANK_TRANSFER);

		donation2.setDonator(person1);
		donation2.setAmount(1L);
		donation2.setDate(cal2.getTime());
		donation2.setDedication("Spendenaufruf Neujahr 2013");
		donation2.setNote("bla2");
		donation2.setType(Donation.DonationType.SMS);

		donation3.setDonator(person2);
		donation3.setAmount(50L);
		donation3.setDate(cal3.getTime());
		donation3.setDedication("test3");
		donation3.setNote("bla3");
		donation3.setType(Donation.DonationType.BANK_TRANSFER);

		Calendar minCal = Calendar.getInstance();
		minCal.set(2013, Calendar.JANUARY, 1);
		Calendar maxCal = Calendar.getInstance();
		maxCal.set(2013, Calendar.MARCH, 20);

		// create filter and criterions
		PropertyCriterion minDate = new PropertyCriterion();
		minDate.compare(FilterProperty.DONATION_DATE,
				RelationalOperator.GREATER_EQ, minCal.getTime());

		PropertyCriterion maxDate = new PropertyCriterion();
		maxDate.compare(FilterProperty.DONATION_DATE,
				RelationalOperator.LESS_EQ, maxCal.getTime());

		ConnectedCriterion andCrit = new ConnectedCriterion();
		andCrit.connect(minDate, LogicalOperator.AND, maxDate);

		Filter donationFilter = new Filter(FilterType.DONATION, andCrit);

		try {
			donationDAO.insertOrUpdate(donation1);
			donationDAO.insertOrUpdate(donation2);
			donationDAO.insertOrUpdate(donation3);

			List<Donation> donations = donationDAO.getByFilter(donationFilter);

			assertThat(donations != null && donations.size() == 1
					&& donations.get(0).equals(donation2), is(true));

		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getByFilterWithMinMaxAmountReturnsOneEntity() {
		Person person1 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSex(Person.Sex.MALE);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("01234567889");
		person1.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person1.setAddresses(addresses);
			person1.setMainAddress(address);
			personDAO.insertOrUpdate(person1);
		} catch (PersistenceException e) {
			fail();
		}

		donation1.setDonator(person1);
		donation1.setAmount(150L);
		donation1.setDate(new Date());
		donation1.setDedication("test");
		donation1.setNote("bla");
		donation1.setType(Donation.DonationType.BANK_TRANSFER);

		donation2.setDonator(person1);
		donation2.setAmount(10L);
		donation2.setDate(new Date());
		donation2.setDedication("Spendenaufruf Neujahr 2013");
		donation2.setNote("bla2");
		donation2.setType(Donation.DonationType.SMS);

		donation3.setDonator(person1);
		donation3.setAmount(80L);
		donation3.setDate(new Date());
		donation3.setDedication("test3");
		donation3.setNote("bla3");
		donation3.setType(Donation.DonationType.BANK_TRANSFER);

		// create filter and criterions
		PropertyCriterion minAmount = new PropertyCriterion();
		minAmount.compare(FilterProperty.DONATION_AMOUNT,
				RelationalOperator.GREATER_EQ, 40D);

		PropertyCriterion maxAmount = new PropertyCriterion();
		maxAmount.compare(FilterProperty.DONATION_AMOUNT,
				RelationalOperator.LESS_EQ, 110D);

		ConnectedCriterion andCrit = new ConnectedCriterion();
		andCrit.connect(minAmount, LogicalOperator.AND, maxAmount);

		Filter donationFilter = new Filter(FilterType.DONATION, andCrit);

		try {
			donationDAO.insertOrUpdate(donation1);
			donationDAO.insertOrUpdate(donation2);
			donationDAO.insertOrUpdate(donation3);

			List<Donation> donations = donationDAO.getByFilter(donationFilter);

			assertThat(donations != null && donations.size() == 1
					&& donations.get(0).equals(donation3), is(true));

		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getByFilterWithDedicationPartReturnsTwoEntities() {
		Person person1 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSex(Person.Sex.MALE);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("0123456789");
		person1.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person1.setAddresses(addresses);
			person1.setMainAddress(address);
			personDAO.insertOrUpdate(person1);
		} catch (PersistenceException e) {
			fail();
		}

		donation1.setDonator(person1);
		donation1.setAmount(150L);
		donation1.setDate(new Date());
		donation1.setDedication("Spendenaufruf 2013");
		donation1.setNote("bla5");
		donation1.setType(Donation.DonationType.BANK_TRANSFER);

		donation2.setDonator(person1);
		donation2.setAmount(10L);
		donation2.setDate(new Date());
		donation2.setDedication("Spendenaufruf 2013 J�nner");
		donation2.setNote("bla22");
		donation2.setType(Donation.DonationType.SMS);

		donation3.setDonator(person1);
		donation3.setAmount(80L);
		donation3.setDate(new Date());
		donation3.setDedication("Regelm��ige Spende");
		donation3.setNote("bla31");
		donation3.setType(Donation.DonationType.BANK_TRANSFER);

		// create filter and criterions
		PropertyCriterion dedication = new PropertyCriterion();
		dedication.compare(FilterProperty.DONATION_DEDICATION,
				RelationalOperator.EQUALS, "Spendenaufruf 2013");

		Filter donationFilter = new Filter(FilterType.DONATION, dedication);

		try {
			donationDAO.insertOrUpdate(donation1);
			donationDAO.insertOrUpdate(donation2);
			donationDAO.insertOrUpdate(donation3);

			List<Donation> donations = donationDAO.getByFilter(donationFilter);

			assertThat(donations != null && donations.size() == 2, is(true));

		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getByFilterWithNotePartReturnsOneEntity() {
		Person person1 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSex(Person.Sex.MALE);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("01234567889");
		person1.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person1.setAddresses(addresses);
			person1.setMainAddress(address);
			personDAO.insertOrUpdate(person1);
		} catch (PersistenceException e) {
			fail();
		}

		donation1.setDonator(person1);
		donation1.setAmount(150L);
		donation1.setDate(new Date());
		donation1.setDedication("test");
		donation1.setNote("Notiz blablabla");
		donation1.setType(Donation.DonationType.BANK_TRANSFER);

		donation2.setDonator(person1);
		donation2.setAmount(10L);
		donation2.setDate(new Date());
		donation2.setDedication("Spendenaufruf Neujahr 2013/1");
		donation2.setNote("Neujahrs Spende");
		donation2.setType(Donation.DonationType.SMS);

		donation3.setDonator(person1);
		donation3.setAmount(80L);
		donation3.setDate(new Date());
		donation3.setDedication("T-Shirt");
		donation3.setNote("Bestellid 123123123123");
		donation3.setType(Donation.DonationType.MERCHANDISE);

		// create filter and criterions
		PropertyCriterion note = new PropertyCriterion();
		note.compare(FilterProperty.DONATION_NOTE, RelationalOperator.EQUALS,
				"Bestellid");

		Filter donationFilter = new Filter(FilterType.DONATION, note);

		try {
			donationDAO.insertOrUpdate(donation1);
			donationDAO.insertOrUpdate(donation2);
			donationDAO.insertOrUpdate(donation3);

			List<Donation> donations = donationDAO.getByFilter(donationFilter);

			assertThat(donations != null && donations.size() == 1
					&& donations.get(0).equals(donation3), is(true));

		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getByFilterWithDonationTypeReturnsTwoEntities() {
		Person person1 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSex(Person.Sex.MALE);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("01234567889");
		person1.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person1.setAddresses(addresses);
			person1.setMainAddress(address);
			personDAO.insertOrUpdate(person1);
		} catch (PersistenceException e) {
			fail();
		}

		donation1.setDonator(person1);
		donation1.setAmount(150L);
		donation1.setDate(new Date());
		donation1.setDedication("test");
		donation1.setNote("bla");
		donation1.setType(Donation.DonationType.BANK_TRANSFER);

		donation2.setDonator(person1);
		donation2.setAmount(10L);
		donation2.setDate(new Date());
		donation2.setDedication("Spendenaufruf Neujahr 2013");
		donation2.setNote("bla2");
		donation2.setType(Donation.DonationType.SMS);

		donation3.setDonator(person1);
		donation3.setAmount(80L);
		donation3.setDate(new Date());
		donation3.setDedication("test3");
		donation3.setNote("bla3");
		donation3.setType(Donation.DonationType.BANK_TRANSFER);

		// create filter and criterions
		PropertyCriterion type = new PropertyCriterion();
		type.compare(FilterProperty.DONATION_TYPE, RelationalOperator.EQUALS,
				Donation.DonationType.BANK_TRANSFER.toString());

		Filter donationFilter = new Filter(FilterType.DONATION, type);

		try {
			donationDAO.insertOrUpdate(donation1);
			donationDAO.insertOrUpdate(donation2);
			donationDAO.insertOrUpdate(donation3);

			List<Donation> donations = donationDAO.getByFilter(donationFilter);

			assertThat(donations != null && donations.size() == 2, is(true));

		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getByFilterWithAllReturnsOneEntity() {

		Person person1 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSex(Person.Sex.MALE);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("01234567889");
		person1.setNote("Test Note");

		try {
			addressDAO.insertOrUpdate(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);

			person1.setAddresses(addresses);
			person1.setMainAddress(address);
			personDAO.insertOrUpdate(person1);
		} catch (PersistenceException e) {
			fail();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.set(2012, Calendar.JUNE, 20);
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2013, Calendar.JANUARY, 3);
		Calendar cal3 = Calendar.getInstance();
		cal3.set(2013, Calendar.MAY, 20);

		donation1.setDonator(person1);
		donation1.setAmount(9999L);
		donation1.setDate(cal1.getTime());
		donation1.setDedication("test");
		donation1.setNote("bla");
		donation1.setType(Donation.DonationType.BANK_TRANSFER);

		donation2.setDonator(person1);
		donation2.setAmount(1L);
		donation2.setDate(cal2.getTime());
		donation2.setDedication("Spendenaufruf Neujahr 2013");
		donation2.setNote("bla2");
		donation2.setType(Donation.DonationType.SMS);

		donation3.setDonator(person1);
		donation3.setAmount(50L);
		donation3.setDate(cal3.getTime());
		donation3.setDedication("Spendenaufruf");
		donation3.setNote("bla3");
		donation3.setType(Donation.DonationType.BANK_TRANSFER);

		Calendar minCal = Calendar.getInstance();
		minCal.set(2013, Calendar.JANUARY, 1);
		Calendar maxCal = Calendar.getInstance();
		maxCal.set(2013, Calendar.DECEMBER, 31);

		// create filter and criterions
		PropertyCriterion type = new PropertyCriterion();
		type.compare(FilterProperty.DONATION_TYPE, RelationalOperator.EQUALS,
				Donation.DonationType.BANK_TRANSFER.toString());

		PropertyCriterion note = new PropertyCriterion();
		note.compare(FilterProperty.DONATION_NOTE, RelationalOperator.EQUALS,
				"Bestellid");

		PropertyCriterion dedication = new PropertyCriterion();
		dedication.compare(FilterProperty.DONATION_DEDICATION,
				RelationalOperator.EQUALS, "Spendenaufruf 2013");

		PropertyCriterion minAmount = new PropertyCriterion();
		minAmount.compare(FilterProperty.DONATION_AMOUNT,
				RelationalOperator.GREATER_EQ, 40D);

		PropertyCriterion maxAmount = new PropertyCriterion();
		maxAmount.compare(FilterProperty.DONATION_AMOUNT,
				RelationalOperator.LESS_EQ, 110D);

		PropertyCriterion minDate = new PropertyCriterion();
		minDate.compare(FilterProperty.DONATION_DATE,
				RelationalOperator.GREATER_EQ, minCal.getTime());

		PropertyCriterion maxDate = new PropertyCriterion();
		maxDate.compare(FilterProperty.DONATION_DATE,
				RelationalOperator.LESS_EQ, maxCal.getTime());

		ConnectedCriterion typeAndNote = new ConnectedCriterion();
		typeAndNote.connect(type, LogicalOperator.AND, note);

		ConnectedCriterion dedicationAndtAn = new ConnectedCriterion();
		typeAndNote.connect(dedication, LogicalOperator.AND, typeAndNote);

		ConnectedCriterion amountCrit = new ConnectedCriterion();
		amountCrit.connect(minAmount, LogicalOperator.AND, maxAmount);

		ConnectedCriterion dateCrit = new ConnectedCriterion();
		dateCrit.connect(minDate, LogicalOperator.AND, maxDate);

		ConnectedCriterion dateAndAmount = new ConnectedCriterion();
		dateCrit.connect(dateCrit, LogicalOperator.AND, amountCrit);

		ConnectedCriterion mainCrit = new ConnectedCriterion();
		typeAndNote.connect(dateAndAmount, LogicalOperator.AND,
				dedicationAndtAn);

		Filter donationFilter = new Filter(FilterType.DONATION, mainCrit);

		try {
			donationDAO.insertOrUpdate(donation1);
			donationDAO.insertOrUpdate(donation2);
			donationDAO.insertOrUpdate(donation3);

			List<Donation> donations = donationDAO.getByFilter(donationFilter);

			assertThat(donations != null && donations.size() == 1
					&& donations.get(0).equals(donation3), is(true));

		} catch (PersistenceException e) {
			fail();
		}
	}
}

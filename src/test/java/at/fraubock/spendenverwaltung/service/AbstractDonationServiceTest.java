package at.fraubock.spendenverwaltung.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractDonationServiceTest {

	protected static IDonationService donationService;
	protected static IPersonService personService;

	protected static IPersonDAO personDAO;
	protected static Person person;
	protected static Person person2;
	protected static Person person3;

	protected static IDonationDAO donationDAO;
	protected static Address testAddress;

	protected static Donation donation;
	protected static Donation donation2;
	protected static Donation donation3;
	protected static Donation donationCreated;
	protected static Donation nullDonation;

	public static void setDonationService(IDonationService donationService) {
		AbstractDonationServiceTest.donationService = donationService;
	}

	@Test(expected = ServiceException.class)
	@Transactional
	public void createWithNullParameterShouldThrowException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(donationDAO)
					.insertOrUpdate(null);

			donationService.create(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameterShouldReturnDonation() {
		try {
			Donation returned = donationService.create(donation);
			assertEquals(returned, donationCreated);
			verify(donationDAO).insertOrUpdate(donation);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = ServiceException.class)
	@Transactional
	public void updateWithNullParameterShouldThrowException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(donationDAO)
					.insertOrUpdate(null);

			donationService.update(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	@Transactional
	public void updateWithInvalidParameterShouldThrowException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(donationDAO)
					.insertOrUpdate(nullDonation);

			donationService.update(nullDonation);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParametersShouldReturnUpdatedDonation() {
		try {
			Donation returned = donationService.update(donationCreated);
			assertEquals(returned, donationCreated);
			verify(donationDAO).insertOrUpdate(donationCreated);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = ServiceException.class)
	@Transactional
	public void deleteWithNullParameterShouldThrowException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(donationDAO).delete(null);

			donationService.delete(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void deleteWithValidParameterRemovesEntity() {
		try {
			donationService.delete(donation);
			verify(donationDAO).delete(donation);
		} catch (ServiceException e) {
			fail();
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
		try {
			List<Donation> all = new ArrayList<Donation>();
			all.add(donation);
			all.add(donation2);
			when(donationDAO.getByPerson(person)).thenReturn(all);

			List<Donation> list = donationService.getByPerson(person);
			assert (list != null && list.size() == 2);
			assert (list.get(0).equals(donation) && list.get(1).equals(
					donation2));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional(readOnly = true)
	public void getWithInvalidIdReturnsNull() {
		try {
			when(donationDAO.getByID(10000)).thenThrow(
					new EmptyResultDataAccessException(0));

			donationService.getByID(10000);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() {
		try {
			when(donationDAO.getByID(-1)).thenThrow(
					new IllegalArgumentException());

			donationService.getByID(-1);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidIdReturnsEntity() {
		try {
			when(donationDAO.getByID(donationCreated.getId())).thenReturn(
					donationCreated);

			Donation found = donationService.getByID(donationCreated.getId());

			assertEquals(donationCreated, found);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	protected static void init() throws PersistenceException {
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode("00000");
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");

		// testAddress = addressDAO.create(testAddress);
		List<Address> listTest = new ArrayList<Address>();
		listTest.add(testAddress);

		person = new Person();
		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setEmailNotification(true);
		person.setNote("");
		// person.setAddresses(listTest);
		// person.setMailingAddress(testAddress);

		person2 = new Person();
		person2.setSex(Person.Sex.MALE);
		person2.setCompany("IBM");
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Test2");
		person2.setSurname("Test2");
		person2.setEmail("test2@test2.at");
		person2.setTelephone("02234567889");
		person2.setEmailNotification(true);
		person2.setNote("");
		// person2.setAddresses(listTest2);
		// person2.setMailingAddress(testAddress2);

		person3 = new Person();
		person3.setSex(Person.Sex.MALE);
		person3.setCompany("asfd");
		person3.setTitle("b");
		person3.setGivenName("dfdasd");
		person3.setSurname("ffff");
		person3.setEmail("test2@ff.at");
		person3.setTelephone("111");
		person2.setEmailNotification(true);
		person3.setNote("");
		// person2.setAddresses(listTest2);
		// person2.setMailingAddress(testAddress2);

		donation = new Donation();
		donation.setAmount(new Long(100));
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("test");
		donation.setDonator(person);
		donation.setType(Donation.DonationType.BANK_TRANSFER);

		donation2 = new Donation();
		donation2.setAmount(new Long(200));
		donation2.setDate(new Date());
		donation2.setDedication("test2");
		donation2.setNote("test2");
		donation2.setDonator(person);
		donation2.setType(Donation.DonationType.SMS);

		donation3 = new Donation();
		donation3.setAmount(new Long(300));
		donation3.setDate(new Date());
		donation3.setDedication("test3");
		donation3.setNote("test3");
		donation3.setDonator(person3);
		donation3.setType(Donation.DonationType.MERCHANDISE);

		donationCreated = donation;
		donationCreated.setId(1);

		nullDonation = null;

	}

	@Test(expected = IllegalArgumentException.class)
	public void createCSVWithNullArgument_ThrowsException() {
		donationService.convertToCSV(null);
	}

	@Test
	public void createCSVWithValidArgument_ReturnsCSVString() {
		List<Donation> list = new ArrayList<Donation>();
		donation.setDate(new Date());
		donation2.setDate(new Date());
		donation3.setDate(new Date());
		list.add(donation);
		list.add(donation2);
		list.add(donation3);
		String csv = donationService.convertToCSV(list);
		assertThat(csv.equals(csvExpected), is(true));
	}

	@Test
	public void createCSVWithEmptyList_ReturnsCSVString() {
		List<Donation> list = new ArrayList<Donation>();
		String csv = donationService.convertToCSV(list);
		assertThat(
				csv.equals("Betrag;Datum;Widmung;Art;Notiz;Vorname;Nachname;" +
						"E-Mail;Unternehmen;Land;Stadt;PLZ;Strasse\n"),
				is(true));
	}

	private String csvExpected = "Betrag;Datum;Widmung;Art;Notiz;Vorname;Nachname;E-Mail;Unternehmen;Land;Stadt;PLZ;Strasse\n"
			+ "100;10.06.2013;test;BANK_TRANSFER;test;Test;Test;test@test.at;IBM;n.v.;n.v.;n.v.;n.v.;\n"
			+ "200;10.06.2013;test2;SMS;test2;Test;Test;test@test.at;IBM;n.v.;n.v.;n.v.;n.v.;\n"
			+ "300;10.06.2013;test3;MERCHANDISE;test3;dfdasd;ffff;test2@ff.at;asfd;n.v.;n.v.;n.v.;n.v.;\n";

}
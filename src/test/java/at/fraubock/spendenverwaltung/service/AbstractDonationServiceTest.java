package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;

public abstract class AbstractDonationServiceTest {

	protected IDonationService donationService;

	protected final IPersonDAO personDAO = mock(IPersonDAO.class);
	protected final IDonationDAO donationDAO = mock(IDonationDAO.class);

	private Person person;
	private Person person2;
	private Person person3;
	private Address testAddress;

	private Donation donation;
	private Donation donation2;
	private Donation donation3;
	private Donation donationCreated;
	private Donation nullDonation;

	@Before
	public void init() throws PersistenceException {
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

	@Test(expected = ServiceException.class)
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

	@Test(expected = IllegalArgumentException.class)
	public void createCSVWithNullArgument_ThrowsException() {
		donationService.convertToCSV(null);
	}

	@Test
	public void createCSVWithValidArgument_ReturnsCSVString() {
		List<Donation> list = new ArrayList<Donation>();
		try {
			donation.setDate(new SimpleDateFormat("dd.MM.yyyy").parse("12.06.2013"));
			donation2.setDate(new SimpleDateFormat("dd.MM.yyyy").parse("13.06.2013"));
			donation3.setDate(new SimpleDateFormat("dd.MM.yyyy").parse("14.06.2013"));
		} catch (ParseException e) {
			fail();
		}
		list.add(donation);
		list.add(donation2);
		list.add(donation3);
		String csv = donationService.convertToCSV(list);
		assertTrue(csv.equals(csvExpected));
	}

	@Test
	public void createCSVWithEmptyList_ReturnsCSVString() {
		List<Donation> list = new ArrayList<Donation>();
		String csv = donationService.convertToCSV(list);
		assertTrue(csv
				.equals("Betrag;Datum;Widmung;Art;Notiz;Vorname;Nachname;"
						+ "E-Mail;Unternehmen;Land;Stadt;PLZ;Strasse\n"));
	}

	private String csvExpected = "Betrag;Datum;Widmung;Art;Notiz;Vorname;Nachname;E-Mail;Unternehmen;Land;Stadt;PLZ;Strasse\n"
			+ "100;12.06.2013;test;BANK_TRANSFER;test;Test;Test;test@test.at;IBM;n.v.;n.v.;n.v.;n.v.;\n"
			+ "200;13.06.2013;test2;SMS;test2;Test;Test;test@test.at;IBM;n.v.;n.v.;n.v.;n.v.;\n"
			+ "300;14.06.2013;test3;MERCHANDISE;test3;dfdasd;ffff;test2@ff.at;asfd;n.v.;n.v.;n.v.;n.v.;\n";

}
package service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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

import service.IAddressService;
import service.IPersonService;
import dao.IAddressDAO;
import dao.IDonationDAO;
import dao.IPersonDAO;
import domain.Address;
import domain.Donation;
import domain.Person;
import exceptions.PersistenceException;
import exceptions.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../testspring.xml")
@TransactionConfiguration(defaultRollback=true)

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
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameterShouldThrowException() {
		try {
			when(donationDAO.create(null)).thenThrow(
					new IllegalArgumentException());
			
			donationService.create(null);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameterShouldReturnDonation() {
		try {
			when(donationDAO.create(donation)).thenReturn(donationCreated);

			Donation returned = donationService.create(donation);
			assert (returned.equals(donationCreated));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithNullParameterShouldThrowException() {
		try {
			when(donationDAO.update(null)).thenThrow(
					new IllegalArgumentException());
			
			donationService.update(null);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidParameterShouldThrowException() {
		try {
			when(donationDAO.update(nullDonation)).thenThrow(
					new IllegalArgumentException());

			donationService.update(nullDonation);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParametersShouldReturnUpdatedDonation() {
		try {
			when(donationDAO.update(donationCreated)).thenReturn(
					donationCreated);

			Donation returned = donationService.update(donationCreated);
			assert (returned.equals(donationCreated));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteWithNullParameterShouldThrowException() {
		try {
			doThrow(new IllegalArgumentException()).when(donationDAO).delete(null);
			
			donationService.delete(null);
		} catch (ServiceException e) {
			fail();
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
			assert (list.get(0).equals(donation) && list
					.get(1).equals(donation2));
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

			Donation found = donationService.getByID(donationCreated
					.getId());

			assert (donationCreated.equals(found));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	
	
	
	protected static void init() throws PersistenceException {
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode(00000);
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");
		
		//testAddress = addressDAO.create(testAddress);
		List<Address> listTest = new ArrayList<Address>();
		listTest.add(testAddress);
		
		person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("");
		//person.setAddresses(listTest);
		//person.setMailingAddress(testAddress);
		
		person2 = new Person();
		person2.setSalutation(Person.Salutation.HERR);
		person2.setCompany("IBM");
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Test2");
		person2.setSurname("Test2");
		person2.setEmail("test2@test2.at");
		person2.setTelephone("02234567889");
		person2.setNotificationType(Person.NotificationType.MAIL);
		person2.setNote("");
		//person2.setAddresses(listTest2);
		//person2.setMailingAddress(testAddress2);
		
		person3 = new Person();
		person3.setSalutation(Person.Salutation.HERR);
		person3.setCompany("asfd");
		person3.setTitle("b");
		person3.setGivenName("dfdasd");
		person3.setSurname("ffff");
		person3.setEmail("test2@ff.at");
		person3.setTelephone("111");
		person3.setNotificationType(Person.NotificationType.MAIL);
		person3.setNote("");
		//person2.setAddresses(listTest2);
		//person2.setMailingAddress(testAddress2);
		
		donation = new Donation();
		donation.setAmount(100);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("test");
		donation.setPerson(person);
		donation.setType(Donation.DonationType.BANKING);
		
		donation2 = new Donation();
		donation2.setAmount(200);
		donation2.setDate(new Date());
		donation2.setDedication("test2");
		donation2.setNote("test2");
		donation2.setPerson(person);
		donation2.setType(Donation.DonationType.SMS);
		
		donation3 = new Donation();
		donation3.setAmount(300);
		donation3.setDate(new Date());
		donation3.setDedication("test3");
		donation3.setNote("test3");
		donation3.setPerson(person3);
		donation3.setType(Donation.DonationType.SHOP);
		
		donationCreated = donation;
		donationCreated.setId(1);
		
		nullDonation = null;
		
	}
	
}
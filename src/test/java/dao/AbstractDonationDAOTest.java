package dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Address;
import domain.Donation;
import domain.Person;
import exceptions.PersistenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../testspring.xml")
@TransactionConfiguration(defaultRollback=true)
public abstract class AbstractDonationDAOTest {
	private static final Logger log = Logger.getLogger(AbstractDonationDAOTest.class);

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

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameterShouldThrowException() {
		try {
			donationDAO.create(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameterShouldReturnDonation() {
		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");
		
		person.setSalutation(Person.Salutation.HERR);
		
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("Test Note");
		
		try {
			address = addressDAO.create(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			
			person.setAddresses(addresses);
			person.setMailingAddress(address);
			personDAO.create(person);
		} catch (PersistenceException e) {
			fail();
		}
		
		log.info("Created person: "+ person.getGivenName() +" "+ person.getSurname());
		
		donation.setPerson(person);
		donation.setAmount(100);
		donation.setDate(new Date());
		donation.setDedication("Test dedication");
		donation.setNote("Test note");
		donation.setType(Donation.DonationType.SMS);
		
		try {
			Donation returnedDonation = donationDAO.create(donation);
			Donation savedDonation = donationDAO.getByID(returnedDonation.getId());
			assert(returnedDonation.equals(savedDonation));
			
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
			donationDAO.update(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidParameterShouldThrowException() {
		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");
		
		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("Test Note");
		
		try {
			address = addressDAO.create(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			
			person.setAddresses(addresses);
			person.setMailingAddress(address);
			person = personDAO.create(person);
		} catch (PersistenceException e) {
			fail();
		}

		donation.setPerson(person);
		donation.setAmount(9999);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("lkj");
		donation.setType(Donation.DonationType.BANKING);

		try {
			donation = donationDAO.create(donation);
			donation.setPerson(null);

			donationDAO.update(donation);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParametersShouldReturnUpdatedDonation() {
		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("Test Note");
		
		try {
			address = addressDAO.create(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			
			person.setAddresses(addresses);
			person.setMailingAddress(address);
			person = personDAO.create(person);
		} catch (PersistenceException e) {
			fail();
		}

		donation.setPerson(person);
		donation.setAmount(9999);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("lkj");
		donation.setType(Donation.DonationType.BANKING);

		try {
			donation = donationDAO.create(donation);
			donation.setAmount(5);

			Donation returnedDonation = donationDAO.update(donation);
			
			assertThat(donation.getAmount() == returnedDonation.getAmount(), is(true));
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
			donationDAO.delete(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional
	public void deleteWithValidParameterRemovesEntity() {

		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("Test Note");
		
		try {
			address = addressDAO.create(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			
			person.setAddresses(addresses);
			person.setMailingAddress(address);
			person = personDAO.create(person);
		} catch (PersistenceException e) {
			fail();
		}

		donation.setPerson(person);
		donation.setAmount(9999);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("bla");
		donation.setType(Donation.DonationType.BANKING);

		try {
			donation = donationDAO.create(donation);
			Donation createdDonation = donationDAO.getByID(donation.getId());
			assertThat(createdDonation != null && createdDonation.getId() == donation.getId(), is(true));
			
			donationDAO.delete(donation);
			Donation deletedDonation = donationDAO.getByID(donation.getId());
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly=true)
	public void getAllByPersonReturnsAllEntitiesForPerson() {
		Person person1 = new Person();
		Person person2 = new Person();
		Donation donation1 = new Donation();
		Donation donation2 = new Donation();
		Donation donation3 = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person1.setSalutation(Person.Salutation.HERR);
		person1.setCompany("IBM");
		person1.setTitle("Prof. Dr.");
		person1.setGivenName("Heinz");
		person1.setSurname("Oberhummer");
		person1.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person1.setTelephone("01234567889");
		person1.setNotificationType(Person.NotificationType.MAIL);
		person1.setNote("Test Note");

		person2.setSalutation(Person.Salutation.FRAU);
		person2.setCompany("Mustercompany");
		person2.setTitle("MSc");
		person2.setGivenName("Maxi");
		person2.setSurname("Musti");
		person2.setEmail("maximusti@maximusti.at");
		person2.setTelephone("01234567889");
		person2.setNotificationType(Person.NotificationType.POST);
		person2.setNote("Musternotiz");

		try {
			address = addressDAO.create(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			
			person1.setAddresses(addresses);
			person1.setMailingAddress(address);
			person2.setAddresses(addresses);
			person2.setMailingAddress(address);
			person1 = personDAO.create(person1);
			person2 = personDAO.create(person2);
		} catch (PersistenceException e) {
			fail();
		}
		
		donation1.setPerson(person1);
		donation1.setAmount(9999);
		donation1.setDate(new Date());
		donation1.setDedication("test");
		donation1.setNote("bla");
		donation1.setType(Donation.DonationType.BANKING);
		
		donation2.setPerson(person1);
		donation2.setAmount(1);
		donation2.setDate(new Date());
		donation2.setDedication("test2");
		donation2.setNote("bla2");
		donation2.setType(Donation.DonationType.SMS);
		
		donation3.setPerson(person2);
		donation3.setAmount(50);
		donation3.setDate(new Date());
		donation3.setDedication("test3");
		donation3.setNote("bla3");
		donation3.setType(Donation.DonationType.BANKING);

		try {
			donation1 = donationDAO.create(donation1);
			donation2 = donationDAO.create(donation2);
			donation3 = donationDAO.create(donation3);
			
			List<Donation> donationList1 = donationDAO.getByPerson(person1);
			List<Donation> donationList2 = donationDAO.getByPerson(person2);
			
			assertThat(donationList1 != null
					&& donationList1.size() == 2
					&& donationList1.get(0).getId() == donation1.getId() 
					&& donationList1.get(1).getId() == donation2.getId(), is(true));
			
			assertThat((donationList1 != null
					&& donationList2.size() == 1
					&& donationList2.get(0).getId() == donation3.getId()), is(true));
			
		} catch (PersistenceException e) {
			fail();
		}

	}
	
	@Test (expected = EmptyResultDataAccessException.class)
	@Transactional(readOnly=true)
	public void getWithInvalidIdReturnsNull() {
		try {
			@SuppressWarnings("unused")
			Donation donation = donationDAO.getByID(999999);
		} catch (PersistenceException e) {
			fail();
		}
	}
	
	@Test
	@Transactional(readOnly=true)
	public void getWithValidIdReturnsEntity() {
		Person person = new Person();
		Donation donation = new Donation();
		Address address = new Address();

		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		person.setSalutation(Person.Salutation.HERR);
		person.setCompany("IBM");
		
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNotificationType(Person.NotificationType.MAIL);
		person.setNote("Test Note");
		
		try {
			address = addressDAO.create(address);
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(address);
			
			person.setAddresses(addresses);
			person.setMailingAddress(address);
			person = personDAO.create(person);
		} catch (PersistenceException e) {
			fail();
		}

		donation.setPerson(person);
		donation.setAmount(9999);
		donation.setDate(new Date());
		donation.setDedication("test");
		donation.setNote("bla");
		donation.setType(Donation.DonationType.BANKING);

		try {
			donation = donationDAO.create(donation);
			Donation createdDonation = donationDAO.getByID(donation.getId());
			
			assertThat(createdDonation!=null && createdDonation.getId()==donation.getId(), is(true));
			
		} catch (PersistenceException e) {
			fail();
		}
	}

}

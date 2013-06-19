package at.fraubock.spendenverwaltung.util;

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
import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractStatsTest {

	protected static IPersonDAO personDAO;
	protected static IDonationDAO donationDAO;
	protected static IAddressDAO addressDAO;
	protected static IFilterDAO filterDAO;

	public static void setDonationDao(IDonationDAO donationDAO) {
		AbstractStatsTest.donationDAO = donationDAO;
	}

	public static void setPersonDao(IPersonDAO personDAO) {
		AbstractStatsTest.personDAO = personDAO;
	}

	public static void setAddressDao(IAddressDAO addressDAO) {
		AbstractStatsTest.addressDAO = addressDAO;
	}

	public static void setFilterDao(IFilterDAO filterDAO) {
		AbstractStatsTest.filterDAO = filterDAO;
	}

	@Test
	@Transactional
	public void getCountOfFilterListShoutGetCorrectCount()
			throws PersistenceException {
		Person person = new Person();
		Donation donation = new Donation();
		Donation donation2 = new Donation();
		Address address = new Address();
		Filter filter = new Filter();
		PropertyCriterion crit = new PropertyCriterion();
		Filter filter2 = new Filter();
		PropertyCriterion crit2 = new PropertyCriterion();

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

		addressDAO.insertOrUpdate(address);
		List<Address> addresses = new ArrayList<Address>();
		addresses.add(address);

		person.setAddresses(addresses);
		person.setMainAddress(address);
		personDAO.insertOrUpdate(person);

		donation.setDonator(person);
		donation.setAmount(100L);
		donation.setDate(new Date());
		donation.setDedication("Test dedication");
		donation.setNote("Test note");
		donation.setType(Donation.DonationType.SMS);

		donationDAO.insertOrUpdate(donation);

		donation2.setDonator(person);
		donation2.setAmount(200L);
		donation2.setDate(new Date());
		donation2.setDedication("Test dedication");
		donation2.setNote("Test note");
		donation2.setType(Donation.DonationType.SMS);

		donationDAO.insertOrUpdate(donation2);

		crit.setType(FilterType.DONATION);
		crit.setProperty(FilterProperty.DONATION_AMOUNT);
		crit.setRelationalOperator(RelationalOperator.GREATER_EQ);
		crit.setStrValue("100");

		filter.setCriterion(crit);
		filter.setName("Test");
		filter.setType(FilterType.DONATION);

		filterDAO.insert(filter);

		crit2.setType(FilterType.DONATION);
		crit2.setProperty(FilterProperty.DONATION_AMOUNT);
		crit2.setRelationalOperator(RelationalOperator.LESS);
		crit2.setStrValue("150");

		filter2.setCriterion(crit2);
		filter2.setName("Test2");
		filter2.setType(FilterType.DONATION);

		filterDAO.insert(filter2);

		donationDAO.getByFilter(filter2);

	}

}

package at.fraubock.spendenverwaltung.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
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
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.statistics.DonationStats;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)

public abstract class AbstractStatsTest {
	private static final Logger log = Logger.getLogger(AbstractStatsTest.class);

	protected static IPersonDAO personDAO;
	protected static IDonationDAO donationDAO;
	protected static IAddressDAO addressDAO;
	protected static IFilterDAO filterDAO;
	protected  DonationStats donationStats = new DonationStats();

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
	public void getCountOfFilterListShoutGetCorrectCount(){
		Person person = new Person();
		Donation donation = new Donation();
		Donation donation2 = new Donation();
		Address address = new Address();
		Filter filter = new Filter();
		PropertyCriterion crit = new PropertyCriterion();
		List<Donation> list = new ArrayList<Donation>();
		
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

		} catch (PersistenceException e) {
			fail();
		}
		
		donation2.setDonator(person);
		donation2.setAmount(200L);
		donation2.setDate(new Date());
		donation2.setDedication("Test dedication");
		donation2.setNote("Test note");
		donation2.setType(Donation.DonationType.SMS);

		try {
			donationDAO.insertOrUpdate(donation2);

		} catch (PersistenceException e) {
			fail();
		}
		
		crit.setType(FilterType.DONATION);
		crit.setProperty(FilterProperty.DONATION_AMOUNT);
		crit.setRelationalOperator(RelationalOperator.GREATER_EQ);
		crit.setStrValue("100");
		
		filter.setCriterion(crit);
		filter.setName("Test");
		filter.setType(FilterType.DONATION);
		
		try{
			filterDAO.insertOrUpdate(filter);
		}catch(PersistenceException e){
			fail();
		}
		
		try {
			list = donationDAO.getByFilter(filter);
			
		} catch (PersistenceException e) {
			fail();
			log.error("GetByFilter in AbsractStatsTest does not work");
		}
		
		assert(list.size()==2);
		assert(donationStats.getSum(list)==300.0);
		assert(donationStats.getMean(list)==150.0);
		assert(donationStats.getMax(list)==200.0);
		assert(donationStats.getMin(list)==100.0);
		assert(donationStats.getMedian(list)==150.0);
		
		
	}
	
}

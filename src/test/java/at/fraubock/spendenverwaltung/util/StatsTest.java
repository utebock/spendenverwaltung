package at.fraubock.spendenverwaltung.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;

public class StatsTest extends AbstractStatsTest {
	private static ApplicationContext context;

	/**
	 * obtain personDao bean, initializing ApplicationContext
	 */
	@BeforeClass
	public static void setup() {
		context = new ClassPathXmlApplicationContext("testspring.xml");

		setDonationDao(context.getBean("donationDao", IDonationDAO.class));
		setAddressDao(context.getBean("addressDao", IAddressDAO.class));
		setPersonDao(context.getBean("personDao", IPersonDAO.class));
		setFilterDao(context.getBean("filterDao", IFilterDAO.class));
	}

	/**
	 * manually close ApplicationContext and associated resources
	 */
	@AfterClass
	public static void shutdown() {
		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}

}

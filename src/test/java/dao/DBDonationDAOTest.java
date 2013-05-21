package dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBDonationDAOTest extends AbstractDonationDAOTest {
	
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
	}
	
	/**
	 * manually close ApplicationContext and associated resources
	 */
	@AfterClass 
	public static void shutdown() {
		if(context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}

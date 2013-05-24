package at.fraubock.spendenverwaltung.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;

public class DBPersonDAOTest extends AbstractPersonDAOTest {
	
	private static ApplicationContext context;
	
	/**
	 * obtain personDao bean, initializing ApplicationContext
	 */
	@BeforeClass
	public static void setup() {
		context = new ClassPathXmlApplicationContext("testspring.xml");
		
		setPersonDao(context.getBean("personDao", IPersonDAO.class));
		setAddressDao(context.getBean("addressDao", IAddressDAO.class));
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

package at.fraubock.spendenverwaltung.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;

public class DBAddressDAOTest extends AbstractAddressDAOTest {

	private static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("testspring.xml");
		
		setAddressDAO(context.getBean("addressDao", IAddressDAO.class));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}

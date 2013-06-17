package at.fraubock.spendenverwaltung.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;

public class DBActionDAOTest extends AbstractActionDAOTest {

	private static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("testspring.xml");

		setActionDAO(context.getBean("actionDao", IActionDAO.class));
		setPersonDAO(context.getBean("personDao", IPersonDAO.class));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}

}

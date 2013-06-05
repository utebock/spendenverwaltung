package at.fraubock.spendenverwaltung.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IImportDAO;

/**
 * @author manuel-bichler
 * 
 */
public class DBImportDAOTest extends AbstractImportDAOTest {

	private static ApplicationContext context;

	/**
	 * obtain DAO bean, initializing ApplicationContext
	 */
	@BeforeClass
	public static void setup() {
		context = new ClassPathXmlApplicationContext("testspring.xml");
		setImportDao(context.getBean("importDao", IImportDAO.class));
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

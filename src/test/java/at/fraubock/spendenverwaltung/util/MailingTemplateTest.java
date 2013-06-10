package at.fraubock.spendenverwaltung.util;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class MailingTemplateTest extends AbstractMailingTemplateTest {

	private static ApplicationContext context;
	
	@BeforeClass
	public static void setup() throws PersistenceException {
		/* Nur für Performance Test mit vielen echten Daten
		context = new ClassPathXmlApplicationContext("testspring.xml");
		setPersonDAO(context.getBean("personDao", IPersonDAO.class));*/
		init();
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

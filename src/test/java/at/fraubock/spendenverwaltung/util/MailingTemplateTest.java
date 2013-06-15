package at.fraubock.spendenverwaltung.util;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class MailingTemplateTest extends AbstractMailingTemplateUtilTest {
	
	@BeforeClass
	public static void setup() throws PersistenceException {
		init();
	}
	
	/**
	 * manually close ApplicationContext and associated resources
	 */
	@AfterClass 
	public static void shutdown() {
	}
}

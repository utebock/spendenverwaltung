package at.fraubock.spendenverwaltung.service;

import static org.mockito.Mockito.mock;

import org.junit.BeforeClass;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

/**
 * 
 * @author Chris Steele
 *
 */
public class DBMailingServiceTest extends AbstractMailingServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception, PersistenceException {
		IMailingDAO mailingDAO = mock(IMailingDAO.class);
		MailingServiceImplemented mailingService = new MailingServiceImplemented();
		mailingService.setMailingDAO(mailingDAO);
		AbstractMailingServiceTest.setMailingDAO(mailingDAO);
		AbstractMailingServiceTest.setMailingService(mailingService);
	}
}

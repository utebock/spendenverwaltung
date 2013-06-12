package at.fraubock.spendenverwaltung.service;

import org.junit.Before;

/**
 * 
 * @author Chris Steele
 * 
 */
public class MailingServiceImplTest extends AbstractMailingServiceTest {

	@Before
	public void initService() {
		MailingServiceImplemented mailingService = new MailingServiceImplemented();
		mailingService.setMailingDAO(mailingDAO);
		super.mailingService = mailingService;
	}
}

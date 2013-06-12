package at.fraubock.spendenverwaltung.service;

import org.junit.Before;

import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

public class DonationServiceImplTest extends AbstractDonationServiceTest {

	@Before
	public void setUpBeforeClass() throws Exception, PersistenceException {
		DonationServiceImplemented service = new DonationServiceImplemented();
		service.setDonationDAO(donationDAO);
		donationService = service;
	}
}

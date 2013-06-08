package at.fraubock.spendenverwaltung.service;

import static org.mockito.Mockito.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.DonationServiceImplemented;


public class DonationServiceTest extends AbstractDonationServiceTest{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception, PersistenceException {
		init();
		donationDAO = mock(IDonationDAO.class);
		DonationServiceImplemented service = new DonationServiceImplemented();
		service.setDonationDAO(donationDAO);
		setDonationService(service);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}
}

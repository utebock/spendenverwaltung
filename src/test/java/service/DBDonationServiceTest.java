package service;

import static org.mockito.Mockito.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import dao.IDonationDAO;
import dao.IPersonDAO;
import exceptions.PersistenceException;

public class DBDonationServiceTest extends AbstractDonationServiceTest{
	
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

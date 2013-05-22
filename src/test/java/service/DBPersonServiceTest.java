package service;

import static org.mockito.Mockito.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import dao.IPersonDAO;

public abstract class DBPersonServiceTest extends AbstractPersonServiceTest{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		init();
		personDAO = mock(IPersonDAO.class);
		AddressServiceImplemented service = new AddressServiceImplemented();
		service.setAddressDAO(addressDAO);
		setAddressService(service);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}
}

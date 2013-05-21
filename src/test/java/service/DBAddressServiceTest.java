package service;

import static org.mockito.Mockito.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import dao.IAddressDAO;

public class DBAddressServiceTest extends AbstractAddressServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		init();
		addressDAO = mock(IAddressDAO.class);
		AddressServiceImplemented service = new AddressServiceImplemented();
		service.setAddressDAO(addressDAO);
		setAddressService(service);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}
}

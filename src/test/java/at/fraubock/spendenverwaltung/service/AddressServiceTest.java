package at.fraubock.spendenverwaltung.service;

import static org.mockito.Mockito.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.service.AddressServiceImplemented;


public class AddressServiceTest extends AbstractAddressServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		addressDAO = mock(IAddressDAO.class);
		AddressServiceImplemented service = new AddressServiceImplemented();
		service.setAddressDAO(addressDAO);
		setAddressService(service);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}
}

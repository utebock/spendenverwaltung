package at.fraubock.spendenverwaltung.service;

import org.junit.Before;

public class AddressServiceImplTest extends AbstractAddressServiceTest {

	@Before
	public void initService() {
		addressService = new AddressServiceImplemented();
		((AddressServiceImplemented) addressService).setAddressDAO(addressDAO);
	}

}

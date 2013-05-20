package service;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import domain.Address;
import exceptions.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("DBAddressServiceTest-context.xml")
@TransactionConfiguration(defaultRollback=true)
public abstract class AbstractAddressServiceTest {

	protected static IAddressService addressService;
	
	public static void setAddressService(IAddressService addressService) {
		AbstractAddressServiceTest.addressService = addressService;
	}

	/*
	 * testing create
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() {
		try {
			addressService.create(null);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() {
		try {
			addressService.create(new Address()); // all values are null
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedAddress() {

		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			Address returnedAddress = addressService.create(address);
			address.setId(returnedAddress.getId()); // for #equals

			Address savedAddress = addressService.getByID(returnedAddress
					.getId());

			// check if address was returned correctly
			assert (returnedAddress.equals(address));

			// check if address was saved correctly
			assert (savedAddress.equals(returnedAddress));

		} catch (ServiceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithNullParameter_ThrowsException() {
		try {
			addressService.update(null);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidStateParameter_ThrowsException() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			address = addressService.create(address);
			address.setCity(null); // not allowed null value

			addressService.update(address);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedAddress() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			address = addressService.create(address);
			address.setCity("AnotherCity");

			Address returnedAddress = addressService.update(address);
			Address updatedAddress = addressService.getByID(address.getId());

			// check if returned address is correct
			assert (returnedAddress.getId() == address.getId());
			assert (returnedAddress.getStreet().equals(address.getStreet()));
			assert (returnedAddress.getPostalCode() == address.getPostalCode());
			assert (returnedAddress.getCountry().equals(address.getCountry()));
			assert (!returnedAddress.getCity().equals(address.getCity())); // different
																			// city

			// check if address was updated correctly
			assert (updatedAddress.getId() == address.getId());
			assert (updatedAddress.getStreet().equals(address.getStreet()));
			assert (updatedAddress.getPostalCode() == address.getPostalCode());
			assert (updatedAddress.getCountry().equals(address.getCountry()));
			assert (!updatedAddress.getCity().equals(address.getCity())); // different
																			// city

			assert (returnedAddress.getCity() == updatedAddress.getCity());

		} catch (ServiceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException() {
		try {
			addressService.delete(null);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");
		try {
			address = addressService.create(address);
			Address createdAddress = addressService.getByID(address.getId());
			assert (createdAddress != null);

			addressService.delete(address);
			Address deletedAddress = addressService.getByID(address.getId());
			assert (deletedAddress == null);

		} catch (ServiceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly=true)
	public void getAll_ReturnsAllEntities() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		Address address2 = new Address();
		address2.setStreet("Teststreet2 1/1");
		address2.setPostalCode(00001);
		address2.setCity("Testcity2");
		address2.setCountry("Testcountry2");
		try {
			addressService.create(address);
			addressService.create(address2);

			List<Address> addressList = addressService.getAll();
			assert (addressList != null && addressList.size() == 2);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly=true)
	public void getWithInvalidId_ReturnsNull() {
		try {
			Address address = addressService.getByID(100);
			assert (address == null);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly=true)
	public void getWithValidId_ReturnsEntity() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			Address createdAddress = addressService.create(address);
			Address foundAddress = addressService.getByID(createdAddress
					.getId());

			assert (foundAddress != null && foundAddress.getId() == createdAddress
					.getId());
		} catch (ServiceException e) {
			fail();
		}
	}

}

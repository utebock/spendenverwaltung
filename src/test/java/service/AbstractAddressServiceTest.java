package service;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import domain.Address;
import exceptions.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractAddressServiceTest {

	protected static IAddressService addressService;
	
	protected static Address testAddress;
	protected static Address testAddress2;
	protected static Address nullAddress;
	protected static Address testAddressCreated;

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
			addressService.create(nullAddress); // all values are null
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedAddress() {
		
		try {
			Address returnedAddress = addressService.create(testAddress);
			testAddress.setId(returnedAddress.getId()); // for #equals

			Address savedAddress = addressService.getByID(returnedAddress
					.getId());

			// check if address was returned correctly
			assert (returnedAddress.equals(testAddress));

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
		try {
			addressService.update(nullAddress);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedAddress() {
		try {
			Address returnedAddress = addressService.update(testAddress);
			
			assert (returnedAddress.equals(testAddressCreated));
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
		try {
			addressService.delete(testAddress);
			//TODO verify verwenden
		} catch (ServiceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAll_ReturnsAllEntities() {
		try {
			List<Address> addressList = addressService.getAll();
			
			assert (addressList != null && addressList.size() == 2);
			assert(addressList.get(0).equals(testAddress) && addressList.get(1).equals(testAddress2));
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional(readOnly = true)
	public void getWithInvalidId_ThrowsException() {
		try {
			addressService.getByID(100);
		} catch (ServiceException e) {
			fail();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() {
		try {
			addressService.getByID(-1);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() {

		try {
			Address foundAddress = addressService.getByID(testAddressCreated
					.getId());

			assert (foundAddress != null && foundAddress.getId() == testAddressCreated
					.getId());
		} catch (ServiceException e) {
			fail();
		}
	}

}

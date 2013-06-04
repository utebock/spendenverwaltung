package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractAddressServiceTest {

	protected static IAddressService addressService;

	protected static IAddressDAO addressDAO;
	protected static Address newAddress;
	protected static Address newAddress2;
	protected static Address nullAddress;
	protected static Address newAddressCreated;

	public static void setAddressService(IAddressService addressService) {
		AbstractAddressServiceTest.addressService = addressService;
	}

	/*
	 * testing create
	 */

	@Test(expected = ServiceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(addressDAO)
						.insertOrUpdate(null);
		} catch (PersistenceException e) {
			fail();
		}
		addressService.create(null);
	}

	@Test(expected = ServiceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(addressDAO)
			.insertOrUpdate(nullAddress);
		} catch (PersistenceException e) {
			fail();
		}
		addressService.create(nullAddress);
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedAddress() {
		try {
			//change id so that invocation count doesnt fail this test
			//(both create and update tests call insertOrUpdate on the DAO mock
			//so they should not equal each other)
			newAddress.setId(3);
			Address returnedAddress = addressService.create(newAddress);
			
			assertEquals(returnedAddress, newAddressCreated);
			verify(addressDAO).insertOrUpdate(newAddress);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = ServiceException.class)
	@Transactional
	public void updateWithNullParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(addressDAO)
			.insertOrUpdate(null);

		} catch (PersistenceException e) {
			fail();
		}
		
		addressService.update(null);
	}

	@Test(expected = ServiceException.class)
	@Transactional
	public void updateWithInvalidStateParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(addressDAO)
			.insertOrUpdate(nullAddress);
		} catch (PersistenceException e) {
			fail();
		}
		
		addressService.update(nullAddress);
	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedAddress() {
		try {
			Address returnedAddress = addressService.update(newAddressCreated);
			
			assert (returnedAddress.equals(newAddressCreated));
			verify(addressDAO).insertOrUpdate(newAddressCreated);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = ServiceException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(addressDAO).delete(
					null);
		} catch (PersistenceException e) {
			fail();
		}
		
		addressService.delete(null);
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {
		try {
			addressService.delete(newAddress);
			verify(addressDAO).delete(newAddress);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
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
			List<Address> allAddresses = new ArrayList<Address>();
			allAddresses.add(newAddress);
			allAddresses.add(newAddress2);
			when(addressDAO.getAll()).thenReturn(allAddresses);

			List<Address> addressList = addressService.getAll();
			assert (addressList != null && addressList.size() == 2);
			assert (addressList.get(0).equals(newAddress) && addressList
					.get(1).equals(newAddress2));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() throws ServiceException {
		try {
			when(addressDAO.getByID(-1)).thenThrow(
					new PersistenceException());
		} catch (PersistenceException e) {
			fail();
		}

		addressService.getByID(-1);
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() {
		try {
			when(addressDAO.getByID(newAddressCreated.getId())).thenReturn(
					newAddressCreated);

			Address foundAddress = addressService.getByID(newAddressCreated
					.getId());

			assert (newAddressCreated.equals(foundAddress));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Before
	public void init() {
		newAddress = new Address();
		newAddress.setStreet("Teststreet 1/1");
		newAddress.setPostalCode("00000");
		newAddress.setCity("Testcity");
		newAddress.setCountry("Testcountry");

		newAddressCreated = newAddress;
		newAddressCreated.setId(1);

		newAddress2 = new Address();
		newAddress2.setStreet("Teststreet2 1/1");
		newAddress2.setPostalCode("00001");
		newAddress2.setCity("Testcity2");
		newAddress2.setCountry("Testcountry2");

		nullAddress = new Address();
	}

}

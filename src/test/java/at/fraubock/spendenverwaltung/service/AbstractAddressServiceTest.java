package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
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
			when(addressDAO.insertOrUpdate(null)).thenThrow(
					new IllegalArgumentException());
			
			addressService.create(null);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() {
		try {
			when(addressDAO.create(nullAddress)).thenThrow(
					new IllegalArgumentException());

			addressService.create(nullAddress); // all values are null
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedAddress() {
		try {
			when(addressDAO.create(testAddress)).thenReturn(testAddressCreated);

			Address returnedAddress = addressService.create(testAddress);
			assert (returnedAddress.equals(testAddressCreated));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
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
			when(addressDAO.update(null)).thenThrow(
					new IllegalArgumentException());
			
			addressService.update(null);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidStateParameter_ThrowsException() {
		try {
			when(addressDAO.update(nullAddress)).thenThrow(
					new IllegalArgumentException());

			addressService.update(nullAddress);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedAddress() {
		try {
			when(addressDAO.update(testAddressCreated)).thenReturn(
					testAddressCreated);

			Address returnedAddress = addressService.update(testAddressCreated);
			assert (returnedAddress.equals(testAddressCreated));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
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
			doThrow(new IllegalArgumentException()).when(addressDAO).delete(null);
			
			addressService.delete(null);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {
		try {
			addressService.delete(testAddress);
			verify(addressDAO).delete(testAddress);
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
			allAddresses.add(testAddress);
			allAddresses.add(testAddress2);
			when(addressDAO.getAll()).thenReturn(allAddresses);

			List<Address> addressList = addressService.getAll();
			assert (addressList != null && addressList.size() == 2);
			assert (addressList.get(0).equals(testAddress) && addressList
					.get(1).equals(testAddress2));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = EmptyResultDataAccessException.class)
	@Transactional(readOnly = true)
	public void getWithInvalidId_ThrowsException() {
		try {
			when(addressDAO.getByID(100)).thenThrow(
					new EmptyResultDataAccessException(0));

			addressService.getByID(100);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() {
		try {
			when(addressDAO.getByID(-1)).thenThrow(
					new IllegalArgumentException());

			addressService.getByID(-1);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() {
		try {
			when(addressDAO.getByID(testAddressCreated.getId())).thenReturn(
					testAddressCreated);

			Address foundAddress = addressService.getByID(testAddressCreated
					.getId());

			assert (testAddressCreated.equals(foundAddress));
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	protected static void init() {
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode("00000");
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");
		
		testAddressCreated = testAddress;
		testAddressCreated.setId(1);

		testAddress2 = new Address();
		testAddress2.setStreet("Teststreet2 1/1");
		testAddress2.setPostalCode("00001");
		testAddress2.setCity("Testcity2");
		testAddress2.setCountry("Testcountry2");

		nullAddress = new Address();
	}

}

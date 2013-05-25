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

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() {
		try {
			doThrow(new IllegalArgumentException()).when(addressDAO)
					.insertOrUpdate(null);

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
			doThrow(new IllegalArgumentException()).when(addressDAO)
			.insertOrUpdate(nullAddress);

			addressService.create(nullAddress);
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
			Address returnedAddress = addressService.create(newAddress);
			
			assert (returnedAddress.equals(newAddressCreated));
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

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithNullParameter_ThrowsException() {
		try {
			doThrow(new IllegalArgumentException()).when(addressDAO)
			.insertOrUpdate(null);

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
			doThrow(new IllegalArgumentException()).when(addressDAO)
			.insertOrUpdate(nullAddress);

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

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException() {
		try {
			doThrow(new IllegalArgumentException()).when(addressDAO).delete(
					null);

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

	protected static void init() {
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

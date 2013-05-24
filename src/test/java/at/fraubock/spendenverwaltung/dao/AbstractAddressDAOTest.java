package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractAddressDAOTest {

	protected static IAddressDAO addressDAO;

	public static void setAddressDAO(IAddressDAO addressDAO) {
		AbstractAddressDAOTest.addressDAO = addressDAO;
	}

	/*
	 * testing create
	 */

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() {
		try {
			addressDAO.insertOrUpdate(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() {
		try {
			addressDAO.insertOrUpdate(new Address()); // all values are null
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedAddress() {

		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			addressDAO.insertOrUpdate(address);

			Address savedAddress = addressDAO.getByID(address.getId());

			// check if address was saved correctly
			assert (savedAddress.equals(address));

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
			addressDAO.insertOrUpdate(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void updateWithInvalidStateParameter_ThrowsException() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			addressDAO.insertOrUpdate(address);
			address.setCity(null); // not allowed null value

			addressDAO.insertOrUpdate(address);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedAddress() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			addressDAO.insertOrUpdate(address);
			address.setCity("AnotherCity");

			addressDAO.insertOrUpdate(address);
			Address updatedAddress = addressDAO.getByID(address.getId());

			// check if address was updated correctly
			assert (updatedAddress.getId() == address.getId());
			assert (updatedAddress.getStreet().equals(address.getStreet()));
			assert (updatedAddress.getPostalCode() == address.getPostalCode());
			assert (updatedAddress.getCountry().equals(address.getCountry()));
			assert (!updatedAddress.getCity().equals(address.getCity())); // different
																			// city

			assert (address.getCity() == updatedAddress.getCity());

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
			addressDAO.delete(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			addressDAO.insertOrUpdate(address);
			addressDAO.delete(address);
			List<Address> allAddresses = addressDAO.getAll();
			assert (!allAddresses.contains(address));

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
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		Address address2 = new Address();
		address2.setStreet("Teststreet2 1/1");
		address2.setPostalCode("00001");
		address2.setCity("Testcity2");
		address2.setCountry("Testcountry2");
		try {
			addressDAO.insertOrUpdate(address);
			addressDAO.insertOrUpdate(address2);

			List<Address> addressList = addressDAO.getAll();
			assert (addressList != null && addressList.size() == 2);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithInvalidId_ReturnsNull() throws PersistenceException {
		assertNull(addressDAO.getByID(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() {
		try {
			addressDAO.getByID(-1);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			addressDAO.insertOrUpdate(address);
			Address foundAddress = addressDAO.getByID(address.getId());

			assert (foundAddress != null && foundAddress.getId() == address
					.getId());
		} catch (PersistenceException e) {
			fail();
		}
	}

}

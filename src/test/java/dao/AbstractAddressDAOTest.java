package dao;

import static org.junit.Assert.fail;

import org.junit.Test;

import domain.Address;
import exceptions.PersistenceException;

public abstract class AbstractAddressDAOTest {

	protected IAddressDAO addressDAO;

	/*
	 * testing create
	 */

	@Test(expected = IllegalArgumentException.class)
	public void createWithNullParameter_ThrowsException() {
		try {
			addressDAO.create(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void createWithInvalidStateParameter_ThrowsException() {
		try {
			addressDAO.create(new Address()); // all values are null
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	public void createWithValidParameter_ReturnsSavedAddress() {

		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			Address returnedAddress = addressDAO.create(address);
			Address savedAddress = addressDAO.getByID(returnedAddress.getId());

			// TODO change to #equals when implemented
			// check if returned address is correct
			assert (returnedAddress.getStreet().equals(address.getStreet()));
			assert (returnedAddress.getPostalCode() == address.getPostalCode());
			assert (returnedAddress.getCity() == address.getCity());
			assert (returnedAddress.getCountry() == address.getCountry());

			// check address was saved correctly
			assert (savedAddress.getId() == returnedAddress.getId());
			assert (savedAddress.getStreet().equals(address.getStreet()));
			assert (savedAddress.getPostalCode() == address.getPostalCode());
			assert (savedAddress.getCity() == address.getCity());
			assert (savedAddress.getCountry() == address.getCountry());

		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing update
	 */

	@Test(expected = IllegalArgumentException.class)
	public void updateWithNullParameter_ThrowsException() {
		try {
			addressDAO.update(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateWithInvalidStateParameter_ThrowsException() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			address = addressDAO.create(address);
			address.setCity(null); // not allowed null value

			addressDAO.update(address);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	public void updateWithValidParameters_ReturnsUpdatedAddress() {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode(00000);
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		try {
			address = addressDAO.create(address);
			address.setCity("AnotherCity");

			Address returnedAddress = addressDAO.update(address);
			Address updatedAddress = addressDAO.getByID(address.getId());

			// check if returned address is correct
			assert (returnedAddress.getId() == address.getId());
			assert (returnedAddress.getStreet().equals(address.getStreet()));
			assert (returnedAddress.getPostalCode() == address.getPostalCode());
			assert (returnedAddress.getCountry() == address.getCountry());
			assert (returnedAddress.getCity() != address.getCity()); // different city

			// check if address was updated correctly
			assert (updatedAddress.getId() == address.getId());
			assert (updatedAddress.getStreet().equals(address.getStreet()));
			assert (updatedAddress.getPostalCode() == address.getPostalCode());
			assert (updatedAddress.getCountry() == address.getCountry());
			assert (updatedAddress.getCity() != address.getCity()); // different city

			assert (returnedAddress.getCity() == updatedAddress.getCity());

		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing delete
	 */

	// TODO

	/*
	 * testing find
	 */

	// TODO

}

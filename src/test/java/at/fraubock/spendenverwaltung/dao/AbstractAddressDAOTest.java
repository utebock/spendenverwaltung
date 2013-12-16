/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException()
			throws PersistenceException {
		addressDAO.insertOrUpdate(null);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException()
			throws PersistenceException {
		addressDAO.insertOrUpdate(new Address()); // all values are null
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedAddress() throws Exception {

		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);

		Address savedAddress = addressDAO.getByID(address.getId());

		// check if address was saved correctly
		assertTrue(savedAddress.equals(address));

	}

	/*
	 * testing update
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void updateWithNullParameter_ThrowsException()
			throws PersistenceException {
		addressDAO.insertOrUpdate(null);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void updateWithInvalidStateParameter_ThrowsException()
			throws PersistenceException {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);

		address.setCity(null); // not allowed null value

		addressDAO.insertOrUpdate(address);

	}

	@Test
	@Transactional
	public void updateWithValidParameters_ReturnsUpdatedAddress()
			throws Exception {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		address.setCity("AnotherCity");

		addressDAO.insertOrUpdate(address);
		Address updatedAddress = addressDAO.getByID(address.getId());

		// check if address was updated correctly
		assertTrue(updatedAddress.getId().equals(address.getId()));
		assertTrue(updatedAddress.getStreet().equals(address.getStreet()));
		assertTrue(updatedAddress.getPostalCode().equals(
				address.getPostalCode()));
		assertTrue(updatedAddress.getCountry().equals(address.getCountry()));
		assertTrue(updatedAddress.getCity().equals(address.getCity()));

		assertTrue(address.getCity().equals(updatedAddress.getCity()));

	}

	/*
	 * testing delete
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException()
			throws PersistenceException {
		addressDAO.delete(null);
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() throws Exception {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		addressDAO.delete(address);
		List<Address> allAddresses = addressDAO.getAll();
		assertTrue(!allAddresses.contains(address));

	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAll_ReturnsAllEntities() throws Exception {
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

		addressDAO.insertOrUpdate(address);
		addressDAO.insertOrUpdate(address2);

		List<Address> addressList = addressDAO.getAll();
		assertTrue(addressList.size() == 2);

	}

	@Test
	@Transactional(readOnly = true)
	public void getWithInvalidId_ReturnsNull() throws PersistenceException {
		assertNull(addressDAO.getByID(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() throws Exception {

		addressDAO.getByID(-1);

	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() throws Exception {
		Address address = new Address();
		address.setStreet("Teststreet 1/1");
		address.setPostalCode("00000");
		address.setCity("Testcity");
		address.setCountry("Testcountry");

		addressDAO.insertOrUpdate(address);
		Address foundAddress = addressDAO.getByID(address.getId());

		assertTrue(foundAddress != null
				&& foundAddress.getId().equals(address.getId()));

	}

}

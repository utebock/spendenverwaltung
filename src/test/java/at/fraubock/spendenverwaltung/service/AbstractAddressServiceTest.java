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
package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;

public abstract class AbstractAddressServiceTest {

	protected IAddressService addressService;

	protected final IAddressDAO addressDAO = mock(IAddressDAO.class);;

	private Address newAddress;
	private Address newAddress2;
	private Address nullAddress;
	private Address newAddressCreated;

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

	/*
	 * testing create
	 */

	@Test(expected = ServiceException.class)
	public void createWithNullParameter_ThrowsException() throws Exception {

		doThrow(new PersistenceException()).when(addressDAO).insertOrUpdate(
				null);

		addressService.create(null);
	}

	@Test(expected = ServiceException.class)
	public void createWithInvalidStateParameter_ThrowsException()
			throws Exception {

		doThrow(new PersistenceException()).when(addressDAO).insertOrUpdate(
				nullAddress);

		addressService.create(nullAddress);
	}

	@Test
	public void createWithValidParameter_ReturnsSavedAddress() throws Exception {

		// change id so that invocation count doesnt fail this test
		// (both create and update tests call insertOrUpdate on the DAO mock
		// so they should not equal each other)
		newAddress.setId(3);
		Address returnedAddress = addressService.create(newAddress);

		assertEquals(returnedAddress, newAddressCreated);
		verify(addressDAO).insertOrUpdate(newAddress);

	}

	/*
	 * testing update
	 */

	@Test(expected = ServiceException.class)
	public void updateWithNullParameter_ThrowsException() throws Exception {

		doThrow(new PersistenceException()).when(addressDAO).insertOrUpdate(
				null);

		addressService.update(null);
	}

	@Test(expected = ServiceException.class)
	public void updateWithInvalidStateParameter_ThrowsException()
			throws Exception {

		doThrow(new PersistenceException()).when(addressDAO).insertOrUpdate(
				nullAddress);

		addressService.update(nullAddress);
	}

	@Test
	public void updateWithValidParameters_ReturnsUpdatedAddress()
			throws Exception {

		Address returnedAddress = addressService.update(newAddressCreated);

		assertEquals(newAddressCreated, returnedAddress);
		verify(addressDAO).insertOrUpdate(newAddressCreated);

	}

	/*
	 * testing delete
	 */

	@Test(expected = ServiceException.class)
	public void deleteWithNullParameter_ThrowsException() throws Exception {

		doThrow(new PersistenceException()).when(addressDAO).delete(null);

		addressService.delete(null);
	}

	@Test
	public void deleteWithValidParameter_RemovesEntity() throws Exception {

		addressService.delete(newAddress);
		verify(addressDAO).delete(newAddress);

	}

	/*
	 * testing find
	 */

	@Test
	public void getAll_ReturnsAllEntities() throws Exception {

		List<Address> allAddresses = new ArrayList<Address>();
		allAddresses.add(newAddress);
		allAddresses.add(newAddress2);
		when(addressDAO.getAll()).thenReturn(allAddresses);

		List<Address> addressList = addressService.getAll();
		assertNotNull(addressList);
		assertEquals(2, addressList.size());
		assertEquals(newAddress, addressList.get(0));
		assertEquals(newAddress2, addressList.get(1));

	}

	@Test(expected = ServiceException.class)
	public void getWithNegativeId_ThrowsException() throws Exception {

		when(addressDAO.getByID(-1)).thenThrow(new PersistenceException());

		addressService.getByID(-1);
	}

	@Test
	public void getWithValidId_ReturnsEntity() throws Exception {

		when(addressDAO.getByID(newAddressCreated.getId())).thenReturn(
				newAddressCreated);

		Address foundAddress = addressService
				.getByID(newAddressCreated.getId());

		assertEquals(foundAddress, newAddressCreated);

	}

}

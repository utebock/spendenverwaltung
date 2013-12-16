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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public abstract class AbstractPersonServiceTest {

	protected IPersonService personService;

	protected final IPersonDAO personDAO = mock(IPersonDAO.class);
	private Person person;
	private Person person2;
	private Person nullPerson;
	private Person personCreated;

	private Address testAddress;
	private Address testAddress2;
	private Address testAddressCreated;

	@Before
	public void init() throws PersistenceException {
		testAddress = new Address();
		testAddress.setStreet("Teststreet 1/1");
		testAddress.setPostalCode("00000");
		testAddress.setCity("Testcity");
		testAddress.setCountry("Testcountry");

		// testAddress = addressDAO.create(testAddress);
		List<Address> listTest = new ArrayList<Address>();
		listTest.add(testAddress);

		testAddressCreated = testAddress;
		testAddressCreated.setId(1);

		testAddress2 = new Address();
		testAddress2.setStreet("Teststreet2 1/1");
		testAddress2.setPostalCode("00001");
		testAddress2.setCity("Testcity2");
		testAddress2.setCountry("Testcountry2");

		// testAddress2 = addressDAO.create(testAddress2);
		List<Address> listTest2 = new ArrayList<Address>();
		listTest2.add(testAddress2);

		person = new Person();
		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Test");
		person.setSurname("Test");
		person.setEmail("test@test.at");
		person.setTelephone("01234567889");
		person.setNote("");
		// person.setAddresses(listTest);
		// person.setMailingAddress(testAddress);

		person2 = new Person();
		person2.setSex(Person.Sex.MALE);
		person2.setCompany("IBM");
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Test2");
		person2.setSurname("Test2");
		person2.setEmail("test2@test2.at");
		person2.setTelephone("02234567889");
		person2.setNote("");
		// person2.setAddresses(listTest2);
		// person2.setMailingAddress(testAddress2);

		nullPerson = null;

		personCreated = person;
		personCreated.setId(1);

	}

	@Test(expected = ServiceException.class)
	public void createWithNullParameter_ThrowsException() throws Exception {

		doThrow(new PersistenceException()).when(personDAO)
				.insertOrUpdate(null);
		personService.create(null);

	}

	@Test(expected = ServiceException.class)
	public void createWithInvalidStateParameter_ThrowsException()
			throws Exception {

		doThrow(new PersistenceException()).when(personDAO).insertOrUpdate(
				nullPerson);
		personService.create(nullPerson); // all values are null

	}

	@Test
	public void createWithValidParameter_ReturnsSavedPerson() throws Exception {

		person.setId(10);
		Person returned = personService.create(person);

		assertEquals(returned, personCreated);

		verify(personDAO).insertOrUpdate(person);

	}

	/*
	 * testing update
	 */

	@Test(expected = ServiceException.class)
	public void updateWithNullParameter_ThrowsException() throws Exception {

		doThrow(new PersistenceException()).when(personDAO)
				.insertOrUpdate(null);
		personService.update(null);

	}

	@Test(expected = ServiceException.class)
	public void updateWithInvalidStateParameter_ThrowsException()
			throws Exception {

		doThrow(new PersistenceException()).when(personDAO).insertOrUpdate(
				nullPerson);
		personService.update(nullPerson);

	}

	@Test
	public void updateWithValidParameters_ReturnsUpdatedPerson()
			throws Exception {

		Person returned = personService.update(personCreated);

		assertEquals(personCreated, returned);
		verify(personDAO).insertOrUpdate(personCreated);

	}

	/*
	 * testing delete
	 */

	@Test(expected = ServiceException.class)
	public void deleteWithNullParameter_ThrowsException() throws Exception {

		doThrow(new PersistenceException()).when(personDAO).delete(null);

		personService.delete(null);

	}

	@Test
	public void deleteWithValidParameter_RemovesEntity() throws Exception {

		personService.delete(person);
		verify(personDAO).delete(person);

	}

	/*
	 * testing find
	 */

	@Test
	public void getAll_ReturnsAllEntities() throws Exception {

		List<Person> all = new ArrayList<Person>();
		all.add(person);
		all.add(person2);
		when(personDAO.getAll()).thenReturn(all);

		List<Person> list = personService.getAll();
		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals(person, list.get(0));
		assertEquals(person2, list.get(1));

	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void getWithInvalidId_ThrowsException() throws Exception {

		when(personDAO.getById(10000)).thenThrow(
				new EmptyResultDataAccessException(0));

		personService.getById(10000);

	}

	@Test(expected = ServiceException.class)
	public void getWithNegativeId_ThrowsException() throws Exception {

		when(personDAO.getById(-1)).thenThrow(new PersistenceException());

		personService.getById(-1);

	}

	@Test
	public void getWithValidId_ReturnsEntity() throws Exception {

		when(personDAO.getById(personCreated.getId()))
				.thenReturn(personCreated);

		Person found = personService.getById(personCreated.getId());

		assertThat(personCreated, is(found));

	}

	@Test(expected = IllegalArgumentException.class)
	public void createCSVWithNullArgument_ThrowsException() {
		personService.convertToCSV(null);
	}

	@Test
	public void createCSVWithValidArgument_ReturnsCSVString() {
		List<Person> list = new ArrayList<Person>();
		list.add(person);
		list.add(person2);
		list.add(personCreated);
		String csv = personService.convertToCSV(list);
		assertTrue(csv.equals(csvExpected));
	}

	@Test
	public void createCSVWithEmptyList_ReturnsCSVString() {
		List<Person> list = new ArrayList<Person>();
		String csv = personService.convertToCSV(list);
		assertTrue(csv
				.equals("Vorname;Nachname;E-Mail;Geschlecht;Titel;Unternehmen;Telephon;"
						+ "Empf\u00E4ngt E-Mail;Empf\u00E4ngt Post;Notiz;Land;Stadt;PLZ;Strasse\n"));
	}

	private String csvExpected = "Vorname;Nachname;E-Mail;Geschlecht;Titel;Unternehmen;Telephon;Empf\u00E4ngt E-Mail;Empf\u00E4ngt Post;Notiz;Land;Stadt;PLZ;Strasse\n"
			+ "Test;Test;test@test.at;m\u00E4nnlich;Prof. Dr.;IBM;01234567889;ja;ja;;n.v.;n.v.;n.v.;n.v.;\n"
			+ "Test2;Test2;test2@test2.at;m\u00E4nnlich;Prof. Dr.;IBM;02234567889;ja;ja;;n.v.;n.v.;n.v.;n.v.;\n"
			+ "Test;Test;test@test.at;m\u00E4nnlich;Prof. Dr.;IBM;01234567889;ja;ja;;n.v.;n.v.;n.v.;n.v.;\n";

}

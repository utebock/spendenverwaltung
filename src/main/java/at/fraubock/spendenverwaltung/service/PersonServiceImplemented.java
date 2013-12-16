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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class PersonServiceImplemented implements IPersonService {
	private static final Logger log = Logger
			.getLogger(PersonServiceImplemented.class);

	private IPersonDAO personDAO;
	private IAddressDAO addressDAO;

	public IPersonDAO getPersonDAO() {

		return personDAO;
	}

	public void setPersonDAO(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public IAddressDAO getAddressDAO() {

		return addressDAO;
	}

	public void setAddressDAO(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	@Override
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Throwable.class)
	public Person create(Person p) throws ServiceException {
		try {
			personDAO.insertOrUpdate(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return p;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Person update(Person p) throws ServiceException {
		try {
			personDAO.insertOrUpdate(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return p;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void delete(Person p) throws ServiceException {
		try {
			personDAO.delete(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void deleteAddressAndUpdatePerson(Address a, Person p)
			throws ServiceException {
		update(p);
		try {
			addressDAO.delete(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Person> getAll() throws ServiceException {
		List<Person> list = null;
		try {
			list = personDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Person getById(int id) throws ServiceException {
		Person person = null;
		try {
			person = personDAO.getById(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return person;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Person> getByAttributes(Person p) throws ServiceException {
		List<Person> persons = new ArrayList<Person>();
		try {
			persons = personDAO.getByAttributes(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return persons;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Person> getByFilter(Filter filter) throws ServiceException {
		List<Person> list = null;
		try {
			list = personDAO.getByFilter(filter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return list;
	}

	@Override
	public String convertToCSV(List<Person> persons) {
		if (persons == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}

		String csv = "Vorname;Nachname;E-Mail;Geschlecht;Titel;Unternehmen;Telephon;Empf\u00E4ngt E-Mail;Empf\u00E4ngt Post;Notiz;Land;Stadt;PLZ;Strasse\n";

		for (Person p : persons) {
			csv += nullSafeToString(p.getGivenName()) + ";";
			csv += nullSafeToString(p.getSurname()) + ";";
			csv += nullSafeToString(p.getEmail()) + ";";
			csv += Person.Sex.getDisplayableName(p.getSex()) + ";";
			csv += nullSafeToString(p.getTitle()) + ";";
			csv += nullSafeToString(p.getCompany()) + ";";
			csv += nullSafeToString(p.getTelephone()) + ";";
			csv += (p.isEmailNotification() ? "ja" : "nein") + ";";
			csv += (p.isPostalNotification() ? "ja" : "nein") + ";";
			csv += nullSafeToString(p.getNote()) + ";";
			Address a = p.getMainAddress();
			String nA = "n.v.;";
			csv += a == null ? nA : (nullSafeToString(a.getCountry()) + ";");
			csv += a == null ? nA : (nullSafeToString(a.getCity()) + ";");
			csv += a == null ? nA : (nullSafeToString(a.getPostalCode()) + ";");
			csv += (a == null ? nA : (nullSafeToString(a.getStreet()) + ";")) + "\n";
		}
		return csv;
	}
	
	private String nullSafeToString(Object obj) {
		return obj==null?"n.v.":obj.toString();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Person> getConfirmed() throws ServiceException {
		List<Person> list = null;
		try {
			list = personDAO.getConfirmed();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		return list;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Person> getPersonsByMailing(Mailing mailing)
			throws ServiceException {
		try {
			return personDAO.getPersonsByMailing(mailing);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveAsCSV(List<Person> persons, File csvFile)
			throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(csvFile);
			writer.write(convertToCSV(persons));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.warn(
					"CSV data could not be written to "
							+ csvFile.getAbsolutePath(), e);
			throw e;
		} finally {
			if (writer != null)
				writer.close();
		}
	}

}

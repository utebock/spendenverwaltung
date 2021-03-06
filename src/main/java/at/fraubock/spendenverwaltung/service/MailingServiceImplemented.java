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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.MailingTemplateUtil;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

public class MailingServiceImplemented implements IMailingService {
	private static final Logger log = Logger
			.getLogger(MailingServiceImplemented.class);

	private IMailingDAO mailingDAO;
	private IPersonDAO personDAO;
	private IPersonService personService;

	/**
	 * @return the personService
	 */
	public IPersonService getPersonService() {
		return personService;
	}

	/**
	 * @param personService
	 *            the personService to set
	 */
	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public void setMailingDAO(IMailingDAO mailingDAO) {
		this.mailingDAO = mailingDAO;
	}

	public void setPersonDAO(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Mailing insertOrUpdate(Mailing m) throws ServiceException {
		try {
			mailingDAO.insertOrUpdate(m);
			return m;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void delete(Mailing m) throws ServiceException {
		try {
			mailingDAO.delete(m);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getAll() throws ServiceException {
		try {
			return mailingDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getAllConfirmed() throws ServiceException {
		try {
			return mailingDAO.getAllConfirmed();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void confirmMailing(Mailing mailing) throws ServiceException {
		try {
			mailingDAO.confirmMailing(mailing);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getAllUnconfirmed() throws ServiceException {
		try {
			return mailingDAO.getAllUnconfirmed();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * adds all unconfirmed mailings to a list
	 * 
	 * @returns Map of creators and mailings
	 */
	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<UnconfirmedMailing> getUnconfirmedMailingsWithCreator()
			throws ServiceException {
		try {
			return mailingDAO.getUnconfirmedMailingsWithCreator();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Mailing getById(int id) throws ServiceException {
		try {
			return mailingDAO.getById(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getConfirmedMailingsByPerson(Person person)
			throws ServiceException {
		try {
			return mailingDAO.getConfirmedMailingsByPerson(person);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void deletePersonFromMailing(Person person, Mailing mailing)
			throws ServiceException {
		try {
			mailingDAO.deletePersonFromMailing(person, mailing);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String convertToCSV(List<Mailing> mailings) {
		if (mailings == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}

		String csv = "Datum;Art;Medium;Vorlage\n";

		for (Mailing m : mailings) {
			csv += m.getDate() == null ? "n.v." : (new SimpleDateFormat(
					"dd.MM.yyyy").format(m.getDate())) + ";";
			csv += nullSafeToString(m.getType().getName()) + ";";
			csv += (m.getMedium() == Mailing.Medium.POSTAL ? ("Postalisch;")
					: "E-Mail;");
			csv += (m.getTemplate() == null ? "n.v." : nullSafeToString(m
					.getTemplate().getFileName()));
			csv += "\n";
		}
		return csv;
	}

	private String nullSafeToString(Object obj) {
		return obj == null ? "n.v." : obj.toString();
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public File reproduceDocument(Mailing mailing, String savePath)
			throws ServiceException {
		if (mailing == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}

		MailingTemplate mt = mailing.getTemplate();

		if (mt != null) {
			List<Person> personList;

			try {
				personList = personDAO.getPersonsByMailing(mailing);
				return MailingTemplateUtil.createMailingWithDocxTemplate(
						mt.getFile(), personList,
						savePath == null ? mt.getFileName() : savePath);
			} catch (PersistenceException e) {
				throw new ServiceException(e);
			} catch (IOException e) {
				throw new ServiceException(e);
			} catch (ServiceException e) {
				throw new ServiceException(e);
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getByFilter(Filter filter) throws ServiceException {
		try {
			return mailingDAO.getMailingsByFilter(filter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getBeforeDate(Date date) throws ServiceException {
		Filter beforeFilter = new Filter();
		beforeFilter.setType(FilterType.MAILING);

		PropertyCriterion beforeCriterion = new PropertyCriterion();
		beforeCriterion.setType(FilterType.MAILING);
		beforeCriterion.compare(FilterProperty.MAILING_DATE,
				RelationalOperator.LESS_EQ, date);

		beforeFilter.setCriterion(beforeCriterion);

		try {
			return mailingDAO.getMailingsByFilter(beforeFilter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getAfterDate(Date date) throws ServiceException {
		Filter afterFilter = new Filter();
		afterFilter.setType(FilterType.MAILING);

		PropertyCriterion afterCriterion = new PropertyCriterion();
		afterCriterion.setType(FilterType.MAILING);
		afterCriterion.compare(FilterProperty.MAILING_DATE,
				RelationalOperator.GREATER_EQ, date);

		afterFilter.setCriterion(afterCriterion);

		try {
			return mailingDAO.getMailingsByFilter(afterFilter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Mailing> getBetweenDates(Date start, Date end)
			throws ServiceException {
		Filter betweenFilter = new Filter();
		betweenFilter.setType(FilterType.MAILING);

		PropertyCriterion beforeCriterion = new PropertyCriterion();
		beforeCriterion.setType(FilterType.MAILING);
		beforeCriterion.compare(FilterProperty.MAILING_DATE,
				RelationalOperator.LESS_EQ, end);

		PropertyCriterion afterCriterion = new PropertyCriterion();
		afterCriterion.setType(FilterType.MAILING);
		afterCriterion.compare(FilterProperty.MAILING_DATE,
				RelationalOperator.GREATER_EQ, start);

		ConnectedCriterion betweenCriterion = new ConnectedCriterion();
		betweenCriterion.setType(FilterType.MAILING);
		betweenCriterion.connect(beforeCriterion, LogicalOperator.AND,
				afterCriterion);

		betweenFilter.setCriterion(betweenCriterion);

		try {
			return mailingDAO.getMailingsByFilter(betweenFilter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveAsCSV(List<Mailing> mailings, File csvFile)
			throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(csvFile);
			writer.write(convertToCSV(mailings));
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

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public String convertReceiversToCSVById(int mailingId)
			throws ServiceException {
		Mailing m = getById(mailingId);
		if (m == null)
			return null;
		return personService.convertToCSV(personService.getPersonsByMailing(m));
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public boolean saveReceiversAsCSVById(int mailingId, File csvFile)
			throws IOException, ServiceException {
		Mailing m = getById(mailingId);
		if (m == null)
			return false;
		personService.saveAsCSV(personService.getPersonsByMailing(m), csvFile);
		return true;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public int getSize(Mailing mailing) throws ServiceException {
		try {
			return mailingDAO.getSize(mailing);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public File reproduceDocumentById(int id, String savePath)
			throws ServiceException, IOException {
		return reproduceDocument(getById(id), savePath);
	}
}

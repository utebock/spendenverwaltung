package at.fraubock.spendenverwaltung.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.util.MailingTemplateUtil;

public class MailingServiceImplemented implements IMailingService {

	private IMailingDAO mailingDAO;
	private IPersonDAO personDAO;

	public void setMailingDAO(IMailingDAO mailingDAO) {
		this.mailingDAO = mailingDAO;
	}

	public void setPersonDAO(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Mailing insertOrUpdate(Mailing m) throws ServiceException {
		try {
			mailingDAO.insertOrUpdate(m);
			return m;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void delete(Mailing m) throws ServiceException {
		try {
			mailingDAO.delete(m);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Mailing> getAll() throws ServiceException {
		try {
			return mailingDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<Mailing> getAllConfirmed() throws ServiceException {
		try {
			return mailingDAO.getAllConfirmed();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void confirmMailing(Mailing mailing) throws ServiceException {
		try {
			mailingDAO.confirmMailing(mailing);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
	public List<UnconfirmedMailing> getUnconfirmedMailingsWithCreator() throws ServiceException {
		try {
			return mailingDAO.getUnconfirmedMailingsWithCreator();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Mailing getById(int id) throws ServiceException {
		try {
			return mailingDAO.getById(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Mailing> getConfirmedMailingsByPerson(Person person)
			throws ServiceException {
		try {
			return mailingDAO.getConfirmedMailingsByPerson(person);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String convertToCSV(List<Mailing> mailings) {
		if (mailings == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}

		String csv = "Datum;Art;Medium;\n";

		for (Mailing m : mailings) {
			csv += m.getDate() == null ? "n.v." : (new SimpleDateFormat(
					"dd.MM.yyyy").format(m.getDate())) + ";";
			csv += m.getType().getName() + ";";
			csv += (m.getMedium()==Mailing.Medium.POSTAL?"Postalisch":"E-Mail") + ";\n";
		}
		return csv;
	}

	@Override	
	@Transactional(readOnly = true)
	public void reproduceDocument(Mailing mailing) throws ServiceException {
		if (mailing == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		
		MailingTemplate mt = mailing.getTemplate();
		
		if(mt != null)
		{		
			List<Person> personList;
		
			try {
				personList = personDAO.getPersonsByMailing(mailing);
				MailingTemplateUtil.createMailingWithDocxTemplate(mt.getFile(),
						personList, mt.getFileName());
			} catch (PersistenceException e) {
				throw new ServiceException(e);
			} catch (IOException e) {
				throw new ServiceException(e);
			} catch (ServiceException e) {
				throw new ServiceException(e);
			}
		}
	}
}

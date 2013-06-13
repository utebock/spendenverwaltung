package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

/**
 * @author Chris Steele
 * 
 */
public abstract class AbstractMailingServiceTest {

	protected IMailingService mailingService;
	protected final IMailingDAO mailingDAO = mock(IMailingDAO.class);
	protected final IPersonDAO personDAO = mock(IPersonDAO.class);

	private Mailing validMailing;
	private Mailing validMailingTwo;
	private Mailing validMailingThree;

	private Mailing invalidMailing;

	private Filter validFilter;

	@Before
	public void init() {
		validFilter = new Filter();
		validFilter.setType(FilterType.PERSON);

		PropertyCriterion validCriterion = new PropertyCriterion();
		validCriterion.compare(FilterProperty.PERSON_GIVENNAME,
				RelationalOperator.EQUALS, "Person");
		validFilter.setCriterion(validCriterion);

		validMailing = new Mailing();
		validMailing.setMedium(Mailing.Medium.EMAIL);
		validMailing.setType(Mailing.MailingType.DANKESBRIEF);
		validMailing.setFilter(validFilter);

		validMailingTwo = new Mailing();
		validMailingTwo.setMedium(Mailing.Medium.POSTAL);
		validMailingTwo.setType(Mailing.MailingType.INFOMATERIAL);
		validMailingTwo.setFilter(validFilter);

		validMailingThree = new Mailing();
		validMailingThree.setMedium(Mailing.Medium.EMAIL);
		validMailingThree.setType(Mailing.MailingType.ERLAGSCHEINVERSAND);
		validMailingThree.setFilter(validFilter);

		invalidMailing = new Mailing();
	}

	@Test
	public void createValidMailing() {
		try {
			Mailing result = mailingService.insertOrUpdate(validMailing);
			verify(mailingDAO).insertOrUpdate(validMailing);
			assertEquals(result, validMailing);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}

	}

	@Test(expected = ServiceException.class)
	public void createWrapsException_shouldThrowServiceException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(mailingDAO)
					.insertOrUpdate(invalidMailing);
			mailingService.insertOrUpdate(invalidMailing);
			verify(mailingService).insertOrUpdate(invalidMailing);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	public void getAll() {
		List<Mailing> all = new ArrayList<Mailing>();
		all.add(validMailing);
		all.add(validMailingTwo);
		all.add(validMailingThree);

		try {
			when(mailingDAO.getAll()).thenReturn(all);
			List<Mailing> results = mailingService.getAll();
			assertEquals(results, all);
			verify(mailingDAO).getAll();
		} catch (PersistenceException e) {
			fail();
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	public void createNull_shouldThrowException() throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(mailingDAO)
					.insertOrUpdate(null);
			mailingService.insertOrUpdate(null);
			verify(mailingDAO).insertOrUpdate(null);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	public void deleteMailing() {
		try {
			mailingService.delete(validMailing);
			verify(mailingDAO).delete(validMailing);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	public void deleteMailing_shouldThrowServiceException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(mailingDAO).delete(
					invalidMailing);
			mailingService.delete(invalidMailing);
			verify(mailingDAO).delete(invalidMailing);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	public void getById() {
		try {
			when(mailingDAO.getById(1)).thenReturn(validMailingTwo);
			Mailing result = mailingService.getById(1);
			verify(mailingDAO).getById(1);
			assertEquals(result, validMailingTwo);
		} catch (ServiceException e) {
			fail();
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	public void getMailingsById_shouldThrowServiceException()
			throws ServiceException {
		try {
			doThrow(new PersistenceException()).when(mailingDAO).getById(-1);
			mailingService.getById(-1);
			verify(mailingDAO).getById(-1);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	public void getMailingsByPerson() throws PersistenceException {
		try {
			List<Mailing> mailings = new ArrayList<Mailing>();
			mailings.add(validMailingTwo);
			mailings.add(validMailingThree);

			Person mark = new Person();
			mark.setGivenName("Mark");

			when(mailingDAO.getMailingsByPerson(mark)).thenReturn(mailings);
			List<Mailing> results = mailingService.getMailingsByPerson(mark);
			verify(mailingDAO).getMailingsByPerson(mark);
			assertEquals(results, mailings);
		} catch (ServiceException e) {
			fail();
		}
	}

	@Test(expected = ServiceException.class)
	public void getMailingsByPerson_shouldThrowServiceException()
			throws ServiceException {
		try {
			Person invalidPerson = new Person();
			invalidPerson.setGivenName("Invalid");
			doThrow(new PersistenceException()).when(mailingDAO)
					.getMailingsByPerson(invalidPerson);
			mailingService.getMailingsByPerson(invalidPerson);
			verify(mailingDAO).getMailingsByPerson(invalidPerson);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void createCSVWithNullArgument_ThrowsException() {
		mailingService.convertToCSV(null);
	}

	@Test
	public void createCSVWithValidArgument_ReturnsCSVString() {
		List<Mailing> list = new ArrayList<Mailing>();
		validMailing.setDate(new Date());
		validMailingTwo.setDate(new Date());
		validMailingThree.setDate(new Date());
		list.add(validMailing);
		list.add(validMailingTwo);
		list.add(validMailingThree);
		String csv = mailingService.convertToCSV(list);
		assertTrue(csv.equals(csvExpected));
	}

	@Test
	public void createCSVWithEmptyList_ReturnsCSVString() {
		List<Mailing> list = new ArrayList<Mailing>();
		String csv = mailingService.convertToCSV(list);
		assertTrue(csv.equals("Datum;Art;Medium;\n"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void reproduceDocumentWithNullArgument_ThrowsException()
			throws ServiceException {
		mailingService.reproduceDocument(null);
	}

	@Test
	public void reproduceDocumentWithValidArgument_ProducesDocument()
			throws ServiceException {
		try {
			String fs = File.separator;
			MailingTemplate mt = new MailingTemplate();
			File f = new File("src" + fs + "test" + fs + "resources" + fs
					+ "examplemailing2.docx");
			mt.setFile(f);
			mt.setFileName(f.getName());
			validMailing.setTemplate(mt);

			mailingService.reproduceDocument(validMailing);

			verify(personDAO).getPersonsByMailing(validMailing);
			// TODO how to mock a static method?
			// (MailingTemplateUtil#createMailingWithDocxTemplate())
		} catch (PersistenceException e) {
			fail();
		}
	}

	private String csvExpected = "Datum;Art;Medium;\n10.06.2013;DANKESBRIEF;EMAIL;\n10.06.2013;INFOMATERIAL;POSTAL;\n"
			+ "10.06.2013;ERLAGSCHEINVERSAND;EMAIL;\n";

}

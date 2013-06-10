package at.fraubock.spendenverwaltung.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
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
public class AbstractMailingServiceTest {

	private static IMailingService mailingService;
	private static IMailingDAO mailingDAO;

	private static Mailing validMailing;
	private static Mailing validMailingTwo;
	private static Mailing validMailingThree;

	private static Mailing invalidMailing;

	private static Filter validFilter;

	public static void setMailingService(IMailingService mailingService) {
		AbstractMailingServiceTest.mailingService = mailingService;
	}

	public static void setMailingDAO(IMailingDAO mailingDAO) {
		AbstractMailingServiceTest.mailingDAO = mailingDAO;
	}

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
		assertThat(csv.equals(csvExpected), is(true));
	}

	@Test
	public void createCSVWithEmptyList_ReturnsCSVString() {
		List<Mailing> list = new ArrayList<Mailing>();
		String csv = mailingService.convertToCSV(list);
		assertThat(csv.equals("Datum;Art;Medium;\n"), is(true));
	}

	private String csvExpected = "Datum;Art;Medium;\n10.06.2013;DANKESBRIEF;EMAIL;\n10.06.2013;INFOMATERIAL;POSTAL;\n" +
			"10.06.2013;ERLAGSCHEINVERSAND;EMAIL;\n";

}

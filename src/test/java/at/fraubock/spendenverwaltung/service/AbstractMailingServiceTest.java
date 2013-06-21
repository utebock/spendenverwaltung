package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

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
		validMailingThree
				.setType(Mailing.MailingType.EINZELSPENDEN_DANKESBRIEF);
		validMailingThree.setFilter(validFilter);

		invalidMailing = new Mailing();
		String fs = File.separator;

		MailingTemplate mt = new MailingTemplate();
		File f = new File("src" + fs + "test" + fs + "resources" + fs
				+ "examplemailing2.docx");
		mt.setFile(f);
		mt.setFileName(f.getName());
		validMailingTwo.setTemplate(mt);

	}

	@Test
	public void createValidMailing() throws Exception {

		Mailing result = mailingService.insertOrUpdate(validMailing);
		verify(mailingDAO).insertOrUpdate(validMailing);
		assertEquals(result, validMailing);

	}

	@Test(expected = ServiceException.class)
	public void createWrapsException_shouldThrowServiceException()
			throws Exception {

		doThrow(new PersistenceException()).when(mailingDAO).insertOrUpdate(
				invalidMailing);
		mailingService.insertOrUpdate(invalidMailing);
		verify(mailingService).insertOrUpdate(invalidMailing);

	}

	@Test
	public void getAll() throws Exception {
		List<Mailing> all = new ArrayList<Mailing>();
		all.add(validMailing);
		all.add(validMailingTwo);
		all.add(validMailingThree);

		when(mailingDAO.getAll()).thenReturn(all);
		List<Mailing> results = mailingService.getAll();
		assertEquals(results, all);
		verify(mailingDAO).getAll();

	}

	@Test(expected = ServiceException.class)
	public void createNull_shouldThrowException() throws Exception {

		doThrow(new PersistenceException()).when(mailingDAO).insertOrUpdate(
				null);
		mailingService.insertOrUpdate(null);
		verify(mailingDAO).insertOrUpdate(null);

	}

	@Test
	public void deleteMailing() throws Exception {

		mailingService.delete(validMailing);
		verify(mailingDAO).delete(validMailing);

	}

	@Test(expected = ServiceException.class)
	public void deleteMailing_shouldThrowServiceException() throws Exception {

		doThrow(new PersistenceException()).when(mailingDAO).delete(
				invalidMailing);
		mailingService.delete(invalidMailing);
		verify(mailingDAO).delete(invalidMailing);

	}

	@Test
	public void getById() throws Exception {

		when(mailingDAO.getById(1)).thenReturn(validMailingTwo);
		Mailing result = mailingService.getById(1);
		verify(mailingDAO).getById(1);
		assertEquals(result, validMailingTwo);

	}

	@Test(expected = ServiceException.class)
	public void getMailingsById_shouldThrowServiceException() throws Exception {

		doThrow(new PersistenceException()).when(mailingDAO).getById(-1);
		mailingService.getById(-1);
		verify(mailingDAO).getById(-1);

	}

	@Test
	public void getMailingsByPerson() throws Exception {

		List<Mailing> mailings = new ArrayList<Mailing>();
		mailings.add(validMailingTwo);
		mailings.add(validMailingThree);

		Person mark = new Person();
		mark.setGivenName("Mark");

		when(mailingDAO.getConfirmedMailingsByPerson(mark))
				.thenReturn(mailings);
		List<Mailing> results = mailingService
				.getConfirmedMailingsByPerson(mark);
		verify(mailingDAO).getConfirmedMailingsByPerson(mark);
		assertEquals(results, mailings);

	}

	@Test(expected = ServiceException.class)
	public void getMailingsByPerson_shouldThrowServiceException()
			throws Exception {

		Person invalidPerson = new Person();
		invalidPerson.setGivenName("Invalid");
		doThrow(new PersistenceException()).when(mailingDAO)
				.getConfirmedMailingsByPerson(invalidPerson);
		mailingService.getConfirmedMailingsByPerson(invalidPerson);
		verify(mailingDAO).getConfirmedMailingsByPerson(invalidPerson);

	}

	@Test(expected = IllegalArgumentException.class)
	public void createCSVWithNullArgument_ThrowsException() {
		mailingService.convertToCSV(null);
	}

	@Test
	public void createCSVWithValidArgument_ReturnsCSVString() throws Exception {
		List<Mailing> list = new ArrayList<Mailing>();

		validMailing.setDate(new SimpleDateFormat("dd.MM.yyyy")
				.parse("12.06.2013"));
		validMailingTwo.setDate(new SimpleDateFormat("dd.MM.yyyy")
				.parse("13.06.2013"));
		validMailingThree.setDate(new SimpleDateFormat("dd.MM.yyyy")
				.parse("14.06.2013"));

		list.add(validMailing);
		list.add(validMailingTwo);
		list.add(validMailingThree);
		String csv = mailingService.convertToCSV(list);
		assertEquals(csvExpected, csv);
	}

	@Test
	public void createCSVWithEmptyList_ReturnsCSVString() {
		List<Mailing> list = new ArrayList<Mailing>();
		String csv = mailingService.convertToCSV(list);
		assertTrue(csv.equals("Datum;Art;Medium;Vorlage\n"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void reproduceDocumentWithNullArgument_ThrowsException()
			throws ServiceException {
		mailingService.reproduceDocument(null);
	}

	@Test
	public void reproduceDocumentWithValidArgument_ProducesDocument()
			throws Exception {

		mailingService.reproduceDocument(validMailingTwo);

		verify(personDAO).getPersonsByMailing(validMailingTwo);
		// TODO how to mock a static method?
		// (MailingTemplateUtil#createMailingWithDocxTemplate())

	}

	private String csvExpected = "Datum;Art;Medium;Vorlage\n12.06.2013;Dankesbrief;E-Mail;-\n13.06.2013;Infomaterial;Postalisch;examplemailing2.docx\n"
			+ "14.06.2013;Einzelspenden Dankesbrief;E-Mail;-\n";

}

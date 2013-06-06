package at.fraubock.spendenverwaltung.service;

import static org.mockito.Mockito.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.PersonServiceImplemented;


public class PersonServiceTest extends AbstractPersonServiceTest{
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception, PersistenceException {
		init();
		personDAO = mock(IPersonDAO.class);
		PersonServiceImplemented service = new PersonServiceImplemented();
		service.setPersonDAO(personDAO);
		setPersonService(service);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}
}

package service;

import static org.mockito.Mockito.mock;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import dao.IPersonDAO;
import exceptions.PersistenceException;

public class DBPersonServiceTest extends AbstractPersonServiceTest{
	
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

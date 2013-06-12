package at.fraubock.spendenverwaltung.service;

import org.junit.Before;

import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;


public class PersonServiceImplTest extends AbstractPersonServiceTest{
	
	@Before
	public  void setUpBeforeClass() throws Exception, PersistenceException {
		PersonServiceImplemented service = new PersonServiceImplemented();
		service.setPersonDAO(personDAO);
		super.personService = service;
	}
}

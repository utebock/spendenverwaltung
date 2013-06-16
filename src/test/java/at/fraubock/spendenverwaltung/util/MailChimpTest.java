package at.fraubock.spendenverwaltung.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public class MailChimpTest {

	
	@Test(expected = ServiceException.class)
	public void invalidListId() throws ServiceException{
		MailChimp.addPersonsToList("invalidlistid", new ArrayList<Person>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullArgumentShouldThrowException() throws ServiceException{
		MailChimp.addPersonsToList(null, null);
	}
	
	@Test
	public void getLists() throws ServiceException {
		assertTrue(MailChimp.getLists()!=null);
	}
	
	@Test
	public void testExportToMailChimpWithEmptyList() throws ServiceException{
		String id = (String) MailChimp.getLists().keySet().toArray()[0];
		assertEquals(0,MailChimp.addPersonsToList(id, new ArrayList<Person>()));
	}

}

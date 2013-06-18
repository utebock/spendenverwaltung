package at.fraubock.spendenverwaltung.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService.MailChimpListItem;
import at.fraubock.spendenverwaltung.service.MailChimpServiceImplemented;

public class MailChimpServiceTest {
	
	private static IMailChimpService mailChimpService;
	
	@BeforeClass
	public static void init(){
		mailChimpService = new MailChimpServiceImplemented();
	}

	
	@Test(expected = ServiceException.class)
	public void invalidListId() throws ServiceException{
		mailChimpService.addPersonsToList("invalidlistid", new ArrayList<Person>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullArgumentShouldThrowException() throws ServiceException{
		mailChimpService.addPersonsToList(null, null);
	}
	
	@Test
	public void getLists() throws ServiceException {
		assertTrue(mailChimpService.getLists()!=null);
	}
	
	@Test
	public void testExportToMailChimpWithEmptyList() throws ServiceException{
		List<MailChimpListItem> list = mailChimpService.getLists();
		if(!list.isEmpty()){
			assertEquals(0,mailChimpService.addPersonsToList(list.get(0).getId(), new ArrayList<Person>()));
		}
	}
	
	@Test(expected=ServiceException.class)
	public void setInvalidAPIKey() throws ServiceException{
		mailChimpService.setAPIKey("invalidKey");
	}
	
	@Test
	public void setValidAPIKey() throws ServiceException{
		mailChimpService.setAPIKey("a66d44c0489c37f1960447b70ee480c7-us7");
	}

}

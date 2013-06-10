package at.fraubock.spendenverwaltung.util;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Person.Sex;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback=true)
public abstract class AbstractMailingTemplateTest {


	protected static IPersonDAO personDAO;
	private static List<Person> persons;
	
	
	public static void setPersonDAO(IPersonDAO personDAO) {
		AbstractMailingTemplateTest.personDAO = personDAO;
	}

	@Test(expected=IllegalArgumentException.class)
	public void nullArgumentShouldThrowException() throws IOException, ServiceException{
		MailingTemplate.createMailingWithDocxTemplate(null, null, null);
	}
	
	/* Just to test Mailing template without GUI 
	 * Test runs, but opens a java window and need some time
	 * will be removed later
	@Test
	public void trySimpleExample() throws ServiceException, IOException {
		MailingTemplate.template(new File("src/test/resources/examplemailing.docx"), persons, "Output");
	}
	*/
	
	@Test(expected=IOException.class)
	public void invalidFileShouldThrowIOException() throws ServiceException, IOException{
		MailingTemplate.createMailingWithDocxTemplate(new File("INVALID FILE"), persons, "invalid.pdf");
	}
	
	protected static void init() throws PersistenceException{
		persons = new ArrayList<Person>();
		
		//persons = personDAO.getAll();
		Address address1 = new Address();
		address1.setCity("Wien");
		address1.setStreet("Burgring 1");
		address1.setPostalCode("1010");
		address1.setCountry("Österreich");
		
		Person person1;
		person1 = new Person();
		person1.setTitle("Dipl.Ing.");
		person1.setSurname("Maier");
		person1.setGivenName("Hannes");
		person1.setSex(Sex.MALE);
		person1.setMainAddress(address1);
		
		persons.add(person1);
		
		
		Address address2 = new Address();
		address2.setCity("Wien");
		address2.setStreet("Bahnhofstraße 23");
		address2.setPostalCode("1050");
		address2.setCountry("Österreich");
		
		Person person2;
		person2 = new Person();
		person2.setTitle("Prof.");
		person2.setSurname("Bauer");
		person2.setGivenName("Sandra");
		person2.setSex(Sex.FEMALE);
		person2.setMainAddress(address2);
		
		persons.add(person2);
	}

}

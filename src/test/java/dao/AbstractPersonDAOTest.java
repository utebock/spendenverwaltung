package dao;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import domain.Person;
import exceptions.PersistenceException;

public abstract class AbstractPersonDAOTest {

	protected IPersonDAO personDAO;
	
	@Test
	public void createShouldCreatePersonWithValidParameters() throws PersistenceException{
		Person person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setNotificationType(Person.NotificationType.MAIL);
		Person heinz = personDAO.create(person);
		
		assertThat(heinz == null, is(false));
		assertThat(heinz.getGivenName(), is("Heinz"));
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void createWithInvalidParametersShouldThrowException() throws PersistenceException{
		personDAO.create(null);
	}
	
	@Test
	public void updateShouldUpdatePerson() throws PersistenceException{
		Person person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setNotificationType(Person.NotificationType.MAIL);
		
		personDAO.create(person);
		person.setSurname("XXX");
		personDAO.update(person);
		Person p = personDAO.getByID(person.getID());
	
		assertThat(p.getSurname(), is("XXX"));	
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void updateWithInvalidParametersShouldThrowException() throws PersistenceException{
		Person person = null;
		personDAO.update(person);
	}
	
	@Test (expected = NullPointerException.class) 
	public void updateNonExistentPersonShouldThrowException() throws PersistenceException{
		Person person = personDAO.getByID(1000000);
		person.setSurname("XXX");
		personDAO.update(person);
	}
	
	@Test
	public void getByIdShouldGetPersonByID() throws PersistenceException{
		Person person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setNotificationType(Person.NotificationType.MAIL);
		personDAO.create(person);
		
		Person p = personDAO.getByID(person.getID());
		
		assertThat(p.getGivenName(), is("Heinz"));
	}
	
	@Test (expected = NullPointerException.class)
	public void getByIdOfNonExistentPersonShouldThrowException() throws PersistenceException{
		Person person = personDAO.getByID(1000000);
		assertNull(person);
	}
	
	@Test
	public void deleteShouldDeletePerson() throws PersistenceException{
		Person person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setNotificationType(Person.NotificationType.MAIL);
		Person p = personDAO.create(person);
		personDAO.delete(p);
		Person p2 = personDAO.getByID(p.getID());
		assertNull(p2);
	}
	
	@Test (expected = NullPointerException.class)
	public void deleteNonExistentPersonShouldThrowException() throws PersistenceException{
		Person person = personDAO.getByID(1000000);
		personDAO.delete(person);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void deleteNullShouldThrowException() throws PersistenceException{
		personDAO.delete(null);
	}
	
	@Test
	public void getAllShouldGetAllPersons() throws PersistenceException{
		Person person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setNotificationType(Person.NotificationType.MAIL);
		personDAO.create(person);
		
		Person person2 = new Person();
		person2.setSalutation(Person.Salutation.HERR);
		person2.setTitle("Prof. Dr.");
		person2.setGivenName("Heinz");
		person2.setSurname("Oberhummer");
		person2.setEmail("heinz-oberhummer2@diekonfessionsfreien.at");
		person2.setNotificationType(Person.NotificationType.MAIL);
		personDAO.create(person2);
		
		List<Person> list = personDAO.getAll();
		assertThat(list.isEmpty(), is(false));
		
	}
	
	@Test
	public void getAllShouldReturnFalse() throws PersistenceException{
		Person person = new Person();
		person.setSalutation(Person.Salutation.HERR);
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setNotificationType(Person.NotificationType.MAIL);
		personDAO.create(person);
		
		Person person2 = personDAO.getByID(1000000);
		
		List<Person> list = personDAO.getAll();
		assertThat(list.contains(person2), is(false));
	}
}

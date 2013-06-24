package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Person.Sex;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractConfirmationDAOTest {

	protected static IPersonDAO personDao;
	protected static IConfirmationDAO confirmationDao;
	protected static IDonationDAO donationDao;
	
	protected static Donation donation;
	protected static Donation donation2;
	protected static Person person;
	protected static ConfirmationTemplate template;
	protected static Confirmation c1;
	protected static Confirmation c2;
	
	public static void setPersonDao(IPersonDAO personDao) {
		AbstractConfirmationDAOTest.personDao = personDao;
	}
	public static void setConfirmationDao(IConfirmationDAO confirmationDao) {
		AbstractConfirmationDAOTest.confirmationDao = confirmationDao;
	}
	public static void setDonationDao(IDonationDAO donationDao) {
		AbstractConfirmationDAOTest.donationDao = donationDao;
	}
	
	
	@Test(expected=PersistenceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException() throws PersistenceException{
		confirmationDao.insertOrUpdate(null);
	}
	
	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException() throws PersistenceException {
		confirmationDao.insertOrUpdate(new Confirmation()); // all values are null
	}
	
	@Test
	@Transactional
	public void createWithValidParameters() throws PersistenceException {
		confirmationDao.insertOrUpdate(c1);
		Confirmation result = confirmationDao.getById(c1.getId());
		assertEquals(c1, result);
	}
	
	@Test(expected=PersistenceException.class)
	@Transactional
	public void createWithInvalidTemplate_ThrowsException() throws PersistenceException{
		c1.setTemplate(null);
		confirmationDao.insertOrUpdate(c1);
	}
	
	@Test
	@Transactional
	public void updateWithValidParameters() throws PersistenceException {
		confirmationDao.insertOrUpdate(c1);
		Date oldDate = c1.getDate();
		c1.setDate(new Date(new Date().getTime()+100000));
		confirmationDao.insertOrUpdate(c1);
		assertFalse(c1.getDate().equals(oldDate));
		assertEquals(c1, confirmationDao.getById(c1.getId()));
	}
	
	@Test
	@Transactional
	public void insertWithValidParametersMultipleDonations() throws PersistenceException {
		confirmationDao.insertOrUpdate(c2);
		assertEquals(c2, confirmationDao.getById(c2.getId()));
	}
	
	@Test(expected = PersistenceException.class)
	@Transactional
	public void insertWithSingleAndMultipleDonations_ThrowsException() throws PersistenceException{
		c1.setFromDate(new Date());
		c1.setToDate(new Date());
		confirmationDao.insertOrUpdate(c1);
	}
	
	
	@Test
	@Transactional
	public void getAll_shouldReturnList() throws PersistenceException{
		confirmationDao.insertOrUpdate(c1);
		confirmationDao.insertOrUpdate(c2);
		
		List<Confirmation> list = new ArrayList<Confirmation>();
		list.add(c1);
		list.add(c2);
	
		assertEquals(list, confirmationDao.getAll());	
	}
	
	@Test
	@Transactional
	public void getAll_shouldReturnEmptyList() throws PersistenceException {
			List<Confirmation> results = confirmationDao.getAll();
			assertTrue(results.isEmpty());
	}
	
	@Test
	@Transactional
	public void findByInvalidId_shouldReturnNull() throws PersistenceException {
			assertNull(confirmationDao.getById(-1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void deleteWithInvalid_Throws_Exception() throws PersistenceException{
		confirmationDao.delete(null);
	}
	
	@Test
	@Transactional
	public void deleteWithValidShouldDelete() throws PersistenceException{
		assertTrue(confirmationDao.getAll().size()==0);
		confirmationDao.insertOrUpdate(c1);
		confirmationDao.insertOrUpdate(c2);
		assertTrue(confirmationDao.getAll().size()==2);
		confirmationDao.delete(c1);
		confirmationDao.delete(c2);
		assertTrue(confirmationDao.getAll().size()==0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getByTemplateWithNull_shouldThrowException() throws PersistenceException{
		confirmationDao.getByConfirmationTemplate(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getByTemplateWithTemplateWithNOID_shouldThrowException() throws PersistenceException{
		confirmationDao.getByConfirmationTemplate(template);
	}
	
	@Test
	@Transactional
	public void getByTemplate_ShouldReturn() throws PersistenceException{
		ConfirmationTemplate template2 = new ConfirmationTemplate();
		template2.setFile(template.getFile());
		template2.setName("Template2");
		
		confirmationDao.insertOrUpdate(c1);
		confirmationDao.insertOrUpdate(c2);
		assertEquals(confirmationDao.getByConfirmationTemplate(template).size(),2);
		
		c2.setTemplate(template2);
		confirmationDao.insertOrUpdate(c2);
		assertEquals(confirmationDao.getByConfirmationTemplate(template).size(),1);
		assertEquals(confirmationDao.getByConfirmationTemplate(template2).size(),1);
		
	}
	
	@Test
	@Transactional
	public void getByPersonNameLike_ShouldReturn() throws PersistenceException{
		Person p = new Person();
		p.setGivenName("Sebastian");
		p.setSurname("Braun");
		p.setSex(Sex.MALE);
		personDao.insertOrUpdate(p);
		c1.setPerson(p);
		
		confirmationDao.insertOrUpdate(c1);
		confirmationDao.insertOrUpdate(c2);
		
		List<Confirmation> result = confirmationDao.getByPersonNameLike("Sebastia");
		assertEquals(result.size(), 1);
	}
	
	public static void initData() throws PersistenceException{
		person = new Person();

		person.setSex(Person.Sex.MALE);
		person.setCompany("IBM");
		person.setTitle("Prof. Dr.");
		person.setGivenName("Heinz");
		person.setSurname("Oberhummer");
		person.setEmail("heinz-oberhummer@diekonfessionsfreien.at");
		person.setTelephone("01234567889");
		person.setNote("");
		person.setPostalNotification(false);
		person.setEmailNotification(true);
		personDao.insertOrUpdate(person);
		
		donation = new Donation();
		donation.setAmount(25l);
		donation.setDate(new GregorianCalendar(2013, 01, 05).getTime());
		donation.setDedication("Spende");
		donation.setDonator(person);
		donation.setNote("Spendennotiz");
		donation.setType(DonationType.BANK_TRANSFER);
		donationDao.insertOrUpdate(donation);
		
		donation2 = new Donation();
		donation2.setAmount(50l);
		donation2.setDate(new GregorianCalendar(2012, 11, 21).getTime());
		donation2.setDedication("Spende Nov");
		donation2.setDonator(person);
		donation2.setType(DonationType.BAR);
		donationDao.insertOrUpdate(donation2);
		
		template = new ConfirmationTemplate();
		String fs = File.separator;
		File f = new File(
				"src"+fs+"test"+fs+"resources"+fs+"examplemailing.docx");
		template.setFile(f);
		template.setName("SpendenbestätigungsTest");
		
		c1 = new Confirmation();
		c1.setDate(new Date());
		c1.setDonation(donation);
		c1.setTemplate(template);
		c1.setPerson(person);
		
		c2 = new Confirmation();
		c2.setDate(new Date());
		c2.setTemplate(template);
		c2.setPerson(person);
		c2.setFromDate(new GregorianCalendar(2000, 01, 01).getTime());
		c2.setToDate(new GregorianCalendar(2013, 6, 17).getTime());
		
	}

}

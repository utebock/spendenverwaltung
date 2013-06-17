package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;

/**
 * 
 * @author romanvoglhuber
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class DBConfirmationDAOTest extends AbstractConfirmationDAOTest {
	
	private static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBConfirmationDAOTest.context = new ClassPathXmlApplicationContext("testspring.xml");

		AbstractConfirmationDAOTest.setConfirmationDao(context.getBean("confirmationDao", IConfirmationDAO.class));
		AbstractConfirmationDAOTest.setDonationDao(context.getBean("donationDao", IDonationDAO.class));
		AbstractConfirmationDAOTest.setPersonDao(context.getBean("personDao", IPersonDAO.class));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@Before
	@Transactional
	public void setUp() throws Exception{
		initData();
	}

}

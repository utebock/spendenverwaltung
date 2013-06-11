package at.fraubock.spendenverwaltung.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingTemplateDAO;

public class DBMailingTemplateDAOTest extends AbstractMailingTemplateDAOTest {
	
	private static ApplicationContext context;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		context = new ClassPathXmlApplicationContext("testspring.xml");
		
		setMailingTemplateDAO(context.getBean("mailingTemplateDao", IMailingTemplateDAO.class));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
	
}

package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public class AbstractMailingTemplateDAOTest {

	protected static IMailingTemplateDAO mailingTemplateDAO;

	public static void setMailingTemplateDAO(
			IMailingTemplateDAO mailingTemplateDAO) {
		AbstractMailingTemplateDAOTest.mailingTemplateDAO = mailingTemplateDAO;
	}

	/*
	 * testing create
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException()
			throws PersistenceException {
		mailingTemplateDAO.insert(null);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException()
			throws PersistenceException {
		mailingTemplateDAO.insert(new MailingTemplate());
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedMailingTemplate() {
		MailingTemplate mt = new MailingTemplate();
		File f = new File("");
		mt.setFile(f);
		mt.setFileName(f.getName());
		mt.setFileSize((int) f.length());

		try {
			mailingTemplateDAO.insert(mt);
			
			MailingTemplate savedMt = mailingTemplateDAO.getByID(mt.getId());
			assert (savedMt.equals(mt));
		} catch (PersistenceException e) {
			fail();
		}
	}

}

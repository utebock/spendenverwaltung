package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Before;
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
public abstract class AbstractMailingTemplateDAOTest {

	protected static IMailingTemplateDAO mailingTemplateDAO;

	private MailingTemplate mt;
	private MailingTemplate mt2;

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
		try {
			mailingTemplateDAO.insert(mt);

			MailingTemplate savedMt = mailingTemplateDAO.getByID(mt.getId());
			assert (savedMt.equals(mt));
		} catch (PersistenceException e) {
			e.printStackTrace();
			fail();
		}
	}

	/*
	 * testing delete
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException()
			throws PersistenceException {
		mailingTemplateDAO.delete(null);
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() {
		try {
			mailingTemplateDAO.insert(mt);
			mailingTemplateDAO.delete(mt);
			List<MailingTemplate> allMailingTemplatees = mailingTemplateDAO
					.getAll();
			assert (!allMailingTemplatees.contains(mt));

		} catch (PersistenceException e) {
			fail();
		}
	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAll_ReturnsAllEntities() {
		try {
			mailingTemplateDAO.insert(mt);
			mailingTemplateDAO.insert(mt2);

			List<MailingTemplate> mailingTemplateList = mailingTemplateDAO
					.getAll();
			assert (mailingTemplateList != null && mailingTemplateList.size() == 2);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithInvalidId_ReturnsNull() throws PersistenceException {
		assertNull(mailingTemplateDAO.getByID(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() {
		try {
			mailingTemplateDAO.getByID(-1);
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() {

		try {
			mailingTemplateDAO.insert(mt);
			MailingTemplate foundMailingTemplate = mailingTemplateDAO
					.getByID(mt.getId());

			assert (foundMailingTemplate != null && foundMailingTemplate
					.getId() == mt.getId());
		} catch (PersistenceException e) {
			fail();
		}
	}

	@Before
	public void init() {
		String fs = File.separator;
		mt = new MailingTemplate();
		File f = new File(
				"src"+fs+"test"+fs+"resources"+fs+"examplemailing2.docx");
		mt.setFile(f);
		mt.setFileName(f.getName());
		
		mt2 = new MailingTemplate();
		File f2 = new File(
				"src"+fs+"test"+fs+"resources"+fs+"examplemailing2.docx");
		mt2.setFile(f2);
		mt2.setFileName(f2.getName());
	}
}

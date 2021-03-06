/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testspring.xml")
@TransactionConfiguration(defaultRollback = true)
public abstract class AbstractConfirmationTemplateDAOTest {

	protected static IConfirmationTemplateDAO confirmationTemplateDAO;
	private ConfirmationTemplate ct1;
	private ConfirmationTemplate ct2;

	public static void setConfirmationTemplateDAO(
			IConfirmationTemplateDAO confirmationTemplateDAO) {
		AbstractConfirmationTemplateDAOTest.confirmationTemplateDAO = confirmationTemplateDAO;
	}

	/*
	 * testing create
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithNullParameter_ThrowsException()
			throws PersistenceException {
		confirmationTemplateDAO.insertOrUpdate(null);
	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void createWithInvalidStateParameter_ThrowsException()
			throws PersistenceException {
		confirmationTemplateDAO.insertOrUpdate(new ConfirmationTemplate());
	}

	@Test
	@Transactional
	public void createWithValidParameter_ReturnsSavedConfirmationTemplate()
			throws Exception {

		confirmationTemplateDAO.insertOrUpdate(ct1);

		ConfirmationTemplate savedCt = confirmationTemplateDAO.getByID(ct1
				.getId());
		assertTrue(savedCt.equals(ct1));

	}

	/*
	 * testing delete
	 */

	@Test(expected = PersistenceException.class)
	@Transactional
	public void deleteWithNullParameter_ThrowsException()
			throws PersistenceException {
		confirmationTemplateDAO.delete(null);
	}

	@Test
	@Transactional
	public void deleteWithValidParameter_RemovesEntity() throws Exception {

		confirmationTemplateDAO.insertOrUpdate(ct1);
		confirmationTemplateDAO.delete(ct1);
		List<ConfirmationTemplate> allConfirmationTemplatees = confirmationTemplateDAO
				.getAll();
		assertTrue(!allConfirmationTemplatees.contains(ct1));

	}

	/*
	 * testing find
	 */

	@Test
	@Transactional(readOnly = true)
	public void getAll_ReturnsAllEntities() throws Exception {

		confirmationTemplateDAO.insertOrUpdate(ct1);
		confirmationTemplateDAO.insertOrUpdate(ct2);

		List<ConfirmationTemplate> confirmationTemplateList = confirmationTemplateDAO
				.getAll();
		assertTrue(confirmationTemplateList != null
				&& confirmationTemplateList.size() == 2);

	}

	@Test
	@Transactional(readOnly = true)
	public void getWithInvalidId_ReturnsNull() throws PersistenceException {
		assertNull(confirmationTemplateDAO.getByID(10000));
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional(readOnly = true)
	public void getWithNegativeId_ThrowsException() throws Exception {

		confirmationTemplateDAO.getByID(-1);

	}

	@Test
	@Transactional(readOnly = true)
	public void getWithValidId_ReturnsEntity() throws Exception {

		confirmationTemplateDAO.insertOrUpdate(ct1);
		ConfirmationTemplate foundConfirmationTemplate = confirmationTemplateDAO
				.getByID(ct1.getId());

		assertTrue(foundConfirmationTemplate != null
				&& foundConfirmationTemplate.getId() == ct1.getId());

	}

	@Test(expected = PersistenceException.class)
	@Transactional
	public void InsertTwoTemplatesWithSameName_ThrowsException()
			throws PersistenceException {
		confirmationTemplateDAO.insertOrUpdate(ct1);
		ct2.setName(ct1.getName());
		confirmationTemplateDAO.insertOrUpdate(ct2);
	}

	@Before
	public void init() {
		String fs = File.separator;
		ct1 = new ConfirmationTemplate();
		File f = new File("src" + fs + "test" + fs + "resources" + fs
				+ "examplemailing.docx");
		ct1.setFile(f);
		ct1.setName(f.getName());

		ct2 = new ConfirmationTemplate();
		File f2 = new File("src" + fs + "test" + fs + "resources" + fs
				+ "examplemailing2.docx");
		ct2.setFile(f2);
		ct2.setName(f2.getName());
	}
}

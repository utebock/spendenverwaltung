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
package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Test;


public abstract class AbstractConfirmationServiceTest {

	protected IConfirmationService confirmationService;
	protected IConfirmationDAO confirmationDao = mock(IConfirmationDAO.class);
	protected IConfirmationTemplateDAO confirmationTemplateDao = mock(IConfirmationTemplateDAO.class);
	
	private ConfirmationTemplate ct1;
	private Confirmation c1;
	private Confirmation c2;
	
	public void init(){
		ct1 = new ConfirmationTemplate();
		File f = new File("src"+File.separator+"test"+File.separator+"resources"+File.separator+"examplemailing.docx");
		ct1.setFile(f);
		ct1.setName("Spendenbestätigung v1");
		
		Person p = new Person();
		Donation d = new Donation();
		
		c1 = new Confirmation();
		c1.setDate(new Date());
		c1.setDonation(d);
		c1.setPerson(p);
		
		c2 = new Confirmation();
		c2.setDate(new Date());
		c2.setPerson(p);
		c2.setFromDate(new Date());
		c2.setToDate(new Date(new Date().getTime()+10000));
	}
	
	@Test(expected = ServiceException.class)
	public void insertOrUpdateConfirmationTemplateShouldThrowServiceException() throws PersistenceException, ServiceException{
		doThrow(new PersistenceException()).when(confirmationTemplateDao).insertOrUpdate(ct1);
		confirmationService.insertOrUpdateConfirmationTemplate(ct1);
		verify(confirmationTemplateDao).insertOrUpdate(ct1);
	}
	
	@Test(expected = ServiceException.class)
	public void insertOrUpdateConfirmationTemplateWithNull_shouldThrowException() throws PersistenceException, ServiceException{
		doThrow(new PersistenceException()).when(confirmationTemplateDao).insertOrUpdate(null);
		confirmationService.insertOrUpdateConfirmationTemplate(null);
		verify(confirmationTemplateDao).insertOrUpdate(null);
	}
	
	@Test(expected = ServiceException.class)
	public void deleteConfirmationTemplateShouldThrowServiceException() throws ServiceException, PersistenceException{
		doThrow(new PersistenceException()).when(confirmationTemplateDao).delete(ct1);
		confirmationService.delete(ct1);
		verify(confirmationTemplateDao).delete(ct1);
	}
	
	@Test(expected = ServiceException.class)
	public void deleteConfirmationTemplateWhichIsInUseShouldThrowServiceException() throws PersistenceException, ServiceException{
		List<Confirmation> confirmationList = new ArrayList<Confirmation>();
		confirmationList.add(new Confirmation());
		when(confirmationDao.getByConfirmationTemplate(ct1)).thenReturn(confirmationList);
		confirmationService.delete(ct1);
		verify(confirmationDao.getByConfirmationTemplate(ct1));
	}
	
	@Test(expected = ServiceException.class)
	public void insertOrUpdateShouldThrowServiceException() throws PersistenceException, ServiceException{
		doThrow(new PersistenceException()).when(confirmationDao).insertOrUpdate(c1);
		confirmationService.insertOrUpdate(c1);
		verify(confirmationDao).insertOrUpdate(c1);
	}
	
	@Test(expected = ServiceException.class)
	public void insertOrUpdateWithNullShouldThrowServiceException() throws PersistenceException, ServiceException{
		doThrow(new PersistenceException()).when(confirmationDao).insertOrUpdate(null);
		confirmationService.insertOrUpdate(null);
		verify(confirmationDao).insertOrUpdate(null);
	}
	
	@Test
	public void insertOrUpdateWithValidParameter() throws ServiceException, PersistenceException{
		Confirmation returned = confirmationService.insertOrUpdate(c1);
		assertEquals(returned, c1);
		verify(confirmationDao).insertOrUpdate(c1);
	}
	
	@Test(expected = ServiceException.class)
	public void getByIdShouldThrowServiceException() throws PersistenceException, ServiceException{
		int id = 1;
		doThrow(new PersistenceException()).when(confirmationDao).getById(id);
		confirmationService.getById(id);
		verify(confirmationDao).getById(id);
	}
	
	@Test
	public void getByIdWithValidParameter() throws ServiceException, PersistenceException{
		int id = 123;
		c1.setId(id);
		confirmationService.insertOrUpdate(c1);
		when(confirmationDao.getById(id)).thenReturn(c1);
		Confirmation returned = confirmationService.getById(id);
		assertEquals(returned, c1);
		verify(confirmationDao).insertOrUpdate(c1);
		verify(confirmationDao).getById(id);
		
	}
	
	@Test(expected = ServiceException.class)
	public void deleteShouldThrowServiceException() throws PersistenceException, ServiceException{
		doThrow(new PersistenceException()).when(confirmationDao).delete(c1);
		confirmationService.delete(c1);
		verify(confirmationDao).delete(c1);
	}
	
	@Test
	public void deleteWithValidParameter() throws ServiceException, PersistenceException{
		int id = 123;
		c1.setId(id);
		confirmationService.delete(c1);
		verify(confirmationDao).delete(c1);
		
	}
	
	@Test(expected = ServiceException.class)
	public void getAllShouldThrowServiceException() throws PersistenceException, ServiceException{
		doThrow(new PersistenceException()).when(confirmationDao).getAll();
		confirmationService.getAll();
		verify(confirmationDao).getAll();
	}
	
	@Test
	public void getAllWithValidParameter() throws ServiceException, PersistenceException{
		List<Confirmation> list = new ArrayList<Confirmation>();
		list.add(c1);
		list.add(c2);
		when(confirmationDao.getAll()).thenReturn(list);
		List<Confirmation> returned = confirmationService.getAll();
		assertEquals(returned, list);
		verify(confirmationDao).getAll();
		
	}
	
}

package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;
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
	
	public void init(){
		ct1 = new ConfirmationTemplate();
		File f = new File("src"+File.separator+"test"+File.separator+"resources"+File.separator+"examplemailing.docx");
		ct1.setFile(f);
		ct1.setName("Spendenbestätigung v1");
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
}

package at.fraubock.spendenverwaltung.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;

public class ConfirmationServiceImplemented implements IConfirmationService {

	private static final Logger log = Logger
			.getLogger(ConfirmationServiceImplemented.class);
	
	private IConfirmationDAO confirmationDAO;
	private IConfirmationTemplateDAO confirmationTemplateDAO;
	
	public void setConfirmationDAO(IConfirmationDAO confirmationDAO) {
		this.confirmationDAO = confirmationDAO;
	}

	public void setConfirmationTemplateDAO(
			IConfirmationTemplateDAO confirmationTemplateDAO) {
		this.confirmationTemplateDAO = confirmationTemplateDAO;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public ConfirmationTemplate insertOrUpdateConfirmationTemplate(ConfirmationTemplate template)
			throws ServiceException {
		try{
			confirmationTemplateDAO.insertOrUpdate(template);
			return template;
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}

	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<ConfirmationTemplate> getAllConfirmationTempaltes() throws ServiceException {
		try{
			return confirmationTemplateDAO.getAll();
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void reproduceDocument(Confirmation confirmation)
			throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ConfirmationTemplate template) throws ServiceException {
		try {
			if(confirmationDAO.getByConfirmationTemplate(template).isEmpty()){
				confirmationTemplateDAO.delete(template);
			}
			else{
				throw new ServiceException("Can't delete a template which is used by a saved confirmation");
			}
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
		
	}

}

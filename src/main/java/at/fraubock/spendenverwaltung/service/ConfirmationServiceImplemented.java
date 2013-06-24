package at.fraubock.spendenverwaltung.service;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;
import at.fraubock.spendenverwaltung.util.ConfirmationTemplateUtil;

public class ConfirmationServiceImplemented implements IConfirmationService {

	private static final Logger log = Logger
			.getLogger(ConfirmationServiceImplemented.class);
	
	private IConfirmationDAO confirmationDAO;
	private IConfirmationTemplateDAO confirmationTemplateDAO;
	private IDonationDAO donationDAO;
	
	public void setConfirmationDAO(IConfirmationDAO confirmationDAO) {
		this.confirmationDAO = confirmationDAO;
	}

	public void setConfirmationTemplateDAO(
			IConfirmationTemplateDAO confirmationTemplateDAO) {
		this.confirmationTemplateDAO = confirmationTemplateDAO;
	}
	
	public void setDonationDAO(IDonationDAO donationDAO){
		this.donationDAO = donationDAO;
	}

	@Override
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Throwable.class)
	public Confirmation insertOrUpdate(Confirmation confirmation)
			throws ServiceException {
		try {
			confirmationDAO.insertOrUpdate(confirmation);
			return confirmation;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Confirmation getById(int id) throws ServiceException {
			try {
				return confirmationDAO.getById(id);
			} catch (PersistenceException e) {
				throw new ServiceException(e);
			}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void delete(Confirmation confirmation) throws ServiceException {
		try {
			confirmationDAO.delete(confirmation);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Confirmation> getAll() throws ServiceException {
		try {
			return confirmationDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Throwable.class)
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
	public File reproduceDocument(Confirmation confirmation, String outputName)
			throws ServiceException {
		File file;
		List<Donation> donations;
		if(confirmation.getDonation()!=null){
			donations = new ArrayList<Donation>();
			donations.add(confirmation.getDonation());
		}
		else{
			try {
				donations = donationDAO.getByPerson(confirmation.getPerson());
			} catch (PersistenceException e) {
				throw new ServiceException(e);
			}
			for(int i = donations.size()-1; i>0; i--){
				Donation d = donations.get(i);
				if(d.getDate().before(confirmation.getFromDate())||d.getDate().after(confirmation.getToDate())){
					donations.remove(d);
				}
			}
			
		}
		
		try {
			ConfirmationTemplateUtil.createMailingWithDocxTemplate(confirmation.getTemplate().getFile(), donations, outputName);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		file = new File(outputName);
		return file;
	}
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
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
	
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public List<Confirmation> getByPersonNameLike(String searchString) throws ServiceException {
		try{
			return confirmationDAO.getByPersonNameLike(searchString);
		}
		catch(PersistenceException e){
			throw new ServiceException(e);
		}
	}


	

}

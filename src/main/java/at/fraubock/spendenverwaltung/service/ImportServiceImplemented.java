package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.dao.IImportDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Person.Sex;
import at.fraubock.spendenverwaltung.interfaces.domain.csvimport.ImportRow;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.CSVImport;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class ImportServiceImplemented implements IImportService {
	private IPersonService personService;
	private IDonationService donationService;
	private IAddressService addressService;
	
	private IImportDAO importDAO;
	
	private static final Logger log = Logger.getLogger(ImportServiceImplemented.class);

	public void nativeImport(File file) throws ServiceException{
		Map<String, String> columnMapping = new HashMap<String, String>();
		
		Person person;
		Donation donation;
		Address address;
		Import imp;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		log.debug("Native import file: "+file);
		
		Properties config = new Properties();
		try {
			config.load(getClass().getClassLoader().getResourceAsStream("native_import_config.properties"));

			for(Entry<Object, Object> entry : config.entrySet()){
				if(String.valueOf(entry.getValue()).length()>0){
					columnMapping.put(String.valueOf(entry.getValue()), String.valueOf(entry.getKey()));
				}
			}

			List<ImportRow> importRows = CSVImport.readCSVWithMapping(file, columnMapping);
		
			imp = new Import();
			imp.setImportDate(new Date());
			imp.setSource("native");
			imp.setCreator("");
			try{
				importDAO.insertOrUpdate(imp);
			}
			catch(PersistenceException e){
				throw new ServiceException("Persistence error while creating import ");
			}
			
			for(ImportRow row : importRows){
				//Person
				person = new Person();
				person.setGivenName(row.getGivenName());
				person.setSurname(row.getSurname());
				person.setEmail(row.getEmail());
				person.setSex(Sex.getByName(row.getSex()));
				person.setTitle(row.getTitle());
				person.setCompany(row.getCompany());
				person.setTelephone(row.getTelephone());
				person.setEmailNotification(Boolean.valueOf(row.getEmailNotification()));
				person.setPostalNotification(Boolean.valueOf(row.getPostalNotification()));
				person.setNote(row.getPersonNote());
				
				//Donation
				donation = new Donation();
				try {
					donation.setDate(df.parse(row.getDate()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				donation.setAmount(Long.valueOf((long) (Double.valueOf(row.getAmount())*100)));
				donation.setDedication(row.getDedication());
				donation.setNote(row.getDonationNote());
				donation.setType(DonationType.getByName(row.getType()));
				
				//Address
				address = new Address();
				address.setStreet(row.getStreet());
				address.setCity(row.getCity());
				address.setPostalCode(row.getPostcode());
				address.setCountry(row.getCountry());
				
				//Connect Domains
				address = addressService.create(address);
				List<Address> addresses = person.getAddresses();
				addresses.add(address);
				
				person.setAddresses(addresses);
				person.setMainAddress(address);
				person = personService.create(person);
				
				donation.setDonator(person);
				donation.setSource(imp);
				donationService.create(donation);				
			}
			
			log.debug("Imported "+importRows.size()+" donations");
		} catch (IOException e) {
			throw new ServiceException("IOException "+e.getMessage());
		}
	}
	
	@Override
	public void hypoImport(File file) throws ServiceException {
		
	}

	public IImportDAO getImportDAO() {
		return importDAO;
	}

	public void setImportDAO(IImportDAO importDAO) {
		this.importDAO = importDAO;
	}

	public IPersonService getPersonService() {
		return personService;
	}

	public IDonationService getDonationService() {
		return donationService;
	}

	public IAddressService getAddressService() {
		return addressService;
	}

	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public void setDonationService(IDonationService donationService) {
		this.donationService = donationService;
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}
}

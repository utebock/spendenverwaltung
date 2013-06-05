package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Person.Sex;
import at.fraubock.spendenverwaltung.interfaces.domain.csvimport.ImportRow;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.util.CSVImport;

public class ImportServiceImplemented implements IImportService {

	private static final Logger log = Logger.getLogger(ImportServiceImplemented.class);
	
	public void nativeImport(File file) throws ServiceException{
		Map<String, String> columnMapping = new HashMap<String, String>();
		List<Person> persons;
		List<Donation> donations;
		Person person;
		Donation donation;
		Address address;
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
		
			persons = new ArrayList<Person>();
			donations = new ArrayList<Donation>();
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
				List<Address> addresses = person.getAddresses();
				addresses.add(address);
				person.setAddresses(addresses);
				if(person.getMainAddress()==null){
					person.setMainAddress(address);
				}
				donation.setDonator(person);
				
				
				
				persons.add(person);
				donations.add(donation);
			}
			
			log.debug("Could add "+donations.size()+" donations to "+persons.size()+" persons (not implemented)");
		} catch (IOException e) {
			throw new ServiceException("IOException "+e.getMessage());
		}
	}
}

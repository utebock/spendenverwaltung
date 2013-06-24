package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IImportDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Person.Sex;
import at.fraubock.spendenverwaltung.interfaces.domain.csvimport.ImportRow;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;
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

	private static final Logger log = Logger
			.getLogger(ImportServiceImplemented.class);

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public void nativeImport(File file) throws ServiceException, IOException {
		Map<String, String> columnMapping;
		int rowCount = 0;
		Person person;
		Donation donation;
		Address address;
		List<Donation> donationList;
		List<Address> addresses;

		Import imp;
		List<ImportRow> importRows = null;

		// set up formats for date and amount parsing
		NumberFormat f = NumberFormat.getInstance(Locale.GERMAN);
		if (!(f instanceof DecimalFormat)) {
			String msg = "Number format used for importing native CSV data is not applicable for decimal amounts";
			log.error(msg);
			throw new ServiceException(msg);
		}
		DecimalFormat df = (DecimalFormat) f;
		df.setParseBigDecimal(true);

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",
				Locale.GERMAN);

		log.debug("Native import with CSV: " + file);

		// Read mapping config and read CSV
		columnMapping = readMappingConfig("native_import_config.properties");
		importRows = CSVImport.readCSVWithMapping(file, columnMapping);

		donationList = new ArrayList<Donation>(importRows.size());

		// Create import (domain)
		imp = new Import();
		imp.setImportDate(new Date());
		imp.setSource("native");
		imp.setCreator("");

		createImport(imp);

		// Read all rows
		for (ImportRow row : importRows) {
			try {
				rowCount++;

				// Person
				person = new Person();
				person.setGivenName(row.getGivenName());
				person.setSurname(row.getSurname());
				person.setEmail(row.getEmail());
				person.setSex(Sex.getByName(row.getSex()));
				person.setTitle(row.getTitle());
				person.setCompany(row.getCompany());
				person.setTelephone(row.getTelephone());
				if(row.getEmailNotification().equals("1"))
					person.setEmailNotification(true);
				else
					person.setEmailNotification(false);
				if(row.getPostalNotification().equals("1"))
					person.setPostalNotification(true);
				else
					person.setPostalNotification(false);
				person.setNote(row.getPersonNote());

				// Donation
				donation = new Donation();
				try {
					donation.setDate(dateFormat.parse(row.getDate()));
				} catch (ParseException e) {
					String msg = "An error occurred during date parsing in row "
							+ rowCount
							+ ". The date has to be in dd.MM.yyyy format";
					log.warn(msg);
					throw new ServiceException(msg, e);
				}

				BigDecimal n = (BigDecimal) df.parse(row.getAmount(),
						new ParsePosition(0));
				n = n.multiply(new BigDecimal(100));
				donation.setAmount(n.toBigInteger().longValue());
				donation.setDedication(row.getDedication());
				donation.setNote(row.getDonationNote());
				donation.setType(DonationType.getByName(row.getType()));

				// Address
				address = new Address();
				address.setStreet(row.getStreet());
				address.setCity(row.getCity());
				address.setPostalCode(row.getPostcode());
				address.setCountry(row.getCountry());

				// Connect domains
				person.setMainAddress(address);

				donation.setDonator(person);
				donation.setSource(imp);

				donationList.add(donation);

			} catch (IllegalArgumentException | NullPointerException e) {
				String msg = "Error during native CSV Import in row "
						+ rowCount;
				log.error(msg);
				throw new ServiceException(msg, e);
			}

			// Save data
			for (Donation d : donationList) {
				person = d.getDonator();

				// Address
				address = person.getMainAddress();
				if (address.getId() == null) {
					address = addressService.create(address);
				}
				addresses = person.getAddresses();
				addresses.add(address);
				person.setAddresses(addresses);
				person.setMainAddress(address);

				log.debug("Person: " + person.getGivenName() + " "
						+ person.getSurname() + " " + person.getEmail()
						+ " id:" + person.getId() + " mainaddressid:"
						+ person.getMainAddress().getId());
				// Person
				if (person.getId() == null) {
					person = personService.create(person);
				}
				d.setDonator(person);

				// Donation
				donationService.create(d);
			}
		}

		log.debug("Successfully imported " + importRows.size() + " donations. ");
	}

	public Map<String, String> readMappingConfig(String configName)
			throws ServiceException {
		Map<String, String> columnMapping = new HashMap<String, String>();
		InputStream input;

		Properties config = new Properties();
		try {
			input = getClass().getClassLoader().getResourceAsStream(configName);
			if (input == null) {
				throw new ServiceException("Mapping config file not found: "
						+ configName);
			}
			config.load(input);
		} catch (IOException e) {
			throw new ServiceException(e);
		} catch (IllegalArgumentException e) {
			throw new ServiceException(e);
		}

		for (Entry<Object, Object> entry : config.entrySet()) {
			if (String.valueOf(entry.getValue()).length() > 0) {
				columnMapping.put(String.valueOf(entry.getValue()),
						String.valueOf(entry.getKey()));
			}
		}

		log.debug("read mapping config " + configName);
		return columnMapping;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Import createImport(Import i) throws ServiceException {
		try {
			importDAO.insertOrUpdate(i);
			return i;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public void hypoImport(File file) throws ServiceException, IOException {
		List<ImportRow> rowList;
		try {
			rowList = CSVImport.readHypoCsv(file);
		} catch (ValidationException e) {
			throw new ServiceException(e);
		}

		Import i = new Import();
		i.setImportDate(new Date());
		i.setSource("Hypo-Export");

		List<Donation> donations = new ArrayList<Donation>(rowList.size());

		// set up formats for date and amount parsing
		NumberFormat f = NumberFormat.getInstance(Locale.GERMAN);
		if (!(f instanceof DecimalFormat)) {
			String msg = "Number format used for importing Hypo CSV data is not applicable for decimal amounts";
			log.error(msg);
			throw new ServiceException(msg);
		}
		DecimalFormat df = (DecimalFormat) f;
		df.setParseBigDecimal(true);

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",
				Locale.GERMAN);

		// convert the rows to donation entities
		for (ImportRow row : rowList) {
			Donation d = new Donation();

			BigDecimal n = (BigDecimal) df.parse(row.getAmount(),
					new ParsePosition(0));
			n = n.multiply(new BigDecimal(100));
			d.setAmount(n.toBigInteger().longValue());

			try {
				d.setDate(dateFormat.parse(row.getDate()));
			} catch (ParseException e) {
				String msg = "Date in the Hypo CSV file has wrong format";
				log.warn(msg);
				throw new ServiceException(msg, e);
			}

			d.setNote(row.getDonationNote());

			d.setSource(i);

			d.setType(Donation.DonationType.getByName(row.getType()));

			donations.add(d);
		}

		createImport(i);

		// now, save donations
		for (Donation d : donations) {
			donationService.create(d);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public void smsImport(File file) throws ServiceException, IOException {

		List<ImportRow> rowList;
		rowList = CSVImport.readSmsCsv(file);

		Import i = new Import();
		i.setImportDate(new Date());
		i.setSource("SMS-Export");

		List<Donation> donations = new ArrayList<Donation>(rowList.size());

		// set up formats for date and amount parsing
		NumberFormat f = NumberFormat.getInstance(Locale.GERMAN);
		if (!(f instanceof DecimalFormat)) {
			String msg = "Number format used for importing SMS CSV data is not applicable for decimal amounts";
			log.error(msg);
			throw new ServiceException(msg);
		}
		DecimalFormat df = (DecimalFormat) f;
		df.setParseBigDecimal(true);

		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",
				Locale.GERMAN);

		// convert the rows to donation entities
		for (ImportRow row : rowList) {
			Donation d = new Donation();

			BigDecimal n = (BigDecimal) df.parse(row.getAmount(),
					new ParsePosition(0));
			n = n.multiply(new BigDecimal(100));
			d.setAmount(n.toBigInteger().longValue());

			try {
				d.setDate(dateFormat.parse(row.getDate()));
			} catch (ParseException e) {
				String msg = "Date in the Hypo CSV file has wrong format";
				log.warn(msg);
				throw new ServiceException(msg, e);
			}
			d.setSource(i);

			d.setType(Donation.DonationType.getByName(row.getType()));

			donations.add(d);
		}

		createImport(i);

		// now, save donations
		for (Donation d : donations) {
			donationService.create(d);
		}

	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public List<Import> getAllUnconfirmed() throws ServiceException {
		try {
			return importDAO.getAllUnconfirmed();
		} catch (PersistenceException e) {
			return new ArrayList<Import>();
		}
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
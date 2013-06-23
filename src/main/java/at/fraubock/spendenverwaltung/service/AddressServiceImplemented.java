package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.util.statistics.Province;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * implementation of {@link IAddressService}
 * 
 * @author philipp muhoray
 * 
 */
public class AddressServiceImplemented implements IAddressService {
	private static final Logger log = Logger
			.getLogger(AddressServiceImplemented.class);
	private IAddressDAO addressDAO;

	public IAddressDAO getAddressDAO() {
		return addressDAO;
	}

	public void setAddressDAO(IAddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	/**
	 * checks the integrity of an {@link Address} entity. Especially checks that
	 * Austrian postcodes are 4-digit. Additionally checks that country is
	 * 3-char minimum, starts with capital, and each word has capital only at
	 * the beginning
	 * 
	 * @author manuel-bichler
	 * @throws ValidationException
	 * 
	 */

	private static void validate(Address address) throws ValidationException {
		if (address == null) {
			log.error("Argument was null");
			throw new ValidationException("Address must not be null");
		}
		if (address.getStreet() == null) {
			log.error("Street was null");
			throw new ValidationException("Street must not be null");
		}
		if (address.getCity() == null) {
			log.error("City was null");
			throw new ValidationException("City must not be null");
		}
		if (address.getCountry() == null) {
			log.error("Country was null");
			throw new ValidationException("Country must not be null");
		}
		if (address.getCity().length() == 0) {
			log.warn("Address city was empty string");
			throw new ValidationException(
					"A city of an address must not be the empty string.");
		}
		if (address.getCountry().length() == 0) {
			log.warn("Address country was empty string");
			throw new ValidationException(
					"A country of an address must not be the empty string.");
		}
		if (address.getPostalCode().length() == 0) {
			log.warn("Address postcode was empty string");
			throw new ValidationException(
					"A postcode of an address must not be the empty string.");
		}
		if (address.getStreet().length() == 0) {
			log.warn("Address street was empty string");
			throw new ValidationException(
					"A street of an address must not be the empty string.");
		}
		// check for postcode - Austria matching:
		try {
			Province.getFromAddress(address);
			// if p is OTHER it means we are not in Austria. okay then.
		} catch (IllegalStateException e) {
			log.warn("Address's postcode " + address.getPostalCode()
					+ " is not applicable for country " + address.getCountry());
			throw new ValidationException("When the address's country is "
					+ address.getCountry() + ", a postcode of "
					+ address.getPostalCode() + " is illegal.", e);
		}

		// profile country name:
		if (address.getCountry().length() < 3) {
			String msg = "Address's country "
					+ address.getCountry()
					+ " is shorter than 3 characters. Obviously, this is not a country name.";
			log.warn(msg);
			throw new ValidationException(msg);
		}
		if (!Character.isUpperCase(address.getCountry().codePointAt(0))) {
			String msg = "Address's country "
					+ address.getCountry()
					+ " does not start with an upper case character. Obviously, this is not a country name.";
			log.warn(msg);
			throw new ValidationException(msg);
		}
		for (String s : address.getCountry().split("\\ ")) {
			for (char c : s.substring(1, s.length() - 1).toCharArray()) {
				if (Character.isUpperCase(c)) {
					String msg = "Address's country "
							+ address.getCountry()
							+ " includes the upper case character \'"
							+ c
							+ "\' in the middle of a word. Obviously, this is not a country name.";
					log.warn(msg);
					throw new ValidationException(msg);
				}
			}
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Address create(Address a) throws ServiceException {
		try {
			validate(a);
			addressDAO.insertOrUpdate(a);
			return a;
		} catch (ValidationException | PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public Address update(Address a) throws ServiceException {
		try {
			validate(a);
			addressDAO.insertOrUpdate(a);
			return a;
		} catch (ValidationException | PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void delete(Address a) throws ServiceException {
		try {
			addressDAO.delete(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public List<Address> getAll() throws ServiceException {
		try {
			return addressDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Address getByID(int id) throws ServiceException {
		try {
			return addressDAO.getByID(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String convertToCSV(List<Address> addresses) {
		if (addresses == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		StringWriter stringWriter = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(stringWriter, ';');
		csvWriter
				.writeNext(new String[] { "Stra\u00dfe", "PLZ", "Ort", "Land" });

		for (Address a : addresses) {
			csvWriter.writeNext(new String[] { nullSafeToString(a.getStreet()),
					nullSafeToString(a.getPostalCode()), nullSafeToString(a.getCity()), 
					nullSafeToString(a.getCountry()) });
		}
		try {
			csvWriter.close();
		} catch (IOException e) {
			log.warn("CSV writer on StringWriter could not be closed", e);
		}
		stringWriter.flush();
		return stringWriter.getBuffer().toString();
	}
	
	private String nullSafeToString(Object obj) {
		return obj==null?"n.v.":obj.toString();
	}

	@Override
	public void saveAsCSV(List<Address> addresses, File csvFile)
			throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(csvFile);
			writer.write(convertToCSV(addresses));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			log.warn(
					"CSV data could not be written to "
							+ csvFile.getAbsolutePath(), e);
			throw e;
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public List<Address> getByFilter(Filter filter) throws ServiceException {
		try {
			return addressDAO.getByFilter(filter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}
}

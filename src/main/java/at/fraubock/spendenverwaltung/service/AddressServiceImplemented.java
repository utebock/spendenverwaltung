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
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
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

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Address create(Address a) throws ServiceException {
		try {
			addressDAO.insertOrUpdate(a);
			return a;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Address update(Address a) throws ServiceException {
		try {
			addressDAO.insertOrUpdate(a);
			return a;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void delete(Address a) throws ServiceException {
		try {
			addressDAO.delete(a);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Address> getAll() throws ServiceException {
		try {
			return addressDAO.getAll();
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true)
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
			csvWriter.writeNext(new String[] { a.getStreet(),
					a.getPostalCode(), a.getCity(), a.getCountry() });
		}
		try {
			csvWriter.close();
		} catch (IOException e) {
			log.warn("CSV writer on StringWriter could not be closed", e);
		}
		stringWriter.flush();
		return stringWriter.getBuffer().toString();
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
}

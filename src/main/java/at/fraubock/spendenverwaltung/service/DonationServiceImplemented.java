package at.fraubock.spendenverwaltung.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;

/**
 * implementation of {@link IAddressService}
 * 
 * @author Thomas
 * 
 */
public class DonationServiceImplemented implements IDonationService {

	private static final Logger log = Logger
			.getLogger(DonationServiceImplemented.class);

	private IDonationDAO donationDAO;

	public IDonationDAO getDonationDAO() {
		return donationDAO;
	}

	public void setDonationDAO(IDonationDAO donationDAO) {
		this.donationDAO = donationDAO;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Donation create(Donation d) throws ServiceException {
		try {
			donationDAO.insertOrUpdate(d);
			return d;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Donation update(Donation d) throws ServiceException {
		try {
			donationDAO.insertOrUpdate(d);
			return d;
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public void delete(Donation d) throws ServiceException {
		try {
			donationDAO.delete(d);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public Donation getByID(int id) throws ServiceException {
		try {
			return donationDAO.getByID(id);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public List<Donation> getByPerson(Person p) throws ServiceException {
		try {
			return donationDAO.getByPerson(p);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public List<Donation> getUnconfirmed(Import toImport)
			throws ServiceException {
		try {
			return donationDAO.getUnconfirmed(toImport);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String[] getDonationTypes() {
		DonationType[] donationTypes = Donation.DonationType.values();
		String[] stringDonationTypes = new String[donationTypes.length];
		int counter = 0;

		for (DonationType type : donationTypes)
			stringDonationTypes[counter++] = type.toString();

		return stringDonationTypes;
	}

	@Override
	public DonationType getDonationTypeByIndex(int index) {
		return Donation.DonationType.values()[index];
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public List<Donation> getByFilter(Filter filter) throws ServiceException {
		try {
			return donationDAO.getByFilter(filter);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String convertToCSV(List<Donation> donations) {
		if (donations == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}

		String csv = "Betrag in \u20AC;Datum;Widmung;Art;Notiz;Vorname;Nachname;E-Mail;Unternehmen;Land;Stadt;PLZ;Strasse\n";

		for (Donation d : donations) {
			NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
			csv += nf.format((d.getAmount()/100D)) + ";";
			csv += new SimpleDateFormat("dd.MM.yyyy").format(d.getDate()) + ";";
			csv += d.getDedication() + ";";
			csv += d.getType() + ";";
			csv += d.getNote() + ";";

			String nA = "n.v.";
			Person p = d.getDonator();
			Address a = p.getMainAddress();

			csv += (p == null ? nA : p.getGivenName()) + ";";
			csv += (p == null ? nA : p.getSurname()) + ";";
			csv += (p == null ? nA : p.getEmail()) + ";";
			csv += (p == null ? nA : p.getCompany()) + ";";

			csv += (a == null ? nA : a.getCountry()) + ";";
			csv += (a == null ? nA : a.getCity()) + ";";
			csv += (a == null ? nA : a.getPostalCode()) + ";";
			csv += (a == null ? nA : a.getStreet()) + ";\n";
		}
		return csv;
	}

	@Override
	@Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public void setImportToNull(List<Donation> donationList)
			throws ServiceException {
		try {
			donationDAO.setImportToNull(donationList);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
	public boolean donationExists(Donation donation) throws ServiceException {
		try {
			return donationDAO.donationExists(donation);
		} catch (PersistenceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveAsCSV(List<Donation> donations, File csvFile)
			throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(csvFile);
			writer.write(convertToCSV(donations));
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

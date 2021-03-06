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
package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * service providing functionality for {@link Donation}
 * 
 * @author Thomas
 * 
 */
public interface IDonationService {

	/**
	 * Creates a new donation
	 * 
	 * @param d
	 *            Donation to be created
	 * @return Fully defined donation
	 */
	public Donation create(Donation d) throws ServiceException;

	/**
	 * Updates an existing donation
	 * 
	 * @param d
	 *            Donation to be updated
	 * @return Updated donation
	 */
	public Donation update(Donation d) throws ServiceException;

	/**
	 * Deletes an existing donation
	 * 
	 * @param d
	 *            Donation to be deleted
	 */
	public void delete(Donation d) throws ServiceException;

	/**
	 * Retrieves Donation by ID
	 * 
	 * @param id
	 *            unique donation identification number
	 * @return Donation based on given id or NULL if id non existent
	 */
	public Donation getByID(int id) throws ServiceException;

	/**
	 * Retrieves entire Donation table
	 * 
	 * @return List of all donations
	 */
	public List<Donation> getByPerson(Person p) throws ServiceException;

	/**
	 * Retrieves all unconfirmed donations
	 * 
	 * @return List of all unconfirmed donations
	 */
	public List<Donation> getUnconfirmed(Import toImport)
			throws ServiceException;

	/**
	 * Puts all Donation Types in a String Array
	 * 
	 * @return Returns a String Array including all Donation Types
	 */
	public String[] getDonationTypes();

	/**
	 * Returns the DonationType by a given index
	 * 
	 * @param index
	 *            Index of the selected Donationtype
	 * @return DonationType with index "index"
	 */
	public DonationType getDonationTypeByIndex(int index);

	/**
	 * Retrieves confirmed donations matching the given filter
	 * 
	 * @param filter
	 *            the filter to be used
	 * @return a list of all those confirmed donations matching the given
	 *         filter, sorted by id descending, including their donators
	 * @throws PersistenceException
	 *             if communication to the underlying persistence system failed
	 */
	public List<Donation> getByFilter(Filter filter) throws ServiceException;

	/**
	 * converts a list of donations to a CSV string.
	 * 
	 * @param donations
	 * @return CSV representation of the donations
	 */
	public String convertToCSV(List<Donation> donations);

	/**
	 * Converts a list of donations to a CSV and saves it as the given file.
	 * 
	 * @author manuel-bichler
	 * 
	 * @param donations
	 *            a list of donations
	 * @param csvFile
	 *            the file the CSV should be saved to
	 * @throws IOException
	 *             if writing the file failed
	 */
	public void saveAsCSV(List<Donation> donations, File csvFile)
			throws IOException;

	/**
	 * For the donations in the donation list the import attribute will be
	 * updated to null
	 * 
	 * @param donationList
	 *            List of donations which should be affected
	 * @throws PersistenceException
	 *             If communication to the underlying persistence system failed
	 */
	public void setImportToNull(List<Donation> donationList)
			throws ServiceException;

	/**
	 * check if donation d exists already
	 * 
	 * @param d
	 *            Donation which should be proofed
	 * @return true, is donation already exists
	 * @throws PersistenceException
	 */
	public boolean donationExists(Donation d) throws ServiceException;
}

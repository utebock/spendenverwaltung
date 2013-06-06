package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface IImportService {

	/**
	 * Imports donations with persons, addresses into the database. These
	 * imports need to be validated to show up as confirmed donations. The
	 * column mapping is based on {@link native_import_config.properties}
	 * 
	 * @param file
	 *            The CSV file
	 * @throws ServiceException
	 */
	public void nativeImport(File file) throws ServiceException;

	/**
	 * Imports donations with persons and addresses into the database. These
	 * imports need to be validated to show up as confirmed donations. The
	 * column mapping is based on the Hypo CSV structure.
	 * 
	 * @param file
	 *            the CSV file in Hypo structure
	 * @throws ServiceException
	 */
	public void hypoImport(File file) throws ServiceException;
}

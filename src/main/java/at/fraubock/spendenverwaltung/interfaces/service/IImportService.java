package at.fraubock.spendenverwaltung.interfaces.service;

import java.io.File;
import java.util.Map;

import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public interface IImportService {

	/**
	 * Imports donations with persons, addresses into the database. 
	 * These imports need to be validated to show up as confirmed donations.
	 * The column mapping is based on {@link native_import_config.properties} 
	 * @param file
	 * 			The CSV file
	 * @return 
	 * 			returns the amount of errors during import
	 * @throws ServiceException
	 */
	public int nativeImport(File file) throws ServiceException;
	
	/**
	 * Reads a mapping config file and returns a Map<String, String> with the mapping
	 * @param configName
	 * 			name of config file in resource folder
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, String> readMappingConfig(String configName) throws ServiceException;
	
	
	/**
	 * Creates a new Import
	 * Creator will be overwritten with sql username
	 * @param i
	 * 		Import
	 * @return
	 * @throws ServiceException
	 */
	public Import createImport(Import i) throws ServiceException;
	
}

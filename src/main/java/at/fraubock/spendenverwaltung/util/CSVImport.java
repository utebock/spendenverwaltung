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
package at.fraubock.spendenverwaltung.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.csvimport.ImportRow;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * 
 * @author Roman
 * @author manuel-bichler
 * 
 */
public class CSVImport {

	private static final Logger log = Logger.getLogger(CSVImport.class);

	public CSVImport() {
	}

	/**
	 * Reads the given CSV file and returns a list of string arrays, which
	 * represent each row. Uses the default seperator, quotechar and escapechar.
	 * 
	 * @param file
	 *            CSV file
	 * @return
	 */
	public static List<String[]> ReadCSV(File file) throws IOException {
		return readCSV(file, ';', '"', '\\');
	}

	/**
	 * Reads the given CSV file and returns a list of string arrays, which
	 * represent each row.
	 * 
	 * @param file
	 *            CSV file
	 * @param seperator
	 * @param quotechar
	 * @param escape
	 * @return List of string arrays
	 */
	public static List<String[]> readCSV(File file, char seperator,
			char quotechar, char escape) throws IOException {
		CSVReader reader;
		List<String[]> data = null;

		log.debug("Read CSV into List<String[]> " + file + " with seperator "
				+ seperator + ", quotechar " + quotechar + "and escapechar "
				+ escape);

		reader = new CSVReader(new FileReader(file), seperator, quotechar,
				escape);

		data = reader.readAll();

		reader.close();

		return data;
	}

	/**
	 * Reads the given CSV file and maps the values into a list of
	 * {@link ImportRow}
	 * 
	 * @param file
	 *            CSV file
	 * @param columnMapping
	 *            a Map<String, String> with column name to {@link ImportRow}
	 *            variable name
	 * @return returns a List of {@link ImportRow}
	 * @throws IOException
	 */
	public static List<ImportRow> readCSVWithMapping(File file,
			Map<String, String> columnMapping) throws IOException {
		return readCSVWithMapping(file, ';', '"', '\\', columnMapping);
	}

	/**
	 * Reads the given CSV file and maps the values into a list of
	 * {@link ImportRow}
	 * 
	 * @param file
	 *            CSV file
	 * @param seperator
	 *            seperator character
	 * @param quotechar
	 *            quote character
	 * @param escape
	 *            escape character
	 * @param columnMapping
	 *            a Map<String, String> with column name to {@link ImportRow}
	 *            variable name
	 * @return returns a List of {@link ImportRow}
	 * @throws IOException
	 */
	public static List<ImportRow> readCSVWithMapping(File file, char seperator,
			char quotechar, char escape, Map<String, String> columnMapping)
			throws IOException {
		List<ImportRow> importRows;

		log.debug("Read CSV into List<ImportRow> " + file + " with seperator "
				+ seperator + ", quotechar " + quotechar + ", escapechar "
				+ escape + " and mapping: " + columnMapping);

		CsvToBean<ImportRow> bean = new CsvToBean<ImportRow>();

		// Init MappingStrategy
		HeaderColumnNameTranslateMappingStrategy<ImportRow> strategy = new HeaderColumnNameTranslateMappingStrategy<ImportRow>();

		// Set Type and columnMapping
		strategy.setType(ImportRow.class);
		strategy.setColumnMapping(columnMapping);

		// Parse CSV with MappingStrategy
		importRows = bean.parse(strategy, new CSVReader(new FileReader(file),
				seperator, quotechar, escape));

		return importRows;
	}

	/**
	 * @param csvFile
	 *            the CSV file in hypo structure
	 * @return a list of rows where the donation dates, the donation type and
	 *         the amounts are set. The donation note is set to "Umsatztext".
	 * @throws IOException
	 *             if reading the file fails
	 * @throws ValidationException
	 *             if the given file has no hypo CSV structure
	 */
	public static List<ImportRow> readHypoCsv(File csvFile) throws IOException,
			ValidationException {
		CSVReader reader = new CSVReader(new FileReader(csvFile), ';');
		List<String[]> lines = reader.readAll();
		reader.close();
		// validate that first row is header
		if (!validateHypoHeader(lines.get(0)))
			throw new ValidationException(
					"Header of given CSV file does not match Hypo structure");

		ArrayList<String[]> dataLines = new ArrayList<String[]>(lines);
		dataLines.remove(0); // remove header line
		ArrayList<ImportRow> beans = new ArrayList<ImportRow>(dataLines.size());
		for (String[] line : dataLines) {
			if (line[7].charAt(0) == '-')
				continue;
			ImportRow row = new ImportRow();
			row.setAmount(line[7]);
			row.setType("bank transfer");
			row.setDate(line[2]); // "Buchungsdatum"
			row.setDonationNote(line[9] + " " + line[10] + " " + line[11] + " "
					+ line[12]); // Text
			beans.add(row);
		}
		return beans;
	}

	/**
	 * @param headerLine
	 *            an array of strings that form the actual CSV header line
	 * @return true if the header line matches the Hypo CSV header format, false
	 *         otherwise
	 */
	private static boolean validateHypoHeader(String[] headerLine) {
		if (headerLine.length != 14)
			return false;
		if (!"Kontonummer".equals(headerLine[0]))
			return false;
		if (!"Auszugsnummer".equals(headerLine[1]))
			return false;
		if (!"Buchungsdatum".equals(headerLine[2]))
			return false;
		if (!"Valutadatum".equals(headerLine[3]))
			return false;
		if (!"Umsatzzeit".equals(headerLine[4]))
			return false;
		if (!"Zahlungsreferenz".equals(headerLine[5]))
			return false;
		if (!"Waehrung".equals(headerLine[6]))
			return false;
		if (!"Betrag".equals(headerLine[7]))
			return false;
		if (!"Buchungstext".equals(headerLine[8]))
			return false;
		if (!"Umsatztext".equals(headerLine[9]))
			return false;
		if (!"".equals(headerLine[10]))
			return false;
		if (!"".equals(headerLine[11]))
			return false;
		if (!"".equals(headerLine[12]))
			return false;
		if (!"".equals(headerLine[13]))
			return false;
		return true;
	}
	
	/**
	 * @param csvFile
	 *            the CSV file in SMS structure
	 * @return a list of rows where the donation dates, the donation type and
	 *         the amounts are set.
	 * @throws IOException
	 *             if reading the file fails
	 */
	public static List<ImportRow> readSmsCsv(File csvFile) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(csvFile), ';');
		List<String[]> lines = reader.readAll();
		reader.close();
		
		ArrayList<String[]> dataLines = new ArrayList<String[]>(lines);
		dataLines.remove(0); // remove header line
		ArrayList<ImportRow> beans = new ArrayList<ImportRow>(dataLines.size());
		for (String[] line : dataLines) {
			if(line[3].equals("Spende erfolgreich")){
				if (line[2].charAt(0) == '-'){
					continue;
				}
				ImportRow row = new ImportRow();
				row.setAmount(line[2]);
				row.setType("sms");
				row.setDate(line[1]); // "Buchungsdatum"
				beans.add(row);
			}
		}
		return beans;
	}
}

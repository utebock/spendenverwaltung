package at.fraubock.spendenverwaltung.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.csvimport.ImportRow;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;


public class CSVImport {

	private static final Logger log = Logger.getLogger(CSVImport.class);
	
	public CSVImport(){
	}
	
	/**
	 * Reads the given CSV file and returns a list of string arrays, 
	 * which represent each row. Uses the default seperator, quotechar and escapechar.
	 * @param file
	 * 			path to CSV file
	 * @return
	 */
	public static List<String[]> ReadCSV(String file) throws IOException{
		return ReadCSV(file, ';', '"', '\\');
	}
	
	/**
	 * Reads the given CSV file and returns a list of string arrays, 
	 * which represent each row. 
	 * @param file
	 * 			path to CSV file
	 * @param seperator
	 * @param quotechar
	 * @param escape
	 * @return
	 * 			List of string arrays 
	 */
	public static List<String[]> ReadCSV(String file, char seperator, char quotechar, char escape) throws IOException{
		CSVReader reader;
		List<String[]> data = null;
		
		log.debug("Read CSV into List<String[]> "+file+" with seperator "+seperator+", quotechar "+
					quotechar+ "and escapechar "+escape);
		
		reader = new CSVReader(new FileReader(file),seperator, quotechar, escape);
		
		data = reader.readAll();
		
		reader.close();

		return data;
	}
	
	
	/**
	 * Reads the given CSV file and maps the values into a list of {@link ImportRow}
	 * @param file
	 * 			path to CSV file
	 * @param columnMapping
	 * 			a Map<String, String> with column name to {@link ImportRow} variable name
	 * @return
	 * 			returns a List of {@link ImportRow} 
	 * @throws IOException
	 */
	public static List<ImportRow> ReadCSVWithMapping(String file, Map<String, String> columnMapping) throws IOException{
		return ReadCSVWithMapping(file, ';', '"', '\\', columnMapping);
	}
	
	/**
	 * Reads the given CSV file and maps the values into a list of {@link ImportRow}
	 * @param file
	 * 			path to CSV file
	 * @param seperator
	 * 			seperator character
	 * @param quotechar
	 * 			quote character
	 * @param escape
	 * 			escape character
	 * @param columnMapping
	 * 			a Map<String, String> with column name to {@link ImportRow} variable name
	 * @return
	 * 			returns a List of {@link ImportRow} 
	 * @throws IOException
	 */
	public static List<ImportRow> ReadCSVWithMapping(String file, char seperator, 
			char quotechar, char escape, Map<String, String> columnMapping) throws IOException{
		List<ImportRow> importRows;
		
		log.debug("Read CSV into List<ImportRow> "+file+" with seperator "+seperator+", quotechar "+
				quotechar+ ", escapechar "+escape+" and mapping: "+columnMapping);
		
		CsvToBean<ImportRow> bean = new CsvToBean<ImportRow>();
		
		//Init MappingStrategy
		HeaderColumnNameTranslateMappingStrategy<ImportRow> strategy = 
				new HeaderColumnNameTranslateMappingStrategy<ImportRow>();
		
		//Set Type and columnMapping
		strategy.setType(ImportRow.class);
		strategy.setColumnMapping(columnMapping);
		
		//Parse CSV with MappingStrategy
		importRows = bean.parse(strategy, new CSVReader(new FileReader(file), seperator, quotechar, escape));
		
		return importRows;
	}
}

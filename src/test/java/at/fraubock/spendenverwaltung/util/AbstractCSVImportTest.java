package at.fraubock.spendenverwaltung.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Test;

import at.fraubock.spendenverwaltung.gui.App;
import at.fraubock.spendenverwaltung.interfaces.domain.csvimport.ImportRow;

public abstract class AbstractCSVImportTest {

	@Test(expected = FileNotFoundException.class)
	public void fileNotFound() throws IOException{
		CSVImport.ReadCSV(new File(""));
	}
	
	@Test
	public void importHypoCSVExample() throws IOException {
		List<String[]> data = null;
	
		data = CSVImport.ReadCSV(new File("src/test/resources/hypo_export.csv"));
		
		assertTrue(data.size() == 4);
		assertEquals(data.get(0)[0], "Kontonummer");
		assertEquals(data.get(1)[2], "16.04.2013");
		assertEquals(data.get(2)[6], "EUR");
		assertEquals(data.get(3)[7], "50");
	}
	
	@Test
	public void importHypo() throws IOException{
		Map<String, String> columnMapping = new HashMap<String, String>();
		columnMapping.put("Buchungsdatum", "date");
		columnMapping.put("Betrag", "amount");
		columnMapping.put("Umsatztext", "surname");
		
		List<ImportRow> importRows = CSVImport.readCSVWithMapping(new File("src/test/resources/hypo_export.csv"), columnMapping);
		
		assertTrue(importRows.size() == 3);
		assertEquals(importRows.get(0).getAmount(),"10");
		assertEquals(importRows.get(1).getDate(), "16.04.2013");
		assertEquals(importRows.get(2).getSurname(),"Dipl.-Ing. Michael Milch");
	}
	
	@Test
	public void importHypoWithConfig() throws IOException{
		Map<String, String> columnMapping = new HashMap<String, String>();
		
		Properties config = new Properties();
		config.load(App.class.getClassLoader().getResourceAsStream("hypo_import_config.properties"));
		
		for(Entry<Object, Object> entry : config.entrySet()){
			if(String.valueOf(entry.getValue()).length()>0){
				columnMapping.put(String.valueOf(entry.getValue()), String.valueOf(entry.getKey()));
			}
		}
		
		List<ImportRow> importRows = CSVImport.readCSVWithMapping(new File("src/test/resources/hypo_export.csv"), columnMapping);
	
		assertTrue(importRows.size() == 3);
		assertEquals(importRows.get(0).getAmount(),"10");
		assertEquals(importRows.get(1).getDate(), "16.04.2013");
		assertEquals(importRows.get(2).getSurname(),"Dipl.-Ing. Michael Milch");
	}
	
	@Test
	public void importSMSSpendenWithConfig() throws IOException{
		Map<String, String> columnMapping = new HashMap<String, String>();
		
		Properties config = new Properties();
		config.load(App.class.getClassLoader().getResourceAsStream("sms_import_config.properties"));
		
		for(Entry<Object, Object> entry : config.entrySet()){
			if(String.valueOf(entry.getValue()).length()>0){
				columnMapping.put(String.valueOf(entry.getValue()), String.valueOf(entry.getKey()));
			}
		}
		
		List<ImportRow> importRows = CSVImport.readCSVWithMapping(new File("src/test/resources/sms_spenden_export.csv"), columnMapping);
		
		assertTrue(importRows.size() == 7);
		assertEquals(importRows.get(0).getTelephone(), "436761234567");
		assertEquals(importRows.get(1).getDate(), "06.02.2013 15:38");
		assertEquals(importRows.get(2).getAmount(), "10.00");
		assertEquals(importRows.get(3).getDedication(), "keine Antwort");
		assertEquals(importRows.get(4).getDonationNote(), "FACEBOOK");
		assertEquals(importRows.get(5).getPersonNote(), "NEIN");
	}

}

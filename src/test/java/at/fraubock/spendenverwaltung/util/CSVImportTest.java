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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import at.fraubock.spendenverwaltung.interfaces.domain.csvimport.ImportRow;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;

public class CSVImportTest extends AbstractCSVImportTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void hypoImport() throws Exception {
		List<ImportRow> rows = CSVImport.readHypoCsv(new ClassPathResource(
				"hypo_export.csv").getFile());
		assertEquals(3, rows.size());
		ImportRow row1 = new ImportRow();
		row1.setDate("16.04.2013");
		row1.setAmount("10");
		row1.setDonationNote("Huber Sabine  Spende Wohnprojekt REF: 18600130415U00010769966D003 ");
		row1.setType("bank transfer");
		ImportRow row2 = new ImportRow();
		row2.setDate("16.04.2013");
		row2.setAmount("15");
		row2.setDonationNote("Brigitte Maier 4863 Breitendorf 1 Spende REF: 201111304152AB3-DP1002005994");
		row2.setType("bank transfer");
		ImportRow row3 = new ImportRow();
		row3.setDate("15.04.2013");
		row3.setAmount("50");
		row3.setDonationNote("Dipl.-Ing. Michael Milch Spende REF: 203151304122AIG-103615351666 ");
		row3.setType("bank transfer");
		assertEquals(row1, rows.get(0));
		assertEquals(row2, rows.get(1));
		assertEquals(row3, rows.get(2));
	}

	@Test(expected = ValidationException.class)
	public void hypoImportWithWrongFile_shouldThrow() throws Exception {
		CSVImport.readHypoCsv(new ClassPathResource("native.csv").getFile());
	}

}

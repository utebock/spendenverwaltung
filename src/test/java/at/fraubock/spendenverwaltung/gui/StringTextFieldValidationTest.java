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
package at.fraubock.spendenverwaltung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;

public class StringTextFieldValidationTest {

	private StringTextField field;
	
	@Before
	public void initialize() {
		field = new StringTextField(ComponentConstants.SHORT_TEXT);	
	}
	
	@Test 
	public void validateNull() {
		field.setText(null);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateInvalidNull() {
		field = new StringTextField(ComponentConstants.SHORT_TEXT, false);
		
		field.setText(null);
		
		assertFalse(field.validateContents());
	}
	
	@Test
	public void validateLengthTooLong() {
		String test = "";
		
		for(int i=0; i<31; i++){
			test += "1";
		}
		
		assertEquals(31, test.length());
		
		field.setText(test);
		
		assertFalse(field.validateContents());
	} 
	
	@Test
	public void validateLength() {
		String test = "";
		
		for(int i=0; i<30; i++){
			test += "1";
		}
		
		field.setText(test);
		
		assertTrue(field.validateContents());
	}
}

package at.fraubock.spendenverwaltung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.gui.components.AlphaNumericTextField;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;

public class AlphaNumericTextFieldValidationTest {

	private AlphaNumericTextField field;
	
	@Before
	public void initialize() {
		field = new AlphaNumericTextField(ComponentConstants.SHORT_TEXT);	
	}
	
	public void validateValidAlphaNumericString() {
		field.setText("aaalskdjlkd");
		
		assertTrue(field.validateContents());
	}
	
	public void validateValidAlphaNumericString2() {
		field.setText("33333");
		
		assertTrue(field.validateContents());
	}
	
	public void validateValidAlphaNumericString3() {
		field.setText("a1b2c3");
		
		assertTrue(field.validateContents());
	}
	
	public void validateInvalidAlphaNumericString() {
		field.setText("abc$11");
		
		assertFalse(field.validateContents());
	}
	
	public void validateInvalidAlphaNumericString2() {
		field.setText("abcä11");
		
		assertFalse(field.validateContents());
	}
	
	public void validateInvalidAlphaNumericString3() {
		field.setText("abc_a11");
		
		assertFalse(field.validateContents());
	}
	
	@Test 
	public void validateNull() {
		field.setText(null);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateInvalidNull() {
		field = new AlphaNumericTextField(ComponentConstants.SHORT_TEXT, false);
		
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

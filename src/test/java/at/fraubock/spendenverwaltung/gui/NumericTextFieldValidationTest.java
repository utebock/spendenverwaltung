package at.fraubock.spendenverwaltung.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;

public class NumericTextFieldValidationTest {

	private NumericTextField field;
	private static char separator = DecimalFormatSymbols.getInstance(Locale.GERMAN).getDecimalSeparator();
	
	@Before
	public void initialize() {
		field = new NumericTextField(ComponentConstants.SHORT_TEXT);	
	}
	
	@Test 
	public void validateValidNumber() {
		field.setText("0"+separator+"22");
		
		assertTrue(field.validateContents());
	}
	
	@Test 
	public void validateValidNumber2() {
		field.setText("1");
		
		assertTrue(field.validateContents());
	}
	
	@Test 
	public void validateValidNumber3() {
		field.setText("1"+separator+"1");
		
		assertTrue(field.validateContents());
	}
	
	@Test 
	public void validateValidNumber4() {
		field.setText("100000000000"+separator+"12");
		
		assertTrue(field.validateContents());
	}
	
	@Test 
	public void validateInvalidNumber() {
		field.setText("1000"+separator+"123");
		
		assertFalse(field.validateContents());
	}
	
	@Test 
	public void validateInvalidNumber2() {
		field.setText("-1"+separator+"11");
		
		assertFalse(field.validateContents());
	}
	
	@Test 
	public void validateInvalidNumber3() {
		field.setText("a1"+separator+"11");
		
		assertFalse(field.validateContents());
	}
	
	@Test 
	public void validateInvalidNumber4() {
		field.setText("11"+separator+"1a");
		
		assertFalse(field.validateContents());
	}
	
	
	@Test 
	public void validateNull() {
		field.setText(null);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateInvalidNull() {
		field = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
		
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
	
	@Test
	public void hundredthsOkay1() {
		field.setText("20"+separator+"14");
		assertTrue(field.validateContents());
		assertEquals(2014, field.getHundredths());
	}
	
	@Test
	public void hundredthsOkay2() {
		field.setText("144");
		assertTrue(field.validateContents());
		assertEquals(14400, field.getHundredths());
	}
	
	
}

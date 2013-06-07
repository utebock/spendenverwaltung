package at.fraubock.spendenverwaltung.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.EmailTextField;

public class EmailTextFieldValidationTest {

	private EmailTextField field;
	
	@Before
	public void initialize() {
		field = new EmailTextField(ComponentConstants.MEDIUM_TEXT);
	}
	
	@Test
	public void validateValidEmail() {
		String validEmail = "0708104@gmail.com";
	
		field.setText(validEmail);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateValidEmail2() {
		String validEmail = "070810_4@gmail.com";
	
		field.setText(validEmail);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateValidEmail3() {
		String validEmail = "0708104+lol@gmail.com";
	
		field.setText(validEmail);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateValidEmail4() {
		String invalidEmail = "0708104@tuwien.ac.at.ad.az";
	
		field.setText(invalidEmail);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateValidEmail5() {
		String invalidEmail = "+0708104@tuwien.ac.at.ad.az";
	
		field.setText(invalidEmail);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateInvalidEmail6() {
		String invalidEmail = "+07.081+0+4@tuwien.ac.at.ad.az";
	
		field.setText(invalidEmail);
		
		assertFalse(field.validateContents());
	}
	
	@Test
	public void validateIninvalidEmail2() {
		String invalidEmail = "0708104@gmail";
	
		field.setText(invalidEmail);
		
		assertFalse(field.validateContents());
	}
	
	@Test
	public void validateIninvalidEmail3() {
		String invalidEmail = "070810 4@gmail.com";
	
		field.setText(invalidEmail);
		
		assertFalse(field.validateContents());
	}
	
	@Test
	public void validateIninvalidEmail4() {
		String invalidEmail = "070810$4@gmail.com";
	
		field.setText(invalidEmail);
		
		assertFalse(field.validateContents());
	}
	
	@Test 
	public void validateNull() {
		field.setText(null);
		
		assertTrue(field.validateContents());
	}
	
	@Test
	public void validateInvalidNull() {
		field = new EmailTextField(ComponentConstants.MEDIUM_TEXT, false);
		
		field.setText(null);
		
		assertFalse(field.validateContents());
	}
	
	@Test
	public void validateLengthTooLong() {
		String test = "";
		
		for(int i=0; i<121; i++){
			test += "a";
		}
		
		test = test.substring(0, test.length() - "@gmail.com".length());
		assertEquals(111, test.length());
		
		test += "@gmail.com";
		
		assertEquals(121, test.length());
		
		field.setText(test);
		
		assertFalse(field.validateContents());
	} 
	
	@Test
	public void validateLength() {
		String test = "";
		
		for(int i=0; i<120; i++){
			test += "a";
		}
		test = test.substring(0, test.length() - "@gmail.com".length());
		assertEquals(110, test.length());
		
		test += "@gmail.com";
		
		assertEquals(120, test.length());
		
		field.setText(test);
		
		assertTrue(field.validateContents());
	} 
}

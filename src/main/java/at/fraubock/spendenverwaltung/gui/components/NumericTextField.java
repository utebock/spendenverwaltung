package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

public class NumericTextField implements ValidateableComponent {

	private CustomTextField field;
	
	public NumericTextField(ComponentConstants length) {
		field = new CustomTextField(length.getValue(length));
	}
	
	//validation allows for 2 decimals after decimal point
	@Override
	public boolean validateContents() {
		  if(!field.getText().matches("\\d+(\\.(\\d){1,2})?")) {
			  field.invalidateInput();
			  return false;
		  }
		  return true;
	}
	
	public void setText(String text) {
		field.setText(text);
	}
	
	public String getText() {
		return field.getText();
	}
} 

package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

public class AlphabeticTextField implements ValidateableComponent {
	
	private CustomTextField field;
	
	public AlphabeticTextField(ComponentConstants length) {
		field = new CustomTextField(length.getValue(length));
	}

	@Override
	public boolean validateContents() {
		  if(!field.getText().matches("[a-zA-Z)]*")) {
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

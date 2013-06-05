package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

public class EmailTextField implements ValidateableComponent {

	private CustomTextField field;
	
	public EmailTextField(ComponentConstants length) {
		field = new CustomTextField(length.getValue(length));
	}

	@Override
	public boolean validateContents() {
		
		//"0708104@gmail.com"
			
		  if(!field.getText().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
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

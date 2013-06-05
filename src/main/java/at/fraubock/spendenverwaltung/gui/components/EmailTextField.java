package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

/**
 * 
 * @author Chris Steele
 *
 */
public class EmailTextField extends CustomTextField implements ValidateableComponent {
	
	private static final long serialVersionUID = 1L;
	private boolean nullAllowed = true;
	private ComponentConstants length;

	public EmailTextField(ComponentConstants length) {
		super(length.getValue(length));
		this.length = length;
	}
	
	public EmailTextField(ComponentConstants length, boolean nullAllowed) {
		super(length.getValue(length));
		this.length = length;
		this.nullAllowed = nullAllowed;
	}

	@Override
	public boolean validateContents() {
		 if(getText() == null) {
			  if(!nullAllowed)
				  return false;
		 }
		 else {
			 if(getText().length() > length.getValue(length))
				 return false;
			 
			 if(!getText().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
				 invalidateInput();
				 return false;
			 }
		 }
		 return true;
	}
}

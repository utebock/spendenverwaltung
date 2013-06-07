package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

/**
 * 
 * @author Chris Steele
 * 
 * EmailTextField extends CustomTextField and implements the ValidateableComponent
 * interface, allowing for easy validation of the contents. Validation is based
 * on the regex at http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
 *
 * see tests for concrete examples
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
		 if(getText().equals("")){
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

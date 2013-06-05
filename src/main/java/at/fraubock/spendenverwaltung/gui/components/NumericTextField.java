package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

/**
 * 
 * @author Chris Steele
 *
 */
public class NumericTextField extends CustomTextField implements ValidateableComponent {

	private static final long serialVersionUID = 1L;
	private boolean nullAllowed = true;
	private ComponentConstants length;

	
	public NumericTextField(ComponentConstants length) {
		super(length.getValue(length));
	}
	
	public NumericTextField(ComponentConstants length, boolean nullAllowed) {
		super(length.getValue(length));
		this.nullAllowed = nullAllowed;
	}
	
	//validation allows for 2 decimals after decimal point
	@Override
	public boolean validateContents() {
		  if(getText() == null) {
			  if(!nullAllowed)
				  return false;
		  }
		  else {
			  if(getText().length() > length.getValue(length))
				  return false;
			  if(!getText().matches("\\d+(\\.(\\d){1,2})?")) {
				  invalidateInput();
				  return false;
			  }
		  }
		  
		  return true;
	}
} 

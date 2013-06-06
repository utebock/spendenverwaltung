package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

/**
 * 
 * @author Chris Steele
 *
 */
public class AlphabeticTextField extends CustomTextField implements ValidateableComponent {

	private static final long serialVersionUID = 1L;

	private ComponentConstants length;
	
	public AlphabeticTextField(ComponentConstants length) {
		super(length.getValue(length));
		this.length = length;
	}

	@Override
	public boolean validateContents() {
		  if(!getText().matches("[a-zA-Z]*") || getText().length() > length.getValue(length)) {
			  invalidateInput();
			  return false;
		  }
		  return true;
	}
	
}

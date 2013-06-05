package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

/**
 * 
 * @author Chris Steele
 *
 */
public class StringTextField extends CustomTextField implements ValidateableComponent {
	
	private static final long serialVersionUID = 1L;
	private boolean nullAllowed = true;
	private ComponentConstants length;
	
	public StringTextField(ComponentConstants length) {
		super(length.getValue(length));
	}
	
	public StringTextField(ComponentConstants length, boolean nullAllowed) {
		super(length.getValue(length));
		this.nullAllowed = nullAllowed;
	}

	@Override
	public boolean validateContents() {
		if(getText() == null) {
			if(nullAllowed == true)
				return false;
		} else if(getText().length() > length.getValue(length)) {
			return false;
		}
		
		return true;
	}

}

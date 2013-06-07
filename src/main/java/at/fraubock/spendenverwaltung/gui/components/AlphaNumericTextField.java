package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

public class AlphaNumericTextField extends CustomTextField implements
		ValidateableComponent {
	
	private static final long serialVersionUID = 1L;
	private boolean nullAllowed = true;

	private ComponentConstants length;
	
	public AlphaNumericTextField(ComponentConstants length) {
		super(length.getValue(length));
		this.length = length;
	}
	
	public AlphaNumericTextField(ComponentConstants length, boolean nullAllowed) {
		super(length.getValue(length));
		this.length = length;
		this.nullAllowed = nullAllowed;
	}

	@Override
	public boolean validateContents() {
		if(getText().equals("")) {
			if(!nullAllowed) {
				 invalidateInput();
				 return false;
			}
		} else if(!getText().matches("[a-zA-Z0-9]*") || getText().length() > length.getValue(length)) {
			  invalidateInput();
			  return false;
		}
		return true;
	}
}

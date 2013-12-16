/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.gui.components;

import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;

/**
 * 
 * @author Chris Steele
 * @deprecated not useful for this project, as any field might have to contain other characters...
 */
@Deprecated
public class AlphabeticTextField extends CustomTextField implements ValidateableComponent {

	private static final long serialVersionUID = 1L;
	private boolean nullAllowed = true;

	private ComponentConstants length;
	
	public AlphabeticTextField(ComponentConstants length) {
		super(length.getValue(length));
		this.length = length;
	}
	
	public AlphabeticTextField(ComponentConstants length, boolean nullAllowed) {
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
		} else if(!getText().matches("[a-zA-Z]*") || getText().length() > length.getValue(length)) {
			  invalidateInput();
			  return false;
		}
		return true;
	}
	
}

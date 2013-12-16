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

public enum ComponentConstants {
	SHORT_TEXT(30), MEDIUM_TEXT(120), LONG_TEXT(1024);
	
	private final int value;
	
	ComponentConstants(int value) {
		this.value = value;
	}

	public int getValue(ComponentConstants constant) {
		return this.value;
	}
}

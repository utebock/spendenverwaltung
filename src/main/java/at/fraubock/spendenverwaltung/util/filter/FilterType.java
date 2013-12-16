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
package at.fraubock.spendenverwaltung.util.filter;

/**
 * types of entities to be used for filtering.
 * 
 * @author philipp muhoray
 * 
 */
public enum FilterType {

	PERSON("validated_persons", "Personen"), DONATION("validated_donations",
			"Spenden"), MAILING("confirmed_mailings", "Aussendungen"), ADDRESS(
			"validated_addresses", "Adressen");

	private String type;
	private String display;

	private FilterType(String type, String display) {
		this.display = display;
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

	public String getDisplayName() {
		return display;
	}

	public static FilterType getTypeForString(String value) {
		if ("validated_persons".equals(value)) {
			return PERSON;
		} else if ("validated_donations".equals(value)) {
			return DONATION;
		} else if ("confirmed_mailings".equals(value)) {
			return MAILING;
		} else if ("validated_addresses".equals(value)) {
			return ADDRESS;
		}
		return null;
	}
}

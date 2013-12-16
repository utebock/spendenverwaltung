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
package at.fraubock.spendenverwaltung.service;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;


/**
 * checks the integrity of any {@link Address} entity
 * TODO not a satisfying solution, refactoring needed
 * (program logic implemented by exceptions, doesnt propagate errors, etc)
 * 
 * @author philipp muhoray
 * 
 */
@Deprecated
public class AddressValidator {

	private static final Logger log = Logger.getLogger(AddressValidator.class);
	
	public static void validate(Address address) {

		if (address == null) {
			log.error("Argument was null");
			throw new IllegalArgumentException("Address must not be null");
		}

		if (address.getStreet() == null) {
			log.error("Street was null");
			throw new IllegalArgumentException("Street must not be null");
		}

		if (address.getCity() == null) {
			log.error("City was null");
			throw new IllegalArgumentException("City must not be null");
		}

		if (address.getCountry() == null) {
			log.error("Country was null");
			throw new IllegalArgumentException("Country must not be null");
		}
	}
}

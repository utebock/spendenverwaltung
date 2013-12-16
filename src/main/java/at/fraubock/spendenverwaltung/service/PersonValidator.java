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

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
@Deprecated
public class PersonValidator {

	public synchronized void validate(Person person) {

		if (person == null) {
			throw new IllegalArgumentException("Person must not be null");
		}
		if (person.getId() != null && person.getId() < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}
		if (person.getSex() == null) {
			throw new IllegalArgumentException("Sex was null");
		}
		if (person.getAddresses() == null) // rather use empty list
			throw new IllegalArgumentException("Addresses was null");
		if (person.getMainAddress() != null
				&& !person.getAddresses().contains(person.getMainAddress()))
			throw new IllegalArgumentException(
					"Main address must also be present in the address list");
		if (person.getTitle() != null && person.getSurname() == null)
			throw new IllegalArgumentException(
					"Person with title must have a surname");
		if (person.getSurname() == null && person.getCompany() == null)
			throw new IllegalArgumentException(
					"Person must have at least a company or a surname");
		switch (person.getSex()) {
		case FAMILY:
		case MALE:
		case FEMALE:
			if (person.getSurname() == null)
				throw new IllegalArgumentException(
						"Non-company person must have a surname");
		default:
		}
	}
}

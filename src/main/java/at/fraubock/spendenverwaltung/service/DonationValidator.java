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

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;


public class DonationValidator {

	public synchronized void validate(Donation d) {

		if (d == null)
			throw new IllegalArgumentException("Donation must not be null");
		// donator may be null. a null donator means anonymous donation.
		if (d.getAmount() < 0)
			throw new IllegalArgumentException("Amount must not be less than 0");
		if (d.getDate() == null)
			throw new IllegalArgumentException("Date must not be null");
		if (d.getType() == null)
			throw new IllegalArgumentException("Type must not be null");
	}
}

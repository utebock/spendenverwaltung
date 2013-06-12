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

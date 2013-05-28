package at.fraubock.spendenverwaltung.service;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;

/**
 * 
 * @author Chris Steele
 *
 */

public class MailingValidator {

	public synchronized void validate(Mailing mailing) {
		if(mailing == null) {
			throw new IllegalArgumentException("Mailing was null");
		}
		
		if(mailing.getMedium() == null) {
			throw new IllegalArgumentException("Medium was null");
		}
		
		if(mailing.getId() != null) {
			if(mailing.getId() < 0) {
				throw new IllegalArgumentException("Id was negative");
			}
		}
		
		//TODO other date evaluations?
		if(mailing.getDate() == null) {
			throw new IllegalArgumentException("Date was null");
		}
		
		if(mailing.getType() == null) {
			throw new IllegalArgumentException("Type was null");
		}
		
		/**
		 * fails if the mailing filter was not set, or if the type of
		 * the mailing filter was not set, or if the type of the mailing
		 * filter was not equal to "Person"
		 */
		if(mailing.getFilter() != null) {
			if(mailing.getFilter().getType() == null) {
				throw new IllegalArgumentException("Type of filter was null");
			} else {
				if(!mailing.getFilter().getType().equals("Person")) {
					throw new IllegalArgumentException("Type of filter was not equal to Person"); 
				}
			}
		} else {
			throw new IllegalArgumentException("Filter was null");
		}
	}
}

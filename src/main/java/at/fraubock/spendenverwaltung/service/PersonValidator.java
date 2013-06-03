package at.fraubock.spendenverwaltung.service;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;

public class PersonValidator {

	public synchronized void validate(Person person) {

		if (person == null) {
			throw new IllegalArgumentException("Person must not be null");
		}
		if (person.getId() != null && person.getId() < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}
		if(person.getSex() == null) {
			throw new IllegalArgumentException("Sex was null");
		}
		/**
		 * TODO: define all constraints
		 * 
		 */
	}
}

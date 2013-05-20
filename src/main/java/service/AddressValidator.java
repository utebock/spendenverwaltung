package service;

import domain.Address;

/**
 * checks the integrity of any {@link Address} entity
 * 
 * @author philipp muhoray
 * 
 */
public class AddressValidator {

	public static void validate(Address address) {

		if (address == null) {
			throw new IllegalArgumentException("Address must not be null");
		}

		if (address.getStreet() == null) {
			throw new IllegalArgumentException("Street must not be null");
		}

		if (address.getCity() == null) {
			throw new IllegalArgumentException("City must not be null");
		}

		if (address.getCountry() == null) {
			throw new IllegalArgumentException("Country must not be null");
		}
	}
}

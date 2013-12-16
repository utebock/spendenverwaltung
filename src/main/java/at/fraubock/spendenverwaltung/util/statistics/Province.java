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
package at.fraubock.spendenverwaltung.util.statistics;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Enum representing the 9 Austrian provinces and an "other" province. Uses the
 * file {@value #mappingFileName} to read in postcode - province mappings. The
 * file must be of the defined structure.
 * 
 * Their natural ordering is alphabetically ascending, with {@link #OTHER} as
 * last element
 * 
 * @author manuel-bichler
 * 
 */
public enum Province {
	BURGENLAND("B"), CARINTHIA("K"), LOWER_AUSTRIA("N"), UPPER_AUSTRIA("O"), SALZBURG(
			"Sa"), STYRIA("St"), TYROL("T"), VORARLBERG("V"), VIENNA("W"), OTHER(
			"sonst");

	private static final Logger log = Logger.getLogger(Province.class);

	private static final String mappingFileName = "postcodeProvinceMappingAustria.csv";

	private String abbrev;

	private Province(String abbrev) {
		this.abbrev = abbrev;
	}

	@Override
	public String toString() {
		return abbrev;
	}

	/**
	 * @param abbrev
	 *            an Austrian province abbreviation ( legal values: W, N, B, C,
	 *            Sa, T, V, St, K)
	 * @return the province enum corresponding to the abbreviation (never
	 *         {@link #OTHER})
	 * @throws IllegalArgumentException
	 *             if the given abbreviation is none of the list defined
	 */
	public static Province getFromAbbrev(String abbrev)
			throws IllegalArgumentException {
		switch (abbrev) {
		case "W":
			return VIENNA;
		case "N":
			return LOWER_AUSTRIA;
		case "B":
			return BURGENLAND;
		case "O":
			return UPPER_AUSTRIA;
		case "Sa":
			return SALZBURG;
		case "T":
			return TYROL;
		case "V":
			return VORARLBERG;
		case "St":
			return STYRIA;
		case "K":
			return CARINTHIA;
		default:
			throw new IllegalArgumentException("\"" + abbrev
					+ "\" is not a valid abbreviation for an Austrian province");
		}
	}

	/**
	 * @param d
	 *            any donation (not null)
	 * @return the province the donator's main address is in, or {@link #OTHER}
	 *         if the donation is anonymous, the donator has no address
	 *         specified or does not live in Austria.
	 * @throws IllegalStateException
	 *             when the country is Austria, but the postcode could not be
	 *             mapped
	 */
	public static Province getFromDonation(Donation d)
			throws IllegalStateException {
		if (d.getDonator() == null)
			return OTHER;
		return getFromPerson(d.getDonator());
	}

	/**
	 * 
	 * @param pers
	 *            any person (not null)
	 * @return the province the person's main address is in, or {@link #OTHER}
	 *         if the person has no address specified or does not live in
	 *         Austria.
	 * @throws IllegalStateException
	 *             when the country is Austria, but the postcode could not be
	 *             mapped
	 */
	public static Province getFromPerson(Person pers)
			throws IllegalStateException {
		Address a = pers.getMainAddress();
		if (a == null)
			return OTHER;
		return getFromAddress(a);
	}

	/**
	 * 
	 * @param a
	 *            any address (not null)
	 * @return the province the address is in, or {@link #OTHER} if the address
	 *         is not in Austria.
	 * @throws IllegalStateException
	 *             when the country is Austria, but the postcode could not be
	 *             mapped
	 */
	public static Province getFromAddress(Address a) throws IllegalStateException{
		if (!"\u00D6sterreich".equals(a.getCountry()))

			return OTHER;
		Province p = postcodeProvinceMapping.get(a.getPostalCode());
		if (p == null) {
			String msg = "Possible constraint violation: Postal code of Address "
					+ a
					+ " is not investigateable. Either it is not a valid Austrian postcode as defined in "
					+ mappingFileName
					+ " or "
					+ mappingFileName
					+ " was not read in correctly (would have resulted in an error prior to this one)";
			log.error(msg);
			throw new IllegalStateException(msg);
		}
		return p;
	}

	/**
	 * maps 4-digit postcodes to an Austrian province.
	 */
	private static final Map<String, Province> postcodeProvinceMapping;

	static {
		Map<String, Province> ppM = null;
		try {
			log.info("Trying to read " + mappingFileName
					+ " to generate postcode - province mapping");
			CSVReader reader = new CSVReader(new InputStreamReader(
					Province.class.getClassLoader().getResourceAsStream(
							mappingFileName)), ';');
			List<String[]> lines = reader.readAll();
			reader.close();
			if (!Arrays.equals(lines.get(0),
					new String[] { "PLZ", "Bundesland" }))
				throw new IOException("wrong CSV structure. First line is "
						+ lines.get(0) + " but should be "
						+ new String[] { "PLZ", "Bundesland" });
			ArrayList<String[]> dataLines = new ArrayList<String[]>(lines);
			dataLines.remove(0); // remove header line
			ppM = new HashMap<String, Province>(dataLines.size());
			for (String[] postcodeLine : dataLines) {
				try {
					if (postcodeLine[0].length() != 4)
						throw new NumberFormatException();
					int postalCode = Integer.parseInt(postcodeLine[0]);
					if (postalCode < 1000)
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					String msg = "line in CSV file is not 4-digit above 999";
					throw new IOException(msg, e);
				}
				if (ppM.put(postcodeLine[0],
						Province.getFromAbbrev(postcodeLine[1])) != null)
					throw new IOException("Postcode " + postcodeLine[0]
							+ " is defined more than once in the CSV file");
			}
			log.info("postcode-province mapping file " + mappingFileName
					+ " successfully read, " + ppM.size()
					+ " postcodes mapped.");
		} catch (Exception e) {
			String msg = "Postcode province mapping reading failed";
			log.error(msg, e);
			ppM = null;
			throw new RuntimeException(msg, e);
		} finally {
			postcodeProvinceMapping = ppM;
		}
	}
}

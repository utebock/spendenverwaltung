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

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

/**
 * Statistical operation that may be applied on a set of numerical values
 * 
 * @author manuel-bichler
 * 
 */
public enum Operation {
	MAX("Maximum"), MEDIAN("Median"), MIN("Minimum"), MEAN("Durchschnitt"), COUNT(
			"Anzahl"), SUM("Summe");
	private String displayName;

	/**
	 * @param displayName
	 *            the name to be displayed
	 */
	private Operation(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}

	/**
	 * @param statistics
	 *            any statistics with at least one value
	 * @return the result of this operation applied to the statistics given
	 */
	public double getStatisticValue(DescriptiveStatistics statistics) {
		switch (this) {
		case COUNT:
			return statistics.getN();
		case MAX:
			return statistics.getMax();
		case MEAN:
			return statistics.getMean();
		case MEDIAN:
			return statistics.getPercentile(50);
		case MIN:
			return statistics.getMin();
		case SUM:
			return statistics.getSum();
		default:
			throw new IllegalStateException(
					"Error in program code: an instance of the Operation Enum holds illegal value");
		}
	}

}

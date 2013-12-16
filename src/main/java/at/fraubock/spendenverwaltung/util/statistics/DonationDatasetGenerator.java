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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.util.Pair;

/**
 * A collection of static methods assisting in creating statistical dataset for
 * {@link Donation}s to be used e.g. for feeding charts.
 * 
 * @author manuel-bichler
 * 
 */
public class DonationDatasetGenerator {

	/**
	 * Creates a new dataset.
	 * 
	 * @param dataSets
	 *            a list of rows the statistic shall be calculated from. Each
	 *            row consists of a list of donations and a name.
	 * @param operation
	 *            the statistic operation that shall be used to calculate the
	 *            statistic value for each bucket in each row.
	 * @param periodClass
	 *            the class instance for the class defining the granularity of
	 *            the "buckets" the donations will be thrown into. e.g.
	 *            {@link Year}, {@link Quarter}, {@link Month}, {@link Week},
	 *            {@link Day}.
	 * @param <T>
	 *            the type of the regular time period used for creating the
	 *            buckets, e.g. {@link Year}, {@link Quarter}, {@link Month},
	 *            {@link Week}, {@link Day}.
	 * 
	 * @return the generated dataset, e.g. for use with charts
	 */
	public static <T extends RegularTimePeriod> TimeSeriesCollection createDataset(
			List<Pair<List<Donation>, String>> dataSets, Operation operation,
			Class<T> periodClass) {
		TimeSeriesCollection tsColl = new TimeSeriesCollection();
		for (Pair<List<Donation>, String> row : dataSets) {
			// for each row create a series
			Map<T, DescriptiveStatistics> statMap = new HashMap<T, DescriptiveStatistics>();
			for (Donation d : row.a) {
				@SuppressWarnings("unchecked")
				/*
				 * as guaranteed by the method, it will return a period instance
				 * of the same class that is committed.
				 */
				T period = (T) RegularTimePeriod.createInstance(periodClass,
						d.getDate(), TimeZone.getDefault());
				DescriptiveStatistics stat = statMap.get(period);
				if (stat == null) {
					stat = new DescriptiveStatistics();
					statMap.put(period, stat);
				}
				stat.addValue(d.getAmount().doubleValue() / 100 /* in EUR */);
			}
			TimeSeries series = new TimeSeries(row.b);
			for (Map.Entry<T, DescriptiveStatistics> entry : statMap.entrySet())
				// now add each calculated statistical value to the series
				series.add(entry.getKey(),
						operation.getStatisticValue(entry.getValue()));
			tsColl.addSeries(series);
		}
		return tsColl;
	}

	/**
	 * Creates a new category dataset. Each of the ten categories represents one
	 * Austrian province or the "other" province (other country or no
	 * donator/address info).
	 * 
	 * @param dataSets
	 *            a list of rows the statistic shall be calculated from. Each
	 *            row consists of a list of donations and a name.
	 * @param operation
	 *            the statistic operation that shall be used to calculate the
	 *            statistic value for each bucket in each row.
	 * 
	 * @return the generated dataset, e.g. for use with charts.
	 */
	public static CategoryDataset createDatasetProvinciallyCategorized(
			List<Pair<List<Donation>, String>> dataSets, Operation operation) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Pair<List<Donation>, String> row : dataSets) {
			// for each province create a statistic
			Map<Province, DescriptiveStatistics> statMap = new EnumMap<Province, DescriptiveStatistics>(
					Province.class);
			for (Province p : Province.values())
				statMap.put(p, new DescriptiveStatistics());
			for (Donation d : row.a)
				statMap.get(Province.getFromDonation(d)).addValue(
						d.getAmount().doubleValue() / 100 /* in EUR */);
			for (Map.Entry<Province, DescriptiveStatistics> entry : statMap
					.entrySet())
				// now add each calculated statistical value to the dataset
				dataset.addValue(operation.getStatisticValue(entry.getValue()),
						row.b, entry.getKey());
		}
		return dataset;
	}

}

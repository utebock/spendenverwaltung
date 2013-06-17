package at.fraubock.spendenverwaltung.util.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
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
 * A statistic for {@link Donation}s based on a fixed time period that produces
 * a {@link TimeSeriesCollection} to be used e.g. for feeding charts.
 * 
 * @author manuel-bichler
 * 
 * @param <T>
 *            the type of the regular time period used for creating the buckets,
 *            e.g. {@link Year}, {@link Quarter}, {@link Month}, {@link Week},
 *            {@link Day}.
 */
public class DonationTimeStatisticDatasetGenerator {

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
}

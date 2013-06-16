package at.fraubock.spendenverwaltung.util.statistics;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.jfree.data.category.DefaultCategoryDataset;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;

/**
 * A statistic class is a classified "bucket" for a set of donations. A class
 * has a start and an end date. A class may only contatin donations that were
 * made between those dates (inclusive). A class manages one or more rows, each
 * of which has its own set of donations. However, those rows all share the same
 * start and end date.
 * 
 * @author manuel-bichler
 * 
 */
public class StatisticClass implements Comparable<StatisticClass> {

	private Date start;
	private Date end; // must be after start. exclusive.
	private Map<String, DescriptiveStatistics> rows;
	// each row has its own statistic over their own donation lists
	private String name;

	/**
	 * Constructor.
	 * 
	 * @param start
	 *            start date of the donations contained in this class
	 * @param end
	 *            end date of the donations contained in this class. Must not be
	 *            before start date.
	 * @param name
	 *            name of this class (e.g. "2012" if start date is 2012-01-01
	 *            and end date is 2012-12-31)
	 * @param rowNames
	 *            list of (mutually unequal) names of rows
	 */
	public StatisticClass(Date start, Date end, String name,
			List<String> rowNames) {
		super();
		rows = new HashMap<String, DescriptiveStatistics>();
		for (String rowName : rowNames) {
			rows.put(rowName, new DescriptiveStatistics());
		}
		this.start = start;
		this.end = end;
		this.name = name;
	}

	/**
	 * inserts a donation to a given row
	 * 
	 * @param donation
	 *            the donation to be inserted
	 * @param rowName
	 *            the row the donation should be inserted into (must be one of
	 *            those specified in the constructor)
	 * @return if the donation was made within the boundary dates of this class,
	 *         it will be added to the given row and <code>true</code> will be
	 *         returned. If the donation was not made within the boundary, it
	 *         will not be added and <code>false</code> will be returned.
	 */
	public boolean insert(Donation donation, String rowName) {
		if (donation.getDate().before(start) || donation.getDate().after(end)
				|| donation.getDate().equals(end)) {
			return false;
		} else {
			rows.get(rowName)
					.addValue(donation.getAmount().doubleValue() / 100 /* EUR */);
			return true;
		}
	}

	/**
	 * @return name of this class
	 */
	public String getName() {
		return name;
	}

	/**
	 * adds the statistically calculated amounts (one per row, in Euros) to the
	 * given dataset.
	 * 
	 * @param dataset
	 *            a dataset to insert into
	 * @param operation
	 *            the statistical operation that shall be applied to the
	 *            donations of each row
	 */
	public void addToCategoryDataset(DefaultCategoryDataset dataset,
			Operation operation) {
		for (Map.Entry<String, DescriptiveStatistics> row : rows.entrySet()) {
			DescriptiveStatistics stat = row.getValue();
			switch (operation) {
			case COUNT:
				dataset.addValue(stat.getN(), row.getKey(), this);
				break;
			case MAX:
				dataset.addValue(stat.getMax(), row.getKey(), this);
				break;
			case MEAN:
				dataset.addValue(stat.getMean(), row.getKey(), this);
				break;
			case MEDIAN:
				dataset.addValue(stat.getPercentile(50), row.getKey(), this);
				break;
			case MIN:
				dataset.addValue(stat.getMin(), row.getKey(), this);
				break;
			case SUM:
				dataset.addValue(stat.getSum(), row.getKey(), this);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * Compares two statistical classes for ordering, using their start dates
	 * 
	 * @param o
	 *            an other statistic class
	 * @return the value 0 if the argument class's start date is equal to this
	 *         class's start date; a value less than 0 if this class's start
	 *         date is before the argument class's start date; and a value
	 *         greater than 0 if this class's start date is after the argument
	 *         class's start date.
	 */
	@Override
	public int compareTo(StatisticClass o) {
		return this.start.compareTo(o.start);
	}

	/**
	 * @return this class's name
	 */
	@Override
	public String toString() {
		return name;
	}

}

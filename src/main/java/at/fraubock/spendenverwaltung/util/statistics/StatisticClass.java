package at.fraubock.spendenverwaltung.util.statistics;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.jfree.data.category.DefaultCategoryDataset;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;

public class StatisticClass implements Comparable<StatisticClass> {

	private Date start;
	private Date end; // must be after start. exclusive.
	private Map<String, List<Donation>> rows; // each row has its own donation
												// list
	private String name;

	public StatisticClass(Date start, Date end, String name,
			List<String> rowNames) {
		super();
		rows = new HashMap<String, List<Donation>>();
		for (String rowName : rowNames) {
			rows.put(rowName, new LinkedList<Donation>());
		}
		this.start = start;
		this.end = end;
		this.name = name;
	}

	public boolean insert(Donation donation, String rowName) {
		if (donation.getDate().before(start) || donation.getDate().after(end)
				|| donation.getDate().equals(end)) {
			return false;
		} else {
			rows.get(rowName).add(donation);
			return true;
		}
	}

	public String getName() {
		return name;
	}

	public static enum Operation {
		MIN, MAX, SUM, COUNT, MEAN, MEDIAN;
	}

	public void addToCategoryDataset(DefaultCategoryDataset dataset,
			Operation operation) {
		for (Map.Entry<String, List<Donation>> row : rows.entrySet()) {
			DescriptiveStatistics stat = new DescriptiveStatistics();
			for (Donation d : row.getValue()) {
				stat.addValue(d.getAmount().doubleValue());
			}
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

	@Override
	public int compareTo(StatisticClass o) {
		return this.start.compareTo(o.start);
	}

	@Override
	public String toString() {
		return name;
	}

}

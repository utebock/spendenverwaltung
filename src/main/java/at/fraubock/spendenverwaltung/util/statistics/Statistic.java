package at.fraubock.spendenverwaltung.util.statistics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.data.category.DefaultCategoryDataset;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.util.Pair;

public class Statistic {

	private static final Logger log = Logger.getLogger(Statistic.class);

	private Map<GregorianCalendar, StatisticClass> classes; // key: start date
															// of class

	public Statistic(List<Pair<List<Donation>, String>> dataSets,
			Classification classfication) {

		List<String> rowNames = new ArrayList<String>();

		GregorianCalendar start = null;
		GregorianCalendar end = null;

		for (Pair<List<Donation>, String> dataSet : dataSets) {
			rowNames.add(dataSet.b);
			for (Donation d : dataSet.a) {
				if (start == null) {
					start = new GregorianCalendar();
					start.setTime(d.getDate());
					end = new GregorianCalendar();
					end.setTime(d.getDate());
				} else {
					if (d.getDate().before(start.getTime())) {
						start.setTime(d.getDate());
					}
					if (d.getDate().after(end.getTime())) {
						end.setTime(d.getDate());
					}
				}
			}
		}

		// create classifications:
		classes = new HashMap<GregorianCalendar, StatisticClass>();
		// key is start date of class
		switch (classfication) {
		case DAY: {
			GregorianCalendar day = start;
			while (!day.after(end)) {
				GregorianCalendar nextDay = (GregorianCalendar) day.clone();
				nextDay.add(Calendar.DAY_OF_WEEK, 1);
				StatisticClass category = new StatisticClass(day.getTime(),
						nextDay.getTime(), DateFormat.getInstance().format(
								day.getTime()), rowNames);
				classes.put(day, category);
				day = nextDay;
			}
		}
			break;
		case MONTH: {
			GregorianCalendar month = start;
			month.set(Calendar.DAY_OF_MONTH, 1);
			while (!month.after(end)) {
				GregorianCalendar nextMonth = (GregorianCalendar) month.clone();
				nextMonth.add(Calendar.MONTH, 1);
				StatisticClass category = new StatisticClass(month.getTime(),
						nextMonth.getTime(),
						new SimpleDateFormat("MM/YY").format(month.getTime()),
						rowNames);
				classes.put(month, category);
				month = nextMonth;
			}
		}
			break;
		case QUARTER: {
			GregorianCalendar quarter = start;
			quarter.set(Calendar.DAY_OF_MONTH, 1);
			quarter.set(Calendar.MONTH,
					quarter.get(Calendar.MONTH)
							- (quarter.get(Calendar.MONTH) - 1) % 3);
			while (!quarter.after(end)) {
				GregorianCalendar nextQuarter = (GregorianCalendar) quarter
						.clone();
				nextQuarter.add(Calendar.MONTH, 3);
				StatisticClass category = new StatisticClass(
						quarter.getTime(),
						nextQuarter.getTime(),
						new SimpleDateFormat("MM/YY").format(quarter.getTime()),
						rowNames);
				classes.put(quarter, category);
				quarter = nextQuarter;
			}
		}
			break;
		case WEEK: {
			GregorianCalendar week = start;
			week.set(Calendar.DAY_OF_WEEK, 1);
			while (!week.after(end)) {
				GregorianCalendar nextWeek = (GregorianCalendar) week.clone();
				nextWeek.add(Calendar.WEEK_OF_YEAR, 1);
				StatisticClass category = new StatisticClass(week.getTime(),
						nextWeek.getTime(),
						new SimpleDateFormat("ww/YY").format(week.getTime()),
						rowNames);
				classes.put(week, category);
				week = nextWeek;
			}
		}
			break;
		case YEAR: {
			GregorianCalendar year = start;
			year.set(Calendar.DAY_OF_YEAR, 1);
			while (!year.after(end)) {
				GregorianCalendar nextYear = (GregorianCalendar) year.clone();
				nextYear.add(Calendar.YEAR, 1);
				StatisticClass category = new StatisticClass(year.getTime(),
						nextYear.getTime(),
						new SimpleDateFormat("YYYY").format(year.getTime()),
						rowNames);
				classes.put(year, category);
				year = nextYear;
			}
		}
			break;
		default:
			break;

		}

		// add donations to their classifications:
		for (Pair<List<Donation>, String> dataSet : dataSets) {
			for (Donation d : dataSet.a) {
				GregorianCalendar donDate = new GregorianCalendar();
				donDate.setTime(d.getDate());
				switch (classfication) {
				case DAY:
					break;
				case MONTH:
					donDate.set(Calendar.DAY_OF_MONTH, 1);
					break;
				case QUARTER:
					donDate.set(Calendar.DAY_OF_MONTH, 1);
					donDate.set(Calendar.MONTH, donDate.get(Calendar.MONTH)
							- (donDate.get(Calendar.MONTH) - 1) % 3);
					break;
				case WEEK:
					donDate.set(Calendar.DAY_OF_WEEK, 1);
					break;
				case YEAR:
					donDate.set(Calendar.DAY_OF_YEAR, 1);
					break;
				default:
					break;
				}
				StatisticClass c = classes.get(donDate);
				if (c == null) {
					log.error("Program malfunction: either statistic class not created or calendar comparision fails (because there are hours/minutes/seconds/millis somewhere)");
				}
				c.insert(d, dataSet.b);
			}
		}

	}

	public DefaultCategoryDataset createDataset(Operation operation) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (StatisticClass c : classes.values()) {
			c.addToCategoryDataset(dataset, operation);
		}
		return dataset;
	}
}

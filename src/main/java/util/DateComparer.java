package util;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author manuel-bichler
 * 
 */
public class DateComparer {

	/**
	 * @param d1
	 * @param d2
	 * @return true if the given dates are on the same day
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
	}
}

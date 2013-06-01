package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JComboBox;

public class PropertyComparatorFactory {
	
	public static JComboBox<String> getStringComparator() {
		return new JComboBox<String>(new String[]{"ist gleich", "ist \u00E4hnlich", "ist ungleich"});
	}
	
	public static JComboBox<String> getNumberComparator() {
		return new JComboBox<String>(new String[]{"ist gleich", "ist gr\u00F6\u00DFer", "ist gr\u00F6\u00DFer gleich", 
				"ist kleiner", "ist kleiner gleich", "ist ungleich"});
	}
}

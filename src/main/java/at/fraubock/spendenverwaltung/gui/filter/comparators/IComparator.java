package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JComponent;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public interface IComparator {

	public PropertyCriterionTO getPropertyCriterionTOForProperty(
			FilterProperty property) throws InvalidInputException;

	public JComponent getPanel();

}

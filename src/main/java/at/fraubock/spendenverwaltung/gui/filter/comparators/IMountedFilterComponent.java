package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JComponent;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;

public interface IMountedFilterComponent {

	public MountedFilterCriterion getMountedFilterCriterionTOForFilter(
			Filter filter) throws InvalidInputException;

	public JComponent getComponent();
	
}

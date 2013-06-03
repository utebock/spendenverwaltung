package at.fraubock.spendenverwaltung.gui.filter.comparators;

import javax.swing.JComponent;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.service.to.MountedFilterCriterionTO;

public interface IMountedFilterComponent {

	public MountedFilterCriterionTO getMountedFilterCriterionTOForFilter(
			Filter filter) throws InvalidInputException;

	public JComponent getComponent();
	
}

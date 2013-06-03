package at.fraubock.spendenverwaltung.gui.filter;

import javax.swing.JComponent;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.comparators.IMountedFilterComponent;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;

/**
 * this component represents a specific mountable filter in the filter gui
 * 
 * @author philipp muhoray
 * 
 */
public class MountedFilterConfigurator implements ICriterionConfigurator {

	private Filter filter;
	private IMountedFilterComponent comparator;

	public MountedFilterConfigurator(Filter filter,
			IMountedFilterComponent comparator) {
		this.filter = filter;
		this.comparator = comparator;
	}

	@Override
	public CriterionTO createCriterion() throws InvalidInputException {
		return comparator.getMountedFilterCriterionTOForFilter(filter);
	}

	@Override
	public JComponent getConfigComponent() {
		return comparator.getComponent();
	}
}

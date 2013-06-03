package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.comparators.IMountedFilterComponent;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;
import at.fraubock.spendenverwaltung.util.FilterType;

/**
 * this component represents a specific mountable filter in the filter gui
 * 
 * @author philipp muhoray
 * 
 */
public class MountedFilterConfigurator extends JPanel implements
		ICriterionConfigurator {

	private FilterType type;
	private IMountedFilterComponent comparator;
	private JComboBox<Filter> filterBox;
	private IFilterService filterService;

	public MountedFilterConfigurator(FilterType type,
			IMountedFilterComponent comparator, IFilterService filterService) {
		this.filterService = filterService;
		this.type = type;
		this.comparator = comparator;
		List<Filter> filters = new ArrayList<Filter>();
		try {
			filters = filterService.getAll(type);
		} catch (ServiceException e) {
			// TODO err msg + log
		}

		add(filterBox = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				filters)));
	}

	@Override
	public CriterionTO createCriterion() throws InvalidInputException {
		return null;//comparator.getMountedFilterCriterionTOForFilter(filter);
	}

	@Override
	public JComponent getConfigComponent() {
		return comparator.getComponent();
	}
}

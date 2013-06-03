package at.fraubock.spendenverwaltung.gui.filter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.comparators.DonationToPersonComp;
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
	private JComboBox<Filter> filterBox;
	private IFilterService filterService;
	private String display;
	private DonationToPersonComp comp;

	public MountedFilterConfigurator(FilterType type, String display) {
		this.display = display;
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/spring.xml");
		filterService = context.getBean("filterService", IFilterService.class);
		this.type = type;
		List<Filter> filters = new ArrayList<Filter>();
		try {
			filters = filterService.getAll(type);
		} catch (ServiceException e) {
			// TODO err msg + log
		}

		add(filterBox = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				filters)));

		add(comp = new DonationToPersonComp());
	}

	@Override
	public CriterionTO createCriterion() throws InvalidInputException {
		return comp.createCriterion((Filter) filterBox.getModel()
				.getSelectedItem());
	}

	@Override
	public JComponent getConfigComponent() {
		return this;
	}

	@Override
	public String toString() {
		return display;
	}
}

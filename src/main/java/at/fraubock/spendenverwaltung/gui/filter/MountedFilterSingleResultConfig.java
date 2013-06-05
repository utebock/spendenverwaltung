package at.fraubock.spendenverwaltung.gui.filter;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

/**
 * this component represents a specific mountable filter in the filter gui
 * 
 * @author philipp muhoray
 * 
 */
public class MountedFilterSingleResultConfig extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -7505945596725892100L;

	private JComboBox<Filter> filterBox;
	private String display;

	public MountedFilterSingleResultConfig(String display, List<Filter> filters) {
		this.display = display;

		add(filterBox = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				filters)));
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		Filter filter = (Filter) filterBox.getModel().getSelectedItem();

		MountedFilterCriterion crit = new MountedFilterCriterion();
		crit.setMount(filter);
		crit.setRelationalOperator(RelationalOperator.EQUALS);
		crit.setCount(1);
		return crit;
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

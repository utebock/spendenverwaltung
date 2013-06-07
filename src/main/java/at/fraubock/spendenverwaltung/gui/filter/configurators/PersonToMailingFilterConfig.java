package at.fraubock.spendenverwaltung.gui.filter.configurators;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.util.FilterType;

/**
 * implements {@link ICriterionConfigurator} for {@link MountedFilterCriterion}s
 * that mount Mailing filters and are added to person filters.
 * 
 * @author philipp muhoray
 * 
 */
public class PersonToMailingFilterConfig extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -8507188088817762907L;

	private JComboBox<Filter> filterBox;
	private String display;

	public PersonToMailingFilterConfig(String display, List<Filter> filters) {
		this.display = display;

		add(filterBox = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				filters)));
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		return null;
	}

	@Override
	public JComponent getConfigComponent() {
		return this;
	}

	@Override
	public String toString() {
		return display;
	}

	@Override
	public boolean applyCriterion(Criterion criterion) {
		if (criterion instanceof MountedFilterCriterion) {
			MountedFilterCriterion crit = (MountedFilterCriterion) criterion;
			if (crit.getMount().getType() == FilterType.MAILING) {
//				this.picker.getModel().setSelectedItem(
//						crit.getRelationalOperator());
//
//				if (crit.getCount() != null) {
//					this.amount.setText("" + crit.getCount());
//					euro = false;
//					euroLabel.setText("mal");
//				} else {
//					this.amount.setText("" + crit.getSum());
//					euro = true;
//					euroLabel.setText("\u20AC");
//				}

				return true;
			}
		}
		return false;
	}
}

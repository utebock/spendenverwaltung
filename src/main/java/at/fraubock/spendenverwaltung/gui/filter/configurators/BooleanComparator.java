package at.fraubock.spendenverwaltung.gui.filter.configurators;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

/**
 * implements {@link ICriterionConfigurator} for {@link PropertyCriterion}s of
 * type {@link Boolean}.
 * 
 * @author philipp muhoray
 * 
 */
public class BooleanComparator extends JPanel implements ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private JCheckBox checkBox;
	private FilterProperty property;
	private String display;

	public BooleanComparator(FilterProperty property, String display) {
		this.display = display;
		this.property = property;
		add(checkBox = new JCheckBox());
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		PropertyCriterion crit = new PropertyCriterion();
		crit.setProperty(property);
		crit.setRelationalOperator(RelationalOperator.EQUALS);
		crit.setBoolValue(checkBox.isSelected());
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

	@Override
	public boolean applyCriterion(Criterion criterion) {
		if(criterion instanceof PropertyCriterion) {
			PropertyCriterion prop = (PropertyCriterion)criterion;
			if(prop.getProperty() == this.property) {
				this.checkBox.setSelected(prop.getBoolValue());
				return true;
			}
		}
		return false;
	}
}

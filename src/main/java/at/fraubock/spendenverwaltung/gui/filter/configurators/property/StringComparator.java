package at.fraubock.spendenverwaltung.gui.filter.configurators.property;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;

/**
 * implements {@link ICriterionConfigurator} for {@link PropertyCriterion}s of
 * type {@link String}.
 * 
 * @author philipp muhoray
 * 
 */
public class StringComparator extends JPanel implements ICriterionConfigurator {

	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private JTextField textField;
	private FilterProperty property;
	private String display;

	public StringComparator(FilterProperty property, String display) {
		super(new MigLayout());
		this.display = display;
		this.property = property;
		add(picker = new RelationalOperatorPicker(RelationType.FOR_STRING),
				"split 2");
		add(textField = new JTextField(20), "wrap");
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		PropertyCriterion crit = new PropertyCriterion();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setStrValue(textField.getText());
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
		if (criterion instanceof PropertyCriterion) {
			PropertyCriterion prop = (PropertyCriterion) criterion;
			if (prop.getProperty() == this.property) {
				this.picker.setSelectedItem(
						RelationalOperatorGuiWrapper.getForOperator(prop
								.getRelationalOperator()));

				this.textField.setText("" + prop.getStrValue());

				return true;
			}
		}
		return false;
	}
}
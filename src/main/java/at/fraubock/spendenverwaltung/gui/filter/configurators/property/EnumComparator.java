package at.fraubock.spendenverwaltung.gui.filter.configurators.property;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;

/**
 * implements {@link ICriterionConfigurator} for {@link PropertyCriterion}s of
 * type {@link String}. should be used when only a known set of values are
 * allowed.
 * 
 * @author philipp muhoray
 * 
 */
public class EnumComparator extends JPanel implements ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private JComboBox<Object> comboBox;
	private LinkedHashMap<Object, String> enums;
	private FilterProperty property;
	private String display;

	/**
	 * @param enums
	 *            - maps the objects to be rendered in the GUI to the strings
	 *            that will be used for filtering
	 */
	public EnumComparator(LinkedHashMap<Object, String> enums,
			FilterProperty property, String display) {
		this.display = display;
		this.property = property;
		this.enums = enums;
		add(picker = new RelationalOperatorPicker(RelationType.FOR_ENUM));
		add(comboBox = new JComboBox<Object>(new SimpleComboBoxModel<Object>(
				new ArrayList<Object>(enums.keySet()))));
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		PropertyCriterion crit = new PropertyCriterion();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setStrValue(enums.get(comboBox.getModel().getSelectedItem()));
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

				for (Object obj : enums.keySet()) {
					String str = enums.get(obj);
					if (prop.getStrValue().equals(str)) {
						this.comboBox.setSelectedItem(obj);
					}
				}

				return true;
			}
		}
		return false;
	}
}

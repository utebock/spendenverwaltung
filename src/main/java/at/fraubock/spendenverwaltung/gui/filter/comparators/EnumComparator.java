package at.fraubock.spendenverwaltung.gui.filter.comparators;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.service.to.PropertyCriterionTO;
import at.fraubock.spendenverwaltung.util.FilterProperty;

public class EnumComparator extends JPanel implements IComparator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private JComboBox<Object> comboBox;
	private LinkedHashMap<Object, String> enums;

	/**
	 * @param enums
	 *            - maps the objects to be rendered in the GUI to the strings
	 *            that will be used for filtering
	 */
	public EnumComparator(LinkedHashMap<Object, String> enums) {
		this.enums = enums;
		add(picker = new RelationalOperatorPicker(RelationType.FOR_ENUM));
		add(comboBox = new JComboBox<Object>(new SimpleComboBoxModel<Object>(
				new ArrayList<Object>(enums.keySet()))));
	}

	@Override
	public PropertyCriterionTO getPropertyCriterionTOForProperty(FilterProperty property) {
		PropertyCriterionTO crit = new PropertyCriterionTO();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setStrValue(enums.get(comboBox.getModel().getSelectedItem()));
		return crit;
	}
	
	@Override
	public JComponent getPanel() {
		JPanel panel = new JPanel();
		panel.add(picker);
		panel.add(comboBox);
		return panel;
	}
}

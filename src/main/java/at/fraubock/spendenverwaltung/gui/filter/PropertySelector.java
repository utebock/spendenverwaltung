package at.fraubock.spendenverwaltung.gui.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.service.to.CriterionTO;

public class PropertySelector extends JPanel {
	private static final long serialVersionUID = 1564048026876101371L;

	private JPanel comparator;
	private ComboBoxModel<PropertyCriterionComponent> cbModel;

	public PropertySelector(List<PropertyCriterionComponent> models) {
		super(new MigLayout());
		comparator = new JPanel();
		comparator.add(models.get(0).getComparatorComponent());

		final JComboBox<PropertyCriterionComponent> propertyCB = new JComboBox<PropertyCriterionComponent>(
				cbModel = new SimpleComboBoxModel<PropertyCriterionComponent>(
						models));

		propertyCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropertyCriterionComponent model = (PropertyCriterionComponent) propertyCB
						.getModel().getSelectedItem();
				comparator.removeAll();
				comparator.add(model.getComparatorComponent());
				comparator.repaint();
				comparator.revalidate();
			}
		});

//		add(opPicker = new LogicalOperatorPicker(), "wrap");
		add(propertyCB);
		add(comparator);
	}

	/**
	 * creates a {@link CriterionTO} based on the values of this selector
	 * 
	 * @return
	 */
	 public CriterionTO toCriterionTO() {
		 return ((PropertyCriterionComponent) cbModel.getSelectedItem()).toPropertyCriterionTO();
	 }
}

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
	private ComboBoxModel<PropertyComponent> cbModel;

	public PropertySelector(List<PropertyComponent> models) {
		super(new MigLayout());
		comparator = new JPanel();
		comparator.add(models.get(0));

		final JComboBox<PropertyComponent> propertyCB = new JComboBox<PropertyComponent>(
				cbModel = new SimpleComboBoxModel<PropertyComponent>(
						models));

		propertyCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropertyComponent model = (PropertyComponent) propertyCB
						.getModel().getSelectedItem();
				comparator.removeAll();
				comparator.add(model);
				comparator.repaint();
				comparator.revalidate();
			}
		});
		
		add(propertyCB);
		add(comparator);
	}

	/**
	 * creates a {@link CriterionTO} based on the values of this selector
	 * 
	 * @return
	 */
	 public CriterionTO toCriterionTO() {
		 return ((PropertyComponent) cbModel.getSelectedItem()).toPropertyCriterionTO();
	 }
}

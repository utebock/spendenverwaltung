/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.gui.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.plaf.metal.MetalBorders;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;

/**
 * this component represents a panel that let's you create a criterion in the
 * {@link CreateFilter} GUI. therefore it takes a list of
 * {@link ICriterionConfigurator} and presents them in a drop-down box. if
 * chosen, the configurator's panel will appear on the screen where the user can
 * configure the chosen type of criterion.
 * 
 * @author philipp muhoray
 * 
 */
public class CriterionSelector extends JPanel {
	private static final long serialVersionUID = -2885373338561255928L;

	List<ICriterionConfigurator> configurators;

	/* the drop-down box showing all possible choices for criterions */
	private ComboBoxModel<ICriterionConfigurator> cbModel;

	public CriterionSelector(List<ICriterionConfigurator> configurators) {
		this(configurators, null);
	}

	public CriterionSelector(List<ICriterionConfigurator> configurators,
			Criterion crit) {
		super(new MigLayout());
		// border layout for better definition
		setBorder(BorderFactory.createTitledBorder(
				MetalBorders.getTextFieldBorder(), "Kriterium"));

		this.configurators = configurators;

		final JComboBox<ICriterionConfigurator> propertyCB = new JComboBox<ICriterionConfigurator>(
				cbModel = new SimpleComboBoxModel<ICriterionConfigurator>(
						configurators));
		ICriterionConfigurator selCon = null;
		for (ICriterionConfigurator con : configurators) {
			if (con.applyCriterion(crit)) {
				propertyCB.setSelectedItem(con);
				selCon = con;
				break;
			}
		}

		propertyCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ICriterionConfigurator model = (ICriterionConfigurator) propertyCB
						.getModel().getSelectedItem();
				remove(1);
				add(model.getConfigComponent(), 1);
				repaint();
				revalidate();
			}
		});

		add(propertyCB);
		add(selCon != null ? selCon.getConfigComponent() : configurators.get(0)
				.getConfigComponent());
	}

	public Criterion toCriterionTO() throws InvalidInputException {
		return ((ICriterionConfigurator) cbModel.getSelectedItem())
				.createCriterion();
	}
}

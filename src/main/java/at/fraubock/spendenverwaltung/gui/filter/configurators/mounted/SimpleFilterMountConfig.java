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
package at.fraubock.spendenverwaltung.gui.filter.configurators.mounted;

import java.awt.Font;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

/**
 * implements {@link ICriterionConfigurator} for {@link MountedFilterCriterion}s
 * where no comparison is needed
 * 
 * @author philipp muhoray
 * 
 */
public class SimpleFilterMountConfig extends JPanel implements
		ICriterionConfigurator {
	private static final long serialVersionUID = -7505945596725892100L;

	private FilterType type;
	private JComboBox<Filter> filterBox;
	private String display;

	public SimpleFilterMountConfig(String display, List<Filter> filters,
			FilterType type, String descrText) {
		this.display = display;
		this.type = type;

		JLabel descr = null;
		if(filters.isEmpty()) {
			descr = new JLabel("Es existiert kein weiterer Filter dieser Art.");
			add(descr);
		} else {
			descr = new JLabel(descrText);
			add(descr);
			add(filterBox = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
					filters)));
		}
		descr.setFont(new Font("Headline", Font.PLAIN, 14));
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

	@Override
	public boolean applyCriterion(Criterion criterion) {
		if (criterion instanceof MountedFilterCriterion) {
			MountedFilterCriterion crit = (MountedFilterCriterion) criterion;
			if (crit.getMount().getType() == this.type) {

				this.filterBox.setSelectedItem(crit.getMount());

				return true;
			}
		}
		return false;
	}

}

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
package at.fraubock.spendenverwaltung.gui.filter.configurators.property;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

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
		if(property == FilterProperty.DONATION_IS_ANON) {
			if(checkBox.isSelected()) {
				crit.setRelationalOperator(RelationalOperator.IS_NULL);
			} else {
				crit.setRelationalOperator(RelationalOperator.NOT_NULL);
			}
		} else {
			crit.setRelationalOperator(RelationalOperator.EQUALS);
			crit.setBoolValue(checkBox.isSelected());
		}
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
				if(property == FilterProperty.DONATION_IS_ANON) {
					if(prop.getRelationalOperator()==RelationalOperator.NOT_NULL) {
						this.checkBox.setSelected(false);
					} else {
						this.checkBox.setSelected(true);
					}
				} else {
					this.checkBox.setSelected(prop.getBoolValue());
				}
				return true;
			}
		}
		return false;
	}
}

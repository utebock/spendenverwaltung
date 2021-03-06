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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.gui.InvalidInputException;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationType;
import at.fraubock.spendenverwaltung.gui.filter.RelationalOperatorPicker.RelationalOperatorGuiWrapper;
import at.fraubock.spendenverwaltung.gui.filter.configurators.ICriterionConfigurator;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;

/**
 * implements {@link ICriterionConfigurator} for {@link PropertyCriterion}s of
 * type {@link Number}.
 * 
 * @author philipp muhoray
 * 
 */
public class NumberComparator extends JPanel implements ICriterionConfigurator {
	private static final long serialVersionUID = 5674883209607705490L;

	private RelationalOperatorPicker picker;
	private NumericTextField textField;
	private FilterProperty property;
	private String display;

	public NumberComparator(FilterProperty property, String display) {
		this.display = display;
		this.property = property;
		add(picker = new RelationalOperatorPicker(
				RelationType.FOR_NUMBER));
		add(textField = new NumericTextField(false));
		add(new JLabel("\u20AC"));
	}

	private Double getNumber() throws InvalidInputException {
		if(textField.validateContents()) {
			return Double.valueOf(textField.getHundredths());
		} else {
			throw new InvalidInputException(
					"Bitte geben Sie eine g\u00FCltige Zahl ein!");
		}
	}

	@Override
	public Criterion createCriterion() throws InvalidInputException {
		PropertyCriterion crit = new PropertyCriterion();
		crit.setProperty(property);
		crit.setRelationalOperator(picker.getPickedOperator());
		crit.setNumValue(getNumber());
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

				this.textField.setNumericValue(prop.getNumValue()/100D);

				return true;
			}
		}
		return false;
	}
}

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
package at.fraubock.spendenverwaltung.util.filter;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;

/**
 * a relational operator used for the evaluate function in
 * {@link PropertyCriterion} and {@link MountedFilterCriterion}
 * 
 * @author philipp muhoray
 * 
 */
public enum RelationalOperator {

	GREATER(">"), LESS("<"), EQUALS("="), UNEQUAL("<>"), LIKE("LIKE"), GREATER_EQ(
			">="), LESS_EQ("<="), NOT_NULL("IS NOT NULL"), IS_NULL("IS NULL");

	private String expr;

	private RelationalOperator(String expr) {
		this.expr = expr;
	}

	public String toExpression() {
		return expr;
	}
}

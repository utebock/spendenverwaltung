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
package at.fraubock.spendenverwaltung.interfaces.domain.filter;

/**
 * thrown whenever a {@link Criterion} is added to a {@link Filter} or another
 * criterion that is not of the same type
 * 
 * @author philipp muhoray
 * 
 */
public class InapplicableFilterTypeException extends RuntimeException {
	private static final long serialVersionUID = -8199287323050669950L;

	public InapplicableFilterTypeException(String msg) {
		super(msg);
	}

}

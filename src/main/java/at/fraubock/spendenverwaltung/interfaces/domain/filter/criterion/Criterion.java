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
package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.util.filter.FilterBuilder;
import at.fraubock.spendenverwaltung.util.filter.FilterType;

/**
 * a criterion represents a condition for a specific entity that can be
 * fulfilled or not. conceptually, a criterion composes of 1) input of an entity
 * of a specific type (defined by the {@link FilterType}) 2) evaluation function
 * which evaluates this criterion's condition based on the input (defined in a
 * concrete sub type of this abstract class) 3) output that determines the
 * result of the evaluation function (boolean)
 * 
 * @NOTE criterions are solely data structures that hold information about how
 *       an object should be filtered. they first have to be interpreted into
 *       corresponding query statements fulfilling the definition. this is done
 *       by the {@link FilterBuilder}.
 * 
 * @author philipp muhoray
 * 
 */
public abstract class Criterion {

	private Integer id;
	protected FilterType type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Criterion other = (Criterion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}

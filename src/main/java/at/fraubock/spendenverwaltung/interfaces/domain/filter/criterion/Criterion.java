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
}

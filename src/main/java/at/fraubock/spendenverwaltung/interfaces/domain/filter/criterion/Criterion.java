package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.util.FilterType;

/**
 * a criterion represents a condition for a specific entity that can be
 * fulfilled or not. conceptually, a criterion composes of 
 * 1) input of an entity of a specific type (defined by the {@link FilterType}) 
 * 2) evaluation function which evaluates this criterion's condition based on the input
 * (defined in a concrete sub type of this abstract class)
 * 3) output that determines the result of the evaluation function (boolean)
 * 
 * @NOTE since we are using SQL statements for filtering rather than lists of
 *       entity instances, the evaluation function is NOT given programmatically
 *       but rather per definition. that means that there is no need for a method
 *       like <code>abstract boolean validate(Entity)</code> (which would go
 *       along with the idea behind this concept). instead, criterions are
 *       solely data structures that have to be interpreted into corresponding
 *       SQL statements fulfilling the definition. this is done by the
 *       {@link FilterToSqlBuilder}.
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

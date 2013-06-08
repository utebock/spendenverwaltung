package at.fraubock.spendenverwaltung.interfaces.domain.filter;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.FilterType;

/**
 * conceptually a filter can be seen as a function that takes a set of entities
 * and returns another set, only consisting of those entities fulfilling given
 * conditions. the conditions are defined in the {@link Criterion}. therefore,
 * each entity will be handed to the criterion and evaluated by it's evaluation
 * function. the output determines whether this entity should be in the result
 * set of this filter
 * 
 * the filter only accepts entities which are defined by it's {@link FilterType}
 * .
 * 
 * an anonymous filter is a non-stand-alone filter, which means it can't exist
 * on it's own but must be mounted into another filter (and thus is not
 * represented in the GUI).
 * 
 * @author philipp muhoray
 * 
 */
public class Filter {

	private Integer id;

	/* the type of entities this filter accepts */
	private FilterType type;

	/* the criterion each entity of the input set must fulfill */
	private Criterion criterion;

	/* the name to be shown to the user */
	private String name;

	/* determines whether this filter can exist on it's own */
	private boolean anonymous = false;

	public Filter() {

	}

	public Filter(FilterType type) {
		this(type, null);
	}

	public Filter(FilterType type, Criterion criterion) {
		this(type, criterion, null);
	}

	public Filter(FilterType type, Criterion criterion, String name) {
		this.setType(type);
		this.setCriterion(criterion);
		this.setName(name);
	}

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
		if (criterion != null) {
			criterion.setType(type);
		}
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
		if (criterion != null) {
			criterion.setType(type);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	@Override
	public String toString() {
		return name != null ? name : "";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filter other = (Filter) obj;
		
		if(this.getId()!=null && this.getId().equals(other.getId())) {
			return true;
		}
		
		return false;
	}

}

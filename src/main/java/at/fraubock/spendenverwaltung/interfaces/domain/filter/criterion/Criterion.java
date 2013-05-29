package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.util.FilterType;

public abstract class Criterion {
	
	private Integer id;
	protected FilterType type;

	public Integer getCriterionId() {
		return id;
	}

	public void setCriterionId(Integer id) {
		this.id = id;
	}

	public FilterType getType() {
		return type;
	}
	
}

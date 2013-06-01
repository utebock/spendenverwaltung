package at.fraubock.spendenverwaltung.service.to;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.FilterType;

public abstract class CriterionTO {

	protected FilterType type;

	public abstract Criterion createCriterion();

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

}

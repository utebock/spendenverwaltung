package at.fraubock.spendenverwaltung.interfaces.domain.filter;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.FilterType;

public class Filter {
	
	private Integer id;
	private FilterType type;
	private Criterion criterion;
	private String name;
	private boolean anonymous = false;
	
	public Filter(FilterType type, Criterion head) {
		this.type = type;
		this.criterion = head;
	}

	public FilterType getType() {
		return type;
	}

	public Criterion getCriterion() {
		return criterion;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}

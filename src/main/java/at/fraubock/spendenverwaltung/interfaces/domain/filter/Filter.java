package at.fraubock.spendenverwaltung.interfaces.domain.filter;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.FilterType;

public class Filter {
	
	private Integer id;
	private FilterType type;
	private Criterion criterion;
	private String name;
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
		if(criterion!=null) {
			criterion.setType(type);
		}
	}
	public Criterion getCriterion() {
		return criterion;
	}
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
		if(criterion!=null) {
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
		return name!=null?name:"";
	}
	
}

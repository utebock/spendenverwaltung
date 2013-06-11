package at.fraubock.spendenverwaltung.interfaces.domain.filter.to;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

/**
 * a transfer object for {@link Filter} entities. it takes a list of
 * {@link Criterion}s and {@link LogicalOperator}s that defines the structure of
 * the resulting filter. the first operator connects the first and second
 * criterion, the following operator connects the first operator with the third
 * criterion, and so on
 * 
 * 
 * @author philipp muhoray
 * 
 */
public class FilterTO {

	private String name;
	private boolean anonymous;
	private boolean isPrivate;
	private FilterType type;
	private List<Criterion> criterions;
	private LogicalOperator operator;

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	public List<Criterion> getCriterions() {
		return criterions;
	}

	public void setCriterions(List<Criterion> criterions) {
		this.criterions = criterions;
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

	public LogicalOperator getOperator() {
		return operator;
	}

	public void setOperator(LogicalOperator operator) {
		this.operator = operator;
	}
	
	public boolean getPrivate(){
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate){
		this.isPrivate = isPrivate;
	}
}

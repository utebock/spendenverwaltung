package at.fraubock.spendenverwaltung.service.to;

import java.util.List;

import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class FilterTO {

	private String name;
	private boolean anonymous;
	private FilterType type;
	private List<CriterionTO> criterions;
	private List<LogicalOperator> operators;

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	public List<CriterionTO> getCriterions() {
		return criterions;
	}

	public void setCriterions(List<CriterionTO> criterions) {
		this.criterions = criterions;
	}

	public List<LogicalOperator> getOperators() {
		return operators;
	}

	public void setOperators(List<LogicalOperator> operators) {
		this.operators = operators;
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

}

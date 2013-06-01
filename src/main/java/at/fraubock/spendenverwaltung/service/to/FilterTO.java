package at.fraubock.spendenverwaltung.service.to;

import java.util.List;

import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class FilterTO {

	private List<CriterionTO> criterions;
	private List<LogicalOperator> operators;

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	private FilterType type;

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

}

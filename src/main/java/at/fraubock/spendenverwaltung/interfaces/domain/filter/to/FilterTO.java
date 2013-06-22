package at.fraubock.spendenverwaltung.interfaces.domain.filter.to;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter.FilterPrivacyStatus;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;

/**
 * a transfer object for {@link Filter} entities. it takes a list of
 * {@link Criterion}s and a {@link LogicalOperator} that defines how the
 * criterions will be connected.
 * 
 * @author philipp muhoray
 * 
 */
public class FilterTO {

	private String name;
	private boolean anonymous;
	private FilterPrivacyStatus privacyStatus;
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

	public FilterPrivacyStatus getPrivacyStatus() {
		return privacyStatus;
	}

	public void setPrivacyStatus(FilterPrivacyStatus privacyStatus) {
		this.privacyStatus = privacyStatus;
	}
}

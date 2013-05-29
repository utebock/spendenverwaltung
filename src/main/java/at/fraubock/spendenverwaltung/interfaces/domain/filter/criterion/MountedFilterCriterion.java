package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class MountedFilterCriterion extends Criterion {

	private Integer id;
	private Filter mount;
	private RelationalOperator relationalOperator;
	private Integer count;
	private FilterProperty property;
	private Double sum;
	private Double avg;
	
	public MountedFilterCriterion(FilterType type, Filter mount, RelationalOperator relationalOperator) {
		this.type = type;
		this.mount = mount;
		this.relationalOperator = relationalOperator;
	}

	public Filter getMount() {
		return mount;
	}
	
	public RelationalOperator getRelationalOperator() {
		return relationalOperator;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public FilterProperty getProperty() {
		return property;
	}

	public void setProperty(FilterProperty property) {
		this.property = property;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public Double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

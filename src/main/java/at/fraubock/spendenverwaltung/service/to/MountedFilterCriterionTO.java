package at.fraubock.spendenverwaltung.service.to;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class MountedFilterCriterionTO extends CriterionTO {

	private Filter mount;
	private RelationalOperator relationalOperator;
	private Integer count;
	private FilterProperty property;
	private Double sum;
	private Double avg;

	public Filter getMount() {
		return mount;
	}

	public void setMount(Filter mount) {
		this.mount = mount;
	}

	public RelationalOperator getRelationalOperator() {
		return relationalOperator;
	}

	public void setRelationalOperator(RelationalOperator relationalOperator) {
		this.relationalOperator = relationalOperator;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
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

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}

	@Override
	public Criterion createCriterion() {
		// TODO Auto-generated method stub
		return null;
	}

}

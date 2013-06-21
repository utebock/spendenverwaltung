package at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.util.filter.FilterBuilder;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

/**
 * this class represents a {@link Criterion} that evaluates an entity based on
 * the result of another {@link Filter} (mounted filter). since a filter is
 * defined as taking a list of entities as it's input and returning a list of
 * entities as it's output, we need to determine two things: what is the input
 * set for the mounted filter and what to do with it's output set? remember that
 * this class is a criterion and a criterion always takes a single entity as
 * input, evaluates it and returns a boolean value. therefore the input of the
 * mounted filter must somehow be related to this criterion's input, and the
 * result set of the mounted filter must somehow be evaluated to return a
 * boolean value. the latter is determined by this criterion's settings: the
 * output can either be evaluated by:
 * 
 * - the amount of entities in the filter's result set. therefore <code>count</code>
 * must be set. 
 * - the sum of a specific {@link FilterProperty} in the filter's result set.
 * therefore <code>property</code> and <code>sum</code> must be set. 
 * - the average of a specific {@link FilterProperty} in the filter's result set 
 * (sum divided by number of results). therefore <code>property</code> and 
 * <code>avg</code> must be set. 
 * 
 * these values will be compared with the mounted filter's result set based on the given 
 * {@link RelationalOperator}.
 * 
 * now that we can define how to evaluate the result set, we must define the
 * input set for the filter. there is no general way of doing so, each mounting
 * has to be handled separately based on the relations of the two mounted tables.
 * this is done exclusively in the {@link FilterBuilder}.
 * 
 * for example, if a donation filter is mounted to a person filter, the SQL
 * builder decides which donations the donation filter gets as input (which will
 * obviously be all donations of the current person). if filters of the same
 * types are mounted, the mounted filter takes as input only the current entity
 * of the super filter. the input of a mounted filter is always defined
 * implicitly in the SQL builder. see the commenting there for further
 * information.
 * 
 * @author philipp muhoray
 * 
 */
public class MountedFilterCriterion extends Criterion {

	private Filter mount;
	private RelationalOperator relationalOperator;

	/* evaluates the number of entities in the filter's result set */
	private Integer count;

	/* determines the property of the mounted filter to evaluate sum and avg */
	private FilterProperty property;

	/* evaluates the sum of a property in the filter's result set */
	private Double sum;

	/*
	 * evaluates the average (sum/amount) of a property in the filter's result
	 * set
	 */
	private Double avg;

	/**
	 * mounts the given {@link Filter} and compares the number of it's result
	 * set with the number in <code>count</code> using the given
	 * {@link RelationalOperator}
	 * 
	 * @param mount
	 * @param relationalOperator
	 * @param count
	 */
	public void mountAndCompareCount(Filter mount,
			RelationalOperator relationalOperator, int count) {
		this.setMount(mount);
		this.setRelationalOperator(relationalOperator);
		this.setCount(count);
	}

	/**
	 * mounts the given {@link Filter} and compares the sum of the
	 * {@link FilterProperty} with the number in <code>sum</code> using the
	 * given {@link RelationalOperator}
	 * 
	 * @param mount
	 * @param relationalOperator
	 * @param property
	 * @param sum
	 */
	public void mountAndCompareSum(Filter mount,
			RelationalOperator relationalOperator, FilterProperty property,
			double sum) {
		this.setMount(mount);
		this.setRelationalOperator(relationalOperator);
		this.setProperty(property);
		this.setSum(sum);
	}

	/**
	 * mounts the given {@link Filter} and compares the average (sum/count) of
	 * the {@link FilterProperty} with the number in <code>avg</code> using the
	 * given {@link RelationalOperator}
	 * 
	 * @param mount
	 * @param relationalOperator
	 * @param property
	 * @param avg
	 */
	public void mountAndCompareAverage(Filter mount,
			RelationalOperator relationalOperator, FilterProperty property,
			double avg) {
		this.setMount(mount);
		this.setRelationalOperator(relationalOperator);
		this.setProperty(property);
		this.setAvg(avg);
	}

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

}

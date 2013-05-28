package at.fraubock.spendenverwaltung.interfaces.domain.filter.cells;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class MountedFilter extends FilterCell {

	private Integer id;
	private Filter mount;
	private String type;
	private RelationalOperator relationalOperator;
	private Integer count;
	private String property;
	private Double sum;
	private Double avg;
	
	@Override
	public String createSqlExpression() {
		String statement = "("+mount.createSqlStatement();
		
		String mountedType = mount.getType();
		if(this.type.equals(mountedType)) {
			statement += " and "+mountedType+".id="+type+".id)";
		} else if(this.type.equals("person")) {
			statement += " and "+type+".id="+mountedType+".personid)";
		} else {
			statement += " and "+mountedType+".id="+type+".personid)";
		}
		
		statement+=relationalOperator.getMath();
		
		if(count!=null) {
			statement = statement.replace("select * from", "select count(*) from");
			statement += count;
		} else if(property!=null) {
			if(sum!=null) {
				statement = statement.replace("select * from", "select sum("+property+") from");
				statement += sum;
			} else if(avg!=null) {
				statement = statement.replace("select * from", "select avg("+property+") from");
				statement += avg;
			}
		}
		
		
		
		return statement;
	}

	public Filter getMount() {
		return mount;
	}

	public void setMount(Filter mount) {
		this.mount = mount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public RelationalOperator getRelationalOperator() {
		return relationalOperator;
	}
	public void setRelationalOperator(RelationalOperator relationalOperator) {
		this.relationalOperator = relationalOperator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

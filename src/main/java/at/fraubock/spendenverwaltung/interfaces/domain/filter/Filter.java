package at.fraubock.spendenverwaltung.interfaces.domain.filter;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.cells.FilterCell;

public class Filter {
	
	private Integer id;
	protected String type;
	protected String name;
	protected boolean anonymous;
	protected FilterCell head;
	
	public String createSqlStatement() {
		return "select * from "+type+" as "+type+" where "+head.createSqlExpression();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FilterCell getHead() {
		return head;
	}

	public void setHead(FilterCell head) {
		this.head = head;
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

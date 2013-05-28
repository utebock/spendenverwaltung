package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.Date;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;

public class Mailing {
	
	private Integer id;
	
	private Filter personFilter;
	
	private String type;
	private Medium medium;
	
	private Date date;
	
	public static enum Medium {
		EMAIL, POSTAL
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Medium getMedium() {
		return medium;
	}

	public void setMedium(Medium medium) {
		this.medium = medium;
	}
	
	public Filter getFilter() {
		return personFilter;
	}
	
	public void setFilter(Filter personFilter) {
		this.personFilter = personFilter;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.Date;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;

public class Mailing {
	
	private Integer id;
	
	private Filter personFilter;
	
	private MailingType type;
	
	private Medium medium;
	
	private Date date;
	
	public static enum MailingType {
		ALLGEMEINER_DANKESBRIEF("allgemeiner Dankesbrief"), 
		DANKESBRIEF("Dankesbrief"),
		DAUERSPENDER_DANKESBRIEF("Dauerspender Dankesbrief"),
		EINZELSPENDEN_DANKESBRIEF("Einzelspenden Dankesbrief"),
		ERLAGSCHEINVERSAND("Erlagscheinversand"),
		INFOMATERIAL("Infomaterial"),
		SPENDENAUFRUF("Spendenaufruf"),
		SPENDENBRIEF("Spendenbrief"),
		T_SHIRT_VERSAND("T-Shirt Versand");
		
		private final String name;

		private MailingType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static MailingType getByName(String name) {
			switch (name) {
			case "allgemeiner Dankesbrief":
				return ALLGEMEINER_DANKESBRIEF;
			case "Dankesbrief":
				return DANKESBRIEF;
			case "Dauerspender Dankesbrief":
				return DAUERSPENDER_DANKESBRIEF;
			case "Einzelspenden Dankesbrief":
				return EINZELSPENDEN_DANKESBRIEF;
			case "Erlagscheinversand":
				return ERLAGSCHEINVERSAND;
			case "Infomaterial":
				return INFOMATERIAL;
			case "Spendenaufruf":
				return SPENDENAUFRUF;
			case "Spendenbrief":
				return SPENDENBRIEF;
			case "T-Shirt Versand":
				return T_SHIRT_VERSAND;
			default:
				throw new IllegalArgumentException("No such MailingType");
			}
		}
			
	}
	
	public static enum Medium {
		EMAIL("email"), POSTAL("postal");
		
		private final String name;

		private Medium(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static Medium getByName(String name) {
			switch (name) {
			case "email":
				return EMAIL;
			case "postal":
				return POSTAL;
			default:
				throw new IllegalArgumentException("No such MailingType");
			}
		}
		
		
	}

	public MailingType getType() {
		return type;
	}

	public void setType(MailingType type) {
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

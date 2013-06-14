package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.Date;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.util.DateComparer;

public class Mailing {

	private Integer id;

	private Filter personFilter;

	private MailingType type;

	private Medium medium;

	private Date date;

	private MailingTemplate template;

	public static enum MailingType {
		ALLGEMEINER_DANKESBRIEF("allgemeiner Dankesbrief"), DANKESBRIEF(
				"Dankesbrief"), DAUERSPENDER_DANKESBRIEF(
				"Dauerspender Dankesbrief"), EINZELSPENDEN_DANKESBRIEF(
				"Einzelspenden Dankesbrief"), ERLAGSCHEINVERSAND(
				"Erlagscheinversand"), INFOMATERIAL("Infomaterial"), SPENDENAUFRUF(
				"Spendenaufruf"), SPENDENBRIEF("Spendenbrief"), T_SHIRT_VERSAND(
				"T-Shirt Versand");

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

	public MailingTemplate getTemplate() {
		return template;
	}

	public void setTemplate(MailingTemplate template) {
		this.template = template;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((medium == null) ? 0 : medium.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mailing other = (Mailing) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!DateComparer.isSameDay(this.date, other.date))
			return false;
		if (medium != other.medium)
			return false;
		if (type != other.type)
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}
}

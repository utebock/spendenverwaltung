package at.fraubock.spendenverwaltung.interfaces.domain.filter;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.util.filter.FilterType;

/**
 * this class represents a filter for retrieving a subset of entities of
 * different types. conceptually a filter can be seen as a function that takes a
 * set of entities and returns another set, only consisting of those entities
 * fulfilling given conditions. the conditions are defined in the
 * {@link Criterion}. therefore, each entity will be handed to the criterion and
 * evaluated by it's evaluation function. the output determines whether this
 * entity should be in the result set of this filter.
 * 
 * the filter only accepts entities which are defined by it's {@link FilterType}
 * 
 * an anonymous filter is a non-stand-alone filter, which means it can't exist
 * on it's own but must be mounted into another filter (and thus is not
 * represented in the GUI).
 * 
 * @author philipp muhoray
 * 
 */
public class Filter {

	private Integer id;

	/* the type of entities this filter accepts */
	private FilterType type;

	/* the criterion each entity of the input set must fulfill */
	private Criterion criterion;

	/* the name to be shown to the user */
	private String name;

	/* determines whether this filter can exist on it's own */
	private boolean anonymous = false;

	/* name of the user who created the filter */
	private String owner;

	/* determines whether this filter is private or public */
	private FilterPrivacyStatus privacyStatus = FilterPrivacyStatus.PRIVATE;

	public static enum FilterPrivacyStatus {
		PRIVATE("privat"), READ("anzeigen"), READ_UPDATE("anzeigen, bearbeiten"), READ_UPDATE_DELETE(
				"anzeigen, bearbeiten, löschen");

		private final String name;

		private FilterPrivacyStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

		public static FilterPrivacyStatus getByName(String name) {
			switch (name) {
			case "privat":
				return PRIVATE;
			case "anzeigen":
				return READ;
			case "anzeigen, bearbeiten":
				return READ_UPDATE;
			case "anzeigen, bearbeiten, löschen":
				return READ_UPDATE_DELETE;
			default:
				throw new IllegalArgumentException(
						"No privacy status for name: " + name);
			}
		}

		public static String[] toStringArray() {
			return new String[] { "privat", "anzeigen", "anzeigen, bearbeiten",
					"anzeigen, bearbeiten, löschen" };
		}
	};

	public Filter() {

	}

	public Filter(FilterType type) {
		this(type, null);
	}

	public Filter(FilterType type, Criterion criterion) {
		this(type, criterion, null);
	}

	public Filter(FilterType type, Criterion criterion, String name) {
		this.setType(type);
		this.setCriterion(criterion);
		this.setName(name);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
		if (criterion != null) {
			criterion.setType(type);
		}
	}

	public Criterion getCriterion() {
		return criterion;
	}

	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
		if (criterion != null) {
			criterion.setType(type);
		}
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

	public FilterPrivacyStatus getPrivacyStatus() {
		return privacyStatus;
	}

	public void setPrivacyStatus(FilterPrivacyStatus privacyStatus) {
		this.privacyStatus = privacyStatus;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return name != null ? name : "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (anonymous ? 1231 : 1237);
		result = prime * result
				+ ((criterion == null) ? 0 : criterion.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((privacyStatus == null) ? 0 : privacyStatus.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Filter other = (Filter) obj;
		if (anonymous != other.anonymous)
			return false;
		if (criterion == null) {
			if (other.criterion != null)
				return false;
		} else if (!criterion.equals(other.criterion))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (privacyStatus != other.privacyStatus)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}

package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.Date;

/**
 * Domain model representing an import
 * 
 * @author manuel-bichler
 * 
 */
public class Import {
	private Integer id;
	private String creator;
	private Date importDate;
	private String source;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the user who created this import
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the user who created this import
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the date this import was made
	 */
	public Date getImportDate() {
		return importDate;
	}

	/**
	 * @param importDate
	 *            the date this import was made
	 */
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	/**
	 * @return the source. e.g. 'SMS-Aktion', 'Hypo-Export', 'Hypo-Export
	 *         automatisch', 'native', ...
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set. e.g. 'SMS-Aktion', 'Hypo-Export',
	 *            'Hypo-Export automatisch', 'native', ...
	 */
	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((importDate == null) ? 0 : importDate.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		Import other = (Import) obj;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (importDate == null) {
			if (other.importDate != null)
				return false;
		} else if (!importDate.equals(other.importDate))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

}

package at.fraubock.spendenverwaltung.interfaces.domain;

import java.io.File;

/**
 * domain model representing a mailing template (which is a text file stored in
 * the database for reusing)
 * 
 * @author philipp muhoray
 * 
 */
public class MailingTemplate {

	private Integer id;
	private String name;
	private File file;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return name;
	}

	public void setFileName(String fileName) {
		this.name = fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;

		MailingTemplate other = (MailingTemplate) obj;

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

		// don't compare the absolute file path because the files could be on
		// different places but still match (e.g. tmp files)
		if (file == null) {
			if (other.file != null)
				return false;
		} else {
			if (other.file == null)
				return false;
			if (file.length() != other.file.length())
				return false;
		}

		return true;
	}

}

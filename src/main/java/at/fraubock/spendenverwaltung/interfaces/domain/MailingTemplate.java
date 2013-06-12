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
	private String fileName;
	private File file;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;

		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;

		return true;
	}

}

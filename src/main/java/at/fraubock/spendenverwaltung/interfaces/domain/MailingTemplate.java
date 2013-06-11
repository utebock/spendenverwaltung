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

	private int id;
	private String fileName;
	private int fileSize;
	private File file;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}

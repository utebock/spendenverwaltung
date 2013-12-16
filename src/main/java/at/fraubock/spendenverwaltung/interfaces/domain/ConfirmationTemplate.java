/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.interfaces.domain;

import java.io.File;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class ConfirmationTemplate {
	private Integer id;
	private String name;
	private File file;
	
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public File getFile() {
		return file;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
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

		ConfirmationTemplate other = (ConfirmationTemplate) obj;

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

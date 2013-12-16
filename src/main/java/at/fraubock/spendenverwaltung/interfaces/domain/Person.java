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

import java.util.ArrayList;
import java.util.List;

public class Person {

	public static enum Sex {
		MALE("male"), FEMALE("female"), FAMILY("family"), COMPANY("company");

		private final String name;

		private Sex(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static Sex getByName(String name) {
			switch (name) {
			case "male":
				return MALE;
			case "female":
				return FEMALE;
			case "family":
				return FAMILY;
			case "company":
				return COMPANY;
			default:
				throw new IllegalArgumentException("No such Sex");
			}
		}

		public static String getDisplayableName(Sex sex) {
			switch (sex) {
			case MALE:
				return "m\u00E4nnlich";
			case FEMALE:
				return "weiblich";
			case FAMILY:
				return "Familie";
			case COMPANY:
				return "Unternehmen";
			default:
				throw new IllegalArgumentException("No such Sex");
			}
		}
	}

	private Integer id;
	private Sex sex;
	private String title;
	private String company;
	private String givenName;
	private String surname;
	private List<Address> addresses = new ArrayList<Address>();
	private Address mainAddress;
	private String email;
	private String telephone;
	private boolean emailNotification = true;
	private boolean postalNotification = true;
	private String note;

	public Person() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public boolean isPostalNotification() {
		return postalNotification;
	}

	public void setPostalNotification(boolean postalNotification) {
		this.postalNotification = postalNotification;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            must be of format +(country code) (number) eg. "+43 660234352"
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Address getMainAddress() {
		return mainAddress;
	}

	/**
	 * @Precondition mainAddress is contained in the list of Addresses of this
	 *               Person
	 */
	public void setMainAddress(Address mainAddress) {
		this.mainAddress = mainAddress;
	}

	@Override
	public String toString() {
		return "Id: " + id + "; Given Name: " + givenName + "; Surname: "
				+ surname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (emailNotification ? 1231 : 1237);
		result = prime * result
				+ ((givenName == null) ? 0 : givenName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mainAddress == null) ? 0 : mainAddress.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + (postalNotification ? 1231 : 1237);
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result
				+ ((telephone == null) ? 0 : telephone.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Person other = (Person) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (emailNotification != other.emailNotification)
			return false;
		if (givenName == null) {
			if (other.givenName != null)
				return false;
		} else if (!givenName.equals(other.givenName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mainAddress == null) {
			if (other.mainAddress != null)
				return false;
		} else if (!mainAddress.equals(other.mainAddress))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (postalNotification != other.postalNotification)
			return false;
		if (sex != other.sex)
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}

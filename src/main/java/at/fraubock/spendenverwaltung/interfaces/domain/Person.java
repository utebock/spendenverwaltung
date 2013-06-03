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
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (this.getClass() != obj.getClass()) {
			return false;
		}

		Person other = (Person) obj;

		if (this.getId().equals(other.getId())
				&& this.getGivenName().equals(other.getGivenName())
				&& this.getSurname().equals(other.getSurname())
				&& this.getSex().equals(other.getSex())
				&& this.getCompany().equals(other.getCompany())
				&& this.getMainAddress().equals(other.getMainAddress())
				&& this.getNote().equals(other.getNote())
				&& this.getTitle().equals(other.getTitle())
				&& this.getTelephone().equals(other.getTelephone())
				&& this.isEmailNotification() == other.isEmailNotification()
				&& this.isPostalNotification() == other.isPostalNotification()
				&& this.getEmail().equals(other.getEmail())) {
			return true;
		}

		return false;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addresses == null) ? 0 : addresses.hashCode());
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
}

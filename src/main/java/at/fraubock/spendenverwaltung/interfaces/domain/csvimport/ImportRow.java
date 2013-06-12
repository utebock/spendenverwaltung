package at.fraubock.spendenverwaltung.interfaces.domain.csvimport;

public class ImportRow {
	// Person
	private String givenName;
	private String surname;
	private String email;
	private String sex;
	private String title;
	private String company;
	private String telephone;
	private String emailNotification;
	private String postalNotification;
	private String personNote;

	// Donation
	private String date;
	private String amount;
	private String dedication;
	private String donationNote;
	private String type;

	// Address
	private String street;
	private String postcode;
	private String city;
	private String country;

	public ImportRow() {

	}

	public String getGivenName() {
		return givenName;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public String getTitle() {
		return title;
	}

	public String getCompany() {
		return company;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getPersonNote() {
		return personNote;
	}

	public String getDate() {
		return date;
	}

	public String getAmount() {
		return amount;
	}

	public String getDedication() {
		return dedication;
	}

	public String getDonationNote() {
		return donationNote;
	}

	public String getStreet() {
		return street;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setPersonNote(String personNote) {
		this.personNote = personNote;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setDedication(String dedication) {
		this.dedication = dedication;
	}

	public void setDonationNote(String donationNote) {
		this.donationNote = donationNote;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSex() {
		return sex;
	}

	public String getEmailNotification() {
		return emailNotification;
	}

	public String getPostalNotification() {
		return postalNotification;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setEmailNotification(String emailNotification) {
		this.emailNotification = emailNotification;
	}

	public void setPostalNotification(String postalNotification) {
		this.postalNotification = postalNotification;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((dedication == null) ? 0 : dedication.hashCode());
		result = prime * result
				+ ((donationNote == null) ? 0 : donationNote.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime
				* result
				+ ((emailNotification == null) ? 0 : emailNotification
						.hashCode());
		result = prime * result
				+ ((givenName == null) ? 0 : givenName.hashCode());
		result = prime * result
				+ ((personNote == null) ? 0 : personNote.hashCode());
		result = prime
				* result
				+ ((postalNotification == null) ? 0 : postalNotification
						.hashCode());
		result = prime * result
				+ ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result
				+ ((telephone == null) ? 0 : telephone.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		ImportRow other = (ImportRow) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (dedication == null) {
			if (other.dedication != null)
				return false;
		} else if (!dedication.equals(other.dedication))
			return false;
		if (donationNote == null) {
			if (other.donationNote != null)
				return false;
		} else if (!donationNote.equals(other.donationNote))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (emailNotification == null) {
			if (other.emailNotification != null)
				return false;
		} else if (!emailNotification.equals(other.emailNotification))
			return false;
		if (givenName == null) {
			if (other.givenName != null)
				return false;
		} else if (!givenName.equals(other.givenName))
			return false;
		if (personNote == null) {
			if (other.personNote != null)
				return false;
		} else if (!personNote.equals(other.personNote))
			return false;
		if (postalNotification == null) {
			if (other.postalNotification != null)
				return false;
		} else if (!postalNotification.equals(other.postalNotification))
			return false;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}

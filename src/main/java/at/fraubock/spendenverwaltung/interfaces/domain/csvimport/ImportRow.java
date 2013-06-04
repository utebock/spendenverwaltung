package at.fraubock.spendenverwaltung.interfaces.domain.csvimport;

public class ImportRow {
	//Person
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
	
	//Donation
	private String date;
	private String amount;
	private String dedication;
	private String donationNote;
	private String type;
	
	//Address
	private String street;
	private String postcode;
	private String city;
	private String country;
	
	public ImportRow(){
		
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
	
	
	
}

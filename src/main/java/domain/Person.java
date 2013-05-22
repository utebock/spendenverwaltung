package domain;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private int id;
	
	public static enum Salutation{
		HERR, FRAU, FA, FAM
	};
	private Salutation salutation;
	private boolean isDeleted;
	private String title;
	private String company;
	private String givenName;
	private String surname;
	private List<Address> addresses = new ArrayList<Address>();
	private Address mailing_address;
	private String email;
	private String telephone;
	public static enum NotificationType{
		MAIL, POST
	};
	private NotificationType notificationType;
	private String note;
	
	public Person(){
		
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public Salutation getSalutation(){
		return salutation;
	}
	public void setSalutation(Salutation salutation){
		this.salutation = salutation;
	}
	
	public boolean getDeleted(){
		return isDeleted;
	}
	
	public void setDeleted(boolean isDeleted){
		this.isDeleted = isDeleted;
	}
	
	public String getGivenName(){
		return givenName;
	}
	public void setGivenName(String givenName){
		this.givenName = givenName;
	}
	
	public String getSurname(){
		return surname;
	}
	public void setSurname(String surname){
		this.surname = surname;
	}
	
	public List<Address> getAddresses(){
		return addresses;
	}
	public void setAddresses(List<Address> addresses){
		this.addresses = addresses;
	}
	
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getCompany(){
		return company;
	}
	public void setCompany(String company){
		this.company = company;
	}
	
	public String getTelephone(){
		return telephone;
	}
	public void setTelephone(String telephone){
		this.telephone = telephone;
	}
	
	public NotificationType getNotificationType(){
		return notificationType;
	}
	public void setNotificationType(NotificationType notificationType){
		this.notificationType = notificationType;
	}
	
	public String getNote(){
		return note;
	}
	public void setNote(String note){
		this.note = note;
	}

	public Address getMailingAddress() {
		return mailing_address;
	}

	public void setMailingAddress(Address mailing_address) {
		this.mailing_address = mailing_address;
	}
	
	@Override
	public String toString(){
		return "Id: "+ id +"; Given Name: "+givenName+"; Surname: "+surname;
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

		if (this.getId() == other.getId()
				&& this.getGivenName().equals(other.getGivenName())
				&& this.getSurname().equals(other.getSurname())
				&& this.getAddresses().equals(other.getAddresses())
				&& this.getSalutation().equals(other.getSalutation())
				&& this.getCompany().equals(other.getCompany())
				&& this.getMailingAddress().equals(other.getMailingAddress())
				&& this.getNote().equals(other.getNote())
				&& this.getTitle().equals(other.getTitle())
				&& this.getTelephone().equals(other.getTelephone())
				&& this.getNotificationType().equals(other.getNotificationType())
				&& this.getEmail().equals(other.getEmail())){
			return true;
		}
		
		return false;
	}
}

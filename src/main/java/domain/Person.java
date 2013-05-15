package domain;

public class Person {

	private int id;
	private String givenName;
	private String surname;
	private int addressID;
	private String email;
	private String salutation;
	private String title;
	private String company;
	private String telephone;
	private String notificationType;
	private String note;
	
	public Person(){
		
	}
	
	public int getID(){
		return id;
	}
	public void setID(int id){
		this.id = id;
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
	
	public int getAddressID(){
		return addressID;
	}
	public void setAddressID(int addressID){
		this.addressID = addressID;
	}
	
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		
	}
	
	public String getSalutation(){
		return salutation;
	}
	public void setSalutation(String salutation){
		this.salutation = salutation;
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
	
	public String getNotificationType(){
		return notificationType;
	}
	public void setNotificationType(String notificationType){
		this.notificationType = notificationType;
	}
	
	public String getNote(){
		return note;
	}
	public void setNote(String note){
		this.note = note;
	}
}

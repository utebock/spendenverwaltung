package domain;

public class Person {

	private int id;
	
	public static enum Salutation{
		HERR, FRAU, FA, FAM
	};
	private Salutation salutation;
	private String title;
	private String company;
	private String givenName;
	private String surname;
	private Address address;
	private String email;
	private String telephone;
	public static enum NotificationType{
		MAIL, POST
	};
	private NotificationType notificationType;
	private String note;
	
	public Person(){
		
	}
	
	public int getID(){
		return id;
	}
	public void setID(int id){
		this.id = id;
	}
	
	public Salutation getSalutation(){
		return salutation;
	}
	public void setSalutation(Salutation salutation){
		this.salutation = salutation;
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
	
	public Address getAddress(){
		return address;
	}
	public void setAddress(Address address){
		this.address = address;
	}
	
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		
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
}

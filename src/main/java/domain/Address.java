package domain;

/**
 * domain model representing an address
 * 
 * @author philipp muhoray
 * 
 */
public class Address {

	private int id;
	private String street;
	private int postalCode;
	private String city;
	private String country;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

		Address other = (Address) obj;

		if (this.getId() == other.getId()
				&& this.getPostalCode() == other.getPostalCode()
				&& this.getStreet().equals(other.getStreet())
				&& this.getCity().equals(other.getCity())
				&& this.getCountry().equals(other.getCountry())) {
			return true;
		}
		
		return false;
	}
	
	//TODO override #hashCode for hash-based data structures
}
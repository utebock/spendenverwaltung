package domain;

/**
 * domain model representing an address
 * 
 * @author philipp muhoray
 * 
 */
public class Address {

	private Integer id;
	private String street;
	private String postalCode;
	private String city;
	private String country;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
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

		if (this.getId().equals(other.getId())
				&& this.getPostalCode().equals(other.getPostalCode())
				&& this.getStreet().equals(other.getStreet())
				&& this.getCity().equals(other.getCity())
				&& this.getCountry().equals(other.getCountry())) {
			return true;
		}

		return false;
	}

	// TODO override #hashCode for hash-based data structures

	@Override
	public String toString() {
		return "id='" + this.getId() + "', street='" + this.getStreet()
				+ "', city='" + this.getCity() + "', country='"
				+ this.getCountry() + "', postal code='" + this.getPostalCode()
				+ "'";
	}
}

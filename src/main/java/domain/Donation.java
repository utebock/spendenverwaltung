package domain;

import java.util.Date;

/**
 * Domain model representing Donation
 * 
 * @author Thomas
 *
 */
public class Donation {
	private int id;
	private Person person;
	private long amount;
	private Date date;
	private String dedication;
	public static enum DonationType{
		SMS, BAR, BANK_TRANSFER, MERCHANDISE, ONLINE
	};
	private DonationType type;
	private String note;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDedication() {
		return dedication;
	}
	public void setDedication(String dedication) {
		this.dedication = dedication;
	}
	public DonationType getType() {
		return type;
	}
	public void setType(DonationType type) {
		this.type = type;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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

		Donation donation = (Donation) obj;

		if (this.getId() == donation.getId()
				&& this.getAmount() == donation.getAmount()
				&& this.getDate().equals(donation.getDate())
				&& this.getDedication() == donation.getDedication()
				&& this.getPerson().getId() == donation.getPerson().getId()
				&& this.getType() == donation.getType()
				&& this.getNote().equals(donation.getNote())) {
			return true;
		}
		
		return false;
	}
}

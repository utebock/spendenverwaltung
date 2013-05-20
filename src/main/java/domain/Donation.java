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
	private double amount;
	private Date date;
	private String dedication;
	public static enum DonationType{
		
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
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
}

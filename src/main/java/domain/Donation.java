package domain;

import java.util.Date;

import util.DateComparer;

/**
 * Domain model representing Donation
 * 
 * @author Thomas
 * @author manuel-bichler
 */
public class Donation {
	public static enum DonationType {
		SMS("sms"), BAR("bar"), BANK_TRANSFER("bank transfer"), MERCHANDISE(
				"merchandise"), ONLINE("online");

		private final String name;

		private DonationType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static DonationType getByName(String name) {
			switch (name) {
			case "sms":
				return SMS;
			case "bar":
				return BAR;
			case "bank transfer":
				return BANK_TRANSFER;
			case "merchandise":
				return MERCHANDISE;
			case "online":
				return ONLINE;
			default:
				throw new IllegalArgumentException("No such donation type");
			}
		}
	};

	private Integer id;
	private Person donator;
	private Long amount;
	private Date date;
	private String dedication;
	private DonationType type;
	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Person getDonator() {
		return donator;
	}

	public void setDonator(Person donator) {
		this.donator = donator;
	}

	/**
	 * @return the amount of this donation, in EUR-cents
	 */
	public Long getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            this donation's amount, in EUR-cents
	 */
	public void setAmount(Long amount) {
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

		Donation other = (Donation) obj;

		if (this.getId().equals(other.getId())
				&& this.getAmount() == other.getAmount()
				&& DateComparer.isSameDay(this.date, other.date)
				&& this.getDedication().equals(other.getDedication())
				&& this.getDonator().getId().equals(other.getDonator().getId())
				&& this.getType().equals(other.getType())
				&& this.getNote().equals(other.getNote())) {
			return true;
		}

		return false;
	}

	// TODO: implement hashCode
}

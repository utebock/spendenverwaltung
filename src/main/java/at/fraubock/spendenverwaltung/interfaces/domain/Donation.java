package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.Date;

import at.fraubock.spendenverwaltung.util.DateComparer;

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
		
		@Override
		public String toString() {
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
				throw new IllegalArgumentException(
						"No donation type for name: " + name);
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
	private Import source;

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

	/**
	 * @return the import this donation is from if this donation is not yet
	 *         validated. <code>null</code> if this donation is already
	 *         validated.
	 */
	public Import getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the import this donation is from if this donation is not yet
	 *            validated. <code>null</code> if this donation is already
	 *            validated.
	 */
	public void setSource(Import source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((dedication == null) ? 0 : dedication.hashCode());
		result = prime * result + ((donator == null) ? 0 : donator.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		Donation other = (Donation) obj;

		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!DateComparer.isSameDay(this.date, other.date))
			return false;
		if (dedication == null) {
			if (other.dedication != null)
				return false;
		} else if (!dedication.equals(other.dedication))
			return false;
		if (donator == null) {
			if (other.donator != null)
				return false;
		} else if (!donator.equals(other.donator))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}

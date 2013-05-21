package domain;

import java.util.Date;

/**
 * a donation filter is a set of criteria a donation may or may not fulfill.
 * 
 * A field that is set to <code>null</code> means that this field will not be
 * used for filtering.
 * 
 * The criteria of a filter are to be understood conjunctive, i.e. only
 * donations matching ALL of the given criteria are meant to match the filter.
 * 
 * @author manuel-bichler
 * 
 */
public class DonationFilter {

	private Integer minAmount, maxAmount;
	private Date minDate, maxDate;
	private String dedicationPart, notePart;
	private Donation.DonationType type;

	/**
	 * default constructor. Sets all criteria to <code>null</code>, so that the
	 * new filter matches all donations.
	 */
	public DonationFilter() {
	}

	/**
	 * @return the minimum amount a matching donation has to be of, or
	 *         <code>null</code> if the is no lower bound for this filter
	 */
	public Integer getMinAmount() {
		return minAmount;
	}

	/**
	 * @param minAmount
	 *            the minimum amount a matching donation has to be of, or
	 *            <code>null</code> if the shall be no lower bound for this
	 *            filter
	 */
	public void setMinAmount(Integer minAmount) {
		this.minAmount = minAmount;
	}

	/**
	 * @return the maximum amount a matching donation has to be of, or
	 *         <code>null</code> if the is no upper bound for this filter
	 */
	public Integer getMaxAmount() {
		return maxAmount;
	}

	/**
	 * @param maxAmount
	 *            the maximum amount a matching donation has to be of, or
	 *            <code>null</code> if the shall be no upper bound for this
	 *            filter
	 */
	public void setMaxAmount(Integer maxAmount) {
		this.maxAmount = maxAmount;
	}

	/**
	 * @return the earliest date a matching donation has to be donated on, or
	 *         <code>null</code> if the donation's age is not bounded above in
	 *         this filter
	 */
	public Date getMinDate() {
		return minDate;
	}

	/**
	 * @param minDate
	 *            the earliest date a matching donation has to be donated on, or
	 *            <code>null</code> if the donation's age shall not not bounded
	 *            above in this filter
	 */
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	/**
	 * @return the latest date a matching donation has to be donated on, or
	 *         <code>null</code> if the donation's age is not bounded below in
	 *         this filter
	 */
	public Date getMaxDate() {
		return maxDate;
	}

	/**
	 * @param maxDate
	 *            the latest date a matching donation has to be donated on, or
	 *            <code>null</code> if the donation's age shall not not bounded
	 *            below in this filter
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * @return a sting a matching donation's dedication must contain, or
	 *         <code>null</code> if the dedication is irrelevant for this filter
	 */
	public String getDedicationPart() {
		return dedicationPart;
	}

	/**
	 * @param dedicationPart
	 *            a sting a matching donation's dedication must contain, or
	 *            <code>null</code> if the dedication shall be irrelevant for
	 *            this filter
	 */
	public void setDedicationPart(String dedicationPart) {
		this.dedicationPart = dedicationPart;
	}

	/**
	 * @return a sting a matching donation's note must contain, or
	 *         <code>null</code> if the note is irrelevant for this filter
	 */
	public String getNotePart() {
		return notePart;
	}

	/**
	 * @param notePart
	 *            a sting a matching donation's note must contain, or
	 *            <code>null</code> if the note shall be irrelevant for this
	 *            filter
	 */
	public void setNotePart(String notePart) {
		this.notePart = notePart;
	}

	/**
	 * @return the type matching donations have to have, or <code>null</code> if
	 *         the type is irrelevant for this filter
	 */
	public Donation.DonationType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type matching donations have to have, or <code>null</code>
	 *            if the type shall be irrelevant for this filter
	 */
	public void setType(Donation.DonationType type) {
		this.type = type;
	}

}

package domain;

/**
 * a person filter is a set of criteria a person may or may not fulfill.
 * 
 * A field that is set to <code>null</code> means that this field will not be
 * used for filtering.
 * 
 * The criteria of a filter are to be understood conjunctive, i.e. only persons
 * matching ALL of the given criteria are meant to match the filter.
 * 
 * @author manuel-bichler
 * 
 */
public class PersonFilter {

	private String givenNamePart, surnamePart, titlePart;
	private Boolean addressSet, emailSet, telephoneSet;
	private String telephonePart, notePart;
	private Boolean wantsPostalNotification, wantsEmailNotification;
	private Person.Salutation salutation;

	/**
	 * default constructor. Sets all criteria to <code>null</code>, so that the
	 * new filter matches all persons.
	 */
	public PersonFilter() {
	}

	/**
	 * @return a sting a matching person's given name must contain, or
	 *         <code>null</code> if the given name is irrelevant for this filter
	 */
	public String getGivenNamePart() {
		return givenNamePart;
	}

	/**
	 * @param givenNamePart
	 *            a sting a matching person's given name must contain, or
	 *            <code>null</code> if the given name shall be irrelevant for
	 *            this filter
	 */
	public void setGivenNamePart(String givenNamePart) {
		this.givenNamePart = givenNamePart;
	}

	/**
	 * @return a sting a matching person's surname must contain, or
	 *         <code>null</code> if the surname is irrelevant for this filter
	 */
	public String getSurnamePart() {
		return surnamePart;
	}

	/**
	 * @param surnamePart
	 *            a sting a matching person's surname must contain, or
	 *            <code>null</code> if the surname shall be irrelevant for this
	 *            filter
	 */
	public void setSurnamePart(String surnamePart) {
		this.surnamePart = surnamePart;
	}

	/**
	 * @return a sting a matching person's title must contain, or
	 *         <code>null</code> if the title is irrelevant for this filter
	 */
	public String getTitlePart() {
		return titlePart;
	}

	/**
	 * @param titlePart
	 *            a sting a matching person's title must contain, or
	 *            <code>null</code> if the title shall be irrelevant for this
	 *            filter
	 */
	public void setTitlePart(String titlePart) {
		this.titlePart = titlePart;
	}

	/**
	 * @return true if matching persons must have a postal address specified,
	 *         false if they must not, or <code>null</code> if holding an
	 *         address is irrelevant for this filter
	 */
	public Boolean getHasAddress() {
		return addressSet;
	}

	/**
	 * @param hasAddress
	 *            whether matching persons must or must not have a postal
	 *            address specified, or <code>null</code> if holding an address
	 *            shall be irrelevant to this filter
	 */
	public void setHasAddress(Boolean hasAddress) {
		this.addressSet = hasAddress;
	}

	/**
	 * @return true if matching persons must have an email address specified,
	 *         false if they must not, or <code>null</code> if holding an email
	 *         address is irrelevant for this filter
	 */
	public Boolean getHasEmail() {
		return emailSet;
	}

	/**
	 * @param hasEmail
	 *            whether matching persons must or must not have an email
	 *            address specified, or <code>null</code> if holding an email
	 *            address shall be irrelevant to this filter
	 */
	public void setHasEmail(Boolean hasEmail) {
		this.emailSet = hasEmail;
	}

	/**
	 * @return true if matching persons must have a telephone number specified,
	 *         false if they must not, or <code>null</code> if holding a
	 *         telephone number is irrelevant for this filter
	 */
	public Boolean getHasTelephone() {
		return telephoneSet;
	}

	/**
	 * @param hasTelephone
	 *            whether matching persons must or must not have a telephone
	 *            number specified, or <code>null</code> if holding a telephone
	 *            number shall be irrelevant to this filter
	 */
	public void setHasTelephone(Boolean hasTelephone) {
		this.telephoneSet = hasTelephone;
	}

	/**
	 * @return a sting a matching person's telephone number must contain, or
	 *         <code>null</code> if the telephone number is irrelevant for this
	 *         filter
	 */
	public String getTelephonePart() {
		return telephonePart;
	}

	/**
	 * @param telephonePart
	 *            a sting a matching person's telephone number must contain, or
	 *            <code>null</code> if the telephone number shall be irrelevant
	 *            for this filter
	 */
	public void setTelephonePart(String telephonePart) {
		this.telephonePart = telephonePart;
	}

	/**
	 * @return a sting a matching person's note must contain, or
	 *         <code>null</code> if the note is irrelevant for this filter
	 */
	public String getNotePart() {
		return notePart;
	}

	/**
	 * @param notePart
	 *            a sting a matching person's note must contain, or
	 *            <code>null</code> if the note shall be irrelevant for this
	 *            filter
	 */
	public void setNotePart(String notePart) {
		this.notePart = notePart;
	}

	/**
	 * @return true if matching persons must have specified to want to receive
	 *         postal information, false if they must have specified not to want
	 *         to receive postal information, or <code>null</code> if willing to
	 *         receive such information is irrelevant for this filter
	 */
	public Boolean getWantsPostalNotification() {
		return wantsPostalNotification;
	}

	/**
	 * @param wantsPostalNotification
	 *            whether matching persons must have specified to want or not to
	 *            want to receive postal information, or <code>null</code> if
	 *            willing to receive such information shall be irrelevant to
	 *            this filter
	 */
	public void setWantsPostalNotification(Boolean wantsPostalNotification) {
		this.wantsPostalNotification = wantsPostalNotification;
	}

	/**
	 * @return true if matching persons must have specified to want to receive
	 *         email information, false if they must have specified not to want
	 *         to receive email information, or <code>null</code> if willing to
	 *         receive such information is irrelevant for this filter
	 */
	public Boolean getWantsEmailNotification() {
		return wantsEmailNotification;
	}

	/**
	 * @param wantsEmailNotification
	 *            whether matching persons must have specified to want or not to
	 *            want to receive email information, or <code>null</code> if
	 *            willing to receive such information shall be irrelevant to
	 *            this filter
	 */
	public void setWantsEmailNotification(Boolean wantsEmailNotification) {
		this.wantsEmailNotification = wantsEmailNotification;
	}

	/**
	 * @return the salutation matching persons have to have, or
	 *         <code>null</code> if the salutation is irrelevant for this filter
	 */
	public Person.Salutation getSalutation() {
		return salutation;
	}

	/**
	 * @param salutation
	 *            the salutation matching persons have to have, or
	 *            <code>null</code> if the salutation shall be irrelevant for
	 *            this filter
	 */
	public void setSalutation(Person.Salutation salutation) {
		this.salutation = salutation;
	}
}
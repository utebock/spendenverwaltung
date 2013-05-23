package domain;

import java.util.Date;

/**
 * A mailing filter is a set of criteria a mailing may or may not fulfill.
 * 
 * A field that is set to <code>null</code> means that this field will not be
 * used for filtering.
 * 
 * The criteria of a filter are to be understood conjunctive, i.e. only
 * mailings matching ALL of the given criteria are meant to match the filter.
 * 
 * @author romanvoglhuber
 *
 */
public class MailingFilter {
	
	private Date minDate, maxDate;
	private NotificationType notificationType;
	
	public static enum NotificationType{
		POSTAL, EMAIL
	}
	
	//TODO private MailingType mailingType;     //will be defined in Mailing
	
	/**
	 * default construtor. Sets all criteria to <code>null</code>, so that the 
	 * new filter matches all mailings.
	 */
	public MailingFilter(){
		minDate = null;
		maxDate = null;
		notificationType = null;
	}
	
	/**
	 * @return whether all criteria are set to <code>null</code>, so that the
	 *         new filter matches all mailings.
	 */
	public boolean isEmpty(){
		return minDate == null && maxDate == null && notificationType == null;
		//TODO ADD MAILINGTYPE
	}
	
	/**
	 * 
	 * @return the earliest date a matching mailing has to be sent, or
	 * <code>null</code> if the mailing has no minimum date
	 */
	public Date getMinDate() {
		return minDate;
	}
	
	/**
	 * @return the latest date a matching mailing has to be sent, or 
	 * <code>null</code> if the mailing has no maximum date
	 */
	public Date getMaxDate() {
		return maxDate;
	}
	
	/**
	 * @return the NotificationType of a matching mailing, or
	 * <code>null</code> if the NotificationType doesn't matter
	 */
	public NotificationType getNotificationType() {
		return notificationType;
	}
	
	/**
	 * 
	 * @param minDate 
	 * 			the earliest date a matching mailing has to be sent, or 
	 * 			<code>null</code> if the earliest date doesn't matter
	 */
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	
	/**
	 * 
	 * @param maxDate
	 *			the latest date a matching mailing has to be sent, or 
	 * 			<code>null</code> if the latest date doesn't matter
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	
	/**
	 * 
	 * @param notificationType
	 * 			the NotificationType of all matching mailings, or 
	 * 			<code>null</code> if the NotificationType doesn't matter
	 */
	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}
	
	
	
}

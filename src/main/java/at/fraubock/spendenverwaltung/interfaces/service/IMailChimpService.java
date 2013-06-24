package at.fraubock.spendenverwaltung.interfaces.service;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * 
 * @author romanvoglhuber
 * 
 */
public interface IMailChimpService {

	/**
	 * Adds e-mail address, givenname and surname from personlist to a mailchimp
	 * list
	 * 
	 * @param listId
	 *            ID of a mailchimp list
	 * @param persons
	 *            List of Persons
	 * @return Returns the amount of errors from mailchimp (invalid
	 *         e-mailaddress, ...)
	 * @throws ServiceException
	 */
	public int addPersonsToList(String listId, List<Person> persons)
			throws ServiceException;

	/**
	 * Returns a List of MailChimpListItems
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public List<MailChimpListItem> getLists() throws ServiceException;

	/**
	 * Sets the apiKey if the new apiKey is valid. To validate a Key it tries to
	 * connect to MailChimp.
	 * 
	 * @param apiKey
	 * @throws ServiceException
	 */
	public void setAPIKey(String apiKey) throws ServiceException;

	/**
	 * Adds e-mail address, givenname and surname from a mailing (whose id is
	 * given) to a mailchimp list
	 * 
	 * @param listId
	 *            ID of a mailchimp list
	 * @param mailingId
	 *            id of the mailing the data should be read from. Must be of
	 *            type {@link Mailing.Medium#EMAIL}.
	 * @throws ServiceException
	 */
	public void addMailingByIdToList(String listId, int mailingId)
			throws ServiceException;

	/**
	 * Saves name and id of a MailChimpList. toString returns the name.
	 * 
	 * @author romanvoglhuber
	 * 
	 */
	public class MailChimpListItem {
		private String id, name;

		public MailChimpListItem(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}

	}
}

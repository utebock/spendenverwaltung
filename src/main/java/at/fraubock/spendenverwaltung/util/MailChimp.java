package at.fraubock.spendenverwaltung.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ecwid.mailchimp.MailChimpClient;
import com.ecwid.mailchimp.MailChimpException;
import com.ecwid.mailchimp.MailChimpObject;
import com.ecwid.mailchimp.method.list.ListBatchSubscribeMethod;
import com.ecwid.mailchimp.method.list.ListBatchSubscribeResult;
import com.ecwid.mailchimp.method.list.ListInformation;
import com.ecwid.mailchimp.method.list.ListsMethod;
import com.ecwid.mailchimp.method.list.ListsResult;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class MailChimp {
	
	private static final Logger log = Logger.getLogger(MailChimp.class);
	private static MailChimpClient mailChimp= null;
	private static String apiKey;
	private static Boolean double_optin;
	private static Boolean update_extisting;
	
	/**
	 * Reads API-Key and options from mailchimp.properties and initialize MailChimpClient
	 * @throws ServiceException
	 */
	private static void init() throws ServiceException{
		Properties prop = new Properties();
		if(mailChimp==null){
			try {
				prop.load(MailChimp.class.getClassLoader().getResourceAsStream("mailchimp.properties"));
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new ServiceException("Error loading mailjimp.properties File");
			}
			
			double_optin = Boolean.parseBoolean(prop.getProperty("double_optin"));
			update_extisting = Boolean.parseBoolean(prop.getProperty("update_existing"));
			apiKey = prop.getProperty("apiKey");
			mailChimp = new MailChimpClient();
		}
	}
	
	/**
	 * Adds e-mail address, givenname and surname from personlist to a mailchimp list
	 * @param listId
	 * 			ID of a mailchimp list
	 * @param persons
	 * 			List of Persons
	 * @return
	 * 			Returns the amount of errors from mailchimp (invalid e-mailaddress, ...)
	 * @throws ServiceException
	 */
	public static int addPersonsToList(String listId, List<Person> persons) throws ServiceException{
		List<MergeVars> subscribers = new ArrayList<MergeVars>();
		ListBatchSubscribeResult result;
		ListBatchSubscribeMethod subscribeMethod;
		if(listId == null || persons == null){
			throw new IllegalArgumentException("Parameter must not be null");
		}
		
		log.debug("Add "+persons+" to MailChimp list "+listId);
		
		if(mailChimp == null)
			init();
		
		subscribeMethod = new ListBatchSubscribeMethod();
		subscribeMethod.apikey = apiKey;
		subscribeMethod.double_optin = double_optin;
		subscribeMethod.update_existing = update_extisting;
		subscribeMethod.id = listId;
		
		//Create MergeVars for each person
		for(Person p : persons){
			subscribers.add(new MergeVars(p.getEmail(), p.getGivenName(), p.getSurname()));	
		}
		
		//Add MergeVars list
		subscribeMethod.batch = subscribers;
		
		try {
			result = mailChimp.execute(subscribeMethod);
		} catch (IOException | MailChimpException e) {
			throw new ServiceException(e);
		}
		
		return result.error_count;
		
	}
	
	
	/**
	 * Returns a Map<String, String> of MailChimp lists
	 * Key = listID
	 * Value = list name
	 * @return
	 * @throws ServiceException
	 */
	public static Map<String, String> getLists() throws ServiceException{
		Map<String, String> map = new HashMap<String, String>();
		ListsResult result;
		
		log.debug("Get MailChimp Lists");
		
		if(mailChimp == null)
			init();
		
		ListsMethod listsMethod = new ListsMethod();
		listsMethod.apikey = apiKey;
		
		try {
			result = mailChimp.execute(listsMethod);
		} catch (IOException | MailChimpException e) {
			throw new ServiceException(e);
		}
		
		//Only return id and name
		for(ListInformation info : result.data){
			map.put(info.id, info.name);
		}
		
		return map;
	}
	
	
	public static class MergeVars extends MailChimpObject {
		private static final long serialVersionUID = 1L;
		
		@Field
	    public String EMAIL, FNAME, LNAME;

	    public MergeVars() { }

	    public MergeVars(String email, String fname, String lname) {
	        this.EMAIL = email;
	        this.FNAME = fname;
	        this.LNAME = lname;
	    }
	}

}


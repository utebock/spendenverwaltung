package at.fraubock.spendenverwaltung.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

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
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class MailChimpServiceImplemented implements IMailChimpService{
	
	private static final Logger log = Logger.getLogger(MailChimpServiceImplemented.class);
	private MailChimpClient mailChimp= null;
	private String apiKey;
	private final Boolean double_optin = false; //Dont send double optin requests to new e-mail addresses
	private final Boolean update_extisting = true; //update old data
	
	
	public MailChimpServiceImplemented(){	
		mailChimp = new MailChimpClient();
		
		Preferences prefs = Preferences.userNodeForPackage(MailChimpServiceImplemented.class);
		
		//Default API Key is set now and can be removed later
		apiKey = prefs.get("apiKey", "a66d44c0489c37f1960447b70ee480c7-us7");
	}
	
	
	public int addPersonsToList(String listId, List<Person> persons) throws ServiceException{
		List<MergeVars> subscribers = new ArrayList<MergeVars>();
		ListBatchSubscribeResult result;
		ListBatchSubscribeMethod subscribeMethod;
		if(listId == null || persons == null){
			throw new IllegalArgumentException("Parameter must not be null");
		}
		
		log.debug("Add "+persons+" to MailChimp list "+listId);
		
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
	
	
	
	public List<MailChimpListItem> getLists() throws ServiceException{
		List<MailChimpListItem> list = new ArrayList<MailChimpListItem>();
		ListsResult result;
		
		log.debug("Get MailChimp Lists");
		
		ListsMethod listsMethod = new ListsMethod();
		listsMethod.apikey = apiKey;
		
		try {
			result = mailChimp.execute(listsMethod);
		} catch (IOException | MailChimpException e) {
			throw new ServiceException(e);
		}
		
		//Only return id and name
		for(ListInformation info : result.data){
			list.add(new MailChimpListItem(info.id, info.name));
		}
		
		return list;
	}
	
	
	@Override
	public void setAPIKey(String apiKey) throws ServiceException {
		ListsMethod listsMethod = new ListsMethod();
		listsMethod.apikey = apiKey;
		
		try {
			mailChimp.execute(listsMethod);
		} catch (IOException | MailChimpException e) {
			throw new ServiceException(e);
		}
		catch(IllegalArgumentException e){
			throw new ServiceException("API key invalid");
		}
		
		this.apiKey = apiKey;
		Preferences prefs = Preferences.userNodeForPackage(MailChimpServiceImplemented.class);
		
		//save in preference
		prefs.put("apiKey", apiKey);
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


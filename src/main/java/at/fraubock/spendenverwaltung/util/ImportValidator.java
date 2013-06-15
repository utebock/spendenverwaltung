package at.fraubock.spendenverwaltung.util;

import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class ImportValidator {
	private ValidatedData validatedData;
	private IPersonService personService;
	private IDonationService donationService;
	
	public ImportValidator(IPersonService personService, IDonationService donationService){
		this.validatedData = new ValidatedData();
		this.personService = personService;
		this.donationService = donationService;
	}
	
	public static enum ConflictType {
		DUPLICATE,
		ANONYM
	};
	


	public static enum ValidationType {
		EDIT("bearbeiten"), ANONYM("anonym"), NEW_DONATOR("Spender zuweisen"), NOT_IMPORT("nicht importieren");

		private final String name;

		private ValidationType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static ValidationType getByName(String name) {
			switch (name) {
			case "anonym":
				return ANONYM;
			case "Spender zuweisen":
				return NEW_DONATOR;
			case "nicht importieren":
				return NOT_IMPORT;
			case "bearbeiten":
				return EDIT;
			default:
				throw new IllegalArgumentException(
						"No validation type for name: " + name);
			}
		}
		
		public static int indexOf(ValidationType type){
			switch(type){
			case EDIT:
				return 0;
			case ANONYM:
				return 1;
			case NEW_DONATOR:
				return 2;
			case NOT_IMPORT:
				return 3;
			default:
				throw new IllegalArgumentException(
						"No validation type for name: " + type.toString());
			}
		}
		
		public static String[] toArray(){
			return new String[]{ "bearbeiten", "anonym", "Spender zuweisen", "nicht importieren" };
		}
	};
	
	public IPersonService getPersonService() {
		return personService;
	}

	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public ValidatedData validate(List<Person> persons, List<Donation> donations) throws ServiceException{
		validatedData = new ValidatedData();
		Person matchedPerson;
		Person currentPerson;
		Donation currentDonation;
		
		for(int i=0; i<persons.size(); i++){
			currentPerson = persons.get(i);
			currentDonation = donations.get(i);
			matchedPerson = getExistingPerson(currentPerson);
			
			
			if(matchedPerson == null){
				// check if person has a unique identifier
				if( currentPerson != null
						&& (!currentPerson.getTelephone().equals("")
						|| !currentPerson.getEmail().equals("")
						|| currentPerson.getMainAddress() != null)){

					//person doesn't exist -> create new
					validatedData.addNewEntry(currentPerson, currentDonation);
				} else{
					//no unique identifier -> conflict
					validatedData.addConflictEntry(currentPerson, currentDonation, ConflictType.ANONYM);
				}
			} else{
				
				//unique identifier found -> person matched
				currentDonation.setDonator(matchedPerson);

				//validate next entry, if donation is a duplicate
				if(isDuplicate(currentDonation)){
					validatedData.addConflictEntry(currentPerson, currentDonation, ConflictType.DUPLICATE);
					validatedData.addDonationToDelete(currentDonation);
					validatedData.addPersonToDelete(currentPerson);
					continue;
				}
				
				validatedData.addMatchEntry(matchedPerson, currentDonation);
				validatedData.addPersonToDelete(currentPerson);
			}
		}
		
		return validatedData;
	}
	
	public Person getExistingPerson(Person p) throws ServiceException{
		List<Person> matchedPersons = new ArrayList<Person>();
		
		matchedPersons = personService.getByAttributes(p);
		
		if(matchedPersons.size() == 0)
			return null;
		else
			return matchedPersons.get(0);
	}
	
	private boolean isDuplicate(Donation d) throws ServiceException{
		return donationService.donationExists(d);
	}
}

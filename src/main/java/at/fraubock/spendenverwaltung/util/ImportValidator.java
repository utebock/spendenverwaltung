package at.fraubock.spendenverwaltung.util;

import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class ImportValidator {
	private ValidatedData validatedData;
	private IPersonService personService;
	
	public ImportValidator(IPersonService personService){
		this.validatedData = new ValidatedData();
		this.personService = personService;
	}
	
	public static enum ConflictType {
		DUPLICATE,
		ANONYM
	};
	
	public IPersonService getPersonService() {
		return personService;
	}

	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public ValidatedData validate(List<Person> persons, List<Donation> donations) throws ServiceException{
		Person matchedPerson;
		Person currentPerson;
		Donation currentDonation;
		
		for(int i=0; i<persons.size(); i++){
			currentPerson = persons.get(i);
			currentDonation = donations.get(i);
			matchedPerson = getExistingPerson(currentPerson);
			
			
			if(matchedPerson == null){
				//person doesn't exist -> create new
				validatedData.addNewEntry(currentPerson, currentDonation);
			} else{
				// check if person hass a unique identifier
				if(!currentPerson.getTelephone().equals("")
						|| !currentPerson.getEmail().equals("")
						|| currentPerson.getMainAddress() != null){

					//validate next entry, if donation is a duplicate
					if(isDuplicate(currentDonation)){
						validatedData.addConflictEntry(currentPerson, currentDonation, ConflictType.DUPLICATE);
						continue;
					}
					
					//unique identifier found -> person matched
					validatedData.addMatchEntry(currentPerson, currentDonation);
				} else{
					//no unique identifier -> conflict
					validatedData.addConflictEntry(currentPerson, currentDonation, ConflictType.ANONYM);
				}
			}
		}
		
		return validatedData;
	}
	
	private Person getExistingPerson(Person p) throws ServiceException{
		List<Person> matchedPersons = new ArrayList<Person>();
		
		matchedPersons = personService.getByAttributes(p);
		
		if(matchedPersons.size() == 0)
			return null;
		else
			return matchedPersons.get(0);
	}
	
	private boolean isDuplicate(Donation d){
		return false;
	}
}

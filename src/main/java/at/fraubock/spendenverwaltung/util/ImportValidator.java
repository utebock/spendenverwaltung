package at.fraubock.spendenverwaltung.util;

import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;

public class ImportValidator {
	private ValidatedData validatedData;
	
	public ImportValidator(){
		this.validatedData = new ValidatedData();
	}
	
	public ValidatedData validate(List<Person> persons, List<Donation> donations){
		Person matchedPerson;
		Person currentPerson;
		Donation currentDonation;
		
		for(int i=0; i<persons.size(); i++){
			currentPerson = persons.get(i);
			currentDonation = donations.get(i);
			matchedPerson = getExistingPerson(currentPerson);
			
			if(matchedPerson == null){
				validatedData.addNewEntry(matchedPerson, currentDonation);
			} else{
				if(!currentPerson.getTelephone().equals("")
						|| !currentPerson.getEmail().equals("")
						|| currentPerson.getMainAddress() != null){
					validatedData.addMatchEntry(currentPerson, currentDonation);
				} else{
					validatedData.addConflictEntry(currentPerson, currentDonation);
				}
			}
		}
		
		return validatedData;
	}
	
	private Person getExistingPerson(Person p){
		Person matchedPerson = new Person();
		
		return matchedPerson;
	}
}

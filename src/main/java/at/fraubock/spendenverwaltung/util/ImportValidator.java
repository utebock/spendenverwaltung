/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.util;

import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
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
	
	public List<Person> checkPersonDoublesInNewEntries(List<Donation> toCheck){
		List<Person> uniquePersons = new ArrayList<Person>();
		List<Person> toDelete = new ArrayList<Person>();
		Person doublePerson;
		
		for(Donation d : toCheck){
			doublePerson = getDoublePersonIdFromList(d.getDonator(), uniquePersons);
			
			if(doublePerson == null){
				uniquePersons.add(d.getDonator());
			} else{
				toDelete.add(d.getDonator());
				d.setDonator(doublePerson);
			}
		}
		
		return toDelete;
	}
	
	public Person getDoublePersonIdFromList(Person p, List<Person> checkList){
		Person doublePerson = null;
		
		for(Person donator : checkList){
			if(p != null){
				if(donator.getSurname().equals(p.getSurname())
						&& donator.getGivenName().equals(p.getGivenName())
						&& (donator.getEmail().equals(p.getEmail())
							|| (!donator.getTelephone().equals("") && donator.getTelephone().equals(p.getTelephone()))
							|| (donator.getMainAddress() != null && donator.getMainAddress().getCity().equals(p.getMainAddress().getCity())
									&& donator.getMainAddress().getPostalCode().equals(p.getMainAddress().getPostalCode())
									&& donator.getMainAddress().getStreet().equals(p.getMainAddress().getStreet())))){
					return donator;
				}
			}
		}
		
		return doublePerson;
	}
}

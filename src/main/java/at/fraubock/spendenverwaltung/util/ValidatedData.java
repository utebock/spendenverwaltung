package at.fraubock.spendenverwaltung.util;

import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.util.ImportValidator.ConflictType;

public class ValidatedData {
	private List<Donation> donationListConflict;
	private List<Person> personListConflict;
	private List<ConflictType> conflictTypes;
	private List<Donation> donationListNew;
	private List<Person> personListNew;
	private List<Donation> donationListMatch;
	private List<Person> personListMatch;
	private List<Person> personsToDelete;
	private List<Donation> donationsToDelete;
	
	public ValidatedData(){
		this.donationListConflict = new ArrayList<Donation>();
		this.donationListNew = new ArrayList<Donation>();
		this.donationListMatch = new ArrayList<Donation>();
		
		this.personListConflict = new ArrayList<Person>();
		this.personListNew = new ArrayList<Person>();
		this.personListMatch = new ArrayList<Person>();
		
		this.conflictTypes = new ArrayList<ConflictType>();
		
		this.personsToDelete = new ArrayList<Person>();
		this.donationsToDelete = new ArrayList<Donation>();
	}
	
	public List<Donation> getDonationListConflict() {
		return donationListConflict;
	}
	public List<Person> getPersonListConflict() {
		return personListConflict;
	}
	public List<Donation> getDonationListNew() {
		return donationListNew;
	}
	public List<Person> getPersonListNew() {
		return personListNew;
	}
	public List<Donation> getDonationListMatch() {
		return donationListMatch;
	}
	public List<Person> getPersonListMatch() {
		return personListMatch;
	}
	public List<ConflictType> getConflictTypes() {
		return conflictTypes;
	}
	public List<Person> getPersonsToDelete() {
		return personsToDelete;
	}
	public List<Donation> getDonationsToDelete() {
		return donationsToDelete;
	}
	
	
	public void addConflictEntry(Person p, Donation d, ConflictType t){
		personListConflict.add(p);
		donationListConflict.add(d);
		conflictTypes.add(t);
	}
	public void addNewEntry(Person p, Donation d){
		personListNew.add(p);
		donationListNew.add(d);
	}
	public void addMatchEntry(Person p, Donation d){
		personListMatch.add(p);
		donationListMatch.add(d);
	}
	public void addPersonToDelete(Person p){
		personsToDelete.add(p);
	}
	public void addDonationToDelete(Donation d){
		donationsToDelete.add(d);
	}
	
	public void checkPersonDoublesInNewEntries(){
		List<Person> uniquePersons = new ArrayList<Person>();
		Person doublePerson;
		
		for(Donation d : donationListNew){
			doublePerson = getDoublePersonIdFromList(d.getDonator(), uniquePersons);
			
			if(doublePerson == null){
				uniquePersons.add(d.getDonator());
			} else{
				personsToDelete.add(d.getDonator());
				d.setDonator(doublePerson);
			}
		}
	}
	
	private Person getDoublePersonIdFromList(Person p, List<Person> checkList){
		Person doublePerson = null;
		
		for(Person donator : checkList){
			
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
		
		return doublePerson;
	}
	
	public void removeEntryFromList(Person person, Donation donation){
		for(Person p : personListNew){
			if(person == p){
				personsToDelete.add(person);
				personListNew.remove(person);
			}
		}
		for(Person p : personListMatch){
			if(person == p){
				personsToDelete.add(person);
				personListMatch.remove(person);
			}
		}
		for(Person p : personListConflict){
			if(person == p){
				personsToDelete.add(person);
				personListConflict.remove(person);
			}
		}

		for(Donation d : donationListNew){
			if(donation == d){
				donationsToDelete.add(donation);
				donationListNew.remove(donation);
			}
		}
		for(Donation d : donationListMatch){
			if(donation == d){
				donationsToDelete.add(donation);
				donationListMatch.remove(donation);
			}
		}
		for(Donation d : donationListConflict){
			if(donation == d){
				donationsToDelete.add(donation);
				donationListConflict.remove(donation);
			}
		}
	}
}

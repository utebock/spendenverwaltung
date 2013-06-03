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
	
	public ValidatedData(){
		this.donationListConflict = new ArrayList<Donation>();
		this.donationListNew = new ArrayList<Donation>();
		this.donationListMatch = new ArrayList<Donation>();
		
		this.personListConflict = new ArrayList<Person>();
		this.personListNew = new ArrayList<Person>();
		this.personListMatch = new ArrayList<Person>();
		
		this.conflictTypes = new ArrayList<ConflictType>();
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
}

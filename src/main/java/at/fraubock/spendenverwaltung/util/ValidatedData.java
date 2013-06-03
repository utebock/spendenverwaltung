package at.fraubock.spendenverwaltung.util;

import java.util.ArrayList;
import java.util.List;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;

public class ValidatedData {
	private List<Donation> donationListConflict;
	private List<Person> personListConflict;
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
	}
	
	public List<Donation> getDonationListConflict() {
		return donationListConflict;
	}
	public void setDonationListConflict(List<Donation> donationListConflict) {
		this.donationListConflict = donationListConflict;
	}
	public List<Person> getPersonListConflict() {
		return personListConflict;
	}
	public void setPersonListConflict(List<Person> personListConflict) {
		this.personListConflict = personListConflict;
	}
	public List<Donation> getDonationListNew() {
		return donationListNew;
	}
	public void setDonationListNew(List<Donation> donationListNew) {
		this.donationListNew = donationListNew;
	}
	public List<Person> getPersonListNew() {
		return personListNew;
	}
	public void setPersonListNew(List<Person> personListNew) {
		this.personListNew = personListNew;
	}
	public List<Donation> getDonationListMatch() {
		return donationListMatch;
	}
	public void setDonationListMatch(List<Donation> donationListMatch) {
		this.donationListMatch = donationListMatch;
	}
	public List<Person> getPersonListMatch() {
		return personListMatch;
	}
	public void setPersonListMatch(List<Person> personListMatch) {
		this.personListMatch = personListMatch;
	}
	
	public void addConflictEntry(Person p, Donation d){
		personListConflict.add(p);
		donationListConflict.add(d);
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

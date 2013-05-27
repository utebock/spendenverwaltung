package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.ArrayList;
import java.util.List;

public class Mailing {
	
	public static enum Medium {
		EMAIL, POSTAL
	}
	
	private List<Person> persons = new ArrayList<Person>();
	
	//TODO:enum possible for type?
	private String type;
	private Medium medium;
	
	//TODO: private SomeType template;
	
	public void addPerson(Person person) {
		persons.add(person);
	}
	
	public void removePerson(Person person) {
		persons.remove(person);
	}
	
	public List<Person> getPersons() {
		return persons;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Medium getMedium() {
		return medium;
	}

	public void setMedium(Medium medium) {
		this.medium = medium;
	}
}

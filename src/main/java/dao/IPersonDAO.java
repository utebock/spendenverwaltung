package dao;

import java.util.List;

import domain.Person;

public interface IPersonDAO {
	
	/**
	 * Creates a new person
	 * @param p Person to be created
	 * @return Fully defined person
	 */
	public Person create(Person p);
	
	/**
	 * Updates an existing person
	 * @param p Person to be updated
	 * @return Updated person
	 */
	public Person update(Person p);
	
	/**
	 * Deletes an existing person
	 * @param p Person to be deleted
	 */
	public void delete(Person p);

	/**
	 * Retrieves entire Person table
	 * @return List of all persons
	 */
	public List<Person> getAll();
	
	/**
	 * Retrieves Person by ID
	 * @param id unique person identification number
	 * @return Person based on retrieved id
	 */
	public Person getByID(int id);
}

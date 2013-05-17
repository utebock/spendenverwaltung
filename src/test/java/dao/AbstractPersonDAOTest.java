package dao;

import org.junit.Test;


public abstract class AbstractPersonDAOTest {
	
	//TODO you should use the interface here, 
	// since you don't know which implementation
	// you will be testing yet (abstract test!) -pm
	protected PersonDAOImplemented personDAO;
	
	@Test
	public void createShouldCreatePerson(){
		
	}
	
	@Test
	public void createWithInvalidValuesShouldThrowException(){
		
	}
	
	@Test
	public void updateShouldUpdatePerson(){
		
	}
	
	@Test
	public void updateWithInvalidValuesShouldThrowException(){
		
	}
	
	@Test
	public void updateNonExistentPersonShouldThrowException(){
		
	}
	
	@Test
	public void getByIdShouldGetPersonByID(){
		
	}
	
	@Test
	public void getByIdOfNonExistentPersonShouldThrowException(){
		
	}
	
	@Test
	public void deleteShouldDeletePerson(){
		
	}
	
	@Test
	public void deleteNonExistentPersonShouldThrowException(){
		
	}
	
	@Test
	public void getAllShouldGetAllPersons(){
		
	}
	
	@Test
	public void getAllShouldThrowException(){
		
	}
}
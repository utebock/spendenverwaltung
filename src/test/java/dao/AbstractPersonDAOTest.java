package dao;

import org.junit.Test;


public abstract class AbstractPersonDAOTest {
	
	//FIXME the interface should be used here, since we don't know
	// which impl will be tested at this point (abstract testclass!) -pm
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

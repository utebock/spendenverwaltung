package dao;


import java.util.List;

import domain.Person;

public class PersonDAOImplemented implements IPersonDAO{
	
	private IAddressDAO addressDao;
	
	public void setAddressDao(IAddressDAO addressDao) {
		this.addressDao = addressDao;
	}
	
	@Override
	public Person create(Person p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person update(Person p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Person p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Person> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person getByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}


}

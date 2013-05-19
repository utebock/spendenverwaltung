package dao;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import domain.Address;
import exceptions.PersistenceException;

/**
 * implementation of {@link IAddressDAO}
 * 
 * @author philipp muhoray
 * 
 */
public class AddressDAOImplemented implements IAddressDAO {

	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Address create(Address address) throws PersistenceException {
		
		if(address == null){
			throw new IllegalArgumentException("Person must not be null");
		}
		
		String createAddress = "insert into addresses (street_no, postal_code, city, country) values (?,?,?,?)";
		
		Object[] params = new Object[] {address.getStreet(), address.getPostalCode(), address.getCity(), address.getCountry()};
		
		int[] types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
		
		/**
		 * set address id to update result
		 */
		address.setId(jdbcTemplate.update(createAddress, params, types));
		
		return address;
	}

	@Override
	public Address update(Address a) throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Address a) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Address> getAll() throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address getByID(int id) throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

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
	
	private class CreateAddressStatementCreator implements PreparedStatementCreator {

		private Address address;
		
		CreateAddressStatementCreator(Address address) {
			this.address = address;
		}
		
		String createAddress = "insert into addresses (street_no, postal_code, city, country) values (?,?,?,?)";
				
		@Override
		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createAddress, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, address.getStreet());
			ps.setString(2, Integer.toString(address.getPostalCode()));
			ps.setString(3, address.getCity());
			ps.setString(4, address.getCountry());
			return ps;
		}
	}
	
	@Override
	public Address create(Address address) throws PersistenceException {
		
		if(address == null){
			throw new IllegalArgumentException("Person must not be null");
		}
		
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new CreateAddressStatementCreator(address), keyHolder);
		
		/**
		 * set address id to update result
		 */
		address.setId(keyHolder.getKey().intValue());
		
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

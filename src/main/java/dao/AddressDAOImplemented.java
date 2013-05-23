package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import service.AddressValidator;
import domain.Address;
import domain.Person;
import exceptions.PersistenceException;

/**
 * implementation of {@link IAddressDAO}
 * 
 * @author philipp muhoray
 * 
 */
public class AddressDAOImplemented implements IAddressDAO {

	private static final Logger log = Logger
			.getLogger(AddressDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private class CreateAddressStatementCreator implements
			PreparedStatementCreator {

		private Address address;

		CreateAddressStatementCreator(Address address) {
			this.address = address;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			String createAddress = "insert into addresses (street, postcode, "
					+ "city, country) values (?,?,?,?)";

			PreparedStatement ps = connection.prepareStatement(createAddress,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, address.getStreet());
			ps.setString(2, address.getPostalCode());
			ps.setString(3, address.getCity());
			ps.setString(4, address.getCountry());
			return ps;
		}
	}

	private class UpdateAddressStatementCreator implements
			PreparedStatementCreator {

		private Address address;

		UpdateAddressStatementCreator(Address address) {
			this.address = address;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			String updateAddress = "update addresses set street=?, "
					+ "postcode=?, city=?, country=? where id=?";

			PreparedStatement ps = connection.prepareStatement(updateAddress);
			ps.setString(1, address.getStreet());
			ps.setString(2, address.getPostalCode());
			ps.setString(3, address.getCity());
			ps.setString(4, address.getCountry());
			ps.setInt(5, address.getId());
			return ps;
		}
	}

	@Override
	public Address create(final Address a) throws PersistenceException {
		log.info("Creating Address...");

		AddressValidator.validate(a);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new CreateAddressStatementCreator(a), keyHolder);

		// set address id to update result
		a.setId(keyHolder.getKey().intValue());

		log.info("Address entity successfully created:" + a.toString());
		return a;
	}

	@Override
	public Address update(final Address a) throws PersistenceException {
		log.info("Updating Address...");
		AddressValidator.validate(a);
		jdbcTemplate.update(new UpdateAddressStatementCreator(a));
		log.info("Address entity successfully updated:" + a.toString());
		return a;
	}

	@Override
	public void delete(final Address a) throws PersistenceException {
		log.info("Deleting Address...");
		AddressValidator.validate(a);
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String updateAddress = "delete from addresses where id=?";

				PreparedStatement ps = con.prepareStatement(updateAddress);
				ps.setInt(1, a.getId());
				return ps;
			}

		});

		log.info("Address entity successfully deleted:" + a.toString());
	}

	@Override
	public List<Address> getAll() throws PersistenceException {
		log.info("Reading all Addresses.");
		return jdbcTemplate.query("select * from addresses",
				new AddressMapper());
	}

	@Override
	public Address getByID(int id) throws PersistenceException {
		log.info("Reading Address with id='" + id + "'");
		if (id < 0) {
			log.info("Error reading Address with id='" + id + "': Id was less than 0");
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		return jdbcTemplate.queryForObject(
				"select * from addresses where id = ?;", new Object[] { id },
				new AddressMapper());
	}
	
	@Override
	public Address getMainAddressByPerson(Person person) {
		return jdbcTemplate.queryForObject("select id,city,country,postcode,street from " +
				" addresses a,livesat l where l.addressid=a.id and l.personid = ? and l.ismain=true",
				new Object[] { person.getId() }, new int[] {Types.INTEGER}, new AddressMapper());
	}

	private class AddressMapper implements RowMapper<Address> {

		public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
			Address address = new Address();
			address.setId(rs.getInt("id"));
			address.setCity(rs.getString("city"));
			address.setCountry(rs.getString("country"));
			address.setPostalCode(rs.getString("postcode"));
			address.setStreet(rs.getString("street"));
			return address;
		}
	}
}

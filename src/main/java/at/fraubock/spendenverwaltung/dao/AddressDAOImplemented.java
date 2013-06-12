package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IAddressDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;

/**
 * implementation of {@link IAddressDAO}
 * 
 * @author philipp muhoray
 * @author manuel-bichler
 * 
 */
public class AddressDAOImplemented implements IAddressDAO {

	private static final Logger log = Logger
			.getLogger(AddressDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * checks the integrity of any {@link Address} entity TODO not a satisfying
	 * solution, refactoring needed (program logic implemented by exceptions,
	 * doesnt propagate errors, etc)
	 * 
	 * @author philipp muhoray
	 * @throws ValidationException
	 * 
	 */

	private static void validate(Address address) throws ValidationException {
		if (address == null) {
			log.error("Argument was null");
			throw new ValidationException("Address must not be null");
		}

		if (address.getStreet() == null) {
			log.error("Street was null");
			throw new ValidationException("Street must not be null");
		}

		if (address.getCity() == null) {
			log.error("City was null");
			throw new ValidationException("City must not be null");
		}

		if (address.getCountry() == null) {
			log.error("Country was null");
			throw new ValidationException("Country must not be null");
		}
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
	public void insertOrUpdate(final Address a) throws PersistenceException {
		try {
			validate(a);

			if (a.getId() == null) {
				log.info("Inserting Address...");

				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new CreateAddressStatementCreator(a),
						keyHolder);

				// set address id to update result
				a.setId(keyHolder.getKey().intValue());

				log.info("Address entity successfully created: " + a.toString());
			} else {
				log.info("Updating Address...");
				jdbcTemplate.update(new UpdateAddressStatementCreator(a));
				log.info("Address entity successfully updated: " + a.toString());
			}
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public void delete(final Address a) throws PersistenceException {
		try {
			log.info("Deleting Address...");
			try {
				validate(a);
				jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(
							Connection con) throws SQLException {
						String updateAddress = "delete from addresses where id=?";

						PreparedStatement ps = con
								.prepareStatement(updateAddress);
						ps.setInt(1, a.getId());
						return ps;
					}

				});
			} catch (ValidationException e) {
				throw new PersistenceException(e);
			}
			log.info("Address entity successfully deleted:" + a.toString());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Address> getAll() throws PersistenceException {
		try {
			log.info("Reading all Addresses.");
			return jdbcTemplate.query(
					"select * from addresses ORDER BY id DESC",
					new AddressMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public Address getByID(int id) throws PersistenceException {
		try {
			log.info("Reading Address with id='" + id + "'");
			if (id < 0) {
				log.info("Error reading Address with id='" + id
						+ "': Id was less than 0");
				throw new IllegalArgumentException("Id must not be less than 0");
			}

			try {
				return jdbcTemplate.queryForObject(
						"select * from addresses where id = ?;",
						new Object[] { id }, new AddressMapper());
			} catch (IncorrectResultSizeDataAccessException e) {
				if (e.getActualSize() == 0)
					return null;
				else
					throw new PersistenceException(e);
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
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

	@Override
	public List<Address> getConfirmed() throws PersistenceException {
		try {
			log.info("Reading confirmed Addresses.");
			return jdbcTemplate.query(
					"select * from validated_addresses ORDER BY id DESC",
					new AddressMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

}

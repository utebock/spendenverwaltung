package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IImportDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.DonationValidator;
import at.fraubock.spendenverwaltung.util.FilterToSqlBuilder;

/**
 * 
 * @author manuel-bichler
 * 
 */
public class DonationDAOImplemented implements IDonationDAO {

	private JdbcTemplate jdbcTemplate;
	private IPersonDAO personDAO;
	private IImportDAO importDAO;
	private DonationValidator donationValidator;
	private FilterToSqlBuilder filterToSqlBuilder;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setPersonDao(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public void setImportDao(IImportDAO importDAO) {
		this.importDAO = importDAO;
	}

	public void setDonationValidator(DonationValidator donationValidator) {
		this.donationValidator = donationValidator;
	}

	private class CreateDonationStatementCreator implements
			PreparedStatementCreator {

		private Donation donation;

		CreateDonationStatementCreator(Donation donation) {
			this.donation = donation;
		}

		private String createDonations = "insert into donations (personid, amount, donationdate, dedication, type, note, import) values (?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createDonations,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, donation.getDonator().getId());
			ps.setDouble(2, donation.getAmount());
			ps.setTimestamp(3, new Timestamp(donation.getDate().getTime()));
			ps.setString(4, donation.getDedication());
			ps.setString(5, donation.getType().getName());
			ps.setString(6, donation.getNote());
			if (donation.getSource() == null)
				ps.setNull(7, Types.INTEGER);
			else
				ps.setInt(7, donation.getSource().getId());

			return ps;
		}
	}

	@Override
	public void insertOrUpdate(Donation d) throws PersistenceException {
		donationValidator.validate(d);

		if (d.getId() == null) {
			// insert
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateDonationStatementCreator(d),
					keyHolder);

			d.setId(keyHolder.getKey().intValue());

		} else {
			// update
			String updateStatement = "update donations set personid = ?, amount = ?, donationdate = ?, dedication = ?, type = ?, note = ? where id = ?";

			Object[] params = new Object[] { d.getDonator().getId(),
					d.getAmount(), d.getDate(), d.getDedication(),
					d.getType().getName(), d.getNote(), d.getId() };

			int[] types = new int[] { Types.INTEGER, Types.BIGINT,
					Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR,
					Types.VARCHAR, Types.INTEGER };

			jdbcTemplate.update(updateStatement, params, types);
		}
	}

	@Override
	public void delete(Donation d) throws PersistenceException {
		donationValidator.validate(d);

		String deleteStatement = "delete from donations where id = ?";

		Object[] params = new Object[] { d.getId() };

		int[] types = new int[] { Types.INTEGER };

		jdbcTemplate.update(deleteStatement, params, types);
	}

	@Override
	public List<Donation> getConfirmed() throws PersistenceException {

		String select = "SELECT * FROM validated_donations ORDER BY id DESC";

		List<Donation> donations = jdbcTemplate.query(select,
				new DonationMapper());

		return donations;
	}

	@Override
	public List<Donation> getUnconfirmed() throws PersistenceException {

		String select = "SELECT * FROM donations WHERE import IS NOT NULL ORDER BY id DESC";

		List<Donation> donations = jdbcTemplate.query(select,
				new DonationMapper());

		return donations;
	}

	@Override
	public Donation getByID(int id) throws PersistenceException {

		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from donations where id = ?";

		try {
			return jdbcTemplate.queryForObject(select, new Object[] { id },
					new DonationMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}

	@Override
	public List<Donation> getByPerson(Person p) throws PersistenceException {
		if (p == null) {
			throw new IllegalArgumentException("person must not be null");
		}

		String select = "select * from donations where personid = ? ORDER BY id DESC";

		List<Donation> donations = jdbcTemplate.query(select,
				new Object[] { p.getId() }, new DonationMapper());

		return donations;
	}

	public FilterToSqlBuilder getFilterToSqlBuilder() {
		return filterToSqlBuilder;
	}

	public void setFilterToSqlBuilder(FilterToSqlBuilder filterToSqlBuilder) {
		this.filterToSqlBuilder = filterToSqlBuilder;
	}

	private class DonationMapper implements RowMapper<Donation> {

		public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
			Donation donation = new Donation();
			donation.setId(rs.getInt("id"));
			try {
				donation.setDonator(personDAO.getById(rs.getInt("personid")));
				donation.setSource(importDAO.getByID(rs.getInt("import")));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			donation.setAmount(rs.getLong("amount"));
			donation.setDate(rs.getDate("donationdate"));
			donation.setDedication(rs.getString("dedication"));
			donation.setNote(rs.getString("note"));
			donation.setType(Donation.DonationType.getByName(rs
					.getString("type")));

			return donation;
		}
	}

	@Override
	public List<Donation> getAll() throws PersistenceException {
		String select = "SELECT * FROM donations ORDER BY id DESC";

		List<Donation> donations = jdbcTemplate.query(select,
				new DonationMapper());

		return donations;
	}

	@Override
	public List<Donation> getByImport(Import i) throws PersistenceException {
		String select = "SELECT * FROM donations WHERE import = ? ORDER BY id DESC";

		List<Donation> donations = jdbcTemplate.query(select,
				new Object[] { i.getId() }, new DonationMapper());

		return donations;
	}

}

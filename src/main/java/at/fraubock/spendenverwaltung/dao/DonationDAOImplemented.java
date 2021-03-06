/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
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
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;
import at.fraubock.spendenverwaltung.util.filter.FilterBuilder;

/**
 * 
 * @author manuel-bichler
 * 
 */
public class DonationDAOImplemented implements IDonationDAO {

	private JdbcTemplate jdbcTemplate;
	private IPersonDAO personDAO;
	private IImportDAO importDAO;
	private FilterBuilder filterBuilder;

	private static final Logger log = Logger
			.getLogger(DonationDAOImplemented.class);

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setPersonDao(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public void setImportDao(IImportDAO importDAO) {
		this.importDAO = importDAO;
	}

	private static void validate(Donation d) throws ValidationException {
		if (d == null)
			throw new ValidationException("Donation must not be null");
		if (d.getAmount() == null) {
			throw new ValidationException("Amount must not be null");
		} else if (d.getAmount() < 0) {
			throw new ValidationException("Amount must not be less than 0");
		}
		if (d.getDate() == null)
			throw new ValidationException("Date must not be null");
		if (d.getType() == null)
			throw new ValidationException("Type must not be null");
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
			if (donation.getDonator() == null)
				ps.setNull(1, Types.INTEGER);
			else
				ps.setInt(1, donation.getDonator().getId());
			ps.setDouble(2, donation.getAmount());
			ps.setTimestamp(3, new Timestamp(donation.getDate().getTime()));
			ps.setString(4, donation.getDedication());
			ps.setString(5, donation.getType().getName());
			ps.setString(6, donation.getNote());
			if (donation.getSource() == null)
				ps.setNull(7, Types.NULL);
			else
				ps.setInt(7, donation.getSource().getId());

			return ps;
		}
	}

	@Override
	public void insertOrUpdate(Donation d) throws PersistenceException {
		try {
			try {
				validate(d);
			} catch (ValidationException e) {
				throw new PersistenceException(e);
			}

			if (d.getId() == null) {
				// insert
				KeyHolder keyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(new CreateDonationStatementCreator(d),
						keyHolder);

				d.setId(keyHolder.getKey().intValue());

			} else {
				// update
				String updateStatement = "update donations set personid = ?, amount = ?, donationdate = ?, dedication = ?, type = ?, note = ? where id = ?";

				Object[] params = new Object[] {
						d.getDonator() == null ? null : d.getDonator().getId(),
						d.getAmount(), d.getDate(), d.getDedication(),
						d.getType().getName(), d.getNote(), d.getId() };

				int[] types = new int[] { Types.INTEGER, Types.BIGINT,
						Types.TIMESTAMP, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.INTEGER };

				jdbcTemplate.update(updateStatement, params, types);
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public void delete(Donation d) throws PersistenceException {
		try {
			try {
				validate(d);
			} catch (ValidationException e) {
				throw new PersistenceException(e);
			}

			String deleteStatement = "delete from donations where id = ?";

			Object[] params = new Object[] { d.getId() };

			int[] types = new int[] { Types.INTEGER };

			jdbcTemplate.update(deleteStatement, params, types);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Donation> getConfirmed() throws PersistenceException {
		try {

			String select = "SELECT * FROM validated_donations ORDER BY id DESC";

			List<Donation> donations = jdbcTemplate.query(select,
					new DonationMapper());

			return donations;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Donation> getUnconfirmed(Import toImport)
			throws PersistenceException {
		try {
			String select = "SELECT * FROM donations WHERE import=? ORDER BY id DESC";

			List<Donation> donations = jdbcTemplate.query(select,
					new Object[] { toImport.getId() }, new DonationMapper());

			return donations;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public Donation getByID(int id) throws PersistenceException {
		try {

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
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Donation> getByPerson(Person p) throws PersistenceException {
		try {
			if (p == null || p.getId() == null || p.getId() < 1) {
				throw new IllegalArgumentException("Passed person was invalid");
			}

			String select = "select * from validated_donations where personid = ? ORDER BY id DESC";

			List<Donation> donations = jdbcTemplate.query(select,
					new Object[] { p.getId() }, new DonationMapper());

			return donations;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	public FilterBuilder getFilterBuilder() {
		return filterBuilder;
	}

	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}

	private class DonationMapper implements RowMapper<Donation> {

		public Donation mapRow(ResultSet rs, int rowNum) throws SQLException {
			Donation donation = new Donation();
			donation.setId(rs.getInt("id"));
			try {
				int personId = rs.getInt("personid");
				if (!rs.wasNull())
					donation.setDonator(personDAO.getById(personId));
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
		try {
			String select = "SELECT * FROM donations ORDER BY id DESC";

			List<Donation> donations = jdbcTemplate.query(select,
					new DonationMapper());

			return donations;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Donation> getByImport(Import i) throws PersistenceException {
		try {
			String select = "SELECT * FROM donations WHERE import = ? ORDER BY id DESC";

			List<Donation> donations = jdbcTemplate.query(select,
					new Object[] { i.getId() }, new DonationMapper());

			return donations;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Donation> getByFilter(Filter filter)
			throws PersistenceException {
		try {
			String select = filterBuilder.createStatement(filter);
			List<Donation> DonationList = jdbcTemplate.query(select,
					new DonationMapper());
			log.info(DonationList.size() + " list size");

			return DonationList;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public void setImportToNull(List<Donation> donationList)
			throws PersistenceException {
		try {
			String updateStmt = "UPDATE donations SET import=null WHERE id=?";

			for (Donation d : donationList) {
				jdbcTemplate.update(updateStmt, new Object[] { d.getId() },
						new int[] { Types.INTEGER });
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public boolean donationExists(Donation d) throws PersistenceException {
		try {
			String select = "SELECT * FROM validated_donations WHERE personid=? AND amount=? AND donationdate=? AND dedication=? AND type=? AND note=?";

			Object[] params = new Object[] { d.getDonator().getId(),
					d.getAmount(), d.getDate(), d.getDedication(),
					d.getType().getName(), d.getNote() };

			List<Donation> donations = jdbcTemplate.query(select, params,
					new DonationMapper());

			if (donations.size() == 0)
				return false;
			else
				return true;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

}

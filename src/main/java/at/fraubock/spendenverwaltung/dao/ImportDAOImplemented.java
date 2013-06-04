package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IImportDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;

/**
 * Implementation of {@link IImportDAO} for MySQL 5.1 database
 * 
 * @author manuel-bichler
 * 
 */
public class ImportDAOImplemented implements IImportDAO {

	private JdbcTemplate jdbcTemplate;

	private static final Logger log = Logger
			.getLogger(ImportDAOImplemented.class);

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @param i
	 *            any import
	 * @throws ValidationException
	 *             if the given import would not fit into the database schema.
	 */
	private static void validate(Import i) throws ValidationException {
		if (i == null)
			throw new ValidationException("import must not be null");
		if (i.getId() != null && i.getId() < 0)
			throw new ValidationException("id must not be less than 0");
		if (i.getCreator() == null)
			throw new ValidationException("creator must not be null");
		if (i.getCreator().length() > 30)
			throw new ValidationException("creator must be max 30 chars long");
		if (i.getImportDate() == null)
			throw new ValidationException("import date must not be null");
		if (i.getSource() == null)
			throw new ValidationException("source must not be null");
		if (i.getSource().length() > 30)
			throw new ValidationException("source must be max 30 chars long");
	}
	@Override
	public void insertOrUpdate(final Import i) throws PersistenceException {
		log.debug("insertOrUpdate " + i);
		try {
			validate(i);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}

		if (i.getId() == null) {
			// new entry
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con
							.prepareStatement(
									"INSERT INTO imports (creator, import_date, source) VALUES ((SELECT SUBSTRING_INDEX(USER(),'@',1)),?,?)",
									Statement.RETURN_GENERATED_KEYS);
					//ps.setString(1, i.getCreator());
					ps.setDate(1, new Date(i.getImportDate().getTime()));
					ps.setString(2, i.getSource());
					return ps;
				}
			}, keyHolder);

			i.setId(keyHolder.getKey().intValue());
		} else {
			// update
			jdbcTemplate
					.update("UPDATE imports SET creator = ? , import_date = ?, source = ? WHERE id = ?",
							new Object[] { i.getCreator(), i.getImportDate(),
									i.getSource(), i.getId() }, new int[] {
									Types.VARCHAR, Types.DATE, Types.VARCHAR,
									Types.INTEGER });
		}

	}

	@Override
	public void delete(Import i) throws PersistenceException {
		log.debug("delete " + i);
		if (i.getId() == null || i.getId() < 0)
			throw new IllegalArgumentException(
					"id of import to be deleted must be positive");
		jdbcTemplate.update("DELETE FROM imports WHERE id = ?", i.getId());
	}

	@Override
	public List<Import> getAll() throws PersistenceException {
		log.debug("getAll");
		return jdbcTemplate.query("SELECT * FROM imports", new ImportMapper());
	}

	@Override
	public Import getByID(int id) throws PersistenceException {
		log.debug("getById" + id);
		try {
			return jdbcTemplate.queryForObject(
					"SELECT * FROM imports WHERE id = ?", new Object[] { id },
					new int[] { Types.INTEGER }, new ImportMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			throw new PersistenceException(e);
		}
	}

	private static class ImportMapper implements RowMapper<Import> {

		@Override
		public Import mapRow(ResultSet rs, int rowNum) throws SQLException {
			Import i = new Import();
			i.setId(rs.getInt("id"));
			i.setCreator(rs.getString("creator"));
			i.setImportDate(rs.getDate("import_date"));
			i.setSource(rs.getString("source"));
			return i;
		}

	}

}

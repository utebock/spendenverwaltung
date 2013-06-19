package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.dao.criterion.AbstractCriterionDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter.FilterPrivacyStatus;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.FilterValidator;
import at.fraubock.spendenverwaltung.util.FilterType;

public class FilterDAOImplemented implements IFilterDAO {

	private static final Logger log = Logger
			.getLogger(FilterDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;
	private AbstractCriterionDAO abstractCritDAO;
	private FilterValidator validator;

	@Override
	public void insert(Filter f) throws PersistenceException {
		try {
			validator.validate(f);

			if (f.getCriterion() != null) {
				abstractCritDAO.insert(f.getCriterion());
			}

			// new filter to be inserted
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateFilterStatementCreator(f), keyHolder);

			f.setId(keyHolder.getKey().intValue());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}

	}

	@Override
	public void delete(Filter f) throws PersistenceException {
		try {
			validator.validate(f);

			boolean rightsOk = jdbcTemplate
					.queryForObject(
							"SELECT (privacy_status = ? OR owner = SUBSTRING_INDEX(USER(),'@',1)) FROM filter WHERE id = ?",
							new Object[] {
									Filter.FilterPrivacyStatus.READ_UPDATE_DELETE
											.toString(), f.getId() },
							new int[] { Types.VARCHAR, Types.INTEGER },
							Boolean.class);
			if (!rightsOk) {
				throw new PersistenceException("You have no right to delete "
						+ f);
			}

			Object[] params = new Object[] { f.getId() };
			int[] types = new int[] { Types.INTEGER };
			jdbcTemplate.update("delete from filter where id = ?", params,
					types);

			if (f.getCriterion() != null) {
				abstractCritDAO.delete(f.getCriterion());
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0) {
				throw new PersistenceException("No filter with the given id", e);
			} else {
				log.error("Code that should not be reached.", e);
				throw e;
			}
		} catch (DataAccessException e) {
			log.warn(e.getMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Filter> getAll() throws PersistenceException {
		try {
			// FIXME order alphabetically by name?
			FilterMapper mapper = new FilterMapper();
			String select = "SELECT * FROM filter WHERE owner = SUBSTRING_INDEX(USER(),'@',1) OR privacy_status != ? ORDER BY id DESC";
			List<Filter> filterList = jdbcTemplate.query(select,
					new Object[] { FilterPrivacyStatus.PRIVATE.toString() },
					new int[] { Types.VARCHAR }, mapper);
			for (Filter result : filterList) {
				Integer critId = mapper.getCriterionId().get(result.getId());
				if (critId != null) {
					result.setCriterion(abstractCritDAO.getById(critId));
				}
			}

			return filterList;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public Filter getById(int id) throws PersistenceException {
		try {
			if (id < 0) {
				throw new IllegalArgumentException("Id must not be less than 0");
			}

			String select = "select * from filter where id = ? AND (owner = SUBSTRING_INDEX(USER(),'@',1) OR privacy_status != ? )";
			Filter result = null;
			try {
				FilterMapper mapper = new FilterMapper();
				result = jdbcTemplate.queryForObject(select, new Object[] { id,
						FilterPrivacyStatus.PRIVATE.toString() }, new int[] {
						Types.INTEGER, Types.VARCHAR }, mapper);

				Integer criterionId = mapper.getCriterionId().get(
						result.getId());
				if (criterionId != null) {
					result.setCriterion(abstractCritDAO.getById(criterionId));
				}
			} catch (IncorrectResultSizeDataAccessException e) {
				if (e.getActualSize() == 0)
					return null;
				else
					throw new PersistenceException(e);
			}
			return result;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	/* mappers for inserting and reading this entity */

	/**
	 * when creating a prepared statement, the filter's owner will be set to the
	 * current user's username.
	 * 
	 * @author manuel-bichler
	 * 
	 */
	private class CreateFilterStatementCreator implements
			PreparedStatementCreator {

		private Filter filter;

		CreateFilterStatementCreator(Filter filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into filter (type,name,anonymous,criterion,privacy_status,owner) values (?, ?, ?, ?, ?, SUBSTRING_INDEX(USER(),'@',1))";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, filter.getType().toString());
			ps.setString(2, filter.getName());
			ps.setBoolean(3, filter.isAnonymous());
			if (filter.getCriterion() == null) {
				ps.setNull(4, java.sql.Types.INTEGER);
			} else {
				ps.setInt(4, filter.getCriterion().getId());
			}
			ps.setString(5, filter.getPrivacyStatus().getName());
			filter.setOwner(connection.getMetaData().getUserName());
			return ps;
		}
	}

	private class FilterMapper implements RowMapper<Filter> {

		private Map<Integer, Integer> criterionId = new HashMap<Integer, Integer>();

		public Filter mapRow(ResultSet rs, int rowNum) throws SQLException {
			Filter filter = new Filter();
			filter.setId(rs.getInt("id"));
			filter.setName(rs.getString("name"));
			filter.setAnonymous(rs.getBoolean("anonymous"));

			filter.setType(FilterType.getTypeForString(rs.getString("type")));
			filter.setPrivacyStatus(FilterPrivacyStatus.getByName(rs
					.getString("privacy_status")));
			filter.setOwner(rs.getString("owner"));
			Integer crit_id = rs.getInt("criterion");
			if (!rs.wasNull()) {
				this.criterionId.put(filter.getId(), crit_id);
			} else {

			}

			return filter;
		}

		public Map<Integer, Integer> getCriterionId() {
			return criterionId;
		}
	}

	public FilterValidator getValidator() {
		return validator;
	}

	public void setValidator(FilterValidator filterValidator) {
		this.validator = filterValidator;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public AbstractCriterionDAO getAbstractCritDAO() {
		return abstractCritDAO;
	}

	public void setAbstractCritDAO(AbstractCriterionDAO abstractCritDAO) {
		this.abstractCritDAO = abstractCritDAO;
	}
}

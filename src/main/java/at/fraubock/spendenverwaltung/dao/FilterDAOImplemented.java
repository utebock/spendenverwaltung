package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.dao.criterion.AbstractCriterionDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.FilterValidator;
import at.fraubock.spendenverwaltung.util.FilterType;

public class FilterDAOImplemented implements IFilterDAO {

	private JdbcTemplate jdbcTemplate;
	private AbstractCriterionDAO abstractCritDAO;
	private FilterValidator validator;

	@Override
	public void insertOrUpdate(Filter f) throws PersistenceException {
		validator.validate(f);

		if (f.getId() == null) {
			abstractCritDAO.insert(f.getCriterion());

			// new filter to be inserted
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateFilterStatementCreator(f), keyHolder);

			f.setId(keyHolder.getKey().intValue());

		} else {

		}
	}

	@Override
	public void delete(Filter f) throws PersistenceException {
		validator.validate(f);
		String deleteFilters = "delete from filter where id = ?";

		Object[] params = new Object[] { f.getId() };

		int[] types = new int[] { Types.INTEGER };

		jdbcTemplate.update(deleteFilters, params, types);

		abstractCritDAO.delete(f.getCriterion());
	}

	@Override
	public List<Filter> getAll() throws PersistenceException {
		// FIXME order alphabetically by name?

		String select = "SELECT * FROM filter ORDER BY id DESC";
		List<Filter> filterList = jdbcTemplate
				.query(select, new FilterMapper());

		return filterList;
	}

	@Override
	public Filter getById(int id) throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from filter where id = ?";

		try {
			return jdbcTemplate.queryForObject(select, new Object[] { id },
					new FilterMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}

	/* mappers for inserting and reading this entity */

	private class CreateFilterStatementCreator implements
			PreparedStatementCreator {

		private Filter filter;

		CreateFilterStatementCreator(Filter filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into filter (type,name,anonymous,criterion) values (?, ?, ?, ?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, filter.getType().toString());
			ps.setString(2, filter.getName());
			ps.setBoolean(3, filter.isAnonymous());
			ps.setInt(4, filter.getCriterion().getId());
			return ps;
		}
	}

	private class FilterMapper implements RowMapper<Filter> {

		public Filter mapRow(ResultSet rs, int rowNum) throws SQLException {
			Filter filter = new Filter();

			filter.setType(FilterType.getTypeForString(rs.getString("type")));
			try {
				filter.setCriterion(abstractCritDAO.getById(rs
						.getInt("criterion")));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			filter.setId(rs.getInt("id"));
			filter.setName(rs.getString("name"));
			filter.setAnonymous(rs.getBoolean("anonymous"));

			return filter;
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

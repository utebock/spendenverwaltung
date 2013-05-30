package at.fraubock.spendenverwaltung.dao.criterion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.dao.FilterDAOImplemented;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.FilterValidator;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class MountedFilterCriterionDAO {

	private JdbcTemplate jdbcTemplate;
	private AbstractCriterionDAO abstractCritDAO;
	private FilterDAOImplemented filterDAO;
	private FilterValidator validator;

	public void insert(MountedFilterCriterion f) throws PersistenceException {

		if (f.getId() == null) {
			MountedFilterCriterion mount = (MountedFilterCriterion) f;
			validator.validate(mount);
			abstractCritDAO.insert(f);
			KeyHolder mountedKeyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateMountedFilterStatementCreator(mount),
					mountedKeyHolder);
			mount.setId(mountedKeyHolder.getKey().intValue());
		}
	}

	public MountedFilterCriterion getById(int id) throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from mountedfilter_criterion mc join criterion c on mc.id=c.id where mc.id = ?";

		try {
			return jdbcTemplate.queryForObject(select, new Object[] { id },
					new MountedFilterMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}

	public void delete(MountedFilterCriterion f) throws PersistenceException {
		validator.validate(f);

		if (f.getMount().isAnonymous()) { // only delete mount when
											// anonymous
			filterDAO.delete(f.getMount());
		}
		jdbcTemplate.update("delete from mountedfilter_criterion where id = ?",
				new Object[] { f.getId() }, new int[] { Types.INTEGER });
	}

	/* mappers for inserting and reading this entity */

	private class CreateMountedFilterStatementCreator implements
			PreparedStatementCreator {

		private MountedFilterCriterion filter;

		CreateMountedFilterStatementCreator(MountedFilterCriterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into mountedfilter_criterion (mount,relational_operator,count,property,sum,avg) values (?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			Filter mount = filter.getMount();
			try {
				if (mount.getId() == null) {
					filterDAO.insertOrUpdate(filter.getMount());
				}
			} catch (PersistenceException e) {
				throw new SQLException();
			}
			int c = 1;
			ps.setInt(c++, mount.getId());
			ps.setString(c++, filter.getRelationalOperator().toString());
			if (filter.getCount() == null) {
				ps.setNull(c++, java.sql.Types.INTEGER);
			} else {
				ps.setInt(c++, filter.getCount());
			}
			ps.setString(c++, filter.getProperty().toString());
			if (filter.getSum() == null) {
				ps.setNull(c++, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(c++, filter.getSum());
			}

			if (filter.getAvg() == null) {
				ps.setNull(c++, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(c++, filter.getAvg());
			}
			return ps;
		}
	}

	private class MountedFilterMapper implements
			RowMapper<MountedFilterCriterion> {

		public MountedFilterCriterion mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			MountedFilterCriterion criterion = new MountedFilterCriterion();

			try {
				criterion.setMount(filterDAO.getById(rs.getInt("mount")));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			criterion
			.setType(FilterType.getTypeForString(rs.getString("type")));
			
			criterion.setRelationalOperator(RelationalOperator.valueOf(rs
					.getString("relational_operator")));
			criterion.setId(rs.getInt("id"));
			criterion.setCount(rs.getInt("count"));
			
			criterion.setProperty(FilterProperty.getPropertyForString(rs
					.getString("property"),FilterType.getTypeForString(rs.getString("type"))));
			criterion.setSum(rs.getDouble("sum"));
			criterion.setAvg(rs.getDouble("avg"));

			return criterion;
		}
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

	public FilterDAOImplemented getFilterDAO() {
		return filterDAO;
	}

	public void setFilterDAO(FilterDAOImplemented filterDAO) {
		this.filterDAO = filterDAO;
	}

	public FilterValidator getValidator() {
		return validator;
	}

	public void setValidator(FilterValidator validator) {
		this.validator = validator;
	}
	
}

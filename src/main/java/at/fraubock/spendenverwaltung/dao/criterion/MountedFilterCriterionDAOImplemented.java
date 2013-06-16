package at.fraubock.spendenverwaltung.dao.criterion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
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

public class MountedFilterCriterionDAOImplemented implements
		IMountedFilterCriterionDAO {

	private JdbcTemplate jdbcTemplate;
	private AbstractCriterionDAO abstractCritDAO;
	private FilterDAOImplemented filterDAO;
	private FilterValidator validator;

	@Override
	public void insert(MountedFilterCriterion f) throws PersistenceException {

		// if (f.getId() == null) {
		MountedFilterCriterion mount = (MountedFilterCriterion) f;
		validator.validate(mount);
		KeyHolder mountedKeyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new CreateMountedFilterStatementCreator(mount),
				mountedKeyHolder);
		// }
	}

	@Override
	public MountedFilterCriterion getById(int id) throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from mountedfilter_criterion mc join criterion c on mc.id=c.id where mc.id = ?";
		MountedFilterMapper mapper = new MountedFilterMapper();
		try {
			MountedFilterCriterion result = jdbcTemplate.queryForObject(select,
					new Object[] { id }, mapper);

			result.setMount(filterDAO.getById(mapper.getMountIds().get(0)));
			if (mapper.getProperty() != null) {
				result.setProperty(FilterProperty.getPropertyForString(
						mapper.getProperty(), result.getMount().getType()));
			}
			return result;
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}

	@Override
	public void replaceMountId(int mountId, int replaceWith)
			throws PersistenceException {
		try {
			jdbcTemplate
					.update("update mountedfilter_criterion set mount=? where mount = ?",
							new Object[] { replaceWith, mountId }, new int[] {
									Types.INTEGER, Types.INTEGER });
		} catch (DataAccessException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void delete(MountedFilterCriterion f) throws PersistenceException {
		validator.validate(f);
		try {
			jdbcTemplate.update(
					"delete from mountedfilter_criterion where id = ?",
					new Object[] { f.getId() }, new int[] { Types.INTEGER });
		} catch (DataAccessException e) {
			throw new PersistenceException(e);
		}
		if (f.getMount().isAnonymous()) { // only delete mount when
											// anonymous
			filterDAO.delete(f.getMount());
		}
	}

	@Override
	public List<Integer> getAllMountedFilterIds() throws PersistenceException {
		MountedFilterMapper mapper = new MountedFilterMapper();
		String select = "select * from mountedfilter_criterion mc "
				+ "join criterion c on mc.id=c.id";
		try {
			jdbcTemplate.query(select, mapper);
		} catch (DataAccessException e) {
			throw new PersistenceException(e);
		}
		return mapper.getMountIds();
	}

	/* mappers for inserting and reading this entity */

	private class CreateMountedFilterStatementCreator implements
			PreparedStatementCreator {

		private MountedFilterCriterion filter;

		CreateMountedFilterStatementCreator(MountedFilterCriterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into mountedfilter_criterion (id,mount,relational_operator,count,property,sum,avg) values (?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			Filter mount = filter.getMount();
			try {
				if (mount.getId() == null) {
					filterDAO.insert(filter.getMount());
				}
			} catch (PersistenceException e) {
				throw new SQLException();
			}
			int c = 1;
			ps.setInt(c++, filter.getId());
			ps.setInt(c++, mount.getId());
			ps.setString(c++, filter.getRelationalOperator().toString());
			if (filter.getCount() == null) {
				ps.setNull(c++, java.sql.Types.INTEGER);
			} else {
				ps.setInt(c++, filter.getCount());
			}

			if (filter.getProperty() == null) {
				ps.setNull(c++, java.sql.Types.VARCHAR);
			} else {
				ps.setString(c++, filter.getProperty().toString());
			}

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

		private List<Integer> mountIds = new ArrayList<Integer>();
		private String property;

		public MountedFilterCriterion mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			MountedFilterCriterion criterion = new MountedFilterCriterion();
			this.mountIds.add(rs.getInt("mount"));

			criterion
					.setType(FilterType.getTypeForString(rs.getString("type")));

			criterion.setRelationalOperator(RelationalOperator.valueOf(rs
					.getString("relational_operator")));

			criterion.setId(rs.getInt("id"));

			Integer count = rs.getInt("count");
			criterion.setCount(rs.wasNull() ? null : count);

			String prop = rs.getString("property");
			if (!rs.wasNull()) {
				this.setProperty(prop);
			}

			Double sum = rs.getDouble("sum");
			criterion.setSum(rs.wasNull() ? null : sum);
			Double avg = rs.getDouble("avg");
			criterion.setAvg(rs.wasNull() ? null : avg);

			return criterion;
		}

		public List<Integer> getMountIds() {
			return mountIds;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
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

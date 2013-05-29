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

import at.fraubock.spendenverwaltung.interfaces.dao.IFilterDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class FilterDAOImplemented implements IFilterDAO {

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

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
			ps.setInt(4, filter.getCriterion().getCriterionId());
			return ps;
		}
	}

	private class CreateFilterCriterionStatementCreator implements
			PreparedStatementCreator {

		private Criterion filter;

		CreateFilterCriterionStatementCreator(Criterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into criterion (type) values (?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, filter.getType().toString());
			return ps;
		}
	}

	private class CreateLogicalFilterStatementCreator implements
			PreparedStatementCreator {

		private ConnectedCriterion filter;

		CreateLogicalFilterStatementCreator(ConnectedCriterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into connected_criterion (criterionid,logical_operator,operand1,operand2) values (?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, filter.getCriterionId());
			ps.setString(2, filter.getLogicalOperator().toString());
			try {
				insertOrUpdateCriterion(filter.getOperand1());
				insertOrUpdateCriterion(filter.getOperand2());
			} catch (PersistenceException e) {
				throw new SQLException();
			}
			ps.setInt(3, filter.getOperand1().getCriterionId());
			ps.setInt(4, filter.getOperand2().getCriterionId());
			return ps;
		}
	}

	private class CreatePropertyFilterStatementCreator implements
			PreparedStatementCreator {

		private PropertyCriterion filter;

		CreatePropertyFilterStatementCreator(PropertyCriterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into property_criterion (criterionid,relational_operator,property,numValue,strValue,dateValue,daysBack,boolValue) values (?,?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, filter.getCriterionId());
			ps.setString(2, filter.getRelationalOperator().toString());
			ps.setString(3, filter.getProperty().toString());
			ps.setDouble(4, filter.getNumValue());
			ps.setString(5, filter.getStrValue());
			ps.setDate(6, filter.getDateValue());
			ps.setInt(7, filter.getDaysBack());
			ps.setBoolean(8, filter.getBoolValue());
			return ps;
		}
	}

	private class CreateMountedFilterStatementCreator implements
			PreparedStatementCreator {

		private MountedFilterCriterion filter;

		CreateMountedFilterStatementCreator(MountedFilterCriterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into mountedfilter_criterion (criterionid,mount,relational_operator,count,property,sum,avg) values (?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, filter.getCriterionId());
			Filter mount = filter.getMount();
			try {
				if (mount.getId() == null) {
					insertOrUpdate(filter.getMount());
				}
			} catch (PersistenceException e) {
				throw new SQLException();
			}
			ps.setInt(2, mount.getId());
			ps.setString(3, filter.getRelationalOperator().toString());
			ps.setInt(4, filter.getCount());
			ps.setString(5, filter.getProperty().toString());
			ps.setDouble(6, filter.getSum());
			ps.setDouble(7, filter.getAvg());
			return ps;
		}
	}

	@Override
	public void insertOrUpdate(Filter f) throws PersistenceException {
		// TODO validate

		if (f.getId() == null) {
			// new filter to be inserted
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateFilterStatementCreator(f), keyHolder);

			f.setId(keyHolder.getKey().intValue());

			insertOrUpdateCriterion(f.getCriterion());
		} else {
			// TODO update
		}
	}

	private void insertOrUpdateCriterion(Criterion f)
			throws PersistenceException {
		// TODO validate

		if (f.getCriterionId() == null) {
			// new filter to be inserted
			KeyHolder criterionKeyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateFilterCriterionStatementCreator(f),
					criterionKeyHolder);
			f.setCriterionId(criterionKeyHolder.getKey().intValue());

			// now insert the specific criterion type
			if (f instanceof ConnectedCriterion) {
				ConnectedCriterion log = (ConnectedCriterion) f;
				KeyHolder logicalKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(
						new CreateLogicalFilterStatementCreator(log),
						logicalKeyHolder);
				log.setCriterionId(logicalKeyHolder.getKey().intValue());
			} else if (f instanceof PropertyCriterion) {
				KeyHolder propertyKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(new CreatePropertyFilterStatementCreator(
						(PropertyCriterion) f), propertyKeyHolder);
				f.setCriterionId(propertyKeyHolder.getKey().intValue());
			} else if (f instanceof MountedFilterCriterion) {
				MountedFilterCriterion mount = (MountedFilterCriterion) f;
				KeyHolder mountedKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(new CreateMountedFilterStatementCreator(
						mount), mountedKeyHolder);
				mount.setCriterionId(mountedKeyHolder.getKey().intValue());
			}

		} else {
			// TODO update
		}

	}

	@Override
	public void delete(Filter f) throws PersistenceException {
		// TODO validate
		String deleteFilters = "delete from filter where id = ?";

		Object[] params = new Object[] { f.getId() };

		int[] types = new int[] { Types.INTEGER };

		jdbcTemplate.update(deleteFilters, params, types);

		deleteFilterCriterion(f.getCriterion());
	}

	private void deleteFilterCriterion(Criterion f) throws PersistenceException {
		// TODO validate

		// delete the specific criterion type
		if (f instanceof ConnectedCriterion) {
			ConnectedCriterion log = (ConnectedCriterion) f;

			deleteFilterCriterion(log.getOperand1());
			deleteFilterCriterion(log.getOperand2());
			jdbcTemplate.update("delete from connected_criterion where id = ?",
					new Object[] { log.getId() }, new int[] { Types.INTEGER });

		} else if (f instanceof PropertyCriterion) {
			PropertyCriterion prop = (PropertyCriterion) f;
			jdbcTemplate.update("delete from property_criterion where id = ?",
					new Object[] { prop.getId() }, new int[] { Types.INTEGER });
		} else if (f instanceof MountedFilterCriterion) {
			MountedFilterCriterion mount = (MountedFilterCriterion) f;

			if (mount.getMount().isAnonymous()) { // only delete mount when
													// anonymous
				delete(mount.getMount());
			}
			jdbcTemplate
					.update("delete from mountedfilter_criterion where id = ?",
							new Object[] { mount.getId() },
							new int[] { Types.INTEGER });
		}

		// delete the criterion
		jdbcTemplate.update("delete from criterion where id = ?",
				new Object[] { f.getCriterionId() },
				new int[] { Types.INTEGER });
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

	private Criterion getFilterCriterionById(int id)
			throws PersistenceException {
		PropertyCriterion prop = getPropertyFilterByCriterionId(id);
		if (prop != null) {
			return prop;
		}

		ConnectedCriterion logical = getLogicalFilterByCriterionId(id);
		if (logical != null) {
			return logical;
		}

		MountedFilterCriterion mount = getMountedFilterByCriterionId(id);
		if (mount != null) {
			return mount;
		}

		return null;

	}

	private PropertyCriterion getPropertyFilterByCriterionId(int id)
			throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from property_criterion where criterionid = ?";

		try {
			return jdbcTemplate.queryForObject(select, new Object[] { id },
					new PropertyFilterMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}

	private ConnectedCriterion getLogicalFilterByCriterionId(int id)
			throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from logical_criterion where criterionid = ?";

		try {
			return jdbcTemplate.queryForObject(select, new Object[] { id },
					new LogicalFilterMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}

	private MountedFilterCriterion getMountedFilterByCriterionId(int id)
			throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from mountedfilter_criterion where criterionid = ?";

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

	private class FilterMapper implements RowMapper<Filter> {

		public Filter mapRow(ResultSet rs, int rowNum) throws SQLException {
			Criterion head = null;
			try {
				head = getFilterCriterionById(rs.getInt("head"));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			Filter filter = new Filter(
					FilterType.valueOf(rs.getString("type")), head);
			filter.setId(rs.getInt("id"));
			filter.setName(rs.getString("name"));
			filter.setAnonymous(rs.getBoolean("anonymous"));

			return filter;
		}
	}

	private class PropertyFilterMapper implements RowMapper<PropertyCriterion> {

		public PropertyCriterion mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PropertyCriterion filter = new PropertyCriterion(
					FilterType.valueOf(rs.getString("type")),
					FilterProperty.valueOf(rs.getString("property")),
					RelationalOperator.valueOf(rs
							.getString("relational_operator")));
			filter.setCriterionId(rs.getInt("id"));
			filter.setNumValue(rs.getDouble("numValue"));
			filter.setStrValue(rs.getString("strValue"));
			filter.setDateValue(rs.getDate("dateValue"));
			filter.setBoolValue(rs.getBoolean("boolValue"));
			filter.setDaysBack(rs.getInt("daysBack"));
			return filter;
		}
	}

	private class LogicalFilterMapper implements RowMapper<ConnectedCriterion> {

		public ConnectedCriterion mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Criterion operand1 = null;
			Criterion operand2 = null;
			try {
				operand1 = getFilterCriterionById(rs.getInt("operand1"));
				operand2 = getFilterCriterionById(rs.getInt("operand2"));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			ConnectedCriterion filter = new ConnectedCriterion(
					FilterType.valueOf(rs.getString("type")),
					LogicalOperator.valueOf(rs.getString("logical_operator")),
					operand1);
			filter.setCriterionId(rs.getInt("id"));
			filter.setOperand2(operand2);
			return filter;
		}
	}

	private class MountedFilterMapper implements
			RowMapper<MountedFilterCriterion> {

		public MountedFilterCriterion mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Filter mount = null;
			try {
				mount = getById(rs.getInt("mount"));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			MountedFilterCriterion filter = new MountedFilterCriterion(
					FilterType.valueOf(rs.getString("type")), mount,
					RelationalOperator.valueOf(rs
							.getString("relational_operator")));
			filter.setCriterionId(rs.getInt("id"));
			filter.setCount(rs.getInt("count"));
			filter.setProperty(FilterProperty.valueOf(rs.getString("property")));
			filter.setSum(rs.getDouble("sum"));
			filter.setAvg(rs.getDouble("avg"));

			return filter;
		}
	}
}

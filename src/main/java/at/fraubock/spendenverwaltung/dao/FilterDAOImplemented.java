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
import at.fraubock.spendenverwaltung.interfaces.domain.filter.cells.FilterCell;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.cells.LogicalFilter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.cells.MountedFilter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.cells.PropertyFilter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
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

		private String createFilter = "insert into filter (type,name,hidden,head) values (?, ?, ?, ?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, filter.getType());
			ps.setString(2, filter.getName());
			ps.setBoolean(3, filter.isAnonymous());
			ps.setInt(4, filter.getHead().getCellId());
			return ps;
		}
	}

	private class CreateFilterCellStatementCreator implements
			PreparedStatementCreator {

		private FilterCell filter;

		CreateFilterCellStatementCreator(FilterCell filter) {
			this.filter = filter;
		}

		// TODO insert no values?
		private String createFilter = "insert into filter_cell (id) values (?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, filter.getCellId());
			return ps;
		}
	}

	private class CreateLogicalFilterStatementCreator implements
			PreparedStatementCreator {

		private LogicalFilter filter;

		CreateLogicalFilterStatementCreator(LogicalFilter filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into logical_filter (cellid,logical_operator,operand1,operand2) values (?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, filter.getCellId());
			ps.setString(2, filter.getLogicalOperator().toString());
			try {
				insertOrUpdateCell(filter.getOperand1());
				insertOrUpdateCell(filter.getOperand2());
			} catch (PersistenceException e) {
				throw new SQLException();
			}
			ps.setInt(3, filter.getOperand1().getCellId());
			ps.setInt(4, filter.getOperand2().getCellId());
			return ps;
		}
	}

	private class CreatePropertyFilterStatementCreator implements
			PreparedStatementCreator {

		private PropertyFilter filter;

		CreatePropertyFilterStatementCreator(PropertyFilter filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into property_filter (cellid,relational_operator,property,numValue,strValue,dateValue,daysBack,boolValue) values (?,?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, filter.getCellId());
			ps.setString(2, filter.getRelationalOperator().toString());
			ps.setString(3, filter.getProperty());
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

		private MountedFilter filter;

		CreateMountedFilterStatementCreator(MountedFilter filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into mounted_filter (cellid,mount,relational_operator,count,property,sum,avg) values (?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, filter.getCellId());
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
			ps.setString(5, filter.getProperty());
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

			insertOrUpdateCell(f.getHead());
		} else {
			// TODO update
		}
	}

	private void insertOrUpdateCell(FilterCell f) throws PersistenceException {
		// TODO validate

		if (f.getCellId() == null) {
			// new filter to be inserted
			KeyHolder cellKeyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateFilterCellStatementCreator(f),
					cellKeyHolder);
			f.setCellId(cellKeyHolder.getKey().intValue());

			// now insert the specific cell type
			if (f instanceof LogicalFilter) {
				LogicalFilter log = (LogicalFilter) f;
				KeyHolder logicalKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(
						new CreateLogicalFilterStatementCreator(log),
						logicalKeyHolder);
				log.setCellId(logicalKeyHolder.getKey().intValue());
			} else if (f instanceof PropertyFilter) {
				KeyHolder propertyKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(new CreatePropertyFilterStatementCreator(
						(PropertyFilter) f), propertyKeyHolder);
				f.setCellId(propertyKeyHolder.getKey().intValue());
			} else if (f instanceof MountedFilter) {
				MountedFilter mount = (MountedFilter) f;
				KeyHolder mountedKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(new CreateMountedFilterStatementCreator(
						mount), mountedKeyHolder);
				mount.setCellId(mountedKeyHolder.getKey().intValue());
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
		
		deleteFilterCell(f.getHead());
	}

	private void deleteFilterCell(FilterCell f) throws PersistenceException {
		// TODO validate
		
		// delete the specific cell type
		if (f instanceof LogicalFilter) {
			LogicalFilter log = (LogicalFilter) f;

			deleteFilterCell(log.getOperand1());
			deleteFilterCell(log.getOperand2());
			jdbcTemplate.update("delete from logical_filter where id = ?",
					new Object[] { log.getId() }, new int[] { Types.INTEGER });

		} else if (f instanceof PropertyFilter) {
			PropertyFilter prop = (PropertyFilter) f;
			jdbcTemplate.update("delete from property_filter where id = ?",
					new Object[] { prop.getId() }, new int[] { Types.INTEGER });
		} else if (f instanceof MountedFilter) {
			MountedFilter mount = (MountedFilter) f;

			if (mount.getMount().isAnonymous()) { // only delete mount when
													// anonymous
				delete(mount.getMount());
			}
			jdbcTemplate
					.update("delete from mounted_filter where id = ?",
							new Object[] { mount.getId() },
							new int[] { Types.INTEGER });
		}

		
		// delete the cell
		jdbcTemplate.update("delete from filter_cell where id = ?",
				new Object[] { f.getCellId() }, new int[] { Types.INTEGER });
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

	private FilterCell getFilterCellById(int id) throws PersistenceException {
		PropertyFilter prop = getPropertyFilterByCellId(id);
		if (prop != null) {
			return prop;
		}

		LogicalFilter logical = getLogicalFilterByCellId(id);
		if (logical != null) {
			return logical;
		}

		MountedFilter mount = getMountedFilterByCellId(id);
		if (mount != null) {
			return mount;
		}

		return null;

	}

	private PropertyFilter getPropertyFilterByCellId(int id)
			throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from property_filter where cellid = ?";

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

	private LogicalFilter getLogicalFilterByCellId(int id)
			throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from logical_filter where cellid = ?";

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

	private MountedFilter getMountedFilterByCellId(int id)
			throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from mounted_filter where cellid = ?";

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
			Filter filter = new Filter();
			filter.setId(rs.getInt("id"));
			filter.setType(rs.getString("type"));
			filter.setName(rs.getString("name"));
			filter.setAnonymous(rs.getBoolean("anonymous"));
			try {
				filter.setHead(getFilterCellById(rs.getInt("head")));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			return filter;
		}
	}

	private class PropertyFilterMapper implements RowMapper<PropertyFilter> {

		public PropertyFilter mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PropertyFilter filter = new PropertyFilter();
			filter.setCellId(rs.getInt("id"));
			filter.setType(rs.getString("type"));
			filter.setProperty(rs.getString("property"));
			filter.setRelationalOperator(RelationalOperator.valueOf(rs
					.getString("relational_operator")));
			filter.setNumValue(rs.getDouble("numValue"));
			filter.setStrValue(rs.getString("strValue"));
			filter.setDateValue(rs.getDate("dateValue"));
			filter.setBoolValue(rs.getBoolean("boolValue"));
			filter.setDaysBack(rs.getInt("daysBack"));
			return filter;
		}
	}

	private class LogicalFilterMapper implements RowMapper<LogicalFilter> {

		public LogicalFilter mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			LogicalFilter filter = new LogicalFilter();
			filter.setCellId(rs.getInt("id"));
			try {
				filter.setOperand1(getFilterCellById(rs.getInt("operand1")));
				filter.setOperand2(getFilterCellById(rs.getInt("operand2")));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			filter.setLogicalOperator(LogicalOperator.valueOf(rs
					.getString("logical_operator")));
			return filter;
		}
	}

	private class MountedFilterMapper implements RowMapper<MountedFilter> {

		public MountedFilter mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			MountedFilter filter = new MountedFilter();
			filter.setCellId(rs.getInt("id"));
			filter.setType(rs.getString("type"));
			try {
				filter.setMount(getById(rs.getInt("mount")));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
			filter.setRelationalOperator(RelationalOperator.valueOf(rs
					.getString("relational_operator")));
			filter.setCount(rs.getInt("count"));
			filter.setProperty(rs.getString("property"));
			filter.setSum(rs.getDouble("sum"));
			filter.setAvg(rs.getDouble("avg"));

			return filter;
		}
	}
}

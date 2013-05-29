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

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.FilterValidator;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class PropertyCriterionDAO {

	private FilterValidator validator;
	private AbstractCriterionDAO abstractCritDAO;
	private JdbcTemplate jdbcTemplate;

	public void insert(PropertyCriterion f) throws PersistenceException {

		if (f.getId() == null) {
			PropertyCriterion prop = (PropertyCriterion) f;
			validator.validate(prop);
			KeyHolder propertyKeyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreatePropertyFilterStatementCreator(prop),
					propertyKeyHolder);
			f.setId(propertyKeyHolder.getKey().intValue());
		}
	}

	public PropertyCriterion getById(int id) throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from property_criterion pc join criterion c on pc.id=c.id where pc.id = ?";

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

	public void delete(PropertyCriterion f) throws PersistenceException {
		validator.validate(f);
		jdbcTemplate.update("delete from property_criterion where id = ?",
				new Object[] { f.getId() }, new int[] { Types.INTEGER });
	}

	/* mappers for inserting and reading this entity */

	private class CreatePropertyFilterStatementCreator implements
			PreparedStatementCreator {

		private PropertyCriterion filter;

		CreatePropertyFilterStatementCreator(PropertyCriterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into property_criterion (relational_operator,property,numValue,strValue,dateValue,daysBack,boolValue) values (?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			int c = 1;
			ps.setString(c++, filter.getRelationalOperator().toString());
			ps.setString(c++, filter.getProperty().toString());
			if (filter.getNumValue() == null) {
				ps.setNull(c++, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(c++, filter.getNumValue());
			}
			ps.setString(c++, filter.getStrValue());
			ps.setDate(c++, filter.getDateValue());
			if (filter.getDaysBack() == null) {
				ps.setNull(c++, java.sql.Types.INTEGER);
			} else {
				ps.setInt(c++, filter.getDaysBack());
			}
			if (filter.getBoolValue() == null) {
				ps.setNull(c++, java.sql.Types.BOOLEAN);
			} else {
				ps.setBoolean(c++, filter.getBoolValue());
			}
			;
			return ps;
		}
	}

	private class PropertyFilterMapper implements RowMapper<PropertyCriterion> {

		public PropertyCriterion mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PropertyCriterion criterion = new PropertyCriterion();

			criterion
					.setType(FilterType.getTypeForString(rs.getString("type")));
			criterion.setProperty(FilterProperty.getPropertyForString(rs
					.getString("property")));
			criterion.setRelationalOperator(RelationalOperator.valueOf(rs
					.getString("relational_operator")));
			criterion.setId(rs.getInt("id"));
			criterion.setNumValue(rs.getDouble("numValue"));
			criterion.setStrValue(rs.getString("strValue"));
			criterion.setDateValue(rs.getDate("dateValue"));
			criterion.setBoolValue(rs.getBoolean("boolValue"));
			criterion.setDaysBack(rs.getInt("daysBack"));
			return criterion;
		}
	}

	public FilterValidator getValidator() {
		return validator;
	}

	public void setValidator(FilterValidator validator) {
		this.validator = validator;
	}

	public AbstractCriterionDAO getAbstractCritDAO() {
		return abstractCritDAO;
	}

	public void setAbstractCritDAO(AbstractCriterionDAO abstractCritDAO) {
		this.abstractCritDAO = abstractCritDAO;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}

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
package at.fraubock.spendenverwaltung.dao.criterion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.FilterValidator;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

/**
 * DAO for {@link PropertyCriterion} entities.
 * 
 * @author philipp muhoray
 * 
 */
public class PropertyCriterionDAO {

	private FilterValidator validator;
	private AbstractCriterionDAO abstractCritDAO;
	private JdbcTemplate jdbcTemplate;

	public void insert(PropertyCriterion f) throws PersistenceException {

		try {
			PropertyCriterion prop = (PropertyCriterion) f;
			validator.validate(prop);
			KeyHolder propertyKeyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreatePropertyFilterStatementCreator(prop),
					propertyKeyHolder);
		} catch (DataAccessException e) {
			throw new PersistenceException(e);
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
		try {
			jdbcTemplate.update("delete from property_criterion where id = ?",
					new Object[] { f.getId() }, new int[] { Types.INTEGER });
		} catch (DataAccessException e) {
			throw new PersistenceException(e);
		}
	}

	/* mappers for inserting and reading this entity */

	private class CreatePropertyFilterStatementCreator implements
			PreparedStatementCreator {

		private PropertyCriterion prop;

		CreatePropertyFilterStatementCreator(PropertyCriterion filter) {
			this.prop = filter;
		}

		private String createFilter = "insert into property_criterion (id,relational_operator,property,numValue,strValue,dateValue,daysBack,boolValue) values (?,?,?,?,?,?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			int c = 1;

			ps.setInt(c++, prop.getId());
			ps.setString(c++, prop.getRelationalOperator().toString());
			ps.setString(c++, prop.getProperty().toString());

			if (prop.getNumValue() == null) {
				ps.setNull(c++, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(c++, prop.getNumValue());
			}

			ps.setString(c++, prop.getStrValue());

			if (prop.getDateValue() == null) {
				ps.setNull(c++, java.sql.Types.DATE);
			} else {
				ps.setDate(c++, new Date(prop.getDateValue().getTime()));
			}

			if (prop.getDaysBack() == null) {
				ps.setNull(c++, java.sql.Types.INTEGER);
			} else {
				ps.setInt(c++, prop.getDaysBack());
			}

			if (prop.getBoolValue() == null) {
				ps.setNull(c++, java.sql.Types.BOOLEAN);
			} else {
				ps.setBoolean(c++, prop.getBoolValue());
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

			criterion.setProperty(FilterProperty.getPropertyForString(
					rs.getString("property"),
					FilterType.getTypeForString(rs.getString("type"))));
			criterion.setRelationalOperator(RelationalOperator.valueOf(rs
					.getString("relational_operator")));
			criterion.setId(rs.getInt("id"));

			Double dbl = rs.getDouble("numValue");
			criterion.setNumValue(rs.wasNull() ? null : dbl);

			String str = rs.getString("strValue");
			criterion.setStrValue(rs.wasNull() ? null : str);

			Date date = rs.getDate("dateValue");
			criterion.setDateValue(rs.wasNull() ? null : date);

			Boolean bool = rs.getBoolean("boolValue");
			criterion.setBoolValue(rs.wasNull() ? null : bool);

			Integer daysBack = rs.getInt("daysBack");
			criterion.setDaysBack(rs.wasNull() ? null : daysBack);

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

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

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.service.FilterValidator;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;

public class ConnectedCriterionDAO {

	private FilterValidator validator;
	private AbstractCriterionDAO abstractCritDAO;
	private JdbcTemplate jdbcTemplate;

	public void insert(ConnectedCriterion f)
			throws PersistenceException {

		if (f.getId() == null) {
			ConnectedCriterion log = (ConnectedCriterion) f;
			validator.validate(log);
			abstractCritDAO.insert(f);
			abstractCritDAO.insert(log.getOperand1());
			abstractCritDAO.insert(log.getOperand2());
			KeyHolder logicalKeyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(new CreateConnectedCriterionStatementCreator(
					log), logicalKeyHolder);
			log.setId(logicalKeyHolder.getKey().intValue());
		}
	}

	public ConnectedCriterion getById(int id) throws PersistenceException {
		if (id < 0) {
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		String select = "select * from connected_criterion cc join criterion c on cc.id=c.id where cc.id = ?";

		try {
			return jdbcTemplate.queryForObject(select, new Object[] { id },
					new ConnectedCriterionMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}
	
	public void delete(ConnectedCriterion f) throws PersistenceException {
		validator.validate(f);
		abstractCritDAO.delete(f.getOperand1());
		abstractCritDAO.delete(f.getOperand2());
		jdbcTemplate.update("delete from connected_criterion where id = ?",
				new Object[] { f.getId() }, new int[] { Types.INTEGER });
	}

	/* mappers for inserting and reading this entity */

	private class CreateConnectedCriterionStatementCreator implements
			PreparedStatementCreator {

		private ConnectedCriterion filter;

		CreateConnectedCriterionStatementCreator(ConnectedCriterion filter) {
			this.filter = filter;
		}

		private String createFilter = "insert into connected_criterion (logical_operator,operand1,operand2) values (?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			PreparedStatement ps = connection.prepareStatement(createFilter,
					Statement.RETURN_GENERATED_KEYS);
			int c = 1;
			ps.setString(c++, filter.getLogicalOperator().toString());
			try {
				abstractCritDAO.insert(filter.getOperand1());
				abstractCritDAO.insert(filter.getOperand2());
			} catch (PersistenceException e) {
				throw new SQLException();
			}
			ps.setInt(c++, filter.getOperand1().getId());
			if (filter.getOperand2() == null) {
				ps.setNull(c++, java.sql.Types.INTEGER);
			} else {
				ps.setInt(c++, filter.getOperand2().getId());
			}
			return ps;
		}
	}

	private class ConnectedCriterionMapper implements
			RowMapper<ConnectedCriterion> {

		public ConnectedCriterion mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ConnectedCriterion criterion = new ConnectedCriterion();

			criterion
					.setType(FilterType.getTypeForString(rs.getString("type")));
			criterion.setLogicalOperator(LogicalOperator.valueOf(rs
					.getString("logical_operator")));
			criterion.setId(rs.getInt("id"));
			try {
				criterion.setOperand1(abstractCritDAO.getById(rs
						.getInt("operand1")));
				criterion.setOperand2(abstractCritDAO.getById(rs
						.getInt("operand2")));
			} catch (PersistenceException e) {
				throw new SQLException(e);
			}
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
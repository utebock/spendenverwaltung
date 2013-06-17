package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;
import at.fraubock.spendenverwaltung.util.ActionAttribute;

/**
 * implementation of {@link IActionDAO}
 * 
 * @author philipp muhoray
 * 
 */
public class ActionDAOImplemented implements IActionDAO {

	private static final Logger log = Logger
			.getLogger(ActionDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * checks the integrity of an {@link Action} entity
	 * 
	 * @author philipp muhoray
	 * @throws ValidationException
	 * 
	 */

	private static void validate(Action action) throws ValidationException {
		if (action == null) {
			log.error("Argument was null");
			throw new ValidationException("Action must not be null");
		}

		if (action.getActor() == null) {
			log.error("Actor was null");
			throw new ValidationException("Actor must not be null");
		}

		if (action.getType() == null) {
			log.error("Type was null");
			throw new ValidationException("Type must not be null");
		}

		if (action.getEntity() == null) {
			log.error("Entity was null");
			throw new ValidationException("Entity must not be null");
		}

		if (action.getEntityId() == null) {
			log.error("EntityId was null");
			throw new ValidationException("EntityId must not be null");
		}

		if (action.getTime() == null) {
			log.error("Time was null");
			throw new ValidationException("Time must not be null");
		}
	}

	private class CreateActionStatementCreator implements
			PreparedStatementCreator {

		private Action action;

		CreateActionStatementCreator(Action action) {
			this.action = action;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			String createAction = "insert into actions (actor,time,type,entity,entityid,payload) values (?,?,?,?,?,?)";

			PreparedStatement ps = connection.prepareStatement(createAction,
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, action.getActor());
			ps.setTimestamp(2, new Timestamp(action.getTime().getTime()));
			ps.setString(3, action.getType().toString());
			ps.setString(4, action.getEntity().toString());
			ps.setInt(5, action.getEntityId());
			ps.setString(6, action.getPayload());
			return ps;
		}
	}

	@Override
	public void insert(final Action a) throws PersistenceException {
		try {
			validate(a);

			if (a.getId() != null) {
				log.error("Tried to insert an Action entity with id already set. id='"
						+ a.getId() + "'");
				throw new PersistenceException();
			}

			log.info("Inserting Action...");

			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new CreateActionStatementCreator(a), keyHolder);

			// set action id to update result
			a.setId(keyHolder.getKey().intValue());

			log.info("Action entity successfully created: " + a.toString());

		} catch (ValidationException e) {
			throw new PersistenceException(e);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public void delete(final Action a) throws PersistenceException {
		log.info("Deleting Action...");
		try {
			validate(a);

			jdbcTemplate.update("delete from actions where id = ?",
					new Object[] { a.getId() }, new int[] { Types.INTEGER });

			log.info("Action entity successfully deleted:" + a.toString());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Action> getAll() throws PersistenceException {
		try {
			log.info("Reading all Actions.");
			return jdbcTemplate.query(
					"select * from actions ORDER BY time DESC",
					new ActionMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public long countResultsOfAll() throws PersistenceException {
		try {
			log.info("Counting all Actions.");
			return jdbcTemplate.queryForObject("select count(*) from actions",
					Integer.class);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Action> getAllWithLimitedResult(int offset, int count)
			throws PersistenceException {
		try {
			log.info("Reading Actions.");
			return jdbcTemplate.query(
					"select * from actions ORDER BY time DESC LIMIT " + offset
							+ ", " + count, new ActionMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public List<Action> getLimitedResultByAttributeLike(ActionAttribute attribute,
			String value, int offset, int count) throws PersistenceException {

		if (attribute == null || value == null) {
			log.error("Error executing getLimitedResultByAttributeLike: " +
					"attribute and/or value were null");
			throw new IllegalArgumentException(
					"attribute and value must both be set");
		}

		log.info("Reading Actions for attribute='" + attribute.getName() + "', value='"
				+ value + "'");
		
		String name = attribute.getName();
		if(attribute==ActionAttribute.TIME) {
			name = "DATE_FORMAT("+name+", '%d.%m.%Y')";
		}
		try {
			return jdbcTemplate.query("select * from actions where "
					+ name + " LIKE ? ORDER BY time DESC LIMIT " + offset
							+ ", " + count,
					new Object[] { "%"+value+"%" }, new ActionMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public long countResultsOfAttributeLike(ActionAttribute attribute, String value)
			throws PersistenceException {
		log.info("Counting Actions for attribute='" + attribute.getName() + "', value='"
				+ value + "'");
		
		String name = attribute.getName();
		if(attribute==ActionAttribute.TIME) {
			name = "DATE_FORMAT("+name+", '%d.%m.%Y')";
		}
		try {
			return jdbcTemplate.queryForObject("select count(*) from actions where "
					+ name + " LIKE ? ORDER BY time DESC",
					new Object[] { "%"+value+"%" },Integer.class);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}
	

	private class ActionMapper implements RowMapper<Action> {

		public Action mapRow(ResultSet rs, int rowNum) throws SQLException {
			Action action = new Action();
			action.setId(rs.getInt("id"));
			action.setActor(rs.getString("actor"));
			action.setTime(rs.getTimestamp("time"));
			action.setEntity(Action.Entity.getByName(rs.getString("entity")));
			action.setEntityId(rs.getInt("entityid"));
			action.setType(Action.Type.getByName(rs.getString("type")));
			action.setPayload(rs.getString("payload"));
			return action;
		}
	}
}

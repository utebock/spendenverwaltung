package at.fraubock.spendenverwaltung.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import at.fraubock.spendenverwaltung.interfaces.dao.IActionDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;

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
	private List<Object> values;
	private String where;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Action> getLimitedResultByAttributes(ActionSearchVO searchVO,
			int offset, int count) throws PersistenceException {

		if (searchVO == null) {
			log.error("Error executing getLimitedResultByAttributes: "
					+ "searchVO was null");
			throw new IllegalArgumentException("searchVO must not be null");
		}

		log.info("Reading Actions for searchVO:" + searchVO);

		setWhereAndValues(searchVO);

		try {
			return jdbcTemplate.query("select * from actions" + this.where
					+ "ORDER BY time DESC LIMIT " + offset + ", " + count,
					this.values.toArray(), new ActionMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public List<Action> getByAttributesFromOthers(ActionSearchVO searchVO,
			int offset, int count) throws PersistenceException {
		
		List<Action> actions = new ArrayList<Action>();
		String currentActor = fetchCurrentUser();
		
		for(Action a: getLimitedResultByAttributes(searchVO,offset,count)) {
			if(!currentActor.equals(a.getActor())) {
				actions.add(a);
			}
		}
		return actions;
	}

	@Override
	public long getNumberOfResultsByAttributes(ActionSearchVO searchVO)
			throws PersistenceException {

		if (searchVO == null) {
			log.error("Error getNumberOfResultsByAttributes: "
					+ "searchVO was null");
			throw new IllegalArgumentException("searchVO must not be null");
		}

		log.info("Counting Actions for searchVO:" + searchVO);

		setWhereAndValues(searchVO);

		try {
			return jdbcTemplate.queryForObject("select count(*) from actions"
					+ this.where, this.values.toArray(), Integer.class);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	private void setWhereAndValues(ActionSearchVO searchVO) {
		List<Object> values = new ArrayList<Object>();
		String where = " where ";
		if (searchVO.getActor() != null) {
			where += "actor LIKE ? and ";
			values.add("%" + searchVO.getActor() + "%");
		}

		if (searchVO.getEntity() != null) {
			where += "entity = ? and ";
			values.add(searchVO.getEntity().getName());
		}

		if (searchVO.getType() != null) {
			where += "type = ? and ";
			values.add(searchVO.getType().getName());
		}

		if (searchVO.getPayload() != null) {
			where += "payload LIKE ? and ";
			values.add("%" + searchVO.getPayload() + "%");
		}

		if (searchVO.getFrom() != null) {
			where += "time >= ? and ";
			values.add(searchVO.getFrom());
		}

		if (searchVO.getTo() != null) {
			where += "time <= ? and ";
			values.add(searchVO.getTo());
		}

		// we don't want to show these actions to the user:
		this.where = where
				+ " entity NOT LIKE '%criterion%' and entity NOT "
				+ "LIKE '%templates%' and entity <> 'sent_mailings' and (payload "
				+ "NOT LIKE '%andere duerfen: privat%' or actor = '"+fetchCurrentUser()+"') " +
				"and payload NOT LIKE '%filter ist anonym%' ";
		this.values = values;
	}
	
	private String fetchCurrentUser() {
		return jdbcTemplate.queryForObject("SELECT (SUBSTRING_INDEX(USER(),'@',1))", String.class);
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

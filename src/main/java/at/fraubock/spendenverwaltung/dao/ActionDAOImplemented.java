package at.fraubock.spendenverwaltung.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

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
	private List<String> values;
	private String where;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Action> getLimitedResultByAttributes(
			ActionSearchVO searchVO, int offset, int count)
			throws PersistenceException {

		if (searchVO == null) {
			log.error("Error executing getLimitedResultByAttributes: "
					+ "searchVO was null");
			throw new IllegalArgumentException("searchVO must not be null");
		}
		
		log.info("Reading Actions for searchVO:" + searchVO);
		
		setWhereAndValues(searchVO);
		
		try {
			return jdbcTemplate.query("select * from actions" + this.where
					+ " ORDER BY time DESC LIMIT " + offset + ", " + count,
					this.values.toArray(), new ActionMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
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
			return jdbcTemplate.queryForObject("select count(*) from actions" + this.where
					+ " ORDER BY time DESC ",
					this.values.toArray(), Integer.class);
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}
	
	
	private void setWhereAndValues(ActionSearchVO searchVO) {
		List<String> values = new ArrayList<String>();
		String where = "";
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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (searchVO.getFrom() != null) {
			where += "DATE(time) >= DATE(?) and ";
			values.add(sdf.format(searchVO.getFrom()));
		}

		if (searchVO.getTo() != null) {
			where += "DATE(time) <= DATE(?) and ";
			values.add(sdf.format(searchVO.getTo()));
		}
		
		if(!StringUtils.isEmpty(where)) {
			where = " where " +  where.substring(0, where.lastIndexOf(" and "));
		}
		
		this.where = where;
		this.values = values;
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

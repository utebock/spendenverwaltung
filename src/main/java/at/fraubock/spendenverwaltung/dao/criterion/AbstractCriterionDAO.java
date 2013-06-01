package at.fraubock.spendenverwaltung.dao.criterion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.MountedFilterCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;

public class AbstractCriterionDAO {

	private static final Logger log = Logger
			.getLogger(AbstractCriterionDAO.class);

	private JdbcTemplate jdbcTemplate;
	private ConnectedCriterionDAO connectedCritDAO;
	private PropertyCriterionDAO propertyCritDAO;
	private MountedFilterCriterionDAO mountedCritDAO;

	public void insert(Criterion f) throws PersistenceException {

		if (f == null) {
			log.error("Argument was null");
			throw new IllegalArgumentException("Criterion must not be null");
		}

		KeyHolder criterionKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new CreateFilterCriterionStatementCreator(f),
				criterionKeyHolder);
		f.setId(criterionKeyHolder.getKey().intValue());

		if (f instanceof ConnectedCriterion) {
			connectedCritDAO.insert((ConnectedCriterion) f);
		} else if (f instanceof PropertyCriterion) {
			propertyCritDAO.insert((PropertyCriterion) f);
		} else if (f instanceof MountedFilterCriterion) {
			mountedCritDAO.insert((MountedFilterCriterion) f);
		}
	}

	public Criterion getById(int id) throws PersistenceException {
		PropertyCriterion prop = propertyCritDAO.getById(id);
		if (prop != null) {
			return prop;
		}

		ConnectedCriterion logical = connectedCritDAO.getById(id);
		if (logical != null) {
			return logical;
		}

		MountedFilterCriterion mount = mountedCritDAO.getById(id);
		if (mount != null) {
			return mount;
		}

		return null;

	}

	public void delete(Criterion f) throws PersistenceException {
		// delete the specific criterion
		if (f instanceof ConnectedCriterion) {
			connectedCritDAO.delete((ConnectedCriterion) f);
		} else if (f instanceof PropertyCriterion) {
			propertyCritDAO.delete((PropertyCriterion) f);
		} else if (f instanceof MountedFilterCriterion) {
			mountedCritDAO.delete((MountedFilterCriterion) f);
		}

		// delete the criterion
		jdbcTemplate.update("delete from criterion where id = ?",
				new Object[] { f.getId() },
				new int[] { Types.INTEGER });
	}

	/* mappers for inserting and reading this entity */

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

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ConnectedCriterionDAO getConnectedCritDAO() {
		return connectedCritDAO;
	}

	public void setConnectedCritDAO(ConnectedCriterionDAO connectedCritDAO) {
		this.connectedCritDAO = connectedCritDAO;
	}

	public PropertyCriterionDAO getPropertyCritDAO() {
		return propertyCritDAO;
	}

	public void setPropertyCritDAO(PropertyCriterionDAO propertyCritDAO) {
		this.propertyCritDAO = propertyCritDAO;
	}

	public MountedFilterCriterionDAO getMountedCritDAO() {
		return mountedCritDAO;
	}

	public void setMountedCritDAO(MountedFilterCriterionDAO mountedCritDAO) {
		this.mountedCritDAO = mountedCritDAO;
	}

}

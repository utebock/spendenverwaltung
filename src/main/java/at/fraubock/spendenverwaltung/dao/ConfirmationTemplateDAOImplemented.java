package at.fraubock.spendenverwaltung.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;

public class ConfirmationTemplateDAOImplemented implements
		IConfirmationTemplateDAO {
	
	private static final Logger log = Logger.getLogger(ConfirmationTemplateDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void insert(ConfirmationTemplate template)
			throws PersistenceException {
		
		try {
			log.info("Inserting ConfirmationTemplate");
			validate(template);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new ConfirmationTemplateStatementCreator(template),
					keyHolder);

			template.setId(keyHolder.getKey().intValue());

			log.info("ConfirmationTemplate entry successfully created: "
					+ template.getFile().getAbsolutePath());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void delete(final ConfirmationTemplate template)
			throws PersistenceException {
		try {
			log.info("Deleting ConfirmationTemplate...");
				
			validate(template);
			jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					String deleteQuery = "delete from donation_confirmation_templates where id=?";

					PreparedStatement ps = con.prepareStatement(deleteQuery);
					ps.setInt(1, template.getId());
					return ps;
				}

			});

			log.info("ConfirmationTemplate entity successfully deleted:"
					+ template.getFile().getAbsolutePath());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}

	}

	@Override
	public List<ConfirmationTemplate> getAll() throws PersistenceException {
		try {
			log.info("Reading all ConfirmationTemplates.");
			return jdbcTemplate.query(
					"select * from donation_confirmation_templates ORDER BY id DESC",
					new ConfirmationTemplateMapper());
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public ConfirmationTemplate getByID(int id) throws PersistenceException {
		try {
			log.info("Reading ConfirmationTemplate with id='" + id + "'");
			if (id < 0) {
				log.info("Error reading ConfirmationTemplate with id='" + id
						+ "': Id was less than 0");
				throw new IllegalArgumentException("Id must not be less than 0");
			}

			try {
				return jdbcTemplate.queryForObject(
						"select * from donation_confirmation_templates where id = ?",
						new Object[] { id }, new ConfirmationTemplateMapper());
			} catch (IncorrectResultSizeDataAccessException e) {
				if (e.getActualSize() == 0)
					return null;
				else
					throw new PersistenceException(e);
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}
	
	private class ConfirmationTemplateMapper implements RowMapper<ConfirmationTemplate> {

		public ConfirmationTemplate mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ConfirmationTemplate template = new ConfirmationTemplate();
			template.setId(rs.getInt("id"));
			template.setName(rs.getString("name"));

			File file = null;
			try {
				file = File.createTempFile(template.getName(), null);
				file.deleteOnExit();
			} catch (IOException e1) {
				throw new SQLException(e1);
			}
			
			InputStream is = rs.getBinaryStream("data");
			OutputStream os = null;
			try {
				os = new FileOutputStream(file);

				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = is.read(bytes)) != -1) {
					os.write(bytes, 0, read);
				}
			} catch (FileNotFoundException e) {
				log.error("The file with path='"
						+ template.getFile().getAbsolutePath()
						+ "' could not be created: " + e.getMessage());
				throw new SQLException(e);
			} catch (IOException e) {
				log.error("Writing to file with path='"
						+ template.getFile().getAbsolutePath() + "' failed: "
						+ e.getMessage());
				throw new SQLException(e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

			template.setFile(file);
			return template;
		}
	}

	private class ConfirmationTemplateStatementCreator implements
			PreparedStatementCreator {

		private ConfirmationTemplate template;

		public ConfirmationTemplateStatementCreator(ConfirmationTemplate template) {
			this.template = template;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			String createAddress = "insert into donation_confirmation_templates (name, data)"
					+ " values (?,?)";

			PreparedStatement ps = connection.prepareStatement(createAddress,
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, template.getName());
			try {
				ps.setBinaryStream(2, new FileInputStream(template.getFile()),(int) template
						.getFile().length());
			} catch (FileNotFoundException e) {
				log.error("The file with path='"
						+ template.getFile().getAbsolutePath()
						+ "' could not be found.");
				throw new SQLException(e);
			}
			return ps;
		}
	}

	private static void validate(ConfirmationTemplate mt) throws ValidationException {
		if (mt == null) {
			log.error("Argument was null");
			throw new ValidationException("ConfirmationTemplate must not be null");
		}

		if (mt.getName() == null) {
			log.error("File name was null");
			throw new ValidationException("File name must not be null");
		}

		if (mt.getFile() == null) {
			log.error("File was null");
			throw new ValidationException("File must not be null");
		}
	}


}

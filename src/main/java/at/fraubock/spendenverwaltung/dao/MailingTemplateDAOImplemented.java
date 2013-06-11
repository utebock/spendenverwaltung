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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IMailingTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.MailingTemplate;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;

public class MailingTemplateDAOImplemented implements IMailingTemplateDAO {
	private static final Logger log = Logger
			.getLogger(MailingTemplateDAOImplemented.class);

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void insert(final MailingTemplate mt) throws PersistenceException {
		log.info("Inserting MailingTemplate...");

		try {
			validate(mt);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new MailingTemplateStatementCreator(mt), keyHolder);

		mt.setId(keyHolder.getKey().intValue());

		log.info("MailingTemplate entry successfully created: "
				+ mt.getFile().getAbsolutePath());
	}

	@Override
	public void delete(final MailingTemplate mt) throws PersistenceException {
		log.info("Deleting MailingTemplate...");

		try {
			validate(mt);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				String updateAddress = "delete from mailing_templates where id=?";

				PreparedStatement ps = con.prepareStatement(updateAddress);
				ps.setInt(1, mt.getId());
				return ps;
			}

		});

		log.info("MailingTemplate entity successfully deleted:"
				+ mt.getFile().getAbsolutePath());
	}

	@Override
	public List<MailingTemplate> getAll() throws PersistenceException {
		log.info("Reading all MailingTemplates.");
		return jdbcTemplate.query(
				"select * from mailing_templates ORDER BY id DESC",
				new MailingTemplateMapper());
	}

	@Override
	public MailingTemplate getByID(int id) throws PersistenceException {
		log.info("Reading MailingTemplate with id='" + id + "'");
		if (id < 0) {
			log.info("Error reading MailingTemplate with id='" + id
					+ "': Id was less than 0");
			throw new IllegalArgumentException("Id must not be less than 0");
		}

		try {
			return jdbcTemplate.queryForObject(
					"select * from mailing_templates where id = ?",
					new Object[] { id }, new MailingTemplateMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0)
				return null;
			else
				throw new PersistenceException(e);
		}
	}

	private class MailingTemplateMapper implements RowMapper<MailingTemplate> {

		public MailingTemplate mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			MailingTemplate mt = new MailingTemplate();
			mt.setId(rs.getInt("id"));
			mt.setFileName(rs.getString("file_name"));
			mt.setFileSize(rs.getInt("file_size"));

			File file = new File(""); // TODO path?
			InputStream is = rs.getBinaryStream("file");
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
						+ mt.getFile().getAbsolutePath()
						+ "' could not be created: " + e.getMessage());
				throw new SQLException(e);
			} catch (IOException e) {
				log.error("Writing to file with path='"
						+ mt.getFile().getAbsolutePath() + "' failed: "
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

			mt.setFile(file);
			return mt;
		}
	}

	private class MailingTemplateStatementCreator implements
			PreparedStatementCreator {

		private MailingTemplate mt;

		public MailingTemplateStatementCreator(MailingTemplate mt) {
			this.mt = mt;
		}

		@Override
		public PreparedStatement createPreparedStatement(Connection connection)
				throws SQLException {
			String createAddress = "insert into mailing_templates (file_name,file_size,file)"
					+ " values (?,?,?)";

			PreparedStatement ps = connection.prepareStatement(createAddress,
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, mt.getFileName());
			ps.setInt(2, mt.getFileSize());
			try {
				ps.setBinaryStream(3, new FileInputStream(mt.getFile()));
			} catch (FileNotFoundException e) {
				log.error("The file with path='"
						+ mt.getFile().getAbsolutePath()
						+ "' could not be found.");
				throw new SQLException(e);
			}
			return ps;
		}
	}

	private static void validate(MailingTemplate mt) throws ValidationException {
		if (mt == null) {
			log.error("Argument was null");
			throw new ValidationException("MailingTemplate must not be null");
		}

		if (mt.getFileName() == null) {
			log.error("File name was null");
			throw new ValidationException("File name must not be null");
		}

		if (mt.getFile() == null) {
			log.error("File was null");
			throw new ValidationException("File must not be null");
		}
	}

}

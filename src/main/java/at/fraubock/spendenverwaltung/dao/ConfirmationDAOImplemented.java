package at.fraubock.spendenverwaltung.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IConfirmationTemplateDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IDonationDAO;
import at.fraubock.spendenverwaltung.interfaces.dao.IPersonDAO;
import at.fraubock.spendenverwaltung.interfaces.domain.Confirmation;
import at.fraubock.spendenverwaltung.interfaces.domain.ConfirmationTemplate;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.PersistenceException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ValidationException;
import at.fraubock.spendenverwaltung.util.filter.FilterBuilder;

public class ConfirmationDAOImplemented implements IConfirmationDAO {

	private static final Logger log = Logger.getLogger(ConfirmationDAOImplemented.class);
	
	private JdbcTemplate jdbcTemplate;
	private IConfirmationTemplateDAO confirmationTemplateDao;
	private IPersonDAO personDao;
	private IDonationDAO donationDao;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setConfirmationTemplateDao(
			IConfirmationTemplateDAO confirmationTemplateDao) {
		this.confirmationTemplateDao = confirmationTemplateDao;
	}
	
	public void setPersonDao(IPersonDAO personDao) {
		this.personDao = personDao;
	}

	public void setDonationDao(IDonationDAO donationDao) {
		this.donationDao = donationDao;
	}

	
	public static void validate(Confirmation c) throws ValidationException{
		if(c == null)
			throw new ValidationException("Confirmation was null");
		
		if(c.getPerson() == null)
			throw new ValidationException("Person was null");
		
		if(c.getId()!=null){
			if(c.getId()<0)
				throw new ValidationException("Id was negative");
		}
		
		if(c.getTemplate() == null)
			throw new ValidationException("Template was null");
		
		if(c.getDate() == null)
			throw new ValidationException("Date was null");
			
		if(c.getDonation() == null && c.getFromDate() == null && c.getToDate() == null)
			throw new ValidationException("Donation or fromDate and toDate must not be null");
		
		if(c.getDonation() != null && (c.getFromDate() != null || c.getToDate() != null))
			throw new ValidationException("Donation and fromDate/toDate can't be set both");
			
		if(c.getFromDate() != null && c.getToDate() == null)
			throw new ValidationException("toDate was null");
		
		if(c.getFromDate() == null && c.getToDate() != null)
			throw new ValidationException("fromDate was null");
	}
	
	private class CreateConfirmationStatementCreator implements 
	PreparedStatementCreator{
		
		private Confirmation c;
		
		CreateConfirmationStatementCreator(Confirmation c){
			this.c = c;
		}
		
		private String createConfirmation = "insert into donation_confirmations(personid, template, confirmation_date) values(?,?,?)";

		@Override
		public PreparedStatement createPreparedStatement(Connection con)
				throws SQLException {
			PreparedStatement ps = con.prepareStatement(createConfirmation,
					Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, c.getPerson().getId());
			ps.setInt(2, c.getTemplate().getId());
			ps.setDate(3, new java.sql.Date(c.getDate().getTime()));

			return ps;
		}
		
	}

	@Override
	public void insertOrUpdate(Confirmation c) throws PersistenceException {
		try{
			log.debug("Entering insertOrUpdate with param " + c);

			try{
				validate(c);
			}
			catch(ValidationException e){
				throw new PersistenceException(e);
			}


			if(c.getId() == null){
				//Create

				if(c.getTemplate()!=null&&c.getTemplate().getId()==null){
					confirmationTemplateDao.insertOrUpdate(c.getTemplate());
				}
				KeyHolder keyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(new CreateConfirmationStatementCreator(c), keyHolder);

				c.setId(keyHolder.getKey().intValue());

				//Create entries in single_donation_confirmation or multiple_donations_confirmation

				if(c.getDonation()!=null){
					//single_donation_confirmation

					jdbcTemplate.update("insert into single_donation_confirmation(id, donationid) values (?, ?)",
							new Object[] { c.getId(), c.getDonation().getId() });
				}
				else if(c.getFromDate() != null && c.getToDate() != null){
					//multiple_donations_confirmation

					jdbcTemplate.update("insert into multiple_donations_confirmation(id, from_date, to_date) values (?, ?, ?)",
							new Object[] { c.getId(), new java.sql.Date(c.getFromDate().getTime()), 
							new java.sql.Date(c.getToDate().getTime()) });
				}
			}
			else{
				//update

				if(c.getTemplate()!=null&&c.getTemplate().getId()==null){
					confirmationTemplateDao.insertOrUpdate(c.getTemplate());
				}
				
				String update = "update donation_confirmations set personid = ?, template = ?, confirmation_date = ? where id = ?";

				Object [] values = { c.getPerson().getId(), c.getTemplate().getId(), new java.sql.Date(c.getDate().getTime()), c.getId()};

				jdbcTemplate.update(update, values);


				//delete entry in single or multiple donations confirmation
				String delete = "delete from single_donation_confirmation where id = ?";
				jdbcTemplate.update(delete, new Object[] {c.getId()});

				delete = "delete from multiple_donations_confirmation where id = ?";
				jdbcTemplate.update(delete, new Object[] {c.getId()});

				//Create entries in single_donation_confirmation or multiple_donations_confirmation
				if(c.getDonation()!=null){
					//single_donation_confirmation

					jdbcTemplate.update("insert into single_donation_confirmation(id, donationid) values (?, ?)",
							new Object[] { c.getId(), c.getDonation().getId() });
				}
				else if(c.getFromDate() != null && c.getToDate() != null){
					//multiple_donations_confirmation

					jdbcTemplate.update("insert into multiple_donations_confirmation(id, from_date, to_date) values (?, ?, ?)",
							new Object[] { c.getId(), new java.sql.Date(c.getFromDate().getTime()), 
							new java.sql.Date(c.getToDate().getTime()) });
				}
			}

			log.debug("Returning from insertOrUpdate");
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public void delete(Confirmation c) throws PersistenceException {
		try {
			log.debug("Entering delete with param " + c);

			if (c == null || c.getId() == null) {
				throw new IllegalArgumentException(
						"Confirmation's id was null in delete");
			}
			
			jdbcTemplate.update("DELETE FROM donation_confirmations where id=?",
					new Object[] { c.getId() });

			//delete entries in single_donation_confirmation or multiple_donations_confirmation
			if(c.getDonation()!=null){
				//single_donation_confirmation

				String delete = "delete from single_donation_confirmation where id = ?";
				jdbcTemplate.update(delete, new Object[] {c.getId()});
			}
			else if(c.getFromDate() != null && c.getToDate() != null){
				//multiple_donations_confirmation

				String delete = "delete from multiple_donations_confirmation where id = ?";
				jdbcTemplate.update(delete, new Object[] {c.getId()});
			}
			
			log.debug("Returning from delete");
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
		
	}

	@Override
	public List<Confirmation> getAll() throws PersistenceException {
		try {
			log.debug("Entering getAll");
			ConfirmationMapper mapper = new ConfirmationMapper();
			List<Confirmation> confirmations = jdbcTemplate.query(
							"SELECT c.*, s.donationid, m.from_date, m.to_date " +
							"FROM donation_confirmations c " +
								"left outer join single_donation_confirmation s on c.id = s.id " +
								"left outer join multiple_donations_confirmation m on c.id = m.id"
							, mapper);

			for (Confirmation c : confirmations) {
				Integer tmplId = mapper.getTemplateIds().get(c.getId());
				if(tmplId != null){
					c.setTemplate(confirmationTemplateDao.getByID(tmplId));
				}
				
				Integer personId = mapper.getPersonIds().get(c.getId());
				if(personId != null){
					c.setPerson(personDao.getById(personId));
				}
				
				if(c.getFromDate() == null && c.getToDate() == null){
					Integer donationId = mapper.getDonationIds().get(c.getId());
					if(donationId != null){
						c.setDonation(donationDao.getByID(donationId));
					}
				}
			}

			log.debug("Returning from getAll");
			return confirmations;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}

	@Override
	public Confirmation getById(int id) throws PersistenceException {
		try {
			log.debug("Entering getById with param " + id);

			try {
				ConfirmationMapper mapper = new ConfirmationMapper();
				Confirmation c = jdbcTemplate.queryForObject(
						"SELECT c.*, s.donationid, m.from_date, m.to_date " +
						"FROM donation_confirmations c " +
							"left outer join single_donation_confirmation s on c.id = s.id " +
							"left outer join multiple_donations_confirmation m on c.id = m.id " +
						"WHERE c.id=?",
						new Object[] { id }, mapper);

				Integer tmplId = mapper.getTemplateIds().get(c.getId());
				if (tmplId != null) {
					c.setTemplate(confirmationTemplateDao.getByID(tmplId));
				}
				
				Integer personId = mapper.getPersonIds().get(c.getId());
				if(personId != null){
					c.setPerson(personDao.getById(personId));
				}
				
				if(c.getFromDate() == null && c.getToDate() == null){
					Integer donationId = mapper.getDonationIds().get(c.getId());
					if(donationId != null){
						c.setDonation(donationDao.getByID(donationId));
					}
				}

				log.debug("Returning from getById with result " + c);
				return c;
			} catch (EmptyResultDataAccessException e) {
				// return null if query returns 0 rows
				return null;
			}
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public List<Confirmation> getByConfirmationTemplate(ConfirmationTemplate template) throws PersistenceException {
		if(template==null || template.getId() == null){
			throw new IllegalArgumentException("Template and template id must not be null");
		}
		
		try {
			log.debug("Entering getByConfirmationTemplate");
			ConfirmationMapper mapper = new ConfirmationMapper();
			List<Confirmation> confirmations = jdbcTemplate.query(
							"SELECT c.*, s.donationid, m.from_date, m.to_date " +
							"FROM donation_confirmations c " +
								"left outer join single_donation_confirmation s on c.id = s.id " +
								"left outer join multiple_donations_confirmation m on c.id = m.id " +
								"WHERE c.template = ?"
							, new Object[]{template.getId()}, mapper);

			for (Confirmation c : confirmations) {
				Integer tmplId = mapper.getTemplateIds().get(c.getId());
				if(tmplId != null){
					c.setTemplate(confirmationTemplateDao.getByID(tmplId));
				}
				
				Integer personId = mapper.getPersonIds().get(c.getId());
				if(personId != null){
					c.setPerson(personDao.getById(personId));
				}
				
				if(c.getFromDate() == null && c.getToDate() == null){
					Integer donationId = mapper.getDonationIds().get(c.getId());
					if(donationId != null){
						c.setDonation(donationDao.getByID(donationId));
					}
				}
			}

			log.debug("Returning from getByConfirmationTemplate");
			return confirmations;
		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}
	}
	
	@Override
	public List<Confirmation> getByPersonNameLike(String searchString) throws PersistenceException {
		log.debug("Entering getByPersonNameLike with param " + searchString);

		try {
			ConfirmationMapper mapper = new ConfirmationMapper();
			List<Confirmation> confirmations = jdbcTemplate.query(
					"SELECT c.*, s.donationid, m.from_date, m.to_date " +
					"FROM donation_confirmations c " +
						"left outer join single_donation_confirmation s on c.id = s.id " +
						"left outer join multiple_donations_confirmation m on c.id = m.id " +
						"join persons p on c.personid = p.id "+
					"WHERE p.givenname like ? OR p.surname like ?",
					new Object[] { "%"+searchString+"%","%"+searchString+"%" }, mapper);

			for (Confirmation c : confirmations) {
				Integer tmplId = mapper.getTemplateIds().get(c.getId());
				if(tmplId != null){
					c.setTemplate(confirmationTemplateDao.getByID(tmplId));
				}
				
				Integer personId = mapper.getPersonIds().get(c.getId());
				if(personId != null){
					c.setPerson(personDao.getById(personId));
				}
				
				if(c.getFromDate() == null && c.getToDate() == null){
					Integer donationId = mapper.getDonationIds().get(c.getId());
					if(donationId != null){
						c.setDonation(donationDao.getByID(donationId));
					}
				}
			}

			log.debug("Returning from getByPersonNameLike with result " + confirmations);
			return confirmations;
		} catch (EmptyResultDataAccessException e) {
			// return null if query returns 0 rows
			return null;

		} catch (DataAccessException e) {
			log.warn(e.getLocalizedMessage());
			throw new PersistenceException(e);
		}

	}

	private class ConfirmationMapper implements RowMapper<Confirmation> {

		private Map<Integer, Integer> templateIds = new HashMap<Integer, Integer>();
		private Map<Integer, Integer> personIds = new HashMap<Integer, Integer>();
		private Map<Integer, Integer> donationIds = new HashMap<Integer, Integer>();

		@Override
		public Confirmation mapRow(ResultSet rs, int rowNum) throws SQLException {
			Confirmation c = new Confirmation();

			c.setId(rs.getInt("id"));
			c.setDate(new java.util.Date(rs.getDate("confirmation_date")
					.getTime()));
			personIds.put(rs.getInt("id"),rs.getInt("personid"));
			templateIds.put(rs.getInt("id"), rs.getInt("template"));
			if(rs.getDate("from_date")!=null&&rs.getDate("to_date")!=null){
				Date fromDate = new java.util.Date(rs.getDate("from_date").getTime());
				Date toDate = new java.util.Date(rs.getDate("to_date").getTime());
				
				c.setFromDate(fromDate);
				c.setToDate(toDate);
			}
			else{
				donationIds.put(rs.getInt("id"), rs.getInt("donationid"));
			}

			return c;
		}

		public Map<Integer, Integer> getTemplateIds() {
			return templateIds;
		}
		
		public Map<Integer, Integer> getPersonIds(){
			return personIds;
		}
		
		public Map<Integer, Integer> getDonationIds(){
			return donationIds;
		}

	}

	
}

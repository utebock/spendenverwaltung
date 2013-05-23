package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import service.DonationValidator;

import domain.Donation;
import domain.DonationFilter;
import domain.Person;
import exceptions.IllegalDBStateException;
import exceptions.PersistenceException;

public class DonationDAOImplemented implements IDonationDAO{

	private JdbcTemplate jdbcTemplate;
	private IPersonDAO personDAO;
	private DonationValidator donationValidator;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public void setPersonDao(IPersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	public void setDonationValidator(DonationValidator donationValidator){
		this.donationValidator = donationValidator;
	}
	
	private class CreateDonationStatementCreator implements PreparedStatementCreator {

		private Donation donation;
		
		CreateDonationStatementCreator(Donation donation) {
			this.donation = donation;
		}
		
		private String createDonations = "insert into donations (personid, amount, date, dedication, type, note) values (?,?,?,?,?,?)";
				
		@Override
		public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(createDonations, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, donation.getPerson().getId());
					ps.setDouble(2, donation.getAmount());
					ps.setTimestamp(3, new Timestamp(donation.getDate().getTime()));
					ps.setString(4, donation.getDedication());
					ps.setString(5, donation.getType().toString());
					ps.setString(6, donation.getNote());
					
					return ps;
		}
	}
	
	@Override
	public Donation create(Donation d) throws PersistenceException {
		donationValidator.validate(d);
		
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new CreateDonationStatementCreator(d), keyHolder);
		
		d.setId(keyHolder.getKey().intValue());
		
		return d;
	}

	@Override
	public Donation update(Donation d) throws PersistenceException {
		donationValidator.validate(d);
		
		String updateStatement = "update donations set personid = ?, amount = ?, date = ?, dedication = ?, type = ?, note = ? where id = ?;";
		
		Object[] params = new Object[] { d.getPerson().getId(), d.getAmount(), d.getDate(), 
				d.getDedication(), d.getType(), d.getNote(), d.getId() };
		
		int[] types = new int[] { Types.INTEGER, Types.DECIMAL, Types.TIMESTAMP, Types.VARCHAR, 
				Types.VARCHAR, Types.VARCHAR, Types.INTEGER };
		
		jdbcTemplate.update(updateStatement, params, types);
		
		return d;
	}

	@Override
	public void delete(Donation d) throws PersistenceException {
		donationValidator.validate(d);
		
		String deleteStatement = "delete from donations where id = ?;";
		
		Object[] params = new Object[] { d.getId() };
		
		int[] types = new int[] {Types.INTEGER};
		
		jdbcTemplate.update(deleteStatement, params, types);
	}

	@Override
	public Donation getByID(int id) throws PersistenceException {
		
		if(id < 0){
			throw new IllegalArgumentException("Id must not be less than 0");
		}
		
		Donation donation;
		
		String select = "select * from donations where id = ?;";
		
		donation = jdbcTemplate.queryForObject(select, new Object[]{id}, new DonationMapper());
	
		return donation;
	}

	@Override
	public List<Donation> getByPerson(Person p) throws PersistenceException {
		if(p == null){
			throw new IllegalArgumentException("person must not be null");
		}

		String select = "select * from donations where personid = ?;";
		
		List<Donation> donations = jdbcTemplate.query(select, new Object[] {p.getId()}, new DonationMapper());
		
		return donations;
	}
	
	@Override 
	public List<Donation> getByFilter(DonationFilter filter) throws PersistenceException{
		
		if(filter == null || filter.isEmpty()){
			//return getAll();
		}
		
		String select = "SELECT * FROM donations WHERE";
		ArrayList<Object> args = new ArrayList<Object>();
		
		if(filter.getDedicationPart() != null){
			select += " dediction like '%?%' AND";
			args.add(filter.getDedicationPart());
		}
		
		if(filter.getMaxAmount() != null){
			select += " amount <= ? AND";
			args.add(filter.getMaxAmount());
		}
		
		if(filter.getMinAmount() != null){
			select += " amount >= ? AND";
			args.add(filter.getMinAmount());
		}
		
		if(filter.getMaxDate() != null){
			select += " date <= ? AND";
			args.add(new Timestamp(filter.getMaxDate().getTime()));
		}
		
		if(filter.getMinDate() != null){
			select += " date >= ? AND";
			args.add(new Timestamp(filter.getMinDate().getTime()));
		}
		
		if(filter.getNotePart()!=null){
			select += " note like '%?%' AND";
			args.add(filter.getNotePart());
		}
		
		if(filter.getType()!=null){
			select += " type = ? AND";
			args.add(filter.getType());
		}
		
		//remove last AND
		select = select.substring(0, select.length() - 3);
		
		List<Donation> donations = jdbcTemplate.query(select, args.toArray(), new DonationMapper());
		
		
		return donations;
	}

	
	private class DonationMapper implements RowMapper<Donation> {
		
		public Donation mapRow(ResultSet rs, int rowNum) throws SQLException{
			Donation donation = new Donation();
			donation.setId(rs.getInt("id"));
			try {
				donation.setPerson(personDAO.getById(rs.getInt("personid")));
			} catch (PersistenceException e) {
				throw new IllegalDBStateException(e);
			}
			donation.setAmount(rs.getInt("amount"));
			donation.setDate(rs.getDate("date"));
			donation.setDedication(rs.getString("dedication"));
			donation.setNote(rs.getString("note"));
			donation.setType(Donation.DonationType.valueOf(rs.getString("type")));
			
			return donation;
		}
	}
}

package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import domain.Person;
import org.springframework.jdbc.core.RowMapper;

public class PersonMapper implements RowMapper<Person> {
	
	public Person mapRow(ResultSet rs, int rowNum) throws SQLException{
		Person person = new Person();
		person.setId(rs.getInt("id"));
		person.setGivenName(rs.getString("given_name"));
		person.setSurname(rs.getString("surname"));
		/**
		 * FIXME: address to be inserted
		 */
		person.setEmail(rs.getString("email"));
		person.setSalutation(Person.Salutation.valueOf(rs.getString("salutation")));
		person.setTitle(rs.getString("title"));
		person.setCompany(rs.getString("company"));
		person.setTelephone(rs.getString("telephone"));
		person.setNotificationType(Person.NotificationType.valueOf(rs.getString("notification_type")));
		person.setNote(rs.getString("note"));
		
		return person;
	}
}

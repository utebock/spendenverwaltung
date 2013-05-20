package at.ac.tuwien;

import org.springframework.jdbc.core.JdbcTemplate;

import service.AddressServiceImplemented;
import service.PersonServiceImplemented;
import gui.MainFrame;

public class App 
{
	private static JdbcTemplate jdbcTemplate;
	
    public static void main( String[] args )
    {
        MainFrame mf = new MainFrame();
        PersonServiceImplemented personService = new PersonServiceImplemented();
        AddressServiceImplemented addressService = new AddressServiceImplemented();
        mf.setPersonService(personService);
        mf.setAddressService(addressService);
        MainFrame.openMainWindow();
        
    }
}

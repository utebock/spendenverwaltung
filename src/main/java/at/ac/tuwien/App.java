package at.ac.tuwien;

import gui.MainFrame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.IAddressService;
import service.IPersonService;

public class App {
	
    public static void main( String[] args )   {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		
        IPersonService personService = context.getBean("personService", IPersonService.class);
        IAddressService addressService = context.getBean("addressService", IAddressService.class);
        MainFrame mf = new MainFrame();
        
        mf.setPersonService(personService);
        mf.setAddressService(addressService);
        MainFrame.openMainWindow();
        
		if(context != null) {
			((AbstractApplicationContext) context).close();
		}
    }
}

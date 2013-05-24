package at.fraubock.spendenverwaltung.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class StartMainFrame {
	
	public void startMainFrame(){
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring.xml");
		
	    IPersonService personService = context.getBean("personService", IPersonService.class);
	    IAddressService addressService = context.getBean("addressService", IAddressService.class);
	    IDonationService donationService = context.getBean("donationService", IDonationService.class);
	    MainFrame mf = new MainFrame();
	    
	    mf.setPersonService(personService);
	    mf.setAddressService(addressService);
	    mf.setDonationService(donationService);
	    MainFrame.openMainWindow();
	    
		if(context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
	
}

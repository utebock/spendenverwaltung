package at.fraubock.spendenverwaltung.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class StartMainFrame {
	private MainFrame mf;

	public void startMainFrame() {
		mf = new MainFrame();
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/spring.xml");
		IPersonService personService = context.getBean("personService",
				IPersonService.class);
		IAddressService addressService = context.getBean("addressService",
				IAddressService.class);
		IDonationService donationService = context.getBean("donationService",
				IDonationService.class);
		IFilterService filterService = context.getBean("filterService",
				IFilterService.class);
		IImportService importService = context.getBean("importService",
				IImportService.class);

		mf.setPersonService(personService);
		mf.setAddressService(addressService);
		mf.setDonationService(donationService);
		mf.setFilterService(filterService);
		mf.setImportService(importService);
		mf.openMainWindow();

		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}

package at.fraubock.spendenverwaltung.cli;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IConfirmationService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

/**
 * Class providing main method for starting the CLI application of
 * Spendenverwaltung Ute Bock
 * 
 * @author manuel-bichler
 * 
 */
public class CliMain {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/spring.xml");

		IPersonService personService = context.getBean("personService",
				IPersonService.class);
		IDonationService donationService = context.getBean("donationService",
				IDonationService.class);
		IFilterService filterService = context.getBean("filterService",
				IFilterService.class);
		IAddressService addressService = context.getBean("addressService",
				IAddressService.class);
		IMailingService mailingService = context.getBean("mailingService",
				IMailingService.class);
		IConfirmationService confirmationService = context.getBean(
				"confirmationService", IConfirmationService.class);
		IImportService importService = context.getBean("importService",
				IImportService.class);
		IActionService actionService = context.getBean("actionService",
				IActionService.class);
		IMailChimpService mailChimpService = context.getBean(
				"mailChimpService", IMailChimpService.class);
		BasicDataSource databaseDataSource = context.getBean("dataSource",
				BasicDataSource.class);
		String defaultDatabaseUrl = context.getBean("defaultDatabaseUrl",
				String.class);

		CommandExecutor ex = new CommandExecutor(args, System.out, System.err);
		ex.setActionService(actionService);
		ex.setDataSource(databaseDataSource);
		ex.setDefaultDatabaseUrl(defaultDatabaseUrl);
		ex.setDonationService(donationService);
		ex.setFilterService(filterService);
		ex.setImportService(importService);
		ex.setMailingService(mailingService);
		ex.setPersonService(personService);

		int err = (ex).execute(System.console());
		if (err != 0)
			System.exit(err);

		if (context != null) {
			((AbstractApplicationContext) context).close();
		}
	}
}

package at.fraubock.spendenverwaltung.gui;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.gui.views.LoginView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailChimpService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class GuiStarter {

	// private static final Logger log = Logger.getLogger(GuiStarter.class);

	public void startGui() {

		// warning is unnecessary in this case, the resource is in fact closed
		// on exit
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/spring.xml");
		/**
		 * when the GUI is closed, SYSTEM_EXIT is called. this shutdown hook
		 * ensures the graceful shutdown of the context.
		 */
		((AbstractApplicationContext) context).registerShutdownHook();

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
		IImportService importService = context.getBean("importService",
				IImportService.class);
		IActionService actionService = context.getBean("actionService",
				IActionService.class);
		IMailChimpService mailChimpService = context.getBean(
				"mailChimpService", IMailChimpService.class);

		BasicDataSource databaseDataSource = context.getBean("dataSource",
				BasicDataSource.class);

		ViewDisplayer viewDisplayer = new ViewDisplayer();
		ComponentFactory componentFactory = new ComponentFactory();

		ViewActionFactory viewActionFactory = new ViewActionFactory(
				viewDisplayer, personService, donationService, filterService,
				addressService, mailingService, importService, actionService,
				mailChimpService);

		// need to call mainMenu.init() after all views are set in the
		// viewActionFactory
		// layout code is called from constructor, button initialization code is
		// called from init()

		// populate viewActionFactory

		// finish view initialization after viewActionFactory is populated (for
		// button wiring)
		// createPerson.init();
		// createMailingsView.initButtons();
		// mainFilterView.init();
		// donationProgressStatsView.init();
		// findPersonsView.init();
		// mainMenu.init();

		// display initial main menu
		// viewDisplayer.changeView(mainMenu);

		// switch to login view
		LoginView login = new LoginView(databaseDataSource, componentFactory,
				viewActionFactory, viewDisplayer);
		login.init();
		viewDisplayer.changeView(login);
	}

}
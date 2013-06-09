package at.fraubock.spendenverwaltung.gui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.gui.views.CreatePersonView;
import at.fraubock.spendenverwaltung.gui.views.DonationProgressStatsView;
import at.fraubock.spendenverwaltung.gui.views.MainMenuView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class GuiStarter {
	
//	private static final Logger log = Logger.getLogger(GuiStarter.class);
	
	public void startGui() {
		
		//warning is unnecessary in this case, the resource is in fact closed on exit
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring.xml");
		/**
		 * when the GUI is closed, SYSTEM_EXIT is called. this shutdown hook ensures 
		 * the graceful shutdown of the context.
		 */
		((AbstractApplicationContext) context).registerShutdownHook();
		
		IPersonService personService = context.getBean("personService", IPersonService.class);
		IDonationService donationService = context.getBean("donationService", IDonationService.class);
		IFilterService filterService = context.getBean("filterService", IFilterService.class);
		IAddressService addressService = context.getBean("addressService", IAddressService.class);
		IMailingService mailingService = context.getBean("mailingService", IMailingService.class);
		
		ViewDisplayer viewDisplayer = new ViewDisplayer();
		ComponentFactory componentFactory = new ComponentFactory();
		
		/**
		 * 
		 */
		ViewActionFactory viewActionFactory = new ViewActionFactory(viewDisplayer);
		
		//need to call mainMenu.init() after all views are set in the viewActionFactory
		//layout code is called from constructor, button initialization code is called from init()
		MainMenuView mainMenu = new MainMenuView(viewActionFactory, componentFactory);
		CreatePersonView createPerson = new CreatePersonView(componentFactory, viewActionFactory, personService, addressService, donationService, new PersonTableModel());
		MainFilterView mainFilterView = new MainFilterView(filterService, componentFactory, viewActionFactory);
		DonationProgressStatsView donationProgressStatsView = new DonationProgressStatsView(componentFactory, viewActionFactory, donationService, filterService);
		//populate viewActionFactory
		viewActionFactory.setMainMenuView(mainMenu);
		viewActionFactory.setCreatePersonView(createPerson);
		viewActionFactory.setMainFilterView(mainFilterView);
		viewActionFactory.setDonationProgressStatsView(donationProgressStatsView);
		
		
		//finish view initialization after viewActionFactory is populated (for button wiring)
		createPerson.init();
		mainFilterView.init();
		donationProgressStatsView.init();
		mainMenu.init();
		
		//display initial main menu
		viewDisplayer.changeView(mainMenu);
	}

}

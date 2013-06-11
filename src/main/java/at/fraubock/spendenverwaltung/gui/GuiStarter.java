package at.fraubock.spendenverwaltung.gui;

import javax.swing.JDialog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.gui.views.MainMenuView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.interfaces.service.IUserService;

public class GuiStarter {
	
//	private static final Logger log = Logger.getLogger(GuiStarter.class);
	private ApplicationContext context;
	
	public void startGui() {
		
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
		ViewActionFactory viewActionFactory = new ViewActionFactory(viewDisplayer, personService,
				donationService, filterService, addressService, mailingService);
		
		
		//need to call mainMenu.init() after all views are set in the viewActionFactory
		//layout code is called from constructor, button initialization code is called from init()
				
		MainMenuView mainMenu = new MainMenuView(viewActionFactory, componentFactory);
		//populate viewActionFactory
		
		
		//finish view initialization after viewActionFactory is populated (for button wiring)
//		createPerson.init();
//		createMailingsView.initButtons();
//		mainFilterView.init();
//		donationProgressStatsView.init();
//		findPersonsView.init();
		mainMenu.init();
		
		//display initial main menu
		viewDisplayer.changeView(mainMenu);
	}

	public void login(){
		//warning is unnecessary in this case, the resource is in fact closed on exit
		this.context = new ClassPathXmlApplicationContext("/spring.xml");
		/**
		 * when the GUI is closed, SYSTEM_EXIT is called. this shutdown hook ensures 
		 * the graceful shutdown of the context.
		 */
		((AbstractApplicationContext) context).registerShutdownHook();

		IUserService userService = context.getBean("userService", IUserService.class);
		
		JDialog loginDialog = new Login(userService, this);
	}
	
}

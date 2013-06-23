package at.fraubock.spendenverwaltung.gui.views;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

import at.fraubock.spendenverwaltung.gui.MainFilterView;
import at.fraubock.spendenverwaltung.gui.MainFrame;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.MailingTableModel;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.UnconfirmedMailingTableModel;
import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
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
 * 
 * @author Chris Steele
 * 
 *         Holds references of all Views used in this application. Provides
 *         methods that return Actions that switch the current View to that
 *         specified in the Action. These Actions can be used to initialize a
 *         JButton, taking away the need for Views needing to have references of
 *         other Views.
 */

public class ViewActionFactory {

	// private static final Logger log = Logger.getLogger(MainMenuView.class);
	private ViewDisplayer viewDisplayer;

	// services
	IPersonService personService;
	IDonationService donationService;
	IFilterService filterService;
	IAddressService addressService;
	IMailingService mailingService;
	IConfirmationService confirmationService;
	IImportService importService;
	IActionService actionService;
	IMailChimpService mailChimpService;

	public ViewActionFactory(ViewDisplayer viewDisplayer,
			IPersonService personService, IDonationService donationService,
			IFilterService filterService, IAddressService addressService,
			IMailingService mailingService, IConfirmationService confirmationService,
			IImportService importService, IActionService actionService, IMailChimpService mailChimpService) {

		this.viewDisplayer = viewDisplayer;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.mailingService = mailingService;
		this.confirmationService = confirmationService;
		this.filterService = filterService;
		this.importService = importService;
		this.actionService = actionService;
		this.mailChimpService = mailChimpService;
	}

	/**
	 * setters
	 */
	public void setViewDisplayer(ViewDisplayer viewDisplayer) {
		this.viewDisplayer = viewDisplayer;
	}

	public Action getCreatePersonsViewAction() {
		return new DisplayViewAction(new CreatePersonView(
				new ComponentFactory(), this, personService, addressService,
				donationService, new PersonTableModel()),
				"/images/createPerson.png");
	}

	public Action getMainMenuViewAction() {
		return new DisplayViewAction(new MainMenuView(this,
				new ComponentFactory()));
	}

	public Action getFindPersonsViewAction() {
		return new DisplayViewAction(new FindPersonsView(personService,
				addressService, donationService, filterService,
				new ComponentFactory(), this, new PersonTableModel()),
				"/images/showPersons.png");
	}

	public Action getFindPersonsViewAction(PersonTableModel personTableModel) {
		return new DisplayViewAction(new FindPersonsView(personService,
				addressService, donationService, filterService,
				new ComponentFactory(), this, personTableModel));
	}

	public Action getPersonAddressesViewAction(
			PersonTableModel personTableModel, Person selectedPerson) {
		return new DisplayViewAction(new PersonAddressesView(this,
				new ComponentFactory(), personService, addressService,
				selectedPerson, personTableModel));
	}

	public Action getPersonDonationsViewAction(
			PersonTableModel personTableModel, Person selectedPerson) {
		return new DisplayViewAction(new PersonDonationsView(this,
				new ComponentFactory(), donationService, confirmationService,  
				addressService, selectedPerson, personTableModel));
	}

	public Action getFindPersonsView() {
		return new DisplayViewAction(new FindPersonsView(personService,
				addressService, donationService, filterService,
				new ComponentFactory(), this, new PersonTableModel()));
	}

	public Action getMainFilterViewAction() {
		return new DisplayViewAction(new MainFilterView(new ComponentFactory(),
				this, filterService), "/images/filter.png");
	}

	public Action getRemovePersonFromMailingViewAction(Mailing mailing,
			MailingTableModel tableModel) {
		return new DisplayViewAction(new RemovePersonsFromMailingView(this,
				new ComponentFactory(), personService, mailingService, mailing,
				tableModel));
	}

	public Action getRemovePersonFromMailingViewAction(Mailing mailing,
			UnconfirmedMailingTableModel tableModel) {
		return new DisplayViewAction(new RemovePersonsFromMailingView(this,
				new ComponentFactory(), personService, mailingService, mailing,
				tableModel));
	}

	public Action getHistoryViewAction() {
		return new DisplayViewAction(new HistoryView(this,
				actionService), "/images/history.png");
	}

	// TODO richtige view returnen!
	public Action getDonationImportViewAction() {
		return new DisplayViewAction(new ImportDataView(new ComponentFactory(),
				this, importService), "/images/importOverview.png");
	}

	public Action getImportValidationViewAction() {
		return new DisplayViewAction(new ImportValidationView(personService,
				addressService, donationService, importService,
			new ComponentFactory(), this, viewDisplayer), "/images/importValidate.png");
	}

	// TODO richtige view returnen!
	public Action getCreateDonationConfirmationViewAction() {
		return new DisplayViewAction(new CreatePersonView(
				new ComponentFactory(), this, personService, addressService,
				donationService, new PersonTableModel()),
				"/images/createDonationConfirmation.jpg");
	}

	public Action getFindDonationConfirmationViewAction() {
		return new DisplayViewAction(new FindDonationConfirmationsView(personService, addressService,
				donationService, filterService, confirmationService, new ComponentFactory(), this),
				"/images/obtainDonationConfirmation.jpg");
	}

	public Action getCreateMailingsViewAction() {
		return new DisplayViewAction(new CreateMailingsView(this,
				new ComponentFactory(), mailingService, filterService,
				personService, mailChimpService), "/images/createNotification.png");
	}

	public Action getFindMailingsViewAction(MailingTableModel tableModel) {
		return new DisplayViewAction(new FindMailingsView(this,
				new ComponentFactory(), mailingService, filterService,
				tableModel), "/images/showNotifications.png");
	}

	public Action getConfirmMailingsViewAction(
			UnconfirmedMailingTableModel parentMailingTableModel) {
		return new DisplayViewAction(
				new ConfirmMailingsView(this, new ComponentFactory(),
						mailingService, parentMailingTableModel),
				"/images/confirmSendings.png");
	}

	public Action getDonationProgressStatsViewAction() {
		return new DisplayViewAction(new DonationProgressStatsView(
				new ComponentFactory(), this, donationService, filterService),
				"/images/statisticsDonation.png");
	}

	// TODO richtige view returnen!
	public Action getShowMailingStatsViewAction() {
		return new DisplayViewAction(new MailingStatsView(
				new ComponentFactory(), this, mailingService, filterService),
				"/images/statisticsNotification.png");
	}

	// TODO richtige view returnen!
	public Action getShowPersonStatsViewAction() {
		return new DisplayViewAction(new CreatePersonView(
				new ComponentFactory(), this, personService, addressService,
				donationService, new PersonTableModel()),
				"/images/statisticsPerson.jpg");
	}
	
	
	public InitializableView getViewForAction(Action a) {
		if(a instanceof DisplayViewAction) {
			return ((DisplayViewAction)a).getView();
		}
		return null;
	}

	private final class DisplayViewAction extends AbstractAction {

		private InitializableView view;

		public DisplayViewAction(InitializableView view) {
			this.view = view;
		}

		public DisplayViewAction(InitializableView view, String path) {
			this.view = view;
			java.net.URL url = getClass().getResource(path);
			this.putValue(Action.SMALL_ICON, new ImageIcon(url));
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			view.init();
			viewDisplayer.changeView(view);
		}

		public InitializableView getView() {
			return view;
		}
	}

}

package at.fraubock.spendenverwaltung.gui.views;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;

/**
 * 
 * @author Chris Steele
 * 
 * Holds references of all Views used in this application.
 * Provides methods that return Actions that switch the
 * current View to that specified in the Action. These
 * Actions can be used to initialize a JButton, taking
 * away the need for Views needing to have references of
 * other Views. 
 */

public class ViewActionFactory {
	
	private static final Logger log = Logger.getLogger(MainMenuView.class);
	private ViewDisplayer viewDisplayer;
	
	//views
	private CreatePersonView createPersonView;
	private MainMenuView mainMenuView;

/* TODO: add setters for other views & uncomment the relevant methods
 */
	
//	private FindPersonsView findPersonsView;
//  private MainFilterView mainFilterView;
//	private DonationImportView donationImportView;
//	private ImportValidationView importValidationView;
//	private CreateDonationConfirmationView createDonationConfirmationView;
// 	private FindDonationConfirmationView findDonationConfirmationView;
//	private CreateEMailingView createEMailingView;
//	private CreatePostalMailingView createPostalMailingView;
//	private	FindMailingsView findMailingsView;
//	private	ConfirmMailingsView confirmMailingsView;
//	private	DeleteMailingsView - is this necessary? wouldn't this be done over find? -Chris
//	private DonationProgressStatsView donationProgressStatsView;
//	private MailingStatsView mailingStatsView;
//	private PersonStatsView personStatsView;
	
	public ViewActionFactory(ViewDisplayer viewDisplayer) {
		this.viewDisplayer = viewDisplayer;
	}
	
	public ViewActionFactory(ViewDisplayer viewDisplayer, MainMenuView mainMenuView, CreatePersonView createPersonView) {
		this.viewDisplayer = viewDisplayer;
		this.mainMenuView = mainMenuView;
		this.createPersonView = createPersonView;
	}
	
	/**
	 * constructors
	 */
	public void setViewDisplayer(ViewDisplayer viewDisplayer) {
		this.viewDisplayer = viewDisplayer;
	}

	public void setCreatePersonView(CreatePersonView createPersonView) {
		this.createPersonView = createPersonView;
	}

	public void setMainMenuView(MainMenuView mainMenuView) {
		this.mainMenuView = mainMenuView;
	}
	
	public Action getCreatePersonsViewAction() {
		return new DisplayViewAction(createPersonView);
	}
	
	public Action getMainMenuViewAction() {
		return new DisplayViewAction(mainMenuView);
	}
	
//	private FindPersonsView findPersonsView;
//	private MainFilterView mainFilterView;
//	private DonationImportView donationImportView;
//	private ImportValidationView importValidationView;
//	private CreateDonationConfirmationView createDonationConfirmationView;
//	private CreateEMailingView createEMailingView;
//	private CreatePostalMailingView createPostalMailingView;
//	private	FindMailingsView findMailingsView;
//	private	ConfirmMailingsView confirmMailingsView;
//	private	DeleteMailingsView - is this necessary? wouldn't this be done over find? -Chris
//	private DonationProgressStatsView donationProgressStatsView;
//	private MailingStatsView mailingStatsView;
//	private PersonStatsView personStatsView;
	
	public Action getFindPersonsViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getMainFilterViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getDonationImportViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getImportValidationViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getCreateDonationConfirmationViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getFindDonationConfirmationViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getCreateEMailingViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getCreatePostalMailingViewAction() {
		return new DisplayViewAction(new JPanel());
	}

	public Action getFindMailingsViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getConfirmMailingsViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getDeleteMailingsViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getDonationProgressStatsViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getShowMailingStatsViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	public Action getShowPersonStatsViewAction() {
		return new DisplayViewAction(new JPanel());
	}
	
	private final class DisplayViewAction extends AbstractAction {

		private JPanel view;
		
		public DisplayViewAction(JPanel view) {
			this.view = view;
		}
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			viewDisplayer.changeView(view);
		}
	}
}

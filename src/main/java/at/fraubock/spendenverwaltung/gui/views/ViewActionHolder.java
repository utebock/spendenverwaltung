package at.fraubock.spendenverwaltung.gui.views;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

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

public class ViewActionHolder {
	
	private ViewDisplayer viewDisplayer;
	
	private CreatePersonView createPersonsView;

/* classes for following have not been created yet
*  uncomment and add to constructor & bean constructor-arg defs 
*  	when implemented
*  implement logic in the corresponding action */
	
//	private FindPersonsView findPersonsView;
//  private MainFilterView mainFilterView;
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
	
	public ViewActionHolder(ViewDisplayer viewDisplayer, CreatePersonView createPersonsView) {
		this.viewDisplayer = viewDisplayer;
		this.createPersonsView = createPersonsView;
	}
	
	public Action getCreatePersonsViewAction() {
		return new ShowCreatePersonsView();
	}
	
	private final class ShowCreatePersonsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			viewDisplayer.changeView(createPersonsView);
		}
	}

	public Action getFindPersonsViewAction() {
		return new ShowFindPersonsView();
	}
	
	private final class ShowFindPersonsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(findPersonsView);
		}
	}
	
	public Action getMainFilterViewAction() {
		return new ShowMainFilterView();
	}
	
	private final class ShowMainFilterView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(mainFilterView);
		}
	}
	
	public Action getDonationImportViewAction() {
		return new ShowDonationImportView();
	}
	
	private final class ShowDonationImportView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(donationImportView);
		}
	}
	
	public Action getImportValidationViewAction() {
		return new ShowImportValidationView();
	}
	
	private final class ShowImportValidationView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(importValidationView);
		}
	}
	
	public Action getCreateDonationConfirmationViewAction() {
		return new ShowCreateDonationConfirmationView();
	}
	
	private final class ShowCreateDonationConfirmationView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(createDonationConfirmationView);
		}
	}
	
	public Action getCreateEMailingViewAction() {
		return new ShowCreateEMailingView();
	}
	
	private final class ShowCreateEMailingView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(createEMailingView);
		}
	}
	
	public Action getCreatePostalMailingViewAction() {
		return new ShowCreatePostalMailingView();
	}

	private final class ShowCreatePostalMailingView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(createPostalMailingView);
		}
	}

	public Action getFindMailingsViewAction() {
		return new ShowFindMailingsView();
	}
	
	private final class ShowFindMailingsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(findMailingsView);
		}
	}

	public Action getConfirmMailingsViewAction() {
		return new ShowConfirmMailingsView();
	}
	
	private final class ShowConfirmMailingsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(confirmMailingsView);
		}
	}
	
	public Action getDeleteMailingsViewAction() {
		return new ShowDeleteMailingsView();
	}
	
	private final class ShowDeleteMailingsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(deleteMailingsView);
		}
	}
	
	public Action getDonationProgressStatsViewAction() {
		return new ShowDonationProgressStatsView();
	}
	
	private final class ShowDonationProgressStatsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(donationProgressStatsView);
		}
	}
	
	public Action getShowMailingStatsViewAction() {
		return new ShowMailingStatsView();
	}
	
	private final class ShowMailingStatsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(mailingStatsView);
		}
	}
	
	public Action getShowPersonStatsViewAction() {
		return new ShowPersonStatsView();
	}
	
	private final class ShowPersonStatsView extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
//			viewDisplayer.changeView(personStatsView);
		}
	}
}

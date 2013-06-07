package at.fraubock.spendenverwaltung.gui.views;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;

/**
 * 
 * @author Chris Steele
 *
 */
public class ViewActionHolder {
	
	private ViewDisplayer viewDisplayer;
	
	private CreatePersonView createPersonsView;

//  classes for following have not been created yet
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

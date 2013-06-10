package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;

/**
 * 
 * @author Chris Steele
 *
 */

public class CreateMailingsView extends JPanel {

	private static final long serialVersionUID = 1L;

	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private IMailingService mailingService;
	private IFilterService filterService;
	
	JPanel contentPanel, createEMailingPanel, createPostalMailingPanel;
	JLabel emailTitle, postalTitle, emailFilterLabel, postalFilterLabel;
	JButton createEMailingButton, createPostalMailingButton, cancelEMailingButton, cancelPostalMailingButton;
	JSeparator separator;
	
	
	JComboBox<Filter> eMailingPersonFilterChooser, postalPersonFilterChooser;
	//TODO FilterChooser - how to best implement
	
	public CreateMailingsView(ViewActionFactory viewActionFactory, ComponentFactory componentFactory,
			IMailingService mailingService, IFilterService filterService) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.filterService = filterService;
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800,800);
		createEMailingPanel = componentFactory.createPanel(800,350);
		createPostalMailingPanel = componentFactory.createPanel(800,350);
		
		this.add(contentPanel);
		
		contentPanel.add(createEMailingPanel, "wrap");
		//end of emailing panel, add separator
		contentPanel.add(componentFactory.createSeparator(), "wrap, growx");
		contentPanel.add(createPostalMailingPanel);
		
		//set title label
		emailTitle = componentFactory.createLabel("Neue Email Aussendung Erstellen");
		emailTitle.setFont(new Font("Headline", Font.PLAIN, 14));
		createEMailingPanel.add(emailTitle, "wrap");
		
		//filter combobox and label
		emailFilterLabel = componentFactory.createLabel("Personenfilter Auswählen");
		eMailingPersonFilterChooser = new JComboBox<Filter>();
		createEMailingPanel.add(emailFilterLabel);
		createEMailingPanel.add(eMailingPersonFilterChooser, "wrap");
		
		//buttons
		createEMailingButton = new JButton("Anlegen");
		cancelEMailingButton = new JButton("Abbrechen");
		createEMailingPanel.add(cancelEMailingButton, "split 2");
		createEMailingPanel.add(createEMailingButton);
		
		//set up postal mailing panel layout
		postalTitle = componentFactory.createLabel("Neue Postalische Aussendung Erstellen");
		postalTitle.setFont(new Font("Headline", Font.PLAIN, 14));
		createPostalMailingPanel.add(postalTitle, "wrap");
		
		//filter combobox and label
		postalFilterLabel = componentFactory.createLabel("Personenfilter Auswählen");
		postalPersonFilterChooser = new JComboBox<Filter>();
		createPostalMailingPanel.add(postalFilterLabel);
		createPostalMailingPanel.add(postalPersonFilterChooser, "wrap");

		//buttons
		createPostalMailingButton = new JButton("Anlegen");
		cancelPostalMailingButton = new JButton("Abbrechen");
		createPostalMailingPanel.add(cancelPostalMailingButton, "split 2");
		createPostalMailingPanel.add(createPostalMailingButton);
	}
	
	//adds actions to the buttons
	public void initButtons() {
		createEMailingButton.setAction(new CreateEMailingAction());
		createPostalMailingButton.setAction(new CreatePostalMailingAction());
		
		Action cancelAction = viewActionFactory.getMainMenuViewAction();
		cancelAction.putValue(Action.NAME, "Abbrechen");
		cancelEMailingButton.setAction(cancelAction);
		cancelPostalMailingButton.setAction(cancelAction);
	}
	
	private final class CreateEMailingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CreateEMailingAction() {
			super("EMail Aussendung anlegen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO create email mailing with service layer, add mailchimp logic, create
		}
	}

	private final class CreatePostalMailingAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CreatePostalMailingAction() {
			super("Postalische Aussendung anlegen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO create postal mailing with service layer, insert
		}	
	}
	
//	public static void main(String[] args) {
//		JFrame frame = new JFrame("Test create mailings");
//		CreateMailingsView view = new CreateMailingsView(null, new ComponentFactory(), null, null);
//		frame.add(view);
//		frame.pack();
//		frame.setVisible(true);
//	}
}

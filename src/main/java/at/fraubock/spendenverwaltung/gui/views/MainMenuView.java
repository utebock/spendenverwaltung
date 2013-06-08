package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;

public class MainMenuView extends JPanel {
//	private static final Logger log = Logger.getLogger(MainMenuView.class);
	private static final long serialVersionUID = 1L;
	
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	
	private JButton person;
	private JLabel general;
	private JPanel importPanel;
	private JPanel confirmPanel;
	private JButton filter;
	private JLabel persons;
	private JLabel filterLabel;
	private JLabel importLabel;
	private JLabel validationLabel;
	private JSeparator personSeparator;
	private JSeparator importSeparator;
	private JLabel donationImport;
	private JButton imports;
	private JButton validation;
	private JLabel confirm;
	private JButton createConfirm;
	private JButton obtainConfirm;
	private JLabel createConfirmLabel;
	private JLabel obtainConfirmLabel;
	private JSeparator confirmSeparator;
	private JPanel sendPanel;
	private JButton eSending;
	private JLabel sending;
	private JButton postalSending;
	private JLabel eSendingLabel;
	private JLabel postalSendingLabel;
	private JSeparator sendSeparator;
	private JButton showSendings;
	private JButton confirmSendings;
	private JButton deleteSendings;
	private JLabel showSendingsLabel;
	private JLabel confirmSendingsLabel;
	private JLabel deleteSendingsLabel;
	private JPanel statsPanel;
	private JLabel stats;
	private JButton progress;
	private JButton statsSendings;
	private JButton statsPersons;
	private JLabel progressLabel;
	private JLabel statsSendingsLabel;
	private JLabel statsPersonsLabel;
	private JButton search;
	private JLabel searchLabel;
	private JPanel overviewPanel;
	
	public MainMenuView() {
		componentFactory = new ComponentFactory();
	}
	
	public MainMenuView(ViewActionFactory viewActionFactory, ComponentFactory componentFactory) {
		/**
		 * good to know: basic font is 13pt
		 */
		super(new MigLayout());

		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		setUp();
	}
	
	public void setViewActionFactory(ViewActionFactory viewActionFactory) {
		this.viewActionFactory = viewActionFactory;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	public void setUp() {
		overviewPanel = componentFactory.createPanel(800, 850);
		this.add(overviewPanel);
		/**
		 * "Allgemein"-panel contains persons and filter
		 */
		
		JPanel personsPanel = componentFactory.createPanel(800,160);
		
		overviewPanel.add(personsPanel, "wrap 0px");
		general = componentFactory.createLabel("Allgemein");
		general.setFont(new Font("Headline", Font.PLAIN, 14));

		//CreatePersonView
		personsPanel.add(general, "wrap");
		person = new JButton();;
		personsPanel.add(person);
		search = new JButton();;
		personsPanel.add(search, "gap 35");
		filter = new JButton();;
		personsPanel.add(filter, "wrap 0px, gap 25");
		//button labels
		persons = componentFactory.createLabel("Person anlegen");
		personsPanel.add(persons);
		
		//FindPersonsView
		searchLabel = componentFactory.createLabel("Personen suchen");
		personsPanel.add(searchLabel, "gap 25");
		
		//MainFilterView
		filterLabel = componentFactory.createLabel("Filter");
		personsPanel.add(filterLabel, "gap 48");
		
		//separator for next section
		personSeparator = componentFactory.createSeparator();
		overviewPanel.add(personSeparator, "wrap 0px, growx");
		
		/**
		 * "Spendenimport"-panel contains donationimport and validation
		 */
		
		//DonationImport
		importPanel = componentFactory.createPanel(800,160);
		overviewPanel.add(importPanel, "wrap 0px");
		donationImport = componentFactory.createLabel("Spendenimport");
		donationImport.setFont(new Font("Headline", Font.PLAIN, 14));
		importPanel.add(donationImport, "wrap");
		
		imports = new JButton();;
		importPanel.add(imports, "split 2");
		//DonationValidation
		validation = new JButton();;
		importPanel.add(validation, "gap 55, wrap 0px");
		
		importLabel = componentFactory.createLabel("Importe");
		importPanel.add(importLabel, "split 2, gap 18");
		validationLabel = componentFactory.createLabel("Validierung");
		importPanel.add(validationLabel, "gap 73");
		
		importSeparator = componentFactory.createSeparator();
		overviewPanel.add(importSeparator, "wrap 0px, growx");
		

		/**
		 * "Spendenbestaetigung"-panel contains create confirmation and obtain confirmation
		 */
		
		//DonationConfirmation
		confirmPanel = componentFactory.createPanel(800,160);
		overviewPanel.add(confirmPanel, "wrap 0px");
		confirm = componentFactory.createLabel("Spendenbest\u00E4tigung");
		confirm.setFont(new Font("Headline", Font.PLAIN, 14));
		confirmPanel.add(confirm, "wrap");
		//importPanel.add(empty, "wrap");
		createConfirm = new JButton();;
		confirmPanel.add(createConfirm, "split 2");


		//ObtainDonationConfirmation
		obtainConfirm = new JButton();;
		confirmPanel.add(obtainConfirm, "wrap 0px, gap 55");
		
		createConfirmLabel = componentFactory.createLabel("<html><center>Best\u00E4tigung <br> erstellen</html>");
		//createConfirmLabel.setFont(new Font("kleiner", Font.PLAIN, 12));
		confirmPanel.add(createConfirmLabel, "split 2");
		obtainConfirmLabel = componentFactory.createLabel("<html><center>Best\u00E4tigung <br>abrufen</html>");
		//obtainConfirmLabel.setFont(new Font("kleiner", Font.PLAIN, 12));
		confirmPanel.add(obtainConfirmLabel, "gap 65");
		
		confirmSeparator = componentFactory.createSeparator();
		overviewPanel.add(confirmSeparator, "wrap 0px, growx");
		

		/**
		 * "Aussendungen"-panel contains e-aussendung erstellen, briefaussendung erstellen, aussendungen anzeigen, 
		 * aussendungen bestaetigen, aussendung loeschen
		 */
		
		sendPanel = componentFactory.createPanel(800,160);
		overviewPanel.add(sendPanel, "wrap");
		sending = componentFactory.createLabel("Aussendungen");
		sending.setFont(new Font("Headline", Font.PLAIN, 14));
		sendPanel.add(sending, "wrap 0px");
		//importPanel.add(empty, "wrap");
		eSending = new JButton();;
		sendPanel.add(eSending, "split 3");
		postalSending = new JButton();;
		sendPanel.add(postalSending, "gap 55");
		showSendings = new JButton();;
		sendPanel.add(showSendings, "gap 55");
		confirmSendings = new JButton();;
		sendPanel.add(confirmSendings, "gap 40");
		deleteSendings = new JButton();;
		sendPanel.add(deleteSendings, "wrap 0px, gap 40");
		
		eSendingLabel = componentFactory.createLabel("<html><center>E-Aussendung<br>erstellen");
		sendPanel.add(eSendingLabel, "split 3");
		postalSendingLabel = componentFactory.createLabel("<html><center>Briefaussendung<br>erstellen</html>");
		sendPanel.add(postalSendingLabel, "gap 33");
		
		showSendingsLabel = componentFactory.createLabel("<html><center>Aussendungen<br>anzeigen");
		sendPanel.add(showSendingsLabel, "gap 37");
		
		confirmSendingsLabel = componentFactory.createLabel("<html><center>Aussendungen<br>best\u00E4tigen</html>");
		sendPanel.add(confirmSendingsLabel, "gap 33");
		
		deleteSendingsLabel = componentFactory.createLabel("<html><center>Aussendungen<br>l\u00F6schen</html>");
		sendPanel.add(deleteSendingsLabel, "gap 35");

		sendSeparator = componentFactory.createSeparator();
		overviewPanel.add(sendSeparator, "wrap 0px, growx");
		
		/**
		 * "Statistiken"-panel contains spendenentwicklung, aussendungen, personen
		 */
				
		statsPanel = componentFactory.createPanel(800,160);
		overviewPanel.add(statsPanel, "wrap 0px");
		stats = componentFactory.createLabel("Statistiken");
		stats.setFont(new Font("Headline", Font.PLAIN, 14));
		statsPanel.add(stats, "wrap");
		progress = new JButton();;
		statsPanel.add(progress, "split 2");
		statsSendings = new JButton();;
		statsPanel.add(statsSendings, "gap 55");
		statsPersons = new JButton();;
		statsPanel.add(statsPersons, "wrap 0px, gap 45");
		
		progressLabel = componentFactory.createLabel("<html><center>Spenden-<br>entwicklung</html>");
		statsPanel.add(progressLabel, "split 2");
		statsSendingsLabel = componentFactory.createLabel("Aussendungen");
		statsPanel.add(statsSendingsLabel, "gap 55");
		statsPersonsLabel = componentFactory.createLabel("Personen");
		statsPanel.add(statsPersonsLabel, "gap 55");

	}
	
	//call when viewActionFactory is fully populated
	public void init() {
		
		person.setAction(viewActionFactory.getCreatePersonsViewAction());
		search.setAction(viewActionFactory.getFindPersonsViewAction());
		filter.setAction(viewActionFactory.getMainFilterViewAction());
		imports.setAction(viewActionFactory.getDonationImportViewAction());
		validation.setAction(viewActionFactory.getImportValidationViewAction());
		createConfirm.setAction(viewActionFactory.getCreateDonationConfirmationViewAction());
		obtainConfirm.setAction( viewActionFactory.getFindDonationConfirmationViewAction());
		eSending.setAction(viewActionFactory.getCreateEMailingViewAction());
		postalSending.setAction(viewActionFactory.getCreatePostalMailingViewAction());
		showSendings.setAction(viewActionFactory.getFindMailingsViewAction());
		confirmSendings.setAction(viewActionFactory.getConfirmMailingsViewAction());
		deleteSendings.setAction(viewActionFactory.getDeleteMailingsViewAction());
		progress.setAction(viewActionFactory.getDonationProgressStatsViewAction());
		statsSendings.setAction(viewActionFactory.getShowMailingStatsViewAction());
		statsPersons.setAction( viewActionFactory.getShowPersonStatsViewAction());
	}
	
//	public static void main(String[] args) {
//		ViewDisplayer displayer = new ViewDisplayer();
//		displayer.changeView(new MainMenuView(new ViewActionFactory(), new ComponentFactory()));
//	}
}

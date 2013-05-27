package at.fraubock.spendenverwaltung.gui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class Overview extends JPanel{

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
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
	public Overview(IPersonService personService, IAddressService addressService, IDonationService donationService){
		/**
		 * good to know: basic font is 13pt
		 */
		super(new MigLayout());
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		setUpPersons();
	}

	private void setUpPersons() {
		/**
		 * "Allgemein"-panel contains persons and filter
		 */
		JPanel personsPanel = builder.createPanel(800,160);
		this.add(personsPanel, "wrap");
		general = builder.createLabel("Allgemein");
		general.setFont(new Font("Headline", Font.PLAIN, 14));
		personsPanel.add(general, "wrap");
		//personsPanel.add(empty, "wrap");
		person = builder.createImageButton("/images/persons.jpeg", buttonListener, "person_overview");
		personsPanel.add(person);
		
		filter = builder.createImageButton("/images/template.jpg", buttonListener, "filter_overview");
		personsPanel.add(filter, "wrap, gap 25");
		//button labels
		persons = builder.createLabel("Personen");
		personsPanel.add(persons, "gap 22");
		filterLabel = builder.createLabel("Filter");
		personsPanel.add(filterLabel, "wrap, gap 58");
		
		//separator for next section
		personSeparator = builder.createSeparator();
		this.add(personSeparator, "wrap, growx");
		
		/**
		 * "Spendenimport"-panel contains donationimport and validation
		 */
		
		importPanel = builder.createPanel(800,160);
		this.add(importPanel, "wrap");
		donationImport = builder.createLabel("Spendenimport");
		donationImport.setFont(new Font("Headline", Font.PLAIN, 14));
		importPanel.add(donationImport, "wrap");
		//importPanel.add(empty, "wrap");
		imports = builder.createImageButton("/images/template.jpg", buttonListener, "donationImport_overview");
		importPanel.add(imports);
		validation = builder.createImageButton("/images/template.jpg", buttonListener, "donation_validation");
		importPanel.add(validation, "wrap, gap 25");
		
		importLabel = builder.createLabel("Importe");
		importPanel.add(importLabel, "gap 22");
		validationLabel = builder.createLabel("Validierung");
		importPanel.add(validationLabel, "wrap, gap 45");
		
		importSeparator = builder.createSeparator();
		this.add(importSeparator, "wrap, growx");
		

		/**
		 * "Spendenbestaetigung"-panel contains create confirmation and obtain confirmation
		 */
		
		confirmPanel = builder.createPanel(800,160);
		this.add(confirmPanel, "wrap");
		confirm = builder.createLabel("Spendenbest\u00E4tigung");
		confirm.setFont(new Font("Headline", Font.PLAIN, 14));
		confirmPanel.add(confirm, "wrap");
		//importPanel.add(empty, "wrap");
		createConfirm = builder.createImageButton("/images/template.jpg", buttonListener, "create_donation_confirmation");
		confirmPanel.add(createConfirm);
		obtainConfirm = builder.createImageButton("/images/template.jpg", buttonListener, "obtain_donation_confirmation");
		confirmPanel.add(obtainConfirm, "wrap, gap 35");
		
		createConfirmLabel = builder.createLabel("<html><center>Best\u00E4tigung <br> erstellen</html>");
		confirmPanel.add(createConfirmLabel);
		obtainConfirmLabel = builder.createLabel("<html><center>Best\u00E4tigung <br>abrufen</html>");
		confirmPanel.add(obtainConfirmLabel, "wrap, gap 35");
		
		confirmSeparator = builder.createSeparator();
		this.add(confirmSeparator, "wrap, growx");
		
		/**
		 * "Aussendungen"-panel contains e-aussendung erstellen, briefaussendung erstellen, aussendungen anzeigen, 
		 * aussendungen bestaetigen, aussendung loeschen
		 */
		
		sendPanel = builder.createPanel(800,160);
		this.add(sendPanel, "wrap");
		sending = builder.createLabel("Aussendungen");
		sending.setFont(new Font("Headline", Font.PLAIN, 14));
		sendPanel.add(sending, "wrap");
		//importPanel.add(empty, "wrap");
		eSending = builder.createImageButton("/images/template.jpg", buttonListener, "create_eSending");
		sendPanel.add(eSending);
		postalSending = builder.createImageButton("/images/template.jpg", buttonListener, "obtain_postalSending");
		sendPanel.add(postalSending, "gap 35");
		showSendings = builder.createImageButton("/images/template.jpg", buttonListener, "show_sendings");
		sendPanel.add(showSendings, "gap 35");
		confirmSendings = builder.createImageButton("/images/template.jpg", buttonListener, "confirm_sendings");
		sendPanel.add(confirmSendings, "gap 35");
		deleteSendings = builder.createImageButton("/images/template.jpg", buttonListener, "delete_sendings");
		sendPanel.add(deleteSendings, "wrap, gap 35");
		
		eSendingLabel = builder.createLabel("<html><center>E-Aussendung<br>erstellen");
		sendPanel.add(eSendingLabel);
		postalSendingLabel = builder.createLabel("<html><center>Briefaussendung<br>erstellen</html>");
		sendPanel.add(postalSendingLabel, "gap 35");
		
		showSendingsLabel = builder.createLabel("<html><center>Aussendungen<br>anzeigen");
		sendPanel.add(showSendingsLabel, "gap 35");
		
		confirmSendingsLabel = builder.createLabel("<html><center>Aussendungen<br>best\u00E4tigen</html>");
		sendPanel.add(confirmSendingsLabel, "gap 35");
		
		deleteSendingsLabel = builder.createLabel("<html><center>Aussendungen<br>l\u00F6schen</html>");
		sendPanel.add(deleteSendingsLabel, "wrap, gap 35");

		sendSeparator = builder.createSeparator();
		this.add(sendSeparator, "wrap, growx");
		
		/**
		 * "Statistiken"-panel contains spendenentwicklung, aussendungen, personen
		 */
		
		statsPanel = builder.createPanel(800,160);
		this.add(statsPanel, "wrap");
		stats = builder.createLabel("Statistiken");
		stats.setFont(new Font("Headline", Font.PLAIN, 14));
		statsPanel.add(stats, "wrap");
		//importPanel.add(empty, "wrap");
		progress = builder.createImageButton("/images/template.jpg", buttonListener, "stats_progress");
		statsPanel.add(progress);
		statsSendings = builder.createImageButton("/images/template.jpg", buttonListener, "stats_sendings");
		statsPanel.add(statsSendings, "gap 35");
		statsPersons = builder.createImageButton("/images/template.jpg", buttonListener, "stats_persons");
		statsPanel.add(statsPersons, "wrap, gap 35");
		
		progressLabel = builder.createLabel("<html><center>Spenden-<br>entwicklung");
		statsPanel.add(progressLabel);
		statsSendingsLabel = builder.createLabel("Aussendungen");
		statsPanel.add(statsSendingsLabel, "gap 35");
		statsPersonsLabel = builder.createLabel("Personen");
		statsPanel.add(statsPersonsLabel, "gap 35");
	}
	
	public void goToPersons(){
		PersonOverview po = new PersonOverview(personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(po);
		

	}
	

}

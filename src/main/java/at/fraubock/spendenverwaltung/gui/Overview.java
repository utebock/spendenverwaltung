package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class Overview extends JPanel{
	private static final Logger log = Logger.getLogger(Overview.class);
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
	private IFilterService filterService;
	private JButton search;
	private JLabel searchLabel;
	private PersonTableModel personModel;
	private List<Person> personList;
	private JTable showTable;
	private JScrollPane scrollPane;
	private JPanel overviewPanel;
	public Overview(IFilterService filterService, IPersonService personService, IAddressService addressService, IDonationService donationService){
		/**
		 * good to know: basic font is 13pt
		 */
		super(new MigLayout());
		this.filterService = filterService;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;

		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		initTable();
		setUp();
	}

	public void setUp() {
		
		overviewPanel = builder.createPanel(800, 1000);
		JScrollPane pane = new JScrollPane(overviewPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(pane);
		/**
		 * "Allgemein"-panel contains persons and filter
		 */
		
		JPanel personsPanel = builder.createPanel(800,160);
		
		overviewPanel.add(personsPanel, "wrap");
		general = builder.createLabel("Allgemein");
		general.setFont(new Font("Headline", Font.PLAIN, 14));
		personsPanel.add(general, "wrap");
		//personsPanel.add(empty, "wrap");
		person = builder.createImageButton("/images/createPerson.jpg", buttonListener, "create_person");
		personsPanel.add(person);
		
		search = builder.createImageButton("/images/getPersons.jpg", buttonListener, "search_person");
		personsPanel.add(search, "gap 35");
		
		filter = builder.createImageButton("/images/filter.jpg", buttonListener, "filter_overview");
		personsPanel.add(filter, "wrap, gap 35");
		//button labels
		persons = builder.createLabel("Person anlegen");
		personsPanel.add(persons);
		
		searchLabel = builder.createLabel("Personen suchen");
		personsPanel.add(searchLabel, "gap 28");
		
		filterLabel = builder.createLabel("Filter");
		personsPanel.add(filterLabel, "wrap, gap 58");
		
		//separator for next section
		personSeparator = builder.createSeparator();
		overviewPanel.add(personSeparator, "wrap, growx");
		
		/**
		 * "Spendenimport"-panel contains donationimport and validation
		 */
		
		importPanel = builder.createPanel(800,160);
		overviewPanel.add(importPanel, "wrap");
		donationImport = builder.createLabel("Spendenimport");
		donationImport.setFont(new Font("Headline", Font.PLAIN, 14));
		importPanel.add(donationImport, "wrap");
		//importPanel.add(empty, "wrap");
		imports = builder.createImageButton("/images/importOverview.jpg", buttonListener, "donationImport_overview");
		importPanel.add(imports, "split 2");
		validation = builder.createImageButton("/images/importValidate.jpg", buttonListener, "donation_validation");
		importPanel.add(validation, "wrap, gap 35");
		
		importLabel = builder.createLabel("Importe");
		importPanel.add(importLabel, "split 2, gap 22");
		validationLabel = builder.createLabel("Validierung");
		importPanel.add(validationLabel, "wrap, gap 65");
		
		importSeparator = builder.createSeparator();
		overviewPanel.add(importSeparator, "wrap, growx");
		

		/**
		 * "Spendenbestaetigung"-panel contains create confirmation and obtain confirmation
		 */
		
		confirmPanel = builder.createPanel(800,160);
		overviewPanel.add(confirmPanel, "wrap");
		confirm = builder.createLabel("Spendenbest\u00E4tigung");
		confirm.setFont(new Font("Headline", Font.PLAIN, 14));
		confirmPanel.add(confirm, "wrap");
		//importPanel.add(empty, "wrap");
		createConfirm = builder.createImageButton("/images/createDonationConfirmation.jpg", buttonListener, "create_donation_confirmation");
		confirmPanel.add(createConfirm, "split 2");
		obtainConfirm = builder.createImageButton("/images/obtainDonationConfirmation.jpg", buttonListener, "obtain_donation_confirmation");
		confirmPanel.add(obtainConfirm, "wrap, gap 35");
		
		createConfirmLabel = builder.createLabel("<html><center>Best\u00E4tigung <br> erstellen</html>");
		confirmPanel.add(createConfirmLabel, "split 2");
		obtainConfirmLabel = builder.createLabel("<html><center>Best\u00E4tigung <br>abrufen</html>");
		confirmPanel.add(obtainConfirmLabel, "wrap, gap 65");
		
		confirmSeparator = builder.createSeparator();
		overviewPanel.add(confirmSeparator, "wrap, growx");
		
		/**
		 * "Aussendungen"-panel contains e-aussendung erstellen, briefaussendung erstellen, aussendungen anzeigen, 
		 * aussendungen bestaetigen, aussendung loeschen
		 */
		
		sendPanel = builder.createPanel(800,160);
		overviewPanel.add(sendPanel, "wrap");
		sending = builder.createLabel("Aussendungen");
		sending.setFont(new Font("Headline", Font.PLAIN, 14));
		sendPanel.add(sending, "wrap");
		//importPanel.add(empty, "wrap");
		eSending = builder.createImageButton("/images/eNotification.jpg", buttonListener, "create_eSending");
		sendPanel.add(eSending, "split 2");
		postalSending = builder.createImageButton("/images/postalNotification.jpg", buttonListener, "obtain_postalSending");
		sendPanel.add(postalSending, "gap 35");
		showSendings = builder.createImageButton("/images/showNotifications.jpg", buttonListener, "show_sendings");
		sendPanel.add(showSendings, "gap 35");
		confirmSendings = builder.createImageButton("/images/confirmSendings.jpg", buttonListener, "confirm_sendings");
		sendPanel.add(confirmSendings, "gap 35");
		deleteSendings = builder.createImageButton("/images/deleteNotifications.jpg", buttonListener, "delete_sendings");
		sendPanel.add(deleteSendings, "wrap, gap 35");
		
		eSendingLabel = builder.createLabel("<html><center>E-Aussendung<br>erstellen");
		sendPanel.add(eSendingLabel, "split 2");
		postalSendingLabel = builder.createLabel("<html><center>Briefaussendung<br>erstellen</html>");
		sendPanel.add(postalSendingLabel, "gap 30");
		
		showSendingsLabel = builder.createLabel("<html><center>Aussendungen<br>anzeigen");
		sendPanel.add(showSendingsLabel, "gap 35");
		
		confirmSendingsLabel = builder.createLabel("<html><center>Aussendungen<br>best\u00E4tigen</html>");
		sendPanel.add(confirmSendingsLabel, "gap 35");
		
		deleteSendingsLabel = builder.createLabel("<html><center>Aussendungen<br>l\u00F6schen</html>");
		sendPanel.add(deleteSendingsLabel, "wrap, gap 35");

		sendSeparator = builder.createSeparator();
		overviewPanel.add(sendSeparator, "wrap, growx");
		
		/**
		 * "Statistiken"-panel contains spendenentwicklung, aussendungen, personen
		 */
		
		statsPanel = builder.createPanel(800,160);
		overviewPanel.add(statsPanel, "wrap");
		stats = builder.createLabel("Statistiken");
		stats.setFont(new Font("Headline", Font.PLAIN, 14));
		statsPanel.add(stats, "wrap");
		//importPanel.add(empty, "wrap");
		progress = builder.createImageButton("/images/statisticsDonation.jpg", buttonListener, "stats_progress");
		statsPanel.add(progress);
		statsSendings = builder.createImageButton("/images/statisticsNotification.jpg", buttonListener, "stats_sendings");
		statsPanel.add(statsSendings, "gap 35");
		statsPersons = builder.createImageButton("/images/statisticsPerson.jpg", buttonListener, "stats_persons");
		statsPanel.add(statsPersons, "wrap, gap 35");
		
		progressLabel = builder.createLabel("<html><center>Spenden-<br>entwicklung");
		statsPanel.add(progressLabel);
		statsSendingsLabel = builder.createLabel("Aussendungen");
		statsPanel.add(statsSendingsLabel, "gap 35");
		statsPersonsLabel = builder.createLabel("Personen");
		statsPanel.add(statsPersonsLabel, "gap 45");
	}
	
	public void goToCreate(){
		CreatePerson cp = new CreatePerson(personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(cp);
	}

	public void goToFilter() {
		FilterOverview fo = new FilterOverview(filterService, personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(fo);
		
	}
	
	public void initTable(){
		personModel = new PersonTableModel();
		getPersons();
		showTable = new JTable(personModel);
		
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(650,700));
	}
	
	public PersonTableModel getPersonModel(){
		return this.personModel;
	}
	
	private void getPersons(){
		personList = new ArrayList<Person>();
		
		try{
			personList = personService.getAll();
			log.info("List " + personList.size() + " persons");
		}
		catch(ServiceException e){
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		if(personList == null){
			JOptionPane.showMessageDialog(this, "GetAll() returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Person p : personList){
			personModel.addPerson(p);
		}	
	}
	
	public void goToShow(){
		FilterPersons filter = new FilterPersons(personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(filter);
	}
	

}

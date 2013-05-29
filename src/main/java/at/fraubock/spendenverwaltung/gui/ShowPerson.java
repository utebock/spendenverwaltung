package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class ShowPerson extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private PersonOverview personOverview;
	private Person person;
	private Donation donation;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private ActionHandler actionHandler;
	private JPanel panel;
	private JPanel addressPanel;
	private JPanel donationPanel;
	private JTable addressTable;
	private JTable donationTable;
	private DonationTableModel donationModel;
	private AddressTableModel addressModel;
	private JScrollPane donationPane;
	private JScrollPane addressPane;
	private List<Address> addressList;
	private List<Donation> donationList;
	private JButton createAddressBtn;
	private JButton createDonationBtn;
	private JButton backBtn;
	
	private JLabel salutLabel;
	private JLabel salut;
	
	private JLabel titleLabel;
	private JLabel title;

	private JLabel companyLabel;
	private JLabel company;

	private JLabel given_nameLabel;
	private JLabel given_name;
	
	private JLabel surnameLabel;
	private JLabel surname;
	
	private JLabel telephoneLabel;
	private JLabel telephone;
	
	private JLabel mailLabel;
	private JLabel mail;
	
	private JLabel streetLabel;
	private JLabel addressStreetLabel;
	private JLabel street;
	private JTextField streetField;
	
	private JLabel postalLabel;
	private JLabel addressPostalLabel;
	private JLabel postal;
	private JTextField postalField;

	private JLabel cityLabel;
	private JLabel addressCityLabel;
	private JLabel city;
	private JTextField cityField;
	
	private JLabel countryLabel;
	private JLabel addressCountryLabel;
	private JLabel country;
	private JTextField countryField;
	
	private JLabel notifyTypeLabel;
	private JLabel notifyType;
	
	private JLabel noteLabel;
	private JLabel note;
	
	private JLabel donationsLabel;
	
	private JLabel addressesLabel;
	
	private JLabel amountLabel;
	private JTextField amount;
	
	private JLabel donationDateLabel;
	private JTextField donationDate;
	
	private JLabel dedicationLabel;
	private JTextField dedication;
	
	private JLabel typeLabel;
	private JComboBox type;

	private JLabel donationNoteLabel;
	private JTextArea donationNote;
	
	public ShowPerson(Person person, IPersonService personService, IAddressService addressService, IDonationService donationService, PersonOverview personOverview){
		super(new MigLayout());
		
		this.person = person;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.personOverview = personOverview;
		buttonListener = new ButtonListener(this);
		actionHandler = new ActionHandler(this);
		builder = new ComponentBuilder();
		donationModel = new DonationTableModel(this, donationService);
		addressModel = new AddressTableModel();
		setUpPerson();
		setUpDonations();
		setUpAddresses();
	}
	
	public void setUpPerson(){

		panel = builder.createPanel(700, 700);
		this.add(panel);
		
		
		titleLabel = builder.createLabel("Titel: ");
		title = builder.createLabel(person.getTitle());
		panel.add(titleLabel, "gapright 150");
		panel.add(title, "wrap");

		companyLabel = builder.createLabel("Firma: ");
		company = builder.createLabel(person.getCompany());
		panel.add(companyLabel, "gapright 150");
		panel.add(company, "wrap");

		given_nameLabel = builder.createLabel("Vorname: ");
		given_name = builder.createLabel(person.getGivenName());
		panel.add(given_nameLabel, "gapright 150");
		panel.add(given_name, "wrap");
		
		surnameLabel = builder.createLabel("Nachname: ");
		surname = builder.createLabel(person.getSurname());
		panel.add(surnameLabel, "gapright 150");
		panel.add(surname, "wrap");
		
		telephoneLabel = builder.createLabel("Telefonnummer: ");
		telephone = builder.createLabel(person.getTelephone());
		panel.add(telephoneLabel, "gapright 150");
		panel.add(telephone, "wrap");
		
		mailLabel = builder.createLabel("E-Mail: ");
		mail = builder.createLabel(person.getEmail());
		panel.add(mailLabel, "gapright 150");
		panel.add(mail, "wrap");
		
		streetLabel = builder.createLabel("Straße: ");
		street = builder.createLabel(person.getMainAddress().getStreet());
		panel.add(streetLabel, "gapright 150");
		panel.add(street, "wrap");
		
		postalLabel = builder.createLabel("PLZ: ");
		postal = builder.createLabel(person.getMainAddress().getPostalCode());
		panel.add(postalLabel, "gapright 150");
		panel.add(postal, "wrap");
		
		cityLabel = builder.createLabel("Ort: ");
		city = builder.createLabel(person.getMainAddress().getCity());
		panel.add(cityLabel, "gapright 150");
		panel.add(city, "wrap");
		
		countryLabel = builder.createLabel("Land: ");
		country = builder.createLabel(person.getMainAddress().getCountry());
		panel.add(cityLabel, "gapright 150");
		panel.add(city, "wrap");
		
		noteLabel = builder.createLabel("Notiz: ");
		note = builder.createLabel(person.getNote());
		panel.add(noteLabel, "gapright 150");
		panel.add(note, "wrap");
	}
	
	private void setUpAddresses(){
		addressPanel = builder.createPanel(400, 400);
		
		addressTable = new JTable(addressModel);
		
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addressTable.setPreferredSize(new Dimension(400, 200));
		
		addressPane = new JScrollPane(addressTable);

		addressPanel.add(addressPane, "dock north");
		
		addressStreetLabel = builder.createLabel("Straße: ");
		streetField = builder.createTextField(150);
		addressPanel.add(addressStreetLabel, "");
		addressPanel.add(streetField, "wrap 0px");

		addressPostalLabel = builder.createLabel("PLZ: ");
		postalField = builder.createTextField(10);
		addressPanel.add(addressPostalLabel, "");
		addressPanel.add(postalField, "wrap 0px");

		addressCityLabel = builder.createLabel("Ort: ");
		cityField = builder.createTextField(150);
		addressPanel.add(addressCityLabel, "");
		addressPanel.add(cityField, "wrap 0px");

		addressCountryLabel = builder.createLabel("Land: ");
		countryField = builder.createTextField(150);
		addressPanel.add(addressCountryLabel, "");
		addressPanel.add(countryField, "wrap 0px");
		
		createAddressBtn = builder.createButton("Adresse hinzufügen", buttonListener, "create_address_in_show_person");
		addressPanel.add(createAddressBtn, "span 2");
		
		panel.add(addressPanel, "dock east, wrap");
		
		getAddresses();	
	}
	
	private void setUpDonations(){
		donation = new Donation();
		donationPanel = builder.createPanel(400, 400);
		
		donationTable = new JTable(donationModel);
		donationTable.setFillsViewportHeight(true);
		donationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		donationPane = new JScrollPane(donationTable);
		donationPane.setPreferredSize(new Dimension(800,800));
		donationPanel.add(donationPane, "wrap, span 3");
		
		amountLabel = builder.createLabel("Betrag: ");
		amount = builder.createTextField(150);
		donationPanel.add(amountLabel, "");
		donationPanel.add(amount, "wrap 0px");
		
		donationDateLabel = builder.createLabel("Datum: ");
		donationDate = builder.createTextField(150);
		donationPanel.add(donationDateLabel, "");
		donationPanel.add(donationDate, "wrap 0px");
		
		dedicationLabel = builder.createLabel("Widmung: ");
		dedication = builder.createTextField(150);
		donationPanel.add(dedicationLabel, "");
		donationPanel.add(dedication, "wrap 0px");
		
		typeLabel = builder.createLabel("Typ: ");
		type = builder.createComboBox(donationService.getDonationTypes(), actionHandler, "show_person_donation_type");
		donationPanel.add(typeLabel, "");
		donationPanel.add(type, "wrap 0px");
		
		donationNoteLabel = builder.createLabel("Notiz: ");
		donationNote = builder.createTextArea(10, 150);
		donationPanel.add(donationNoteLabel, "");
		donationPanel.add(donationNote, "wrap 0px");
		
		createDonationBtn = builder.createButton("Spende hinzufügen", buttonListener, "create_donation_in_show_person");
		donationPanel.add(createDonationBtn, "");
		
		backBtn = builder.createButton("zurück", buttonListener, "go_from_show_person_to_person_overview");
		donationPanel.add(backBtn, "");
		
		panel.add(donationPanel, "dock south, wrap");
		
		getDonations();
	}
	
	private void getAddresses(){
		addressList = new ArrayList<Address>();

		addressList = person.getAddresses();
		if(addressList == null){
			JOptionPane.showMessageDialog(this, "Addresslist returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Address a : addressList){
			addressModel.addAddress(a);
			addressTable.revalidate();
		}	
	}
	
	private void getDonations(){
		donationList = new ArrayList<Donation>();

		try {
			donationList = donationService.getByPerson(person);
		} catch(ServiceException e){
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		if(donationList == null){
			JOptionPane.showMessageDialog(this, "Donationlist returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Donation d : donationList){
			donationModel.addDonation(d);
			donationTable.revalidate();
		}	
	}

	public void createDonationInDb(){
		donation.setDonator(person);
		donation.setAmount(Long.parseLong(amount.getText()));
		donation.setDate(Date.valueOf(donationDate.getText()));
		donation.setDedication(dedication.getText());
		donation.setNote(note.getText());
		donation.setType(Donation.DonationType.values()[type.getSelectedIndex()]);

		try{
			Donation createdDonation = donationService.create(donation);
			donationModel.addDonation(createdDonation);
		}
		catch(ServiceException e){
			 JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
	    		return;
		}
		JOptionPane.showMessageDialog(this, "Spende erfolgreich angelegt", "Information", JOptionPane.INFORMATION_MESSAGE);
		
		resetDonationFields();
	}
	
	public void resetDonationFields(){
		amount.setText("");
		donationDate.setText("");
		dedication.setText("");
		note.setText("");
		type.setSelectedIndex(0);
	}
	
	public void returnTo(){
		this.removeAll();
		this.revalidate();
		this.repaint();
		personOverview.removeAll();
		personOverview.revalidate();
		personOverview.repaint();
		personOverview.setUp();
	}
	
	public void showErrorDialog(String message){
		
	}
}

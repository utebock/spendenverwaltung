package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
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

public class AddAttributes extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IDonationService donationService;
	private IAddressService addressService;
	private IPersonService personService;
	private PersonTableModel personModel;
	private Person person;
	private Donation donation;
	private Address addr;
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
	
	private JLabel noteLabel;
	private JLabel note;
	
	private JLabel amountLabel;
	private JTextField amount;
	
	private JLabel donationDateLabel;
	private JTextField donationDate;
	
	private JLabel dedicationLabel;
	private JTextField dedication;
	
	private JLabel typeLabel;
	private JComboBox<String[]> type;

	private JLabel donationNoteLabel;
	private JTextArea donationNote;
	private FilterPersons filterPersons;
	private Component mainLabel;
	private JLabel mainLabelAddress;
	private JLabel mainLabelDonation;
	private JPanel addAddressPanel;
	private JPanel addDonationPanel;

	public AddAttributes(Person person, IPersonService personService, IAddressService addressService, IDonationService donationService, FilterPersons filterPersons) {
		super(new MigLayout());
		
		this.person = person;
		this.donationService = donationService;
		this.addressService = addressService;
		this.personService = personService;
		this.filterPersons = filterPersons;
		buttonListener = new ButtonListener(this);
		actionHandler = new ActionHandler(this);
		builder = new ComponentBuilder();
		donationModel = new DonationTableModel(this, donationService);
		addressModel = new AddressTableModel();
		setUpPerson();
		setUpAddressTable();
		setUpDonationTable();
		
	}

	public void setUpPerson(){

		panel = builder.createPanel(800, 150);
		this.add(panel, "wrap");
		
		mainLabel = builder.createLabel("Personendaten: ");
		mainLabel.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(mainLabel, "wrap");
		JLabel empty = builder.createLabel("		");
		panel.add(empty, "wrap");
		
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
		
		noteLabel = builder.createLabel("Notiz: ");
		note = builder.createLabel(person.getNote());
		panel.add(noteLabel, "gapright 150");
		panel.add(note, "wrap");
		
		JSeparator separator = builder.createSeparator();
		this.add(separator, "growx, wrap");
	}
	
	private void setUpAddressTable(){
		addr = new Address();
		addressPanel = builder.createPanel(800, 250);
		this.add(addressPanel, "wrap");
		
		mainLabelAddress = builder.createLabel("Adressdaten: ");
		mainLabelAddress.setFont(new Font("Headline", Font.PLAIN, 14));
		addressPanel.add(mainLabelAddress, "wrap");
		
		JLabel empty = builder.createLabel("		");
		addressPanel.add(empty, "wrap");
		
		addressTable = new JTable(addressModel);
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addressPane = new JScrollPane(addressTable);
		addressPane.setPreferredSize(new Dimension(800, 250));
		addressPanel.add(addressPane, "wrap, growx");
	
		createAddressBtn = builder.createButton("Adresse hinzuf\u00FCgen", buttonListener, "open_create_address_in_show_person");
		addressPanel.add(createAddressBtn, "span 2");
		JSeparator separator = builder.createSeparator();
		this.add(separator, "growx, wrap");
		
		getAddresses();	
	}
	
	public void addAddress(){
		
		addAddressPanel = builder.createPanel(250, 200);
		
		addressStreetLabel = builder.createLabel("Stra\u00DFe: ");
		streetField = builder.createTextField(150);
		addAddressPanel.add(addressStreetLabel, "");
		addAddressPanel.add(streetField, "wrap 0px");

		addressPostalLabel = builder.createLabel("PLZ: ");
		postalField = builder.createTextField(30);
		addAddressPanel.add(addressPostalLabel, "");
		addAddressPanel.add(postalField, "wrap 0px");

		addressCityLabel = builder.createLabel("Ort: ");
		cityField = builder.createTextField(150);
		addAddressPanel.add(addressCityLabel, "");
		addAddressPanel.add(cityField, "wrap 0px");

		addressCountryLabel = builder.createLabel("Land: ");
		countryField = builder.createTextField(150);
		addAddressPanel.add(addressCountryLabel, "");
		addAddressPanel.add(countryField, "wrap 0px");
		
		final JComponent[] addAddress = new JComponent[]{addAddressPanel};
		Object[] options = {"Abbrechen", "Anlegen"};
		int go = JOptionPane.showOptionDialog(this, addAddress, "Adresse anlegen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
		if(go == 1){
			if(streetField.getText().isEmpty() || streetField.getText().equals(null)){
				JOptionPane.showMessageDialog(this, "Bitte Stra\u00DFe eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
				addAddress();
				streetField.requestFocus();
				return;
			}
			else{
				addr.setStreet(streetField.getText());
			}
			
			if(postalField.getText().isEmpty() || postalField.getText().equals(null)){
				JOptionPane.showMessageDialog(this, "Bitte Postleitzahl eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
				addAddress();
				postalField.requestFocus();
				return;
			}
			else{
				addr.setPostalCode(postalField.getText());
			}
			
			if(cityField.getText().isEmpty() || cityField.getText().equals(null)){
				JOptionPane.showMessageDialog(this, "Bitte Ort eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
				addAddress();
				cityField.requestFocus();
				return;
			}
			else{
				addr.setCity(cityField.getText());
			}
			
			if(countryField.getText().isEmpty() || countryField.getText().equals(null)){
				JOptionPane.showMessageDialog(this, "Bitte Land eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
				addAddress();
				countryField.requestFocus();
				return;
			}
			else{
				addr.setCountry(countryField.getText());
			}
			try{
				Address createdAddress = addressService.create(addr);
				addressModel.addAddress(createdAddress);
				addressList = new ArrayList<Address>();
				addressList.add(createdAddress);
				person.setAddresses(addressList);
				personService.update(person);
			}
			catch(ServiceException e){
				 JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		            e.printStackTrace();
		    		return;
			}
			JOptionPane.showMessageDialog(this, "Adresse erfolgreich angelegt.", "Information", JOptionPane.INFORMATION_MESSAGE);
			getAddresses();
			returnTo();
			
		}
	}
	
	private void setUpDonationTable(){
		
		donationPanel = builder.createPanel(800,250);
		this.add(donationPanel);
		
		mainLabelDonation = builder.createLabel("Spendendaten: ");
		mainLabelDonation.setFont(new Font("Headline", Font.PLAIN, 14));
		donationPanel.add(mainLabelDonation, "wrap");
		
		JLabel empty = builder.createLabel("		");
		donationPanel.add(empty, "wrap");
		
		donationTable = new JTable(donationModel);
		donationTable.setFillsViewportHeight(true);
		donationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		donationPane = new JScrollPane(donationTable);
		donationPane.setPreferredSize(new Dimension(800, 250));
		donationPanel.add(donationPane, "wrap, growx");
		createDonationBtn = builder.createButton("Spende hinzuf\u00FCgen", buttonListener, "open_create_donation_in_show_person");
		donationPanel.add(createDonationBtn, "split 2");
		
		backBtn = builder.createButton("Abbrechen", buttonListener, "go_from_show_person_to_person_overview");
		donationPanel.add(backBtn, "");
		
		getDonations();
	}
	
	private void getAddresses(){
		addressList = new ArrayList<Address>();

		addressList = person.getAddresses();
		System.out.println(addressList.size()+ " addresslist");
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

	@SuppressWarnings("unchecked")
	public void addDonation(){

		donation = new Donation();
		
		addDonationPanel = builder.createPanel(250, 200);
		
		typeLabel = builder.createLabel("Typ: ");
		type = builder.createComboBox(donationService.getDonationTypes(), actionHandler);
		addDonationPanel.add(typeLabel, "");
		addDonationPanel.add(type, "wrap 0px, growx");
		
		amountLabel = builder.createLabel("Betrag: ");
		amount = builder.createTextField(150);
		addDonationPanel.add(amountLabel, "");
		addDonationPanel.add(amount, "wrap 0px, growx");
		
		donationDateLabel = builder.createLabel("Datum: ");
		donationDate = builder.createTextField(150);
		addDonationPanel.add(donationDateLabel, "");
		addDonationPanel.add(donationDate, "wrap 0px, growx");
		
		dedicationLabel = builder.createLabel("Widmung: ");
		dedication = builder.createTextField(150);
		addDonationPanel.add(dedicationLabel, "");
		addDonationPanel.add(dedication, "wrap 0px, growx");
		
		donationNoteLabel = builder.createLabel("Notiz: ");
		donationNote = builder.createTextArea(10, 150);
		addDonationPanel.add(donationNoteLabel, "");
		addDonationPanel.add(donationNote, "wrap 0px, growx");
		
		final JComponent[] addDonation = new JComponent[]{addDonationPanel};
		Object[] options = {"Abbrechen", "Anlegen"};
		int go = JOptionPane.showOptionDialog(this, addDonation, "Spende anlegen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
		if(go == 1){
			donation.setDonator(person);
			
			if(amount.getText().isEmpty() || amount.getText().equals(null)){
				JOptionPane.showMessageDialog(this, "Bitte Betrag eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
				addDonation();
				amount.requestFocus();
				return;
			}
			else{
				donation.setAmount(Long.parseLong(amount.getText()));
			}
			
			if(donationDate.getText().isEmpty() || donationDate.getText().equals(null)){
				JOptionPane.showMessageDialog(this, "Bitte Datum eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
				addDonation();
				donationDate.requestFocus();
				return;
			}
			else{
				donation.setDate(Date.valueOf(donationDate.getText()));
			}
			
			donation.setType(Donation.DonationType.values()[type.getSelectedIndex()]);
			donation.setDedication(dedication.getText());
			donation.setNote(donationNote.getText());
			
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
			
		}
	}
	
	public void returnTo(){
		this.removeAll();
		this.revalidate();
		this.repaint();
		filterPersons.removeAll();
		filterPersons.revalidate();
		filterPersons.repaint();
		filterPersons.setUp();
	}
	
	public void showErrorDialog(String message){
		
	}
}

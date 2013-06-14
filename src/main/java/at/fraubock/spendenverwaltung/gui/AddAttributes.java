package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;
import at.fraubock.spendenverwaltung.gui.views.FindPersonsView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
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
	private Person person;
	private Donation donation;
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
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
	private JXDatePicker datePicker;
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
	
	private JLabel addressStreetLabel;
	private StringTextField streetField;
	
	private JLabel addressPostalLabel;
	private NumericTextField postalField;

	private JLabel addressCityLabel;
	private StringTextField cityField;
	
	private JLabel addressCountryLabel;
	private StringTextField countryField;
	
	private JLabel noteLabel;
	private JLabel note;
	
	private JLabel amountLabel;
	private NumericTextField amount;
	
	private JLabel donationDateLabel;
	private JLabel dedicationLabel;
	private StringTextField dedication;
	
	private JLabel typeLabel;
	private JComboBox<String> type;

	private JLabel donationNoteLabel;
	private StringTextField donationNote;
	@SuppressWarnings("unused")
	private FindPersonsView filterPersons;
	private Component mainLabel;
	private JLabel mainLabelAddress;
	private JLabel mainLabelDonation;
	private JPanel addAddressPanel;
	private JPanel addDonationPanel;
	private JPanel overviewPanel;
	private JLabel salutationLabel;
	private JLabel salutation;
	private List<ValidateableComponent> validateableDonationComponents = new ArrayList<ValidateableComponent>();

	public AddAttributes(Person person, IPersonService personService, IAddressService addressService, 
			IDonationService donationService, FindPersonsView filterPersons, 
			ComponentFactory componentFactory, ViewActionFactory viewActionFactory) {
		super(new MigLayout());
		
		this.person = person;
		this.donationService = donationService;
		this.addressService = addressService;
		this.personService = personService;
		this.filterPersons = filterPersons;
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		donationModel = new DonationTableModel(this, donationService);
		addressModel = new AddressTableModel();
		setUpPerson();
		setUpAddressTable();
		setUpDonationTable();
		
	}

	public void setUpPerson(){
		overviewPanel = componentFactory.createPanel(700, 800);
		this.add(overviewPanel);
		
		panel = componentFactory.createPanel(700, 150);
		overviewPanel.add(panel, "wrap");
		
		mainLabel = componentFactory.createLabel("Personendaten: ");
		mainLabel.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(mainLabel, "wrap");
		JLabel empty = componentFactory.createLabel("		");
		panel.add(empty, "wrap");
		
		salutationLabel = componentFactory.createLabel("Anrede: ");
		salutation = componentFactory.createLabel(person.getSex().toString());
		panel.add(salutationLabel, "gapright 150");
		panel.add(salutation, "wrap");
		titleLabel = componentFactory.createLabel("Titel: ");
		title = componentFactory.createLabel(person.getTitle());
		panel.add(titleLabel, "gapright 150");
		panel.add(title, "wrap");

		companyLabel = componentFactory.createLabel("Firma: ");
		company = componentFactory.createLabel(person.getCompany());
		panel.add(companyLabel, "gapright 150");
		panel.add(company, "wrap");

		given_nameLabel = componentFactory.createLabel("Vorname: ");
		given_name = componentFactory.createLabel(person.getGivenName());
		panel.add(given_nameLabel, "gapright 150");
		panel.add(given_name, "wrap");
		
		surnameLabel = componentFactory.createLabel("Nachname: ");
		surname = componentFactory.createLabel(person.getSurname());
		panel.add(surnameLabel, "gapright 150");
		panel.add(surname, "wrap");
		
		telephoneLabel = componentFactory.createLabel("Telefonnummer: ");
		telephone = componentFactory.createLabel(person.getTelephone());
		panel.add(telephoneLabel, "gapright 150");
		panel.add(telephone, "wrap");
		
		mailLabel = componentFactory.createLabel("E-Mail: ");
		mail = componentFactory.createLabel(person.getEmail());
		panel.add(mailLabel, "gapright 150");
		panel.add(mail, "wrap");
		
		noteLabel = componentFactory.createLabel("Notiz: ");
		note = componentFactory.createLabel(person.getNote());
		panel.add(noteLabel, "gapright 150");
		panel.add(note, "wrap");
		
		JSeparator separator = componentFactory.createSeparator();
		overviewPanel.add(separator, "growx, wrap");
	}
	
	private void setUpAddressTable(){
		addressPanel = componentFactory.createPanel(700, 250);
		overviewPanel.add(addressPanel, "wrap");
		
		mainLabelAddress = componentFactory.createLabel("Adressdaten: ");
		mainLabelAddress.setFont(new Font("Headline", Font.PLAIN, 14));
		addressPanel.add(mainLabelAddress, "wrap");
		
		JLabel empty = componentFactory.createLabel("		");
		addressPanel.add(empty, "wrap");
		
		addressTable = new JTable(addressModel);
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addressPane = new JScrollPane(addressTable);
		addressPane.setPreferredSize(new Dimension(650, 200));
		addressPanel.add(addressPane, "wrap, growx");
	
		createAddressBtn = new JButton();
		AddAddressAction addAddress = new AddAddressAction();
		addAddress.putValue(Action.NAME, "Adresse hinzuf\u00FCgen");
		createAddressBtn.setAction(addAddress);
		addressPanel.add(createAddressBtn, "split 2");
		
		backBtn = new JButton();
		Action getBack = viewActionFactory.getFindPersonsView();
		getBack.putValue(Action.NAME, "Abbrechen");
		backBtn.setAction(getBack);
		addressPanel.add(backBtn, "wrap");
		JSeparator separator = componentFactory.createSeparator();
		overviewPanel.add(separator, "growx, wrap");
		
		getAddresses();	
	}
	
	private final class AddAddressAction extends AbstractAction{
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addAddressPanel = componentFactory.createPanel(250, 200);
			
			addressStreetLabel = componentFactory.createLabel("Stra\u00DFe: ");
			streetField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			
			addAddressPanel.add(addressStreetLabel, "");
			addAddressPanel.add(streetField, "wrap 0px");

			addressPostalLabel = componentFactory.createLabel("PLZ: ");
			postalField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			addAddressPanel.add(addressPostalLabel, "");
			addAddressPanel.add(postalField, "wrap 0px, growx");

			addressCityLabel = componentFactory.createLabel("Ort: ");
			cityField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			addAddressPanel.add(addressCityLabel, "");
			addAddressPanel.add(cityField, "wrap 0px");

			addressCountryLabel = componentFactory.createLabel("Land: ");
			countryField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			
			addAddressPanel.add(addressCountryLabel, "");
			addAddressPanel.add(countryField, "wrap 0px");
			
			final JComponent[] addAddress = new JComponent[]{addAddressPanel};
			Object[] options = {"Abbrechen", "Anlegen"};
			int go = JOptionPane.showOptionDialog(overviewPanel, addAddress, "Adresse anlegen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
			
			
			if(go == 1){
				// create new address
				Address addr = new Address();
				if(streetField.validateContents()==false){
					JOptionPane.showMessageDialog(overviewPanel, "Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe", "Warnung", JOptionPane.WARNING_MESSAGE);
					AddAddressAction action = new AddAddressAction();
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
				if(postalField.validateContents()==false){
					JOptionPane.showMessageDialog(overviewPanel, "Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: PLZ", "Warnung", JOptionPane.WARNING_MESSAGE);
					AddAddressAction action = new AddAddressAction();
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
				if(cityField.validateContents()==false){
					JOptionPane.showMessageDialog(overviewPanel, "Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: Ort", "Warnung", JOptionPane.WARNING_MESSAGE);
					AddAddressAction action = new AddAddressAction();
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
				if(countryField.validateContents()==false){
					JOptionPane.showMessageDialog(overviewPanel, "Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: Land", "Warnung", JOptionPane.WARNING_MESSAGE);
					AddAddressAction action = new AddAddressAction();
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
				
				else{
					addr.setStreet(streetField.getText());
					addr.setPostalCode(postalField.getText());
					addr.setCity(cityField.getText());
					addr.setCountry(countryField.getText());
					
					try{
						Address createdAddress = addressService.create(addr);
						addressModel.addAddress(createdAddress);
						addressList = person.getAddresses();
						addressList.add(createdAddress);
						person.setAddresses(addressList);
						personService.update(person);
					}
					catch(ServiceException ex){
						 JOptionPane.showMessageDialog(overviewPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
									"Fehler", JOptionPane.ERROR_MESSAGE);
				            ex.printStackTrace();
				    		return;
					}
					
					JOptionPane.showMessageDialog(overviewPanel, "Adresse erfolgreich angelegt.", "Information", JOptionPane.INFORMATION_MESSAGE);
					addressTable.revalidate();
				}
			}
			else{
				return;
			}
		}
	}
	
	private final class AddDonationAction extends AbstractAction{
		private static final long serialVersionUID = 1L;
		

		@Override
		public void actionPerformed(ActionEvent e) {
			donation = new Donation();
			
			addDonationPanel = componentFactory.createPanel(250, 200);
			
			typeLabel = componentFactory.createLabel("Typ: ");
			type = new JComboBox<String>(donationService.getDonationTypes());
			addDonationPanel.add(typeLabel, "");
			addDonationPanel.add(type, "wrap 0px, growx");
			
			amountLabel = componentFactory.createLabel("Betrag: ");
			amount = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			validateableDonationComponents.add(amount);
			addDonationPanel.add(amountLabel, "");
			addDonationPanel.add(amount, "wrap 0px, growx");
			
			donationDateLabel = componentFactory.createLabel("Datum: ");
			datePicker = new JXDatePicker(new java.util.Date());
			addDonationPanel.add(donationDateLabel, "");
			addDonationPanel.add(datePicker, "wrap 0px, growx");
			
			dedicationLabel = componentFactory.createLabel("Widmung: ");
			dedication =  new StringTextField(ComponentConstants.MEDIUM_TEXT);
			validateableDonationComponents.add(dedication);
			addDonationPanel.add(dedicationLabel, "");
			addDonationPanel.add(dedication, "wrap 0px, growx");
			
			donationNoteLabel = componentFactory.createLabel("Notiz: ");
			donationNote = new StringTextField(ComponentConstants.LONG_TEXT);
			addDonationPanel.add(donationNoteLabel, "");
			addDonationPanel.add(donationNote, "wrap 0px, growx");
			
			final JComponent[] addDonation = new JComponent[]{addDonationPanel};
			Object[] options = {"Abbrechen", "Anlegen"};
			int go = JOptionPane.showOptionDialog(overviewPanel, addDonation, "Spende anlegen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
			
			if(go == 1){
				if(amount.validateContents()==false){
					JOptionPane.showMessageDialog(overviewPanel, "Bitte korrigieren Sie Ihre Eingabe: Spendenbetrag", "Warnung", JOptionPane.WARNING_MESSAGE);
					AddAddressAction action = new AddAddressAction();
					action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
				}
				
				else{
					Long d_amount = null;
					donation.setDonator(person);
					d_amount = amount.getHundredths();
					donation.setAmount(d_amount);
					//donation.setAmount(Long.parseLong(amount.getText()));
					donation.setDate(datePicker.getDate());
					donation.setType(Donation.DonationType.values()[type.getSelectedIndex()]);
					donation.setDedication(dedication.getText());
					donation.setNote(donationNote.getText());
					
					try{
						Donation createdDonation = donationService.create(donation);
						donationModel.addDonation(createdDonation);
					}
					catch(ServiceException ex){
						 JOptionPane.showMessageDialog(overviewPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
									"Fehler", JOptionPane.ERROR_MESSAGE);
				            ex.printStackTrace();
				    		return;
					}
					JOptionPane.showMessageDialog(overviewPanel, "Spende erfolgreich angelegt", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}	
		}
	}
	
	private void setUpDonationTable(){
		
		donationPanel = componentFactory.createPanel(700,250);
		overviewPanel.add(donationPanel);
		
		mainLabelDonation = componentFactory.createLabel("Spendendaten: ");
		mainLabelDonation.setFont(new Font("Headline", Font.PLAIN, 14));
		donationPanel.add(mainLabelDonation, "wrap");
		
		JLabel empty = componentFactory.createLabel("		");
		donationPanel.add(empty, "wrap");
		
		donationTable = new JTable(donationModel);
		donationTable.setFillsViewportHeight(true);
		donationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		donationPane = new JScrollPane(donationTable);
		donationPane.setPreferredSize(new Dimension(650, 200));
		donationPanel.add(donationPane, "wrap, growx");
		
		createDonationBtn = new JButton();
		AddDonationAction donationAction = new AddDonationAction();
		donationAction.putValue(Action.NAME, "Spende hinzuf\u00FCgen");
		createDonationBtn.setAction(donationAction);
		donationPanel.add(createDonationBtn, "split 2");
		
		backBtn = new JButton();
		Action getBack = viewActionFactory.getFindPersonsView();
		getBack.putValue(Action.NAME, "Abbrechen");
		backBtn.setAction(getBack);
		donationPanel.add(backBtn, "");
		
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

	
}

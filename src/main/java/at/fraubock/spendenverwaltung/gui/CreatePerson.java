package at.fraubock.spendenverwaltung.gui;

import java.awt.Font;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class CreatePerson extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private PersonOverview personOverview;
	private IDonationService donationService;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private ActionHandler actionHandler;
	private PersonTableModel personModel;
	private JPanel panel;
	
	private JLabel addDonation;
	private JLabel addPerson;
	private JComboBox<String> salutation;
	private JLabel salutLabel;
	
	private JLabel title;
	private JComboBox<String> titleBox;
	
	private JLabel company;
	private JTextField companyField;
	
	private JLabel given_name;
	private JTextField givenField;
	
	private JLabel surname;
	private JTextField surnameField;
	
	private JLabel telephone;
	private JTextField telephoneField;
	
	private JLabel mail;
	private JTextField mailField;
	
	private JLabel street;
	private JTextField streetField;
	
	private JLabel postal;
	private JTextField postalField;
	
	private JLabel city;
	private JTextField cityField;
	
	private JLabel country;
	private JTextField countryField;
	
	private JLabel notifyType;
	private JRadioButton notifyMail;
	private JRadioButton notifyPost;
	
	private JLabel note;
	private JTextArea noteArea; 
	
	private JLabel donationLabel;
	@SuppressWarnings("rawtypes")
	private JComboBox donationCombo;
	private JTextField amount;
	private JButton ok;
	private JButton cancel;
	
	private Person p = new Person();
	private Address addr = new Address();
	private Donation donation = new Donation();
	private JSeparator separator;
	private JPanel donationPanel;
	private JLabel dedicationLabel;
	private JTextField dedicationField;
	private JLabel dedicationNoteLabel;
	private JTextField dedicationNoteField;

	public CreatePerson(IPersonService personService, IAddressService addressService, IDonationService donationService, PersonOverview personOverview){
		super(new MigLayout());
		
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.personOverview = personOverview;
		this.personModel = this.personOverview.getPersonModel();
		buttonListener = new ButtonListener(this);
		actionHandler = new ActionHandler(this);
		builder = new ComponentBuilder();
		
		panel = builder.createPanel(800,500);
		this.add(panel);
		
		setUpCreate();
	}

	@SuppressWarnings("unchecked")
	private void setUpCreate() {
		
		addPerson = builder.createLabel("Personendaten eintragen");
		addPerson.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(addPerson, "wrap");
		JLabel empty = builder.createLabel("		");
		panel.add(empty, "wrap");
		
		String[] salutCombo = new String[]{"Herr", "Frau", "Fam.", "Firma"};
		salutation = builder.createComboBox(salutCombo, actionHandler, "salutCombo");
		salutLabel = builder.createLabel("Anrede: ");
		panel.add(salutLabel);
		panel.add(salutation, "wrap");
		
		title = builder.createLabel("Titel: ");
		String[] titleCombo = new String[]{"-", "BA", "BSc", "DI", "Dr.", "Ing.", "MA", "Mag.", "MSc.", "Prof."};
		titleBox = builder.createComboBox(titleCombo, actionHandler, "titleCombo");
		panel.add(title);
		panel.add(titleBox, "wrap");
		
		company = builder.createLabel("Firma: ");
		companyField = builder.createTextField(150);
		panel.add(company);
		panel.add(companyField, "wrap, growx");
		
		given_name = builder.createLabel("Vorname: ");
		givenField = builder.createTextField(150);
		panel.add(given_name);
		panel.add(givenField, "wrap, growx");
		
		surname = builder.createLabel("Nachname: ");
		surnameField = builder.createTextField(150);
		panel.add(surname);
		panel.add(surnameField, "wrap, growx");
		
		telephone = builder.createLabel("Telephon: ");
		telephoneField = builder.createTextField(150);
		panel.add(telephone);
		panel.add(telephoneField, "wrap, growx");
		
		mail = builder.createLabel("E-Mail: ");
		mailField = builder.createTextField(150);
		panel.add(mail);
		panel.add(mailField, "wrap, growx");
		
		street = builder.createLabel("Stra\u00DFe: ");
		streetField = builder.createTextField(150);
		panel.add(street);
		panel.add(streetField, "wrap, growx");
		
		postal = builder.createLabel("PLZ: ");
		postalField = builder.createTextField(10);
		panel.add(postal);
		panel.add(postalField, "wrap, growx");
		
		city = builder.createLabel("Ort: ");
		cityField = builder.createTextField(150);
		panel.add(city);
		panel.add(cityField, "wrap, growx");
		
		country = builder.createLabel("Land: ");
		countryField = builder.createTextField(150);
		panel.add(country);
		panel.add(countryField, "wrap, growx");
		
		notifyType = builder.createLabel("Notification Type: ");
		notifyMail = builder.createRadioButton("E-Mail", actionHandler, "notifyMail");
		notifyPost = builder.createRadioButton("Post", actionHandler, "notifyPost");

		panel.add(notifyType);
		panel.add(notifyMail, "split 2");
		panel.add(notifyPost, "wrap");
		
		note = builder.createLabel("Notiz: ");
		noteArea = builder.createTextArea(5, 5);
		panel.add(note);
		panel.add(noteArea, "wrap, growx");
		panel.add(empty, "wrap");

/**
* Next section
*/
		
		separator = builder.createSeparator();
		this.add(separator, "wrap, growx");
		
		donationPanel = builder.createPanel(800, 450);
		this.add(donationPanel);
		
		addDonation = builder.createLabel("Neue Spende anlegen");
		addDonation.setFont(new Font("Headline", Font.PLAIN, 14));
		donationPanel.add(addDonation, "wrap");
		
		String[] donationString = new String[]{"\u00DCberweisung", "Merchandise", "Online-Shop", "Bar", "SMS"};
		donationCombo = builder.createComboBox(donationString, actionHandler, "donationCombo");
		donationLabel = builder.createLabel("Spende durch: ");
		donationPanel.add(donationLabel);
		donationPanel.add(donationCombo, "split 2");
		amount = builder.createTextField(30);
		donationPanel.add(amount, "wrap, growx");
		dedicationLabel = builder.createLabel("Widmung: ");
		dedicationField = builder.createTextField(150);
		dedicationNoteLabel = builder.createLabel("Notiz: ");
		dedicationNoteField = builder.createTextField(150);

		donationPanel.add(dedicationLabel);
		donationPanel.add(dedicationField, "wrap, growx");
		donationPanel.add(dedicationNoteLabel);
		donationPanel.add(dedicationNoteField, "wrap, growx");
		
		donationPanel.add(empty, "wrap");
		
		ok = builder.createButton("Anlegen", buttonListener, "create_person_in_db");
		donationPanel.add(ok, "split 2");
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_person_in_db");
		donationPanel.add(cancel, "wrap");
	}
	
	public void createPersonInDb(){
		/**
		 * Sex
		 */
		String sex = "Herr";
		
		if(salutation.getSelectedItem().equals(sex)){
			sex = "MALE";
		}
		if(salutation.getSelectedItem().equals("Frau")){
			sex = "FEMALE";
		}
		if(salutation.getSelectedItem().equals("Fam.")){
			sex = "FAMILY";
		}
		if(salutation.getSelectedItem().equals("Firma")){
			sex = "COMPANY";
		}
		
		p.setSex(Person.Sex.valueOf(sex));
		
		/**
		 * Title
		 */
		String title = "-";
		
		if(titleBox.getSelectedItem().equals("BA")){
			title = "BA";
		}
		if(titleBox.getSelectedItem().equals("BSc")){
			title = "BSc";
		}
		if(titleBox.getSelectedItem().equals("DI")){
			title = "DI";
		}
		if(titleBox.getSelectedItem().equals("Dr.")){
			title = "Dr.";
		}
		if(titleBox.getSelectedItem().equals("Ing.")){
			title = "Ing.";
		}
		if(titleBox.getSelectedItem().equals("MA")){
			title = "MA";
		}
		if(titleBox.getSelectedItem().equals("Mag.")){
			title = "Mag.";
		}
		if(titleBox.getSelectedItem().equals("MSc")){
			title = "MSc";
		}
		if(titleBox.getSelectedItem().equals("Prof.")){
			title = "Prof.";
		}
		
		p.setTitle(title);
		
		/**
		 * Company
		 */
		p.setCompany(companyField.getText());
		
		/**
		 * Given name
		 */
		p.setGivenName(givenField.getText());
		
		/**
		 * Surname
		 */
		p.setSurname(surnameField.getText());
		
		/**
		 * Telephone
		 */
		p.setTelephone(telephoneField.getText());
		
		/**
		 * Mail
		 */
		p.setEmail(mailField.getText());
		
		/**
		 * Address: Street
		 */
		addr.setStreet(streetField.getText());
		
		/**
		 * Address: Postal code
		 */
		addr.setPostalCode(postalField.getText());
		
		/**
		 * Address: City
		 */
		addr.setCity(cityField.getText());
		
		/**
		 * Address: Country
		 */
		addr.setCountry(countryField.getText());

		p.setMainAddress(addr);
		
		/**
		 * Notification type
		 */
		if(notifyMail.isSelected()){
			p.setEmailNotification(true);
		}
		if(notifyPost.isSelected()){
			p.setPostalNotification(true);
		}
		
		/**
		 * Note
		 */
		String note = noteArea.getText();
		p.setNote(note);
		
		String donationInput = "\u00DCberweisung";
		if(donationCombo.getSelectedItem().equals(donationInput)){
			donationInput = "BANK_TRANSFER";
		}
		if(donationCombo.getSelectedItem().equals("Merchandise")){
			donationInput = "MERCHANDISE";
		}
		if(donationCombo.getSelectedItem().equals("Online-Shop")){
			donationInput = "ONLINE";
		}
		if(donationCombo.getSelectedItem().equals("SMS")){
			donationInput = "SMS";
		}
		if(donationCombo.getSelectedItem().equals("BAR")){
			donationInput = "BAR";
		}
		/**
		 * Donation Type
		 */
		donation.setType(Donation.DonationType.valueOf(donationInput));
		/**
		 * Donation Amount
		 */
		donation.setAmount(Long.parseLong(amount.getText()));
		/**
		 * Donation Date
		 */
		Date date = new Date(new Date().getTime());
		donation.setDate(date);
		/**
		 * Donation Dedication
		 */
		donation.setDedication(dedicationField.getText());
		/**
		 * Donation Note
		 */
		donation.setNote(dedicationNoteField.getText());
		
		try{
			Address createdAddress = addressService.create(addr); // will now be created when person is created - p
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(createdAddress);
			p.setAddresses(addresses);
			Person createdPerson = personService.create(p);
			donation.setDonator(createdPerson);
			donationService.create(donation);
			personModel.addPerson(createdPerson);
		}
		catch(ServiceException e){
			 JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
	    		return;
		}
		JOptionPane.showMessageDialog(this, "Person erfolgreich angelegt", "Information", JOptionPane.INFORMATION_MESSAGE);
		returnTo();
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
}

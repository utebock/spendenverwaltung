package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import domain.Address;
import domain.Person;
import exceptions.ServiceException;

import net.miginfocom.swing.MigLayout;
import service.IAddressService;
import service.IPersonService;

public class CreatePerson extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private PersonOverview personOverview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private ActionHandler actionHandler;
	private JPanel panel;
	
	private JLabel addDonation;
	private JLabel addPerson;
	@SuppressWarnings("rawtypes")
	private JComboBox salutation;
	private JLabel salutLabel;
	
	private JLabel title;
	@SuppressWarnings("rawtypes")
	private JComboBox titleBox;
	
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
	private ButtonGroup buttonGroupNotify;
	private JRadioButton notifyMail;
	private JRadioButton notifyPost;
	
	private JLabel note;
	private JTextArea noteArea; 
	
	private JLabel donation;
	@SuppressWarnings("rawtypes")
	private JComboBox donationCombo;
	private JTextField amount;
	private JButton ok;
	private JButton cancel;
	
	private Person p = new Person();
	private Address addr = new Address();
	
	public CreatePerson(IPersonService personService, IAddressService addressService, PersonOverview personOverview){
		super(new MigLayout());
		
		this.personService = personService;
		this.addressService = addressService;
		this.personOverview = personOverview;
		buttonListener = new ButtonListener(this);
		actionHandler = new ActionHandler(this);
		builder = new ComponentBuilder();
		
		panel = builder.createPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(800, 800));
		panel.setBackground(Color.WHITE);
		this.add(panel);
		
		setUpCreate();
		setUpDonation();
	}

	private void setUpDonation() {
		// TODO Auto-generated method stub
		
	}

	private void setUpCreate() {
		
		addPerson = builder.createLabel("Personendaten eintragen");
		panel.add(addPerson, "wrap");
		JLabel empty = builder.createLabel("		");
		panel.add(empty, "wrap");
		
		String[] salutCombo = new String[]{"Herr", "Frau", "Fam.", "FA."};
		salutation = builder.createComboBox(salutCombo, actionHandler, "salutCombo");
		salutLabel = builder.createLabel("Anrede: ");
		panel.add(salutLabel);
		panel.add(salutation, "wrap");
		
		title = builder.createLabel("Titel: ");
		String[] titleCombo = new String[]{"Ing.", "DI"};
		titleBox = builder.createComboBox(titleCombo, actionHandler, "titleCombo");
		panel.add(title);
		panel.add(titleBox);
		
		company = builder.createLabel("Firma: ");
		companyField = builder.createTextField(50);
		panel.add(company, "gap 150");
		panel.add(companyField, "wrap");
		
		given_name = builder.createLabel("Vorname: ");
		givenField = builder.createTextField(30);
		panel.add(given_name);
		panel.add(givenField);
		
		surname = builder.createLabel("Nachname: ");
		surnameField = builder.createTextField(50);
		panel.add(surname, "gap 150");
		panel.add(surnameField, "wrap");
		
		telephone = builder.createLabel("Telephon: ");
		telephoneField = builder.createTextField(30);
		panel.add(telephone);
		panel.add(telephoneField);
		
		mail = builder.createLabel("E-Mail: ");
		mailField = builder.createTextField(50);
		panel.add(mail, "gap 150");
		panel.add(mailField, "wrap");
		
		street = builder.createLabel("Strasse: ");
		streetField = builder.createTextField(30);
		panel.add(street);
		panel.add(streetField);
		
		postal = builder.createLabel("PLZ: ");
		postalField = builder.createTextField(30);
		panel.add(postal, "gap 150");
		panel.add(postalField, "wrap");
		
		city = builder.createLabel("Ort: ");
		cityField = builder.createTextField(30);
		panel.add(city);
		panel.add(cityField);
		
		country = builder.createLabel("Land: ");
		countryField = builder.createTextField(50);
		panel.add(country, "gap 150");
		panel.add(countryField, "wrap");
		
		notifyType = builder.createLabel("Notification Type: ");
		buttonGroupNotify = new ButtonGroup();
		notifyMail = builder.createRadioButton("E-Mail", actionHandler, "notifyMail");
		notifyPost = builder.createRadioButton("Post", actionHandler, "notifyPost");
		buttonGroupNotify.add(notifyMail);
		buttonGroupNotify.add(notifyPost);
		panel.add(notifyType);
		panel.add(notifyMail);
		panel.add(notifyPost, "split 3");
		
		note = builder.createLabel("Notiz: ");
		noteArea = builder.createTextArea(5, 20);
		panel.add(note, "gap 90");
		panel.add(noteArea, "wrap");
		
		/**
		 * Next section
		 */
		panel.add(empty,"wrap");
		
		addDonation = builder.createLabel("Neue Spende anlegen");
		panel.add(addDonation, "wrap");
		
		String[] donationString = new String[]{"Ueberweisung", "Veranstaltung", "Online-Shop"};
		donationCombo = builder.createComboBox(donationString, actionHandler, "donationCombo");
		donation = builder.createLabel("Spende durch: ");
		panel.add(donation);
		panel.add(donationCombo);
		amount = builder.createTextField(10);
		panel.add(amount, "wrap");
		
		panel.add(empty, "wrap");
		
		ok = builder.createButton("Anlegen", buttonListener, "create_person_in_db");
		panel.add(ok);
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_person");
		panel.add(cancel, "split 2, wrap");
	}
	
	public void createPersonInDb(){
		String salut = "HERR";
		if(salutation.getSelectedItem().equals("Frau")){
			salut = "FRAU";
		}
		if(salutation.getSelectedItem().equals("Fam.")){
			salut = "FAM.";
		}
		if(salutation.getSelectedItem().equals("FA.")){
			salut = "FA.";
		}
		p.setSalutation(Person.Salutation.valueOf(salut));
		
		String title = "Ing.";
		if(titleBox.getSelectedItem().equals("DI")){
			title = "DI";
		}
		p.setTitle(title);
		
		String company = companyField.getText();
		p.setCompany(company);
		
		String givenName = givenField.getText();
		p.setGivenName(givenName);
		
		String surname = surnameField.getText();
		p.setSurname(surname);
		
		String tel = telephoneField.getText();
		p.setTelephone(tel);
		
		String mail = mailField.getText();
		p.setEmail(mail);
		
		String street = streetField.getText();
		addr.setStreet(street);
		
		int postal = Integer.parseInt(postalField.getText());
		addr.setPostalCode(postal);
		
		String city = cityField.getText();
		addr.setCity(city);
		
		String country = countryField.getText();
		addr.setCountry(country);
		
		if(notifyMail.isSelected()){
			p.setNotificationType(Person.NotificationType.MAIL);
		}
		else{
			p.setNotificationType(Person.NotificationType.POST);
		}
		
		String note = noteArea.getText();
		p.setNote(note);
		
		try{
			addressService.create(addr);
			personService.create(p);
		}
		catch(ServiceException e){
			 JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
	    		return;
		}
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

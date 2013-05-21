package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
		
		addPerson = builder.createLabel("Person anlegen");
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
		panel.add(notifyPost, "split 2");
		
		note = builder.createLabel("Notiz: ");
		noteArea = builder.createTextArea(5, 20);
		panel.add(note, "gap 90");
		panel.add(noteArea, "wrap");
		
		
	}

}

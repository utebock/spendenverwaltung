package at.fraubock.spendenverwaltung.gui;

import java.awt.Font;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class EditPerson extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private ActionHandler actionHandler;
	private JPanel panel;
	private JComboBox<String> salutation;
	private JLabel salutLabel;
	
	private JLabel title;
	private JComboBox<String> titleBox;
	
	private JLabel company;
	
	private JLabel given_name;
	
	private JLabel surname;
	private JLabel telephone;
	
	private JLabel mail;
	private JLabel street;
	private JTextField streetField;
	
	private JLabel postal;
	private JTextField postalField;
	
	private JLabel city;
	private JTextField cityField;
	
	private JTextField countryField;
	
	private JLabel notifyType;
	private JRadioButton notifyMail;
	private JRadioButton notifyPost;
	
	private JLabel note;
	private JTextArea noteArea; 
	
	private JButton ok;
	private JButton cancel;
	private PersonTableModel personModel;
	private Person person = new Person();
	private Address addr = new Address();
	private Overview overview;
	private FilterPersons filterPersons;
	private JLabel editPerson;
	private JTextField companyField;
	private JTextField givenField;
	private JTextField surnameField;
	private JTextField telephoneField;
	private JTextField mailField;
	private JLabel country;

	public EditPerson(Person person, IPersonService personService, IAddressService addressService, FilterPersons filterPersons, Overview overview) {
		super(new MigLayout());
		
		this.person = person;
		this.personService = personService;
		this.addressService = addressService;
		this.filterPersons = filterPersons;
		this.overview = overview;
		this.personModel = this.overview.getPersonModel();
		buttonListener = new ButtonListener(this);
		actionHandler = new ActionHandler(this);
		builder = new ComponentBuilder();
		setUpPerson();
	}

	@SuppressWarnings("unchecked")
	public void setUpPerson(){
		panel = builder.createPanel(800, 800);
		this.add(panel);
		editPerson = builder.createLabel("Personendaten aendern");
		editPerson.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(editPerson, "wrap");
		JLabel empty = builder.createLabel("		");
		panel.add(empty, "wrap");
		
		String[] salutCombo = new String[]{"Herr", "Frau", "Fam.", "Firma"};
		salutation = builder.createComboBox(salutCombo, actionHandler);
		salutLabel = builder.createLabel("Anrede: ");
		
		if (person.getSex().equals("MALE")){
			salutation.setSelectedItem("Herr");
		}
		if (person.getSex().equals("FEMALE")){
			salutation.setSelectedItem("Frau");
		}
		if (person.getSex().equals("FAMILIY")){
			salutation.setSelectedItem("Fam.");
		}
		if (person.getSex().equals("COMPANY")){
			salutation.setSelectedItem("Firma");
		}
		
		panel.add(salutLabel);
		panel.add(salutation, "wrap");
		
		
		title = builder.createLabel("Titel: ");
		String[] titleCombo = new String[]{"-", "BA", "BSc", "DI", "Dr.", "Ing.", "MA", "Mag.", "MSc.", "Prof."};
		titleBox = builder.createComboBox(titleCombo, actionHandler);
		
		if(person.getTitle().equals("-")){
			titleBox.setSelectedItem("-");
		}
		if(person.getTitle().equals("BA")){
			titleBox.setSelectedItem("BA");
		}
		if(person.getTitle().equals("BSc")){
			titleBox.setSelectedItem("BSc");
		}
		if(person.getTitle().equals("DI")){
			titleBox.setSelectedItem("DI");
		}
		if(person.getTitle().equals("Dr.")){
			titleBox.setSelectedItem("Dr.");
		}
		if(person.getTitle().equals("Ing.")){
			titleBox.setSelectedItem("Ing.");
		}
		if(person.getTitle().equals("MA")){
			titleBox.setSelectedItem("MA");
		}
		
		if(person.getTitle().equals("Mag.")){
			titleBox.setSelectedItem("Mag.");
		}
		if(person.getTitle().equals("MSc.")){
			titleBox.setSelectedItem("MSc.");
		}
		if(person.getTitle().equals("Prof.")){
			titleBox.setSelectedItem("Prof.");
		}
		
		panel.add(title);
		panel.add(titleBox, "wrap, growx");
		
		company = builder.createLabel("Firma: ");
		companyField = builder.createTextField(person.getCompany());
		panel.add(company);
		panel.add(companyField, "wrap, growx");
		
		given_name = builder.createLabel("Vorname: ");
		givenField = builder.createTextField(person.getGivenName());
		panel.add(given_name);
		panel.add(givenField, "wrap, growx");
		
		surname = builder.createLabel("Nachname: ");
		surnameField = builder.createTextField(person.getSurname());
		panel.add(surname);
		panel.add(surnameField, "wrap, growx");
		
		telephone = builder.createLabel("Telefon: ");
		telephoneField = builder.createTextField(person.getTelephone());
		panel.add(telephone);
		panel.add(telephoneField, "wrap, growx");
		
		mail = builder.createLabel("E-Mail: ");
		mailField = builder.createTextField(person.getEmail());
		panel.add(mail);
		panel.add(mailField, "wrap, growx");
		
		street = builder.createLabel("Stra\u00DFe: ");
		streetField = builder.createTextField(person.getMainAddress().getStreet());
		panel.add(street);
		panel.add(streetField, "wrap, growx");
		
		postal = builder.createLabel("PLZ: ");
		postalField = builder.createTextField(person.getMainAddress().getPostalCode());
		panel.add(postal);
		panel.add(postalField, "wrap, growx");
		
		city = builder.createLabel("Ort: ");
		cityField = builder.createTextField(person.getMainAddress().getCity());
		panel.add(city);
		panel.add(cityField, "wrap, growx");
		
		country = builder.createLabel("Land: ");
		countryField = builder.createTextField(person.getMainAddress().getCountry());
		panel.add(country);
		panel.add(countryField, "wrap, growx");
		
		notifyType = builder.createLabel("Notification Type: ");
		notifyMail = builder.createRadioButton("E-Mail", actionHandler, "notifyMail");
		notifyPost = builder.createRadioButton("Post", actionHandler, "notifyPost");
		
		if(person.isEmailNotification() == true){
			notifyMail.setSelected(true);
		}
		if(person.isPostalNotification() == true){
			notifyPost.setSelected(true);
		}
		
		panel.add(notifyType);
		panel.add(notifyMail, "split 2");
		panel.add(notifyPost, "wrap");
		
		note = builder.createLabel("Notiz: ");
		noteArea = builder.createTextArea(person.getNote());
		panel.add(note);
		panel.add(noteArea, "wrap, growx");
		
		ok = builder.createButton("Bearbeiten", buttonListener, "edit_person_in_db");
		panel.add(ok, "split 2");
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_edit");
		panel.add(cancel, "wrap");
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

	public void editPerson() {
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
		
		person.setSex(Person.Sex.valueOf(sex));
		
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
		
		person.setTitle(title);
		
		/**
		 * Company
		 */
		person.setCompany(companyField.getText());
		
		/**
		 * Given name
		 */
		person.setGivenName(givenField.getText());
		
		/**
		 * Surname
		 */
		person.setSurname(surnameField.getText());
		
		/**
		 * Telephone
		 */
		person.setTelephone(telephoneField.getText());
		
		/**
		 * Mail
		 */
		person.setEmail(mailField.getText());
		
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

		person.setMainAddress(addr);
		
		/**
		 * Notification type
		 */
		if(notifyMail.isSelected()){
			person.setEmailNotification(true);
		}
		if(notifyPost.isSelected()){
			person.setPostalNotification(true);
		}
		
		/**
		 * Note
		 */
		String note = noteArea.getText();
		person.setNote(note);
		
		try{
			Address updatedAddress = addressService.update(addr); // will now be created when person is created - p
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(updatedAddress);
			person.setAddresses(addresses);
			Person updatedPerson = personService.update(person);
			personModel.addPerson(updatedPerson);
		}
		catch(ServiceException e){
			 JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
	    		return;
		}
		JOptionPane.showMessageDialog(this, "Person erfolgreich bearbeitet", "Information", JOptionPane.INFORMATION_MESSAGE);
		returnTo();
	}
	
}

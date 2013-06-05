package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.MainFrame;
import at.fraubock.spendenverwaltung.gui.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.AlphabeticTextField;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.EmailTextField;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class CreatePersonView extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private ComponentFactory componentFactory;
	private PersonTableModel personModel;
	private JPanel panel;
	
	private List<ValidateableComponent> validateablePersonComponents = new ArrayList<ValidateableComponent>();
	private List<ValidateableComponent> validateableDonationComponents = new ArrayList<ValidateableComponent>();

	private JLabel addDonation;
	private JLabel addPerson;
	private JComboBox<String> salutation;
	private JLabel salutLabel;
	
	private JLabel title;
	private JComboBox<String> titleBox;
	
	private JLabel company;
	private StringTextField companyField;
	
	private JLabel given_name;
	private AlphabeticTextField givenField;
	
	private JLabel surname;
	private AlphabeticTextField surnameField;
	
	private JLabel telephone;
	private NumericTextField telephoneField;
	
	private JLabel mail;
	private EmailTextField emailField;
	
	private JLabel street;
	private AlphabeticTextField streetField;
	
	private JLabel postal;
	private NumericTextField postalField;
	
	private JLabel city;
	private AlphabeticTextField cityField;
	
	private JLabel country;
	private AlphabeticTextField countryField;
	
	private JLabel notifyType;
	private JCheckBox notifyMail;
	private JCheckBox notifyPost;
	
	private JLabel note;
	
	private StringTextField noteText; 
	
	private JLabel donationLabel;
	private JComboBox<String> donationCombo;
	private NumericTextField amount;
	
	private JButton ok = new JButton(new SubmitAction());
	private JButton cancel = new JButton(new CancelAction());
	
	private Person person;
	private Address address;
	private Donation donation;
	
	private JSeparator separator;
	private JPanel donationPanel;
	private JLabel dedicationLabel;
	private StringTextField dedicationField;
	private JLabel dedicationNoteLabel;
	private StringTextField dedicationNoteField;
	private JLabel dateLabel;
	//TODO replace with datepicker
	private JTextField dateField;
	private JLabel dateInstruction;
	private JPanel overviewPanel;

	private MainFrame mainframe;
	
	public CreatePersonView(IPersonService personService, IAddressService addressService, IDonationService donationService, MainFrame mainframe, ComponentFactory
			componentFactory){
		super(new MigLayout());
		
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.mainframe = mainframe;
		this.componentFactory = componentFactory;
		//TODO
//		this.personModel = this.overview.getPersonModel();
		setUpCreate();
	}

	private void setUpCreate() {
		overviewPanel = componentFactory.createPanel(800, 850);
		//JScrollPane pane = new JScrollPane(overviewPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(overviewPanel);

		panel = componentFactory.createPanel(800,500);
		overviewPanel.add(panel);
		
		addPerson = componentFactory.createLabel("Personendaten eintragen");
		addPerson.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(addPerson, "wrap");
		JLabel empty = componentFactory.createLabel("		");
		panel.add(empty, "wrap");
		
		String[] salutCombo = new String[]{"Herr", "Frau", "Fam.", "Firma"};
		salutation = new JComboBox<String>(salutCombo);
		
		salutLabel = componentFactory.createLabel("Anrede: ");
		panel.add(salutLabel);
		panel.add(salutation, "wrap");
		
		title = componentFactory.createLabel("Titel: ");
		String[] titleCombo = new String[]{"-", "BA", "BSc", "DI", "Dr.", "Ing.", "MA", "Mag.", "MSc.", "Prof."};
		titleBox = new JComboBox<String>(titleCombo);
		panel.add(title);
		panel.add(titleBox, "wrap");
		
		company = componentFactory.createLabel("Firma: ");
		companyField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(company);
		panel.add(companyField, "wrap, growx");
		validateablePersonComponents.add(companyField);
		
		given_name = componentFactory.createLabel("Vorname: ");
		givenField = new AlphabeticTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(given_name);
		panel.add(givenField, "wrap, growx");
		validateablePersonComponents.add(givenField);
		
		surname = componentFactory.createLabel("Nachname: ");
		surnameField = new AlphabeticTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(surname);
		panel.add(surnameField, "wrap, growx");
		validateablePersonComponents.add(surnameField);

		telephone = componentFactory.createLabel("Telefon: ");
		telephoneField = new NumericTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(telephone);
		panel.add(telephoneField, "wrap, growx");
		validateablePersonComponents.add(telephoneField);
		
		mail = componentFactory.createLabel("E-Mail: ");
		emailField = new EmailTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(mail);
		panel.add(emailField, "wrap, growx");
		validateablePersonComponents.add(emailField);

		street = componentFactory.createLabel("Stra\u00DFe: ");
		streetField = new AlphabeticTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(street);
		panel.add(streetField, "growx, wrap");
		validateablePersonComponents.add(streetField);

		postal = componentFactory.createLabel("PLZ: ");
		postalField = new NumericTextField(ComponentConstants.SHORT_TEXT);
		panel.add(postal);
		panel.add(postalField, "wrap, growx");
		validateablePersonComponents.add(postalField);
		
		city = componentFactory.createLabel("Ort: ");
		cityField = new AlphabeticTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(city);
		panel.add(cityField, "wrap, growx");
		validateablePersonComponents.add(cityField);
		
		country = componentFactory.createLabel("Land: ");
		countryField = new AlphabeticTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(country);
		panel.add(countryField, "wrap, growx");
		validateablePersonComponents.add(countryField);

		notifyType = componentFactory.createLabel("Notification Type: ");
		notifyMail = new JCheckBox("Email");
		notifyPost = new JCheckBox("Postal Service");
		panel.add(notifyType);
		panel.add(notifyMail, "split 2");
		panel.add(notifyPost, "wrap");
		
		note = componentFactory.createLabel("Notiz: ");
		noteText = new StringTextField(ComponentConstants.LONG_TEXT);
		panel.add(note);
		panel.add(noteText, "wrap, growx");
		panel.add(empty, "wrap");
		validateablePersonComponents.add(noteText);

/**
* Next section
*/
		
		separator = componentFactory.createSeparator();
		overviewPanel.add(separator, "wrap, growx");
		
		donationPanel = componentFactory.createPanel(800, 450);
		overviewPanel.add(donationPanel);
		
		addDonation = componentFactory.createLabel("Neue Spende anlegen");
		addDonation.setFont(new Font("Headline", Font.PLAIN, 14));
		donationPanel.add(addDonation, "wrap");
		
		String[] donationString = new String[]{"\u00DCberweisung", "Merchandise", "Online-Shop", "Bar", "SMS"};
		donationCombo = new JComboBox<String>(donationString);
		donationLabel = componentFactory.createLabel("Spende durch: ");
		donationPanel.add(donationLabel);
		donationPanel.add(donationCombo, "split 2");
		amount = new NumericTextField(ComponentConstants.SHORT_TEXT);
		donationPanel.add(amount, "wrap, growx");
		validateableDonationComponents.add(amount);
		
		dateLabel = componentFactory.createLabel("Spendendatum: ");
		dateInstruction = componentFactory.createLabel("(YYYY-MM-DD)");
		dateInstruction.setFont(new Font("Instruction", Font.PLAIN, 9));
		dateField = componentFactory.createTextField(30);
		donationPanel.add(dateLabel, "split 2");
		donationPanel.add(dateInstruction);
		donationPanel.add(dateField, "wrap, growx");
		
		dedicationLabel = componentFactory.createLabel("Widmung: ");
		dedicationField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		validateableDonationComponents.add(dedicationField);
		dedicationNoteLabel = componentFactory.createLabel("Notiz: ");
		dedicationNoteField = new StringTextField(ComponentConstants.LONG_TEXT);
		validateableDonationComponents.add(dedicationNoteField);

		donationPanel.add(dedicationLabel);
		donationPanel.add(dedicationField, "wrap, growx");
		donationPanel.add(dedicationNoteLabel);
		donationPanel.add(dedicationNoteField, "wrap, growx");
		
		donationPanel.add(empty, "wrap");
		
		donationPanel.add(ok, "split 2");
		donationPanel.add(cancel, "wrap");
	}
	
	private final class SubmitAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			boolean personValidated = true;
			
			for(ValidateableComponent comp : validateablePersonComponents) {
				if(!comp.validateContents())
					personValidated = false;
			}
			
			if(personValidated) {
				person = new Person();
				address = new Address();
				
				person.setSex(Person.Sex.values()[salutation.getSelectedIndex()]);
				
				String title = (String) titleBox.getSelectedItem();
				person.setTitle(title);
				person.setCompany(companyField.getText());
				person.setGivenName(givenField.getText());
				person.setSurname(surnameField.getText());
				person.setTelephone(telephoneField.getText());
				person.setEmail(emailField.getText());
				
				address.setStreet(streetField.getText());
				address.setPostalCode(postalField.getText());
				address.setCity(cityField.getText());
				address.setCountry(countryField.getText());
	
				person.setMainAddress(address);
				
				if(notifyMail.isSelected()){
					person.setEmailNotification(true);
				}
				if(notifyPost.isSelected()){
					person.setPostalNotification(true);
				}
				
				String note = noteText.getText();
				person.setNote(note);
			}
			
			boolean donationValidated = true;
			
			for(ValidateableComponent comp : validateableDonationComponents) {
				if(!comp.validateContents())
					donationValidated = false;
			}
			
			if(donationValidated) {
				donation = new Donation();
				
				donation.setType(Donation.DonationType.values()[donationCombo.getSelectedIndex()]);
				
				long d_amount;
				if(amount.getText() == null || amount.getText().isEmpty()){
					d_amount = 0;
				}
				else{
					try{
						d_amount = Long.parseLong(amount.getText());
					}
					catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(null, "Bitte nur Zahlen eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
						amount.requestFocus();
						return;
					}
				}
			
				donation.setAmount(d_amount);
				
				//TODO datepicker
				if(dateField.getText().isEmpty() || dateField.getText().equals(null)){
					dateField.setText("2000-01-01");
	//				JOptionPane.showMessageDialog(this, "Bitte Datum eingeben.", "Warn", JOptionPane.WARNING_MESSAGE);
	//				dateField.requestFocus();
	//				return;
				}
			
				donation.setDate(Date.valueOf(dateField.getText()));
				
				donation.setDedication(dedicationField.getText());
				
				donation.setNote(dedicationNoteField.getText());
			}
			
			if(personValidated) {
				if(person != null) {
					try {
						Address createdAddress = addressService.create(address); // will now be created when person is created - p
						List<Address> addresses = new ArrayList<Address>();
						addresses.add(createdAddress);
						person.setAddresses(addresses);
					
					
						personService.create(person);
					
					
						if(donation != null) {
							donation.setDonator(person);
							donationService.create(donation);
						}
						
						personModel.addPerson(person);
					} catch (ServiceException e1) {
						JOptionPane.showConfirmDialog(null, "An error occured while trying to create a person. Message was "+e1.getMessage());
					}
				}
			}
		}
	}
	
	
    private final class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO go back to overview
		}
		
	}
}

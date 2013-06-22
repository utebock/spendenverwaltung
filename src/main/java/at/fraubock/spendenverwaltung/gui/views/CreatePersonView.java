package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.EmailTextField;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.gui.components.interfaces.ValidateableComponent;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

/**
 * 
 * @author Cornelia Hasil, Chris Steele
 *
 */
public class CreatePersonView extends InitializableView {
	
	private static final Logger log = Logger.getLogger(CreatePersonView.class);

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;

	private ViewActionFactory viewActionFactory;
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

	private JLabel company;
	private StringTextField companyField;
	
	private JLabel given_name;
	private StringTextField givenField;
	
	private JLabel surname;
	private StringTextField surnameField;
	
	private JLabel telephone;
	private NumericTextField telephoneField;
	
	private JLabel mail;
	private EmailTextField emailField;
	
	private JLabel street;
	private StringTextField streetField;
	
	private JLabel postal;
	private StringTextField postalField;
	
	private JLabel city;
	private StringTextField cityField;
	
	private JLabel country;
	private StringTextField countryField;
	
	private JLabel notifyType;
	private JCheckBox notifyMail;
	private JCheckBox notifyPost;
	
	private JLabel note;
	
	private StringTextField noteText; 
	
	private JLabel donationLabel;
	private JComboBox<String> donationCombo;
	private NumericTextField amount;
	
	private JButton submit;
	private JButton cancel;
	
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
	private JXDatePicker datePicker;
	private JPanel overviewPanel;

	private StringTextField titleField;

	private JLabel amountLabel;

	public CreatePersonView(ComponentFactory componentFactory, ViewActionFactory viewActionFactory, 
			IPersonService personService, IAddressService addressService,
			IDonationService donationService, PersonTableModel personModel) {
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.personModel = personModel;
		
		setUpCreate();
	}
	
	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	public void setDonationService(IDonationService donationService) {
		this.donationService = donationService;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}
	
	public void setViewActionFactory(ViewActionFactory viewActionFactory) {
		this.viewActionFactory = viewActionFactory;
	}

	public void setPersonModel(PersonTableModel personModel) {
		this.personModel = personModel;
	}
	
	private void setUpCreate() {
		overviewPanel = componentFactory.createPanel(700, 850);
		//JScrollPane pane = new JScrollPane(overviewPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(overviewPanel);

		panel = componentFactory.createPanel(700,500);
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
		titleField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(title);
		panel.add(titleField, "wrap");
		
		company = componentFactory.createLabel("Firma: ");
		companyField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(company);
		panel.add(companyField, "wrap, growx");
		validateablePersonComponents.add(companyField);
		
		given_name = componentFactory.createLabel("Vorname: ");
		givenField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(given_name);
		panel.add(givenField, "wrap, growx");
		validateablePersonComponents.add(givenField);
		
		surname = componentFactory.createLabel("Nachname: ");
		surnameField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
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
		streetField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(street);
		panel.add(streetField, "growx, wrap");
		validateablePersonComponents.add(streetField);

		postal = componentFactory.createLabel("PLZ: ");
		postalField = new StringTextField(ComponentConstants.SHORT_TEXT);
		panel.add(postal);
		panel.add(postalField, "wrap, growx");
		validateablePersonComponents.add(postalField);
		
		city = componentFactory.createLabel("Ort: ");
		cityField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(city);
		panel.add(cityField, "wrap, growx");
		validateablePersonComponents.add(cityField);
		
		country = componentFactory.createLabel("Land: ");
		countryField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		panel.add(country);
		panel.add(countryField, "wrap, growx");
		validateablePersonComponents.add(countryField);

		notifyType = componentFactory.createLabel("Benachrichtigungstyp: ");
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
		
		donationCombo = new JComboBox<String>(donationService.getDonationTypes());
		donationLabel = componentFactory.createLabel("Spende durch: ");
		donationPanel.add(donationLabel, "split2");
		donationPanel.add(donationCombo, "gap 75, wrap, growx");
		
		amountLabel = componentFactory.createLabel("Betrag: ");
		amount = new NumericTextField(ComponentConstants.SHORT_TEXT);
		donationPanel.add(amountLabel, "split2");
		donationPanel.add(amount, "gap 125, wrap, growx");
		validateableDonationComponents.add(amount);
		
		dateLabel = componentFactory.createLabel("Spendendatum: ");
		datePicker = new JXDatePicker(new java.util.Date());
		donationPanel.add(dateLabel, "split2");
		donationPanel.add(datePicker, "gap 70, wrap, growx");
		
		dedicationLabel = componentFactory.createLabel("Widmung: ");
		dedicationField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		validateableDonationComponents.add(dedicationField);
		dedicationNoteLabel = componentFactory.createLabel("Notiz: ");
		dedicationNoteField = new StringTextField(ComponentConstants.LONG_TEXT);
		validateableDonationComponents.add(dedicationNoteField);

		donationPanel.add(dedicationLabel, "split2");
		donationPanel.add(dedicationField, "gap 105, wrap, growx");
		donationPanel.add(dedicationNoteLabel, "split2");
		donationPanel.add(dedicationNoteField, "gap 129, wrap, growx");
		
		donationPanel.add(empty, "wrap");
		
		submit = new JButton();
		cancel = new JButton();
		
		donationPanel.add(submit, "split 2");
		donationPanel.add(cancel, "wrap");
		init();
	}
	
	public void init() {
		SubmitAction submitAction = new SubmitAction();
		submitAction.putValue(Action.NAME, "Anlegen");
		submit.setAction(submitAction);
		
		Action cancelAction = viewActionFactory.getMainMenuViewAction();
		cancelAction.putValue(Action.NAME, "Abbrechen");
		cancelAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backInButton.png")));
		cancel.setAction(cancelAction);
		cancel.setFont(new Font("bigger", Font.PLAIN, 13));
	}
	
	private final class SubmitAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			boolean personValidated = true;
			boolean donationValidated = true;
			
			for(ValidateableComponent comp : validateablePersonComponents) {
				if(!comp.validateContents())
					personValidated = false;
			}
			
			if(!personValidated) {
				JOptionPane.showMessageDialog(null, "Personenfelder konnten nicht validiert werden");
				return;
			} else {
				person = new Person();
				
				address = new Address();
				
				person.setSex(Person.Sex.values()[salutation.getSelectedIndex()]);
				person.setTitle(titleField.getText());
				person.setCompany(companyField.getText());
				person.setGivenName(givenField.getText());
				person.setSurname(surnameField.getText());
				person.setTelephone(telephoneField.getText());
				person.setEmail(emailField.getText());
				
				if(streetField.getText().equals("") && postalField.getText().equals("")
						&& cityField.getText().equals("") && countryField.getText().equals("")) {
					address = null;
				} else if(streetField.getText().equals("") || postalField.getText().equals("")
						|| cityField.getText().equals("") || countryField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Eine Adresse darf entweder ganz oder garnicht gesetzt sein.");
					return;
				} else {
					address.setStreet(streetField.getText());
					address.setPostalCode(postalField.getText());
					address.setCity(cityField.getText());
					address.setCountry(countryField.getText());
					person.setMainAddress(address);
				}
				
				if(notifyMail.isSelected()==true){
					person.setEmailNotification(true);
				}
				else{
					person.setEmailNotification(false);
				}
				if(notifyPost.isSelected()==true){
					person.setPostalNotification(true);
				}
				else{
					person.setPostalNotification(false);
				}
				
				String note = noteText.getText();
				person.setNote(note);
			}
						
			for(ValidateableComponent comp : validateableDonationComponents) {
				
				//this ONLY happens if something has been entered in the donation field
				if(!comp.validateContents()) {
					donationValidated = false;
					JOptionPane.showMessageDialog(null, "Konnte Spendenh\u00F6he nicht feststellen");
					return;
				}
					
			}
			
			if(donationValidated) {
				donation = new Donation();
				
				donation.setType(Donation.DonationType.values()[donationCombo.getSelectedIndex()]);
				
				Long d_amount = null;
				
				if(!amount.getText().isEmpty()) { 
					d_amount = amount.getHundredths();
					donation.setAmount(d_amount);
					donation.setDate(datePicker.getDate());
					donation.setDedication(dedicationField.getText());	
					donation.setNote(dedicationNoteField.getText());
				} else {
					donation = null;
				}
			}
			
			if(personValidated) {
				if(person != null) {
					try {
						if(address != null) {
							Address createdAddress = addressService.create(address); // will now be created when person is created - p
							List<Address> addresses = new ArrayList<Address>();
							addresses.add(createdAddress);
							person.setAddresses(addresses);
						}
					
						personService.create(person);
						personModel.addPerson(person);
					
						if(donation != null) {
							donation.setDonator(person);
							log.info("Creating donation");
							donationService.create(donation);
						}
						
						Action switchToMenu = viewActionFactory.getMainMenuViewAction();
						switchToMenu.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

					} catch (ServiceException e1) {
						JOptionPane.showConfirmDialog(null, "Ein Fehler ist beim Erstellen der Person aufgetreten.");
					}
				}
			}
		}
	}
}

package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.gui.views.FindPersonsView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
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
	private ViewActionFactory viewActionFactory;
	private JPanel panel;
	private JComboBox<String> salutation;
	private JLabel salutLabel;
	private AddressTableModel addressModel;
	private JLabel title;
	private JComboBox<String> titleBox;
	
	private JLabel company;
	
	private JLabel given_name;
	
	private JLabel surname;
	private JLabel telephone;
	
	private JLabel mail;
	private StringTextField streetField;
	
	private NumericTextField postalField;
	
	private StringTextField cityField;
	
	private StringTextField countryField;
	
	private JLabel notifyType;
	private JCheckBox notifyMail;
	private JCheckBox notifyPost;
	
	private JLabel note;
	private StringTextField noteArea; 
	
	private JButton ok;
	private JButton cancel;
	private PersonTableModel personModel;
	private Person person = new Person();
	private Address addr = new Address();
	@SuppressWarnings("unused")
	private FindPersonsView filterPersons;
	private ComponentFactory componentFactory;
	private JLabel editPerson;
	private StringTextField companyField;
	private StringTextField givenField;
	private StringTextField surnameField;
	private StringTextField telephoneField;
	private StringTextField mailField;
	private JTable addressTable;
	private JScrollPane addressPane;
	private JPanel tablePanel;
	private JPanel overviewPanel;
	private JSeparator separator;
	private JLabel editAdress;
	private List<Address> addressList;
	private JPanel editAddressPanel;
	private JLabel addressStreetLabel;
	private JLabel addressPostalLabel;
	private JLabel addressCityLabel;
	private JLabel addressCountryLabel;
	private JButton ok_addr;
	private JButton delete_addr;
	private JLabel setAsMainAddress;
	private JCheckBox mainAddress;
	private JButton cancelAddr;

	public EditPerson(ComponentFactory componentFactory, ViewActionFactory viewActionFactory, 
			Person person, IPersonService personService, IAddressService addressService, FindPersonsView filterPersons, PersonTableModel personModel) {
		super(new MigLayout());
		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.person = person;
		this.personService = personService;
		this.addressService = addressService;
		this.filterPersons = filterPersons;
		this.personModel = personModel;
		addressModel = new AddressTableModel();
		setUp();
	}
	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
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
	
	public void setFindPersonsView(FindPersonsView filterPersons) {
		this.filterPersons = filterPersons;
	}
	
	public void setPerson(Person person){
		this.person = person;
	}
	
	public void setUp(){
		
		overviewPanel = componentFactory.createPanel(700, 800);
		this.add(overviewPanel);
		
		panel = componentFactory.createPanel(700, 300);
		overviewPanel.add(panel, "wrap");
		editPerson = componentFactory.createLabel("Personendaten \u00E4ndern: ");
		editPerson.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(editPerson, "wrap");
		JLabel empty = componentFactory.createLabel("		");
		panel.add(empty, "wrap");
		
		String[] salutCombo = new String[]{"Herr", "Frau", "Fam.", "Firma"};
		salutation = new JComboBox<String>(salutCombo);
		salutLabel = componentFactory.createLabel("Anrede: ");
		
		if (person.getSex().equals(Person.Sex.MALE)){
			salutation.setSelectedItem("Herr");
		}
		if (person.getSex().equals(Person.Sex.FEMALE)){
			salutation.setSelectedItem("Frau");
		}
		if (person.getSex().equals(Person.Sex.FAMILY)){
			salutation.setSelectedItem("Fam.");
		}
		if (person.getSex().equals(Person.Sex.COMPANY)){
			salutation.setSelectedItem("Firma");
		}
		
		panel.add(salutLabel);
		panel.add(salutation, "wrap");
		
		
		title = componentFactory.createLabel("Titel: ");
		String[] titleCombo = new String[]{"-", "BA", "BSc", "DI", "Dr.", "Ing.", "MA", "Mag.", "MSc.", "Prof."};
		titleBox = new JComboBox<String>(titleCombo);
		
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
		
		company = componentFactory.createLabel("Firma: ");
		companyField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		companyField.setText(person.getCompany());
		panel.add(company);
		panel.add(companyField, "wrap, growx");
		
		given_name = componentFactory.createLabel("Vorname: ");
		givenField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		givenField.setText(person.getGivenName());
		panel.add(given_name);
		panel.add(givenField, "wrap, growx");
		
		surname = componentFactory.createLabel("Nachname: ");
		surnameField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		surnameField.setText(person.getSurname());
		panel.add(surname);
		panel.add(surnameField, "wrap, growx");
		
		telephone = componentFactory.createLabel("Telefon: ");
		telephoneField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		telephoneField.setText(person.getTelephone());
		panel.add(telephone);
		panel.add(telephoneField, "wrap, growx");
		
		mail = componentFactory.createLabel("E-Mail: ");
		mailField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
		mailField.setText(person.getEmail());
		panel.add(mail);
		panel.add(mailField, "wrap, growx");
		
		notifyType = componentFactory.createLabel("Notification Type: ");
		notifyMail = new JCheckBox("Email");
		notifyPost = new JCheckBox("Postal Service");
		
		if(person.isEmailNotification() == true){
			notifyMail.setSelected(true);
		}
		if(person.isPostalNotification() == true){
			notifyPost.setSelected(true);
		}
		
		panel.add(notifyType);
		panel.add(notifyMail, "split 2");
		panel.add(notifyPost, "wrap");
		
		note = componentFactory.createLabel("Notiz: ");
		noteArea = new StringTextField(ComponentConstants.LONG_TEXT);
		panel.add(note);
		panel.add(noteArea, "wrap, growx");
		
		EditPersonAction okAction = new EditPersonAction();
		okAction.putValue(Action.NAME, "Bearbeiten");
		ok = new JButton();
		ok.setAction(okAction);
		panel.add(ok, "split 2");
		
		cancel = new JButton();
		Action getBack = viewActionFactory.getFindPersonsView();
		getBack.putValue(Action.NAME, "Abbrechen");
		cancel.setAction(getBack);
		panel.add(cancel, "wrap");
		
		separator = componentFactory.createSeparator();
		overviewPanel.add(separator, "growx, wrap");
		
		tablePanel = componentFactory.createPanel(700, 200);
		overviewPanel.add(tablePanel, "wrap");
		
		editAdress = componentFactory.createLabel("Adressdaten \u00E4ndern: ");
		editAdress.setFont(new Font("Headline", Font.PLAIN, 14));
		tablePanel.add(editAdress, "wrap");
		empty = componentFactory.createLabel("		");
		tablePanel.add(empty, "wrap");
		
		addressTable = new JTable(addressModel);
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addressPane = new JScrollPane(addressTable);
		addressPane.setPreferredSize(new Dimension(700, 250));
		tablePanel.add(addressPane, "wrap, growx");
		
		EditAddressAction okAddressAction = new EditAddressAction();
		okAddressAction.putValue(Action.NAME, "Bearbeiten");
		ok_addr = new JButton();
		ok_addr.setAction(okAddressAction);
		tablePanel.add(ok_addr, "split 3");
		
		DeleteAddressAction deleteAction = new DeleteAddressAction();
		deleteAction.putValue(Action.NAME, "L\u00F6schen");
		delete_addr = new JButton();
		delete_addr.setAction(deleteAction);
		tablePanel.add(delete_addr);
		
		cancelAddr = new JButton();
		cancelAddr.setAction(getBack);
		tablePanel.add(cancelAddr, "wrap");
		
		getAddresses();	
		
	}
	
	private void getAddresses(){
		addressList = new ArrayList<Address>();

		addressList = person.getAddresses();
		if(addressList == null){
			JOptionPane.showMessageDialog(overviewPanel, "Addresslist returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Address a : addressList){
			addressModel.addAddress(a);
			addressTable.revalidate();
			addressTable.repaint();
		}	
	}
	
	private final class EditPersonAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			person.setSex(Person.Sex.values()[salutation.getSelectedIndex()]);
			
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
		
			person.setCompany(companyField.getText());
			
			person.setGivenName(givenField.getText());
			
			person.setSurname(surnameField.getText());
			
			person.setTelephone(telephoneField.getText());
			
			person.setEmail(mailField.getText());
			
			if(notifyMail.isSelected()){
				person.setEmailNotification(true);
			}
			if(notifyPost.isSelected()){
				person.setPostalNotification(true);
			}
			String note = noteArea.getText();
			person.setNote(note);
			
			try{
				Person updatedPerson = personService.update(person);
				personModel.addPerson(updatedPerson);
			}
			catch(ServiceException ex){
				 JOptionPane.showMessageDialog(overviewPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
						  "Fehler", JOptionPane.ERROR_MESSAGE);
		            ex.printStackTrace();
		    		return;
			}
			JOptionPane.showMessageDialog(overviewPanel, "Person erfolgreich bearbeitet.", "Information", JOptionPane.INFORMATION_MESSAGE);
			Action switchToMenu = viewActionFactory.getFindPersonsViewAction();
			switchToMenu.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			
		}
	}

	private final class DeleteAddressAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int row = addressTable.getSelectedRow();
			if(row == -1){
				JOptionPane.showMessageDialog(overviewPanel, "Bitte Adresse ausw\u00E4hlen.");
				return;
			}
			int id = (Integer) addressModel.getValueAt(row, 4);
			
			try{
				addr = addressService.getByID(id);
			}
			catch(ServiceException ex){
	            JOptionPane.showMessageDialog(overviewPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
						  "Fehler", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
				return;
	        }
			

			Object[] options = {"Abbrechen","L\u00F6schen"};
			int ok = JOptionPane.showOptionDialog(overviewPanel, "Diese Adresse sicher l\u00F6schen?", "Loeschen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
			
			if(ok == 1){
				try {
					List<Address> updatedAddresses = person.getAddresses();
					updatedAddresses.remove(addr);
					person.setAddresses(updatedAddresses);
					personService.deleteAddressAndUpdatePerson(addr, person);
				} 
				catch (ServiceException ex) {
					JOptionPane.showMessageDialog(overviewPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
							  "Fehler", JOptionPane.ERROR_MESSAGE);
		            ex.printStackTrace();
					return;
				}
					
				addressModel.removeAddress(row);
				JOptionPane.showMessageDialog(overviewPanel, "Adresse wurde gel\u00F6scht.", "Information", JOptionPane.INFORMATION_MESSAGE);
				
			}
			else{
				return;
			}
			
			
			
		}
	}

	private final class EditAddressAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int row = addressTable.getSelectedRow();
			if(row == -1){
				JOptionPane.showMessageDialog(overviewPanel, "Bitte Adresse ausw\u00E4hlen.");
				return;
			}
			int id = (Integer) addressModel.getValueAt(row, 4);
			
			try{
				addr = addressService.getByID(id);
			}
			catch(ServiceException ex){
	            JOptionPane.showMessageDialog(overviewPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
						  "Fehler", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
				return;
	        }
			
			editAddressPanel = componentFactory.createPanel(500, 200);
			
			addressStreetLabel = componentFactory.createLabel("Stra\u00DFe: ");
			streetField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			streetField.setText(addr.getStreet());
			editAddressPanel.add(addressStreetLabel, "");
			editAddressPanel.add(streetField, "wrap 0px, growx");

			addressPostalLabel = componentFactory.createLabel("PLZ: ");
			postalField = new NumericTextField(ComponentConstants.SHORT_TEXT);
			postalField.setText(addr.getPostalCode());
			editAddressPanel.add(addressPostalLabel, "");
			editAddressPanel.add(postalField, "wrap 0px, growx");

			addressCityLabel = componentFactory.createLabel("Ort: ");
			cityField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			cityField.setText(addr.getCity());
			editAddressPanel.add(addressCityLabel, "");
			editAddressPanel.add(cityField, "wrap 0px, growx");

			addressCountryLabel = componentFactory.createLabel("Land: ");
			countryField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			countryField.setText(addr.getCountry());
			editAddressPanel.add(addressCountryLabel, "");
			editAddressPanel.add(countryField, "wrap 0px, growx");
			
			setAsMainAddress = componentFactory.createLabel("Erstwohnsitz: ");
			mainAddress = componentFactory.createCheckbox();
			if(person.getMainAddress().equals(addr)){
				mainAddress.setSelected(true);
			}
			else{
				mainAddress.setSelected(false);
			}
			editAddressPanel.add(setAsMainAddress);
			editAddressPanel.add(mainAddress, "wrap 0px");
			
			final JComponent[] editAddress = new JComponent[]{editAddressPanel};
			Object[] options = {"Abbrechen", "Bearbeiten"};
			int go = JOptionPane.showOptionDialog(overviewPanel, editAddress, "Adresse bearbeiten", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
			
			if(go == 1){
				if(streetField.getText().isEmpty() || streetField.getText().equals(null)){
					addr.setStreet(addr.getStreet());
				}
				else{
					if(streetField.validateContents()==true){
						addr.setStreet(streetField.getText());
					}else{
						JOptionPane.showMessageDialog(overviewPanel, "Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe", "Warnung", JOptionPane.WARNING_MESSAGE);
						EditAddressAction action = new EditAddressAction();
						action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
					}
				}
				if(postalField.getText().isEmpty() || postalField.getText().equals(null)){
					addr.setPostalCode(addr.getPostalCode());
				}
				else{
					if(postalField.validateContents()==true){
						addr.setPostalCode(postalField.getText());
					}
					else{
						JOptionPane.showMessageDialog(overviewPanel, "Bitte korrigieren Sie Ihre Eingabe: PLZ", "Warnung", JOptionPane.WARNING_MESSAGE);
						EditAddressAction action = new EditAddressAction();
						action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
					}
				}
				
				if(cityField.getText().isEmpty() || cityField.getText().equals(null)){
					addr.setCity(addr.getCity());
				}
				else{
					if(cityField.validateContents()==true){
						addr.setCity(cityField.getText());
					}
					else{
						JOptionPane.showMessageDialog(overviewPanel, "Bitte korrigieren Sie Ihre Eingabe: Ort", "Warnung", JOptionPane.WARNING_MESSAGE);
						EditAddressAction action = new EditAddressAction();
						action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
					}
				}
				
				if(countryField.getText().isEmpty() || countryField.getText().equals(null)){
					addr.setCountry(addr.getCountry());
				}
				else{
					if(countryField.validateContents()==true){
						addr.setCountry(countryField.getText());
					}else{
						JOptionPane.showMessageDialog(overviewPanel, "Bitte korrigieren Sie Ihre Eingabe: Land", "Warnung", JOptionPane.WARNING_MESSAGE);
						EditAddressAction action = new EditAddressAction();
						action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
					}
					
				}
				
				if(mainAddress.isSelected() == false){
					person.setMainAddress(person.getMainAddress());
				}
				else{
					person.setMainAddress(addr);
				}
				try{
					Address updatedAddress = addressService.update(addr);
					Address removeAddress = new Address();
					List<Address> addresses = person.getAddresses();
					
					for(Address a : addresses){
						if(a.getId() == addr.getId())
							removeAddress = a;
					}
					addresses.remove(removeAddress);
					addresses.add(updatedAddress);
					person.setAddresses(addresses);
					personService.update(person);
					addressModel.removeAll();
					addressModel.insertList(addresses);
				}
				catch(ServiceException ex){
					 JOptionPane.showMessageDialog(overviewPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
							  "Fehler", JOptionPane.ERROR_MESSAGE);
			            ex.printStackTrace();
			    		return;
				}
				
				JOptionPane.showMessageDialog(overviewPanel, "Adresse erfolgreich bearbeitet.", "Information", JOptionPane.INFORMATION_MESSAGE);
				addressTable.revalidate();
				addressTable.repaint();
		}
		else{
			return;
		}
	}
	
	}
}

package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	private JComboBox<String[]> salutation;
	private JLabel salutLabel;
	private AddressTableModel addressModel;
	private JLabel title;
	private JComboBox<String> titleBox;
	
	private JLabel company;
	
	private JLabel given_name;
	
	private JLabel surname;
	private JLabel telephone;
	
	private JLabel mail;
	private JTextField streetField;
	
	private JTextField postalField;
	
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
	private ShowPersons filterPersons;
	private JLabel editPerson;
	private JTextField companyField;
	private JTextField givenField;
	private JTextField surnameField;
	private JTextField telephoneField;
	private JTextField mailField;
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

	public EditPerson(Person person, IPersonService personService, IAddressService addressService, ShowPersons filterPersons, Overview overview) {
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
		addressModel = new AddressTableModel();
		setUp();
	}

	@SuppressWarnings("unchecked")
	public void setUp(){
		
		overviewPanel = builder.createPanel(800, 800);
		//JScrollPane pane = new JScrollPane(overviewPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(overviewPanel);
		
		panel = builder.createPanel(800, 300);
		overviewPanel.add(panel, "wrap");
		editPerson = builder.createLabel("Personendaten \u00E4ndern: ");
		editPerson.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(editPerson, "wrap");
		JLabel empty = builder.createLabel("		");
		panel.add(empty, "wrap");
		
		String[] salutCombo = new String[]{"Herr", "Frau", "Fam.", "Firma"};
		salutation = builder.createComboBox(salutCombo, actionHandler);
		salutLabel = builder.createLabel("Anrede: ");
		
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
		
		separator = builder.createSeparator();
		overviewPanel.add(separator, "growx, wrap");
		
		tablePanel = builder.createPanel(800, 200);
		overviewPanel.add(tablePanel, "wrap");
		
		editAdress = builder.createLabel("Adressdaten \u00E4ndern: ");
		editAdress.setFont(new Font("Headline", Font.PLAIN, 14));
		tablePanel.add(editAdress, "wrap");
		empty = builder.createLabel("		");
		tablePanel.add(empty, "wrap");
		
		addressTable = new JTable(addressModel);
		addressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addressPane = new JScrollPane(addressTable);
		addressPane.setPreferredSize(new Dimension(800, 250));
		tablePanel.add(addressPane, "wrap, growx");
		
		ok_addr = builder.createButton("Bearbeiten", buttonListener, "edit_address_in_db");
		tablePanel.add(ok_addr, "split 3");
		delete_addr = builder.createButton("L\u00F6schen", buttonListener, "delete_address_in_db");
		tablePanel.add(delete_addr);
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_edit");
		tablePanel.add(cancel, "wrap");
		
		
		getAddresses();	
		
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
			addressTable.repaint();
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
	
	public void editPerson() {
		
		
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
		catch(ServiceException e){
			 JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
	    		return;
		}
		JOptionPane.showMessageDialog(this, "Person erfolgreich bearbeitet", "Information", JOptionPane.INFORMATION_MESSAGE);
		returnTo();
	}

	public void editAddress() {
		
		int row = addressTable.getSelectedRow();
		if(row == -1){
			JOptionPane.showMessageDialog(this, "Bitte Adresse ausw\u00E4hlen.");
			return;
		}
		int id = (Integer) addressModel.getValueAt(row, 4);
		
		try{
			addr = addressService.getByID(id);
		}
		catch(ServiceException e){
            JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
			return;
        }
		
		editAddressPanel = builder.createPanel(500, 200);
		
		addressStreetLabel = builder.createLabel("Stra\u00DFe: ");
		streetField = builder.createTextField(150);
		streetField.setText(addr.getStreet());
		editAddressPanel.add(addressStreetLabel, "");
		editAddressPanel.add(streetField, "wrap 0px, growx");

		addressPostalLabel = builder.createLabel("PLZ: ");
		postalField = builder.createTextField(30);
		postalField.setText(addr.getPostalCode());
		editAddressPanel.add(addressPostalLabel, "");
		editAddressPanel.add(postalField, "wrap 0px, growx");

		addressCityLabel = builder.createLabel("Ort: ");
		cityField = builder.createTextField(150);
		cityField.setText(addr.getCity());
		editAddressPanel.add(addressCityLabel, "");
		editAddressPanel.add(cityField, "wrap 0px, growx");

		addressCountryLabel = builder.createLabel("Land: ");
		countryField = builder.createTextField(150);
		countryField.setText(addr.getCountry());
		editAddressPanel.add(addressCountryLabel, "");
		editAddressPanel.add(countryField, "wrap 0px, growx");
		
		setAsMainAddress = builder.createLabel("Erstwohnsitz: ");
		mainAddress = builder.createCheckbox();
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
		int go = JOptionPane.showOptionDialog(this, editAddress, "Adresse bearbeiten", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
		if(go == 1){
			if(streetField.getText().isEmpty() || streetField.getText().equals(null)){
				addr.setStreet(addr.getStreet());
			}
			else{
				addr.setStreet(streetField.getText());
			}
			if(postalField.getText().isEmpty() || postalField.getText().equals(null)){
				addr.setPostalCode(addr.getPostalCode());
			}
			else{
				addr.setPostalCode(postalField.getText());
			}
			
			if(cityField.getText().isEmpty() || cityField.getText().equals(null)){
				addr.setCity(addr.getCity());
			}
			else{
				addr.setCity(cityField.getText());
			}
			
			if(countryField.getText().isEmpty() || countryField.getText().equals(null)){
				addr.setCountry(addr.getCountry());
			}
			else{
				addr.setCountry(countryField.getText());
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
			catch(ServiceException e){
				 JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		            e.printStackTrace();
		    		return;
			}
			
			JOptionPane.showMessageDialog(this, "Adresse erfolgreich bearbeitet.", "Information", JOptionPane.INFORMATION_MESSAGE);
			addressTable.revalidate();
			addressTable.repaint();
		}
	}
	
	public void deleteAddress(){
		
		int row = addressTable.getSelectedRow();
		if(row == -1){
			JOptionPane.showMessageDialog(this, "Bitte Adresse ausw\u00E4hlen.");
			return;
		}
		int id = (Integer) addressModel.getValueAt(row, 4);
		
		try{
			addr = addressService.getByID(id);
		}
		catch(ServiceException e){
            JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Fehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
			return;
        }
		

		Object[] options = {"Abbrechen","L\u00F6schen"};
		int ok = JOptionPane.showOptionDialog(this, "Diese Adresse sicher l\u00F6schen?", "Loeschen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
		if(ok == 1){
			try {
				List<Address> updatedAddresses = person.getAddresses();
				updatedAddresses.remove(addr);
				person.setAddresses(updatedAddresses);
				personService.deleteAddressAndUpdatePerson(addr, person);
			} 
			catch (ServiceException e) {
				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
				return;
			}
				
			addressModel.removeAddress(row);
			JOptionPane.showMessageDialog(this, "Adresse wurde gel\u00F6scht.", "Information", JOptionPane.INFORMATION_MESSAGE);
			
		}
		else{
			return;
		}
	}
}

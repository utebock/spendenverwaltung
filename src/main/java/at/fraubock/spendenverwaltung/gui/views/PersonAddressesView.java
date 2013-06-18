package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.AddressTableModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.NumericTextField;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class PersonAddressesView extends InitializableView {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PersonAddressesView.class);
	
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;
	
	private JPanel contentPanel;
	
	//show feedback messages on the bottom of the view with this label
	private JLabel feedbackLabel, mainAddressLabel;
	
	private JToolBar toolbar;
	private JTable addressesTable;
	private JButton addAddress, editAddress, deleteAddress, backButton;
	
	private IPersonService personService;
	private IAddressService addressService;
	
	//selected in parent view
	private Person selectedPerson;
	//keep state of parent component tablemodel until view switch
	private PersonTableModel personTableModel;
	
	private AddressTableModel addressTableModel;
	
	public PersonAddressesView (ViewActionFactory viewActionFactory, ComponentFactory componentFactory,
			IPersonService personService, IAddressService addressService, Person selectedPerson, PersonTableModel personTableModel) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.personService = personService;
		this.addressService = addressService;
		this.selectedPerson = selectedPerson;
		this.personTableModel = personTableModel;
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
		
		addressesTable = new JTable(addressTableModel);
		addressesTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(addressesTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
		
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "wrap, growx");

		mainAddressLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Headline", Font.PLAIN, 16));
		contentPanel.add(mainAddressLabel);
		
		contentPanel.add(scrollPane, "wrap, growx");
		
		addressesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Headline", Font.PLAIN, 16));
		contentPanel.add(feedbackLabel);
	}
	
	@Override
	public void init() {
		initTable();
		addComponentsToToolbar(toolbar);
	}
	
	public void initTable() {
		addressesTable = new JTable(addressTableModel);
		
		if(selectedPerson != null) {
			if(selectedPerson.getMainAddress() != null) {
				mainAddressLabel.setText(selectedPerson.getMainAddress().toString());
			} else {
				mainAddressLabel.setText("Keine Hauptaddresse vorhanden");
			}
			
			for(Address address : selectedPerson.getAddresses()) {
				addressTableModel.addAddress(address);	
			}
		}
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		
		backButton = new JButton();
		Action getBack = viewActionFactory.getFindPersonsViewAction(personTableModel);
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.jpg")));
		backButton.setAction(getBack);
		
		addAddress = new JButton();
		addAddress.setFont(new Font("Bigger", Font.PLAIN, 13));
		AddAction addAction = new AddAction();
		addAddress.setAction(addAction);

		editAddress = new JButton();
		editAddress.setFont(new Font("Bigger", Font.PLAIN, 13));
		EditAction editAction = new EditAction();
		editAddress.setAction(editAction);

		deleteAddress = new JButton();
		deleteAddress.setFont(new Font("Bigger", Font.PLAIN, 13));
		DeleteAction deleteAction = new DeleteAction();
		deleteAddress.setAction(deleteAction);
		
		toolbar.add(backButton, "split 4, growx");
		toolbar.add(addAddress, "growx");
		toolbar.add(editAddress, "growx");
		toolbar.add(deleteAddress, "growx");
		
	}
	
	private final class AddAction extends AbstractAction{
		private static final long serialVersionUID = 1L;

		private JFrame addAddressFrame;
		private JPanel addAddressPanel;
		private JLabel addressStreetLabel;
		private StringTextField streetField;
		
		private JLabel addressPostalLabel;
		private NumericTextField postalField;

		private JLabel addressCityLabel;
		private StringTextField cityField;
		
		private JLabel addressCountryLabel;
		private StringTextField countryField;
		
		private JLabel validationFeedbackLabel;
		private JButton submitButton;
		private JButton cancelButton;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			addAddressFrame = new JFrame("Neue Adresse");
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
			
			cancelButton.setAction(new CancelAction());
			addAddressPanel.add(cancelButton, "split 2");
			
			submitButton.setAction(new SubmitAction());
			addAddressPanel.add(submitButton, "wrap");
			
			validationFeedbackLabel = componentFactory.createLabel("");
			addAddressPanel.add(validationFeedbackLabel);
			
			addAddressFrame.add(addAddressPanel);
			addAddressFrame.pack();
			addAddressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addAddressFrame.setVisible(true);
			}
		
			private final class SubmitAction extends AbstractAction {
				
				private static final long serialVersionUID = 1L;

				public SubmitAction() {
					super("Anlegen");
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean validationSucceeded = true;
					
					Address address = new Address();
					if(streetField.validateContents()==false){
						validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe");
						validationSucceeded = false;
					}
					if(postalField.validateContents()==false){
						validationFeedbackLabel.setText("Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: PLZ");
						validationSucceeded = false;
					}
					if(cityField.validateContents()==false){
						validationFeedbackLabel.setText("Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: Ort");
						validationSucceeded = false;
					}
					if(countryField.validateContents()==false){
						validationFeedbackLabel.setText("Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: Land");
						validationSucceeded = false;
					}
					
					if(validationSucceeded)
						address.setStreet(streetField.getText());
						address.setPostalCode(postalField.getText());
						address.setCity(cityField.getText());
						address.setCountry(countryField.getText());
						
						try{
							Address createdAddress = addressService.create(address);
							addressTableModel.addAddress(createdAddress);
							
							List<Address> addressesList = selectedPerson.getAddresses();
							addressesList.add(createdAddress);
							selectedPerson.setAddresses(addressesList);
							personService.update(selectedPerson);
							
							feedbackLabel.setText("Adresse erfolgreich angelegt.");
							addAddressFrame.dispose();
						}
						catch(ServiceException ex){
							log.warn(ex.getLocalizedMessage()); 
							validationFeedbackLabel.setText("Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.");
						}
				}
			}

			private final class CancelAction extends AbstractAction {
				
				private static final long serialVersionUID = 1L;
				
				public CancelAction() {
					super("Abbrechen");
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					addAddressFrame.dispose();
				}	
			}
	}
	
	private final class DeleteAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public DeleteAction() {
			super("Adresse löschen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = addressesTable.getSelectedRow();
			if(row != -1) {
				Address address = addressTableModel.getAddressRow(row);
				int dialogResult = JOptionPane.showConfirmDialog (contentPanel, "Wollen sie diese Adresse wirklich löschen?", "Löschen", JOptionPane.YES_NO_OPTION);

				if(dialogResult == JOptionPane.YES_OPTION) {
					try {
						List<Address> updatedAddresses = selectedPerson.getAddresses();
						updatedAddresses.remove(address);
						selectedPerson.setAddresses(updatedAddresses);
						personService.deleteAddressAndUpdatePerson(address, selectedPerson);
						
						addressTableModel.removeAddress(row);
						feedbackLabel.setText("Addresse wurde gelöscht");
					} 
					catch (ServiceException ex) {
						log.warn(ex.getLocalizedMessage());
						feedbackLabel.setText("Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.");
					}
				}
			} else {
				feedbackLabel.setText("Bitte Adresse ausw\u00E4hlen.");
			}
		}
	}
	
	private final class EditAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		
		private JFrame editAddressFrame;
		private JPanel editAddressPanel;
		private JLabel addressStreetLabel;
		private StringTextField streetField;
		
		private JLabel addressPostalLabel;
		private NumericTextField postalField;

		private JLabel addressCityLabel;
		private StringTextField cityField;
		
		private JLabel addressCountryLabel;
		private StringTextField countryField;
		
		private JLabel setAsMainAddress;
		private JCheckBox mainAddress;
		
		private JLabel validationFeedbackLabel;
		private JButton submitButton;
		private JButton cancelButton;
		
		Address address;

		public EditAction() {
			super("Adresse bearbeiten");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = addressesTable.getSelectedRow();
			
			//avoid costs of initializing the frame if no address has been selected
			if(row == -1){
				feedbackLabel.setText("Bitte die Adresse zum bearbeiten ausw\u00E4hlen.");
				return;
			}
			
			address = addressTableModel.getAddressRow(row);
			
			editAddressFrame = new JFrame("Adresse bearbeiten");
			editAddressPanel = componentFactory.createPanel(500, 200);
			
			addressStreetLabel = componentFactory.createLabel("Stra\u00DFe: ");
			streetField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			streetField.setText(address.getStreet());
			editAddressPanel.add(addressStreetLabel, "");
			editAddressPanel.add(streetField, "wrap 0px, growx");

			addressPostalLabel = componentFactory.createLabel("PLZ: ");
			postalField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			postalField.setText(address.getPostalCode());
			editAddressPanel.add(addressPostalLabel, "");
			editAddressPanel.add(postalField, "wrap 0px, growx");

			addressCityLabel = componentFactory.createLabel("Ort: ");
			cityField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			cityField.setText(address.getCity());
			editAddressPanel.add(addressCityLabel, "");
			editAddressPanel.add(cityField, "wrap 0px, growx");

			addressCountryLabel = componentFactory.createLabel("Land: ");
			countryField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			countryField.setText(address.getCountry());
			editAddressPanel.add(addressCountryLabel, "");
			editAddressPanel.add(countryField, "wrap 0px, growx");
			
			setAsMainAddress = componentFactory.createLabel("Erstwohnsitz: ");
			mainAddress = componentFactory.createCheckbox();
			
			if(selectedPerson.getMainAddress().equals(address)){
				mainAddress.setSelected(true);
			}
			else{
				mainAddress.setSelected(false);
			}
			
			editAddressPanel.add(setAsMainAddress);
			editAddressPanel.add(mainAddress, "wrap 0px");
			
			cancelButton.setAction(new CancelAction());
			editAddressPanel.add(cancelButton, "split 2");
			
			submitButton.setAction(new SubmitAction());
			editAddressPanel.add(submitButton, "wrap");
			
			editAddressFrame.add(editAddressPanel);
			editAddressFrame.pack();
			editAddressFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			editAddressFrame.setVisible(true);
		}
		
		private final class CancelAction extends AbstractAction {
			
			private static final long serialVersionUID = 1L;
			
			public CancelAction() {
				super("Abbrechen");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				editAddressFrame.dispose();
			}	
		}
		
		private final class SubmitAction extends AbstractAction {
			
			private static final long serialVersionUID = 1L;
			
			public SubmitAction() {
				super("Speichern");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean validation = true;
				
				if(address == null) {
					feedbackLabel.setText("Addresse war nicht gesetzt beim bearbeiten");
					editAddressFrame.dispose();
				}
		
				if(streetField.validateContents()){
					address.setStreet(streetField.getText());
				} else{
					validation = false;
					validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe");
				}
				
				if(postalField.validateContents()){
					address.setPostalCode(postalField.getText());
				}
				else{
					validation = false;
					validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: PLZ");
				}
			
				if(cityField.validateContents()){
					address.setCity(cityField.getText());
				}
				else{
					validation = false;
					validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Ort");
				}

				if(countryField.validateContents()){
					address.setCountry(countryField.getText());
				} else{
					validation = false;
					validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Land");
				}
			
			if(mainAddress.isSelected()){
				selectedPerson.setMainAddress(address);
			}
			
			if(validation) { 
				try{
					address = addressService.update(address);
				
					personService.update(selectedPerson);
					addressTableModel.fireTableDataChanged();
					
					feedbackLabel.setText("Adresse erfolgreich geändert.");
					editAddressFrame.dispose();
				} catch(ServiceException ex){
					log.warn(ex.getLocalizedMessage());
					validationFeedbackLabel.setText("Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.");
				}
			}
		}
		}
	}
}

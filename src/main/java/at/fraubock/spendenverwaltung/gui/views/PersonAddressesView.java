/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.AddressTableModel;
import at.fraubock.spendenverwaltung.gui.CellRenderer;
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
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
		
		addressTableModel = new AddressTableModel();
		addressesTable = new JTable(addressTableModel);
		addressesTable.setAutoCreateRowSorter(true);
		addressesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addressesTable.setFillsViewportHeight(true);
		addressesTable.setDefaultRenderer(Object.class, new CellRenderer());
		
		JScrollPane scrollPane = new JScrollPane(addressesTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "wrap 20px, growx");

		mainAddressLabel = componentFactory.createLabel("");
		mainAddressLabel.setFont(new Font("Headline", Font.PLAIN, 14));
		contentPanel.add(mainAddressLabel, "wrap 20px");
		
		contentPanel.add(scrollPane, "wrap, growx");
		
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
		if(selectedPerson != null) {
			if(selectedPerson.getMainAddress() != null) {
				mainAddressLabel.setText("Hauptadresse: "+selectedPerson.getMainAddress().getStreet() + " " +
						selectedPerson.getMainAddress().getPostalCode() + " " +
						selectedPerson.getMainAddress().getCity() + " " +
						selectedPerson.getMainAddress().getCountry());
			} else {
				mainAddressLabel.setText("Keine Hauptaddresse vorhanden!");
			}
			
			for(Address address : selectedPerson.getAddresses()) {
				addressTableModel.addAddress(address);	
			}
		}
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		
		backButton = new JButton();
		Action getBack = viewActionFactory.getFindPersonsViewAction(personTableModel);
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.png")));
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
		
		public AddAction() {
			super("<html>&nbsp;Neue Adresse anlegen<html>");
		}

		private JDialog addAddressDialog;
		private JPanel addAddressPanel;
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
		
		@Override
		public void actionPerformed(ActionEvent e) {
			addAddressDialog = new JDialog(SwingUtilities.getWindowAncestor(contentPanel), Dialog.ModalityType.APPLICATION_MODAL);
			addAddressDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			addAddressPanel = componentFactory.createPanel(350, 250);
			
			addressStreetLabel = componentFactory.createLabel("Stra\u00DFe: ");
			streetField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			
			addAddressPanel.add(addressStreetLabel, "split 2");
			addAddressPanel.add(streetField, "gap 47, wrap, growx");

			addressPostalLabel = componentFactory.createLabel("PLZ: ");
			postalField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			addAddressPanel.add(addressPostalLabel, "split 2");
			addAddressPanel.add(postalField, "gap 63, wrap, growx");

			addressCityLabel = componentFactory.createLabel("Ort: ");
			cityField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			addAddressPanel.add(addressCityLabel, "split 2");
			addAddressPanel.add(cityField, "gap 65, wrap, growx");

			addressCountryLabel = componentFactory.createLabel("Land: ");
			countryField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			
			addAddressPanel.add(addressCountryLabel, "split 2");
			addAddressPanel.add(countryField, "gap 55, wrap, growx");
			
			setAsMainAddress = componentFactory.createLabel("Erstwohnsitz: ");
			mainAddress = componentFactory.createCheckbox();
			mainAddress.setSelected(false);
			
			addAddressPanel.add(setAsMainAddress, "split 2");
			addAddressPanel.add(mainAddress, "wrap 20px");
			
			cancelButton = new JButton();
			cancelButton.setAction(new CancelAction());
			
			submitButton = new JButton();
			submitButton.setAction(new SubmitAction());
			addAddressPanel.add(submitButton, "split 2");
			addAddressPanel.add(cancelButton, "wrap");
			
			validationFeedbackLabel = componentFactory.createLabel("");
			addAddressPanel.add(validationFeedbackLabel);
			
			addAddressDialog.add(addAddressPanel);
			addAddressDialog.pack();
			addAddressDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(contentPanel));
			addAddressDialog.setVisible(true);
			
			}
		
			private final class SubmitAction extends AbstractAction {
				
				private static final long serialVersionUID = 1L;

				public SubmitAction() {
					super("Speichern");
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean validationSucceeded = true;
					
					Address address = new Address();
					if(!streetField.validateContents()){
						//validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe");
						JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe", "Warnung", JOptionPane.WARNING_MESSAGE);
						validationSucceeded = false;
					}
					if(!postalField.validateContents()){
						//validationFeedbackLabel.setText("Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: PLZ");
						JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: PLZ", "Warnung", JOptionPane.WARNING_MESSAGE);
						validationSucceeded = false;
					}
					if(!cityField.validateContents()){
						//validationFeedbackLabel.setText("Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: Ort");
						JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: Ort", "Warnung", JOptionPane.WARNING_MESSAGE);
						validationSucceeded = false;
					}
					if(!countryField.validateContents()){
						//validationFeedbackLabel.setText("Bitte \u00FCberpr\u00FCfen Sie Ihre Eingabe: Land");
						JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: Land", "Warnung", JOptionPane.WARNING_MESSAGE);
						validationSucceeded = false;
					}
					
					if(validationSucceeded) {
						
						address.setStreet(streetField.getText());
						address.setPostalCode(postalField.getText());
						address.setCity(cityField.getText());
						address.setCountry(countryField.getText());
						
						try{
							address = addressService.create(address);
							
							addressTableModel.addAddress(address);
							
							//TODO this results in two main addresses...
							
							List<Address> addressesList = selectedPerson.getAddresses();
							addressesList.add(address);
							selectedPerson.setAddresses(addressesList);
							
							if(mainAddress.isSelected()){
								selectedPerson.setMainAddress(address);
							}
							
							personService.update(selectedPerson);
							
							//feedbackLabel.setText("Adresse erfolgreich angelegt.");
							
							mainAddressLabel.setText("Hauptadresse: "+selectedPerson.getMainAddress().getStreet() + " " +
									selectedPerson.getMainAddress().getPostalCode() + " " +
									selectedPerson.getMainAddress().getCity() + " " +
									selectedPerson.getMainAddress().getCountry());
							addAddressDialog.dispose();
							JOptionPane.showMessageDialog(contentPanel, "Adresse erfolgreich angelegt.", "Information", JOptionPane.INFORMATION_MESSAGE);
						}
						catch(ServiceException ex){
							log.warn(ex.getLocalizedMessage()); 
						//	validationFeedbackLabel.setText("Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.");
							JOptionPane.showMessageDialog(contentPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
						}
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
					addAddressDialog.dispose();
				}	
			}
	}
	
	private final class DeleteAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public DeleteAction() {
			super("<html>&nbsp;Adresse l\u00F6schen</html>");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = addressesTable.getSelectedRow();
			if(row != -1) {
				Address address = addressTableModel.getAddressRow(row);
				int dialogResult = JOptionPane.showConfirmDialog (contentPanel, "Wollen Sie diese Adresse wirklich l\u00F6schen?", "L\u00F6schen", JOptionPane.YES_NO_OPTION);

				if(dialogResult == JOptionPane.YES_OPTION) {
					try {
						List<Address> updatedAddresses = selectedPerson.getAddresses();
						updatedAddresses.remove(address);
						selectedPerson.setAddresses(updatedAddresses);
						personService.deleteAddressAndUpdatePerson(address, selectedPerson);
						
						addressTableModel.removeAddress(row);
						//feedbackLabel.setText("Addresse wurde gel\u00F6scht.");
						JOptionPane.showMessageDialog(contentPanel, "Addresse wurde gel\u00F6scht.", "Information", JOptionPane.INFORMATION_MESSAGE);
					} 
					catch (ServiceException ex) {
						log.warn(ex.getLocalizedMessage());
						//feedbackLabel.setText("Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.");
						JOptionPane.showMessageDialog(contentPanel, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				//feedbackLabel.setText("Bitte Adresse ausw\u00E4hlen.");
				JOptionPane.showMessageDialog(contentPanel, "Bitte Adresse ausw\u00E4hlen.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	private final class EditAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		
		private JDialog editAddressDialog;
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
			super("<html>&nbsp;Adresse bearbeiten</html>");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int row = addressesTable.getSelectedRow();
			
			//avoid costs of initializing the frame if no address has been selected
			if(row == -1){
			//	feedbackLabel.setText("Bitte die Adresse zum bearbeiten ausw\u00E4hlen.");
				JOptionPane.showMessageDialog(contentPanel, "Bitte Adresse ausw\u00E4hlen.", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			address = addressTableModel.getAddressRow(row);
			
			editAddressDialog = new JDialog(SwingUtilities.getWindowAncestor(contentPanel), Dialog.ModalityType.APPLICATION_MODAL);
			editAddressDialog.setLocation(300, 300);
			editAddressPanel = componentFactory.createPanel(350, 250);
			
			addressStreetLabel = componentFactory.createLabel("Stra\u00DFe: ");
			streetField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			streetField.setText(address.getStreet());
			editAddressPanel.add(addressStreetLabel, "split2");
			editAddressPanel.add(streetField, "gap 47, wrap, growx");

			addressPostalLabel = componentFactory.createLabel("PLZ: ");
			postalField = new NumericTextField(ComponentConstants.SHORT_TEXT, false);
			postalField.setText(address.getPostalCode());
			editAddressPanel.add(addressPostalLabel, "split 2");
			editAddressPanel.add(postalField, "gap 63, wrap, growx");

			addressCityLabel = componentFactory.createLabel("Ort: ");
			cityField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			cityField.setText(address.getCity());
			editAddressPanel.add(addressCityLabel, "split2");
			editAddressPanel.add(cityField, "gap 65, wrap, growx");

			addressCountryLabel = componentFactory.createLabel("Land: ");
			countryField = new StringTextField(ComponentConstants.MEDIUM_TEXT, false);
			countryField.setText(address.getCountry());
			editAddressPanel.add(addressCountryLabel, "split2");
			editAddressPanel.add(countryField, "gap 55, wrap, growx");
			
			setAsMainAddress = componentFactory.createLabel("Erstwohnsitz: ");
			mainAddress = componentFactory.createCheckbox();
			
			if(selectedPerson.getMainAddress().equals(address)){
				mainAddress.setSelected(true);
			}
			else{
				mainAddress.setSelected(false);
			}
			
			editAddressPanel.add(setAsMainAddress, "split2");
			editAddressPanel.add(mainAddress, "wrap 20px");
			
			cancelButton = new JButton();
			cancelButton.setAction(new CancelEditAction());
			
			submitButton = new JButton();
			submitButton.setAction(new SubmitEditAction());
			editAddressPanel.add(submitButton, "split2");
			editAddressPanel.add(cancelButton, "wrap");
			
			validationFeedbackLabel = componentFactory.createLabel("");
			editAddressPanel.add(validationFeedbackLabel, "wrap");
			
			editAddressDialog.add(editAddressPanel);
			editAddressDialog.pack();
			editAddressDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(contentPanel));
			editAddressDialog.setVisible(true);
		}
		
		private final class CancelEditAction extends AbstractAction {
			
			private static final long serialVersionUID = 1L;
			
			public CancelEditAction() {
				super("Abbrechen");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				editAddressDialog.dispose();
			}	
		}
		
		private final class SubmitEditAction extends AbstractAction {
			
			private static final long serialVersionUID = 1L;
			
			public SubmitEditAction() {
				super("Speichern");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean validation = true;
				
				if(address == null) {
					//feedbackLabel.setText("Addresse war nicht gesetzt beim bearbeiten");
					editAddressDialog.dispose();
					JOptionPane.showMessageDialog(contentPanel, "Addresse beim Bearbeiten nicht gesetzt.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
		
				if(streetField.validateContents()){
					address.setStreet(streetField.getText());
				} else{
					validation = false;
					//validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe");
					JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: Stra\u00DFe", "Warnung", JOptionPane.WARNING_MESSAGE);
				}
				
				if(postalField.validateContents()){
					address.setPostalCode(postalField.getText());
				}
				else{
					validation = false;
					//validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: PLZ");
					JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: PLZ", "Warnung", JOptionPane.WARNING_MESSAGE);
				}
			
				if(cityField.validateContents()){
					address.setCity(cityField.getText());
				}
				else{
					validation = false;
					//validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Ort");
					JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: Ort", "Warnung", JOptionPane.WARNING_MESSAGE);
				}

				if(countryField.validateContents()){
					address.setCountry(countryField.getText());
				} else{
					validation = false;
					//validationFeedbackLabel.setText("Bitte korrigieren Sie Ihre Eingabe: Land");
					JOptionPane.showMessageDialog(contentPanel, "Bitte korrigieren Sie Ihre Eingabe: Land", "Warnung", JOptionPane.WARNING_MESSAGE);
				}
			
				if(validation) { 
				try{
					
					if(mainAddress.isSelected()){
						selectedPerson.setMainAddress(address);
					}
					
					address = addressService.update(address);
				
					personService.update(selectedPerson);
					addressTableModel.fireTableDataChanged();
					
					//feedbackLabel.setText("Adresse erfolgreich ge\u00E4ndert.");
					editAddressDialog.dispose();
					JOptionPane.showMessageDialog(contentPanel, "Adresse erfolgreich ge\u00E4ndert.", "Information", JOptionPane.INFORMATION_MESSAGE);
					
					mainAddressLabel.setText("Hauptadresse: "+selectedPerson.getMainAddress().getStreet() + " " +
							selectedPerson.getMainAddress().getPostalCode() + " " +
							selectedPerson.getMainAddress().getCity() + " " +
							selectedPerson.getMainAddress().getCountry());
				} catch(ServiceException ex){
					log.warn(ex.getLocalizedMessage());
					//validationFeedbackLabel.setText("Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.");
					JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		}
	}
}

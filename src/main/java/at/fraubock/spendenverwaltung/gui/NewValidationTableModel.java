package at.fraubock.spendenverwaltung.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListDataListener;
import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.ImportValidator.ValidationType;

/**
 * 
 * @author thomas
 *
 */
public class NewValidationTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{ "Vorname", "Nachname", "Telefonnummer", "E-Mail Adresse", "Straße", "PLZ", "Ort", "Land", "Betrag", "Datum", "Widmung", "Typ", "Notiz" };
	private Vector<Donation> donations = new Vector<Donation>();
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private ImportValidation parent;
	
	public NewValidationTableModel(IPersonService personService, IAddressService addressService, IDonationService donationService, ImportValidation parent){
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.parent = parent;
	}
	
	public void addEntry (Donation donation){
		donations.add(donation);
	}
	
	public void addList (List<Donation> donationList){
		for(int i=0; i<donationList.size(); i++){
			addEntry(donationList.get(i));
		}
	}
	
	public void removeDonation (int row){
		donations.remove(row);
		fireTableDataChanged();
	}
	
	public Donation getDonationRow(int rowIndex){
		return donations.get(rowIndex);
	}

	public List<Donation> getDonationList(){
		return donations;
	}
	
	@Override
	public int getRowCount() {
		return donations.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Donation donation = (Donation)donations.get(rowIndex);
		Person person = donation.getDonator();
		
		switch(columnIndex){
			case 0: return person == null ? "-" : person.getGivenName();
			case 1: return person == null ? "-" : person.getSurname();
			case 2: return person == null ? "-" : person.getTelephone();
			case 3: return person == null ? "-" : person.getEmail();
			case 4: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getStreet();
			case 5: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getPostalCode();
			case 6: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getCity();
			case 7: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getCountry();
			case 8: return donation.getAmount();
			case 9: return donation.getDate();
			case 10: return donation.getDedication();
			case 11: return donation.getType();
			case 12: return donation.getNote();
			default: return null;
		}
	}
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Donation updateDonation = donations.get(rowIndex);
        Person updatePerson = updateDonation.getDonator();
        Address updateAddress = updatePerson.getMainAddress();
        
        if(columnIndex != 13){
	        switch(columnIndex){
	        	case 0: updatePerson.setGivenName((String) value);
	        			break;
	        	case 1: updatePerson.setSurname((String) value);
						break;
	        	case 2: updatePerson.setTelephone((String) value);
						break;
	        	case 3: updatePerson.setEmail((String) value);
						break;
	        	case 4: updateAddress.setStreet((String) value);
						break;
	        	case 5: updateAddress.setPostalCode((String) value);
						break;
	        	case 6: updateAddress.setCity((String) value);
						break;
	        	case 7: updateAddress.setCountry((String) value);
						break;
	        	case 8: updateDonation.setAmount((Long) value);
						break;
	        	case 9: updateDonation.setDate((Date) value);
						break;
	        	case 10: updateDonation.setDedication((String) value);
						break;
	        	case 11: updateDonation.setType((Donation.DonationType) value);
						break;
	        	case 12: updateDonation.setNote((String) value);
						break;
	        }
	        
	        boolean changedModel = false;
	        
			try {
				List<Person> matchedPersons = personService.getByAttributes(updatePerson);
				if(matchedPersons.size() != 0)
					updateDonation.setDonator(matchedPersons.get(0));
				if(donationService.donationExists(updateDonation)){
					//add to conflictModel
					removeDonation(rowIndex);
					parent.addToConflict(updateDonation);
					changedModel = true;
				} else if (matchedPersons.size() != 0){
					//add to matchModel
					removeDonation(rowIndex);
					parent.addToMatch(updateDonation);
					changedModel = true;
				}
				
				//updateDonation = donationService.update(updateDonation);
				//updatePerson = personService.update(updatePerson);
				//updateAddress = addressService.update(updateAddress);
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(parent, "Updating Row failed", "Error", JOptionPane.ERROR_MESSAGE);
		        e.printStackTrace();
		        return;
			}
		    
			//if donation didn't change model, update row in this model
			if(!changedModel){
				donations.set(rowIndex, updateDonation);
				
		        fireTableCellUpdated(rowIndex, columnIndex);
			}
        }
    }
	
	public Class<?> getColumnClass(int col) {
		
		switch (col) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			case 4: return String.class;
			case 5: return String.class;
			case 6: return String.class;
			case 7: return String.class;
			case 8: return Long.class;
			case 9: return Date.class;
			case 10: return String.class;
			case 11: return Donation.DonationType.class;
			case 12: return String.class;
			default: return null;
		}
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
}




//package at.fraubock.spendenverwaltung.gui;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Vector;
//
//import javax.swing.ComboBoxModel;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.event.ListDataListener;
//import javax.swing.table.AbstractTableModel;
//
//import at.fraubock.spendenverwaltung.interfaces.domain.Address;
//import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
//import at.fraubock.spendenverwaltung.interfaces.domain.Person;
//import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
//import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
//import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
//import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
//import at.fraubock.spendenverwaltung.util.ImportValidator.ValidationType;
//
///**
// * 
// * @author thomas
// *
// */
//public class NewValidationTableModel extends AbstractTableModel{
//
//	private static final long serialVersionUID = 1L;
//	
//	private String[] columnNames = new String[]{ "Vorname", "Nachname", "Telefonnummer", "E-Mail Adresse", "Straße", "PLZ", "Ort", "Land", "Betrag", "Datum", "Widmung", "Typ", "Notiz" };
//	private Vector<Donation> donations = new Vector<Donation>();
//	private Vector<Person> persons = new Vector<Person>();
//	private Vector<Address> addresses = new Vector<Address>();
//	private IPersonService personService;
//	private IAddressService addressService;
//	private IDonationService donationService;
//	private ImportValidation parent;
//	
//	public NewValidationTableModel(IPersonService personService, IAddressService addressService, IDonationService donationService, ImportValidation parent){
//		this.personService = personService;
//		this.addressService = addressService;
//		this.donationService = donationService;
//		this.parent = parent;
//	}
//	
//	public void addEntry (Donation donation, Person person){
//		donations.add(donation);
//		
//		addresses.add(person.getMainAddress());
//		persons.add(person);
//	}
//	
//	public void addList (List<Donation> donationList, List<Person> personList){
//		for(int i=0; i<donationList.size(); i++){
//			addEntry(donationList.get(i), personList.get(i));
//		}
//	}
//	
//	public void removeDonation (int row){
//		donations.remove(row);
//		persons.remove(row);
//		fireTableDataChanged();
//	}
//	
//	public Donation getDonationRow(int rowIndex){
//		return donations.get(rowIndex);
//	}
//	
//	public Person getPersonRow(int rowIndex){
//		return persons.get(rowIndex);
//	}
//
//	public List<Donation> getDonationList(){
//		return donations;
//	}
//	
//	@Override
//	public int getRowCount() {
//		return donations.size();
//	}
//	
//	@Override
//	public Object getValueAt(int rowIndex, int columnIndex) {
//		Donation donation = (Donation)donations.get(rowIndex);
//		Person person = (Person)persons.get(rowIndex);
//		
//		switch(columnIndex){
//			case 0: return person == null ? "-" : person.getGivenName();
//			case 1: return person == null ? "-" : person.getSurname();
//			case 2: return person == null ? "-" : person.getTelephone();
//			case 3: return person == null ? "-" : person.getEmail();
//			case 4: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getStreet();
//			case 5: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getPostalCode();
//			case 6: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getCity();
//			case 7: return (person == null || person.getAddresses().isEmpty()) ? "-" : person.getMainAddress().getCountry();
//			case 8: return donation.getAmount();
//			case 9: return donation.getDate();
//			case 10: return donation.getDedication();
//			case 11: return donation.getType();
//			case 12: return donation.getNote();
//			default: return null;
//		}
//	}
//    public void setValueAt(Object value, int rowIndex, int columnIndex) {
//        Donation updateDonation = donations.get(rowIndex);
//        Person updatePerson = persons.get(rowIndex);
//        Address updateAddress = addresses.get(rowIndex);
//        
//        if(columnIndex != 13){
//	        switch(columnIndex){
//	        	case 0: updatePerson.setGivenName((String) value);
//	        			break;
//	        	case 1: updatePerson.setSurname((String) value);
//						break;
//	        	case 2: updatePerson.setTelephone((String) value);
//						break;
//	        	case 3: updatePerson.setEmail((String) value);
//						break;
//	        	case 4: updateAddress.setStreet((String) value);
//						break;
//	        	case 5: updateAddress.setPostalCode((String) value);
//						break;
//	        	case 6: updateAddress.setCity((String) value);
//						break;
//	        	case 7: updateAddress.setCountry((String) value);
//						break;
//	        	case 8: updateDonation.setAmount((Long) value);
//						break;
//	        	case 9: updateDonation.setDate((Date) value);
//						break;
//	        	case 10: updateDonation.setDedication((String) value);
//						break;
//	        	case 11: updateDonation.setType((Donation.DonationType) value);
//						break;
//	        	case 12: updateDonation.setNote((String) value);
//						break;
//	        }
//	        
//	        boolean changedModel = false;
//	        
//			try {
//				List<Person> matchedPersons = personService.getByAttributes(updatePerson);
//				if(matchedPersons.size() != 0)
//					updateDonation.setDonator(matchedPersons.get(0));
//				if(donationService.donationExists(updateDonation)){
//					//add to conflictModel
//					removeDonation(rowIndex);
//					parent.addToConflict(updateDonation);
//					changedModel = true;
//				} else if (matchedPersons.size() != 0){
//					//add to matchModel
//					removeDonation(rowIndex);
//					parent.addToMatch(updateDonation);
//					changedModel = true;
//				}
//				
//				//updateDonation = donationService.update(updateDonation);
//				//updatePerson = personService.update(updatePerson);
//				//updateAddress = addressService.update(updateAddress);
//			} catch (ServiceException e) {
//				JOptionPane.showMessageDialog(parent, "Updating Row failed", "Error", JOptionPane.ERROR_MESSAGE);
//		        e.printStackTrace();
//		        return;
//			}
//		    
//			//if donation didn't change model, update row in this model
//			if(!changedModel){
//				donations.set(rowIndex, updateDonation);
//				addresses.set(rowIndex, updateAddress);
//				persons.set(rowIndex, updatePerson);
//				
//		        fireTableCellUpdated(rowIndex, columnIndex);
//			}
//        }
//    }
//	
//	public Class<?> getColumnClass(int col) {
//		
//		switch (col) {
//			case 0: return String.class;
//			case 1: return String.class;
//			case 2: return String.class;
//			case 3: return String.class;
//			case 4: return String.class;
//			case 5: return String.class;
//			case 6: return String.class;
//			case 7: return String.class;
//			case 8: return Long.class;
//			case 9: return Date.class;
//			case 10: return String.class;
//			case 11: return Donation.DonationType.class;
//			case 12: return String.class;
//			default: return null;
//		}
//	}
//
//	@Override
//	public int getColumnCount() {
//		return columnNames.length;
//	}
//	
//	@Override
//	public String getColumnName(int col) {
//		return columnNames[col];
//	}
//
//	@Override
//	public boolean isCellEditable(int row, int col) {
//		return true;
//	}
//}
//

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
import javax.swing.table.DefaultTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.ImportValidator;
import at.fraubock.spendenverwaltung.util.ImportValidator.ValidationType;

/**
 * 
 * @author thomas
 *
 */
public class ValidationTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private String[] columnNames = new String[]{"Vorname", "Nachname", "Telefonnummer", "E-Mail Adresse", "Straße", "PLZ", "Ort", "Land", "Betrag", "Datum", "Widmung", "Typ", "Notiz", "Option"};
	private Vector<Donation> donations = new Vector<Donation>();
	private Vector<Person> persons = new Vector<Person>();
	private Vector<Address> addresses = new Vector<Address>();
	private IDonationService donationService;
	private IPersonService personService;
	private IAddressService addressService;
	private JPanel parent;
	private Vector<JComboBox> comboBoxes = new Vector<JComboBox>();
	private boolean editable;
	
	
	public static enum comboBoxOptions{ 
		FINISHED,
		EDIT,
		DELETE,
		ASSIGN
	};
	
	public ValidationTableModel(JPanel parent, IDonationService donationService, IPersonService personService, IAddressService addressService){
		this.parent = parent;
		this.personService = personService;
		this.donationService = donationService;
		this.addressService = addressService;
		this.editable = false;
	}
	
	public void addEntry (Donation donation, Person person){
		JComboBox cb = new JComboBox(ImportValidator.ValidationType.toArray());
		donations.add(donation);
		persons.add(person);
		if(person.getMainAddress() == null)
			addresses.add(null);
		else
			addresses.add(person.getMainAddress());
		comboBoxes.add(cb);
	}
	
	public void addList (List<Donation> donationList, List<Person> personList){
		for(int i=0; i<donationList.size(); i++){
			addEntry(donationList.get(i), personList.get(i));
		}
	}
	
	public void removeDonation (int row){
		donations.remove(row);
		persons.remove(row);
		fireTableDataChanged();
	}
	
	public Donation getDonationRow(int rowIndex){
		return donations.get(rowIndex);
	}
	
	public Person getPersonRow(int rowIndex){
		return persons.get(rowIndex);
	}

	@Override
	public int getRowCount() {
		return donations.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Donation donation = (Donation)donations.get(rowIndex);
		Person person = (Person)persons.get(rowIndex);
		JComboBox cb = (JComboBox)comboBoxes.get(rowIndex);
		
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
			case 13: return cb.getSelectedItem();
			default: return null;
		}
	}
	
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Donation updateDonation = donations.get(rowIndex);
        Person updatePerson = persons.get(rowIndex);
        Address updateAddress = addresses.get(rowIndex);
        
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
	        	case 4: removeAddressFromPerson(persons.get(rowIndex), addresses.get(rowIndex));
	        			updateAddress.setStreet((String) value);
	        			
	        			updatePerson.getAddresses().add(updateAddress);
	        			updatePerson.setAddresses(updatePerson.getAddresses());
	        			updatePerson.setMainAddress(updateAddress);
	        			
						break;
	        	case 5: removeAddressFromPerson(persons.get(rowIndex), addresses.get(rowIndex));
	        			updateAddress.setPostalCode((String) value);
    			
		    			updatePerson.getAddresses().add(updateAddress);
		    			updatePerson.setAddresses(updatePerson.getAddresses());
		    			updatePerson.setMainAddress(updateAddress);
    			
						break;
	        	case 6: removeAddressFromPerson(persons.get(rowIndex), addresses.get(rowIndex));
	        			updateAddress.setCity((String) value);
    			
		    			updatePerson.getAddresses().add(updateAddress);
		    			updatePerson.setAddresses(updatePerson.getAddresses());
		    			updatePerson.setMainAddress(updateAddress);
    			
						break;
	        	case 7: removeAddressFromPerson(persons.get(rowIndex), addresses.get(rowIndex));
	        			updateAddress.setCountry((String) value);
    			
		    			updatePerson.getAddresses().add(updateAddress);
		    			updatePerson.setAddresses(updatePerson.getAddresses());
		    			updatePerson.setMainAddress(updateAddress);
    			
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
	        
				try {
					updateDonation = donationService.update(updateDonation);
					updatePerson = personService.update(updatePerson);
					updateAddress = addressService.update(updateAddress);
				} catch (ServiceException e) {
					JOptionPane.showMessageDialog(parent, "Updating Row failed", "Error", JOptionPane.ERROR_MESSAGE);
			        e.printStackTrace();
			        return;
				}
			
			JOptionPane.showMessageDialog(parent, "Successfully updated row", "Information", JOptionPane.INFORMATION_MESSAGE);
		    
			donations.set(rowIndex, updateDonation);
			addresses.set(rowIndex, updateAddress);
			persons.set(rowIndex, updatePerson);
			
	        fireTableCellUpdated(rowIndex, columnIndex);
        }
        else{
        	JComboBox selectedBox = comboBoxes.get(rowIndex);
        	Person selectedPerson = persons.get(rowIndex);
        	Donation selectedDonation = donations.get(rowIndex);
        	
        	selectedBox.setSelectedItem((String) value);
        	
        	ValidationType selectedType = ValidationType.getByName((String) selectedBox.getSelectedItem());
        	
        	if(selectedType == ValidationType.EDIT){
        		editable = true;
        	} else if(selectedType == ValidationType.ANONYM){
        		selectedDonation.setDonator(null);
				try {
					donationService.update(selectedDonation);
				} catch (ServiceException e) {
					JOptionPane.showMessageDialog(parent, "Updating Row failed", "Error", JOptionPane.ERROR_MESSAGE);
			        e.printStackTrace();
			        return;
				}
        	} else if(selectedType == ValidationType.NEW_DONATOR){
        			//JDialog assignPerson = new AssignPerson(personService, addressService, donationService, this.p.parent);
        	} else{
        		editable = false;
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
			case 13: return ImportValidator.ValidationType.class;
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
		if(editable){
			return true;
		} else{
			return (col == 13);
		}
	}
	
	private void removeAddressFromPerson(Person p, Address a){
		List<Address> personAddresses = p.getAddresses();
		
		for(Address curAddress : personAddresses){
			if(curAddress.getId() == a.getId()){
				personAddresses.remove(curAddress);
				p.setAddresses(personAddresses);
			}
		}
	}
}


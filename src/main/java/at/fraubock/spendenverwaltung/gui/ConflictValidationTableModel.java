package at.fraubock.spendenverwaltung.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import at.fraubock.spendenverwaltung.gui.views.ImportValidationView;
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
public class ConflictValidationTableModel extends AbstractValidationTableModel {

	private static final long serialVersionUID = 1L;
	private List<Person> personsToDelete = new ArrayList<Person>();
	 
	public ConflictValidationTableModel(IPersonService personService, IAddressService addressService, IDonationService donationService, ImportValidationView parent){
		super(personService, addressService, donationService, parent);
	}
	
	public List<Person> getPersonsToDelete(){
		return personsToDelete;
	}
	
	public void setPersonIdToNull(){
		for(Donation d : donations){
			if(d.getDonator() != null)
				d.getDonator().setId(null);
		}
	}
	
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Donation updateDonation = donations.get(rowIndex);
        Person updatePerson = updateDonation.getDonator();
		Person oldDonator = new Person();
		Address updateAddress;

        
        if(columnIndex != 13){
			oldDonator.setId(updatePerson.getId());
			oldDonator.setAddresses(updatePerson.getAddresses());
			oldDonator.setCompany(updatePerson.getCompany());
			oldDonator.setSex(updatePerson.getSex());
			oldDonator.setSurname(updatePerson.getSurname());

			updateAddress = updatePerson.getMainAddress();
			
	        switch(columnIndex){
	        	case 0: updatePerson.setGivenName((String) value);
	        			updatePerson.setId(null);
	        			break;
	        	case 1: updatePerson.setSurname((String) value);
    			updatePerson.setId(null);
						break;
	        	case 2: updatePerson.setTelephone((String) value);
    			updatePerson.setId(null);
						break;
	        	case 3: updatePerson.setEmail((String) value);
    			updatePerson.setId(null);
						break;
	        	case 4: removeAddressFromPerson(donations.get(rowIndex).getDonator(), donations.get(rowIndex).getDonator().getMainAddress());
	        			updateAddress.setStreet((String) value);
	        			
	        			updatePerson.getAddresses().add(updateAddress);
	        			updatePerson.setAddresses(updatePerson.getAddresses());
	        			updatePerson.setMainAddress(updateAddress);

	        			updatePerson.setId(null);
						break;
	        	case 5: removeAddressFromPerson(donations.get(rowIndex).getDonator(), donations.get(rowIndex).getDonator().getMainAddress());
	        			updateAddress.setPostalCode((String) value);
    			
		    			updatePerson.getAddresses().add(updateAddress);
		    			updatePerson.setAddresses(updatePerson.getAddresses());
		    			updatePerson.setMainAddress(updateAddress);

	        			updatePerson.setId(null);
						break;
	        	case 6: removeAddressFromPerson(donations.get(rowIndex).getDonator(), donations.get(rowIndex).getDonator().getMainAddress());
	        			updateAddress.setCity((String) value);
    			
		    			updatePerson.getAddresses().add(updateAddress);
		    			updatePerson.setAddresses(updatePerson.getAddresses());
		    			updatePerson.setMainAddress(updateAddress);

	        			updatePerson.setId(null);
						break;
	        	case 7: removeAddressFromPerson(donations.get(rowIndex).getDonator(), donations.get(rowIndex).getDonator().getMainAddress());
	        			updateAddress.setCountry((String) value);
    			
		    			updatePerson.getAddresses().add(updateAddress);
		    			updatePerson.setAddresses(updatePerson.getAddresses());
		    			updatePerson.setMainAddress(updateAddress);

	        			updatePerson.setId(null);
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
				if(updateDonation.getDonator().getId() != null && donationService.donationExists(updateDonation)){
					changedModel = false;
				} else if (matchedPersons.size() == 0){
					updatePerson.setId(null);
					updateDonation.setDonator(updatePerson);
					removeDonation(rowIndex);
					parent.addToNew(updateDonation);
					changedModel = true;
				} else if (matchedPersons.size() != 0){
					//add to matchModel
					removeDonation(rowIndex);
					parent.addToMatch(updateDonation);
					changedModel = true;
				}
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
        else{
        	JComboBox selectedBox = comboBoxes.get(rowIndex);
        	
        	selectedBox.setSelectedItem((String) value);
        	
        	ValidationType selectedType = ValidationType.getByName((String) selectedBox.getSelectedItem());
        	
        	if(selectedType == ValidationType.NEW_DONATOR){
        			parent.openPersonDialog(donations.get(rowIndex), this);
        	} else if(selectedType == ValidationType.EDIT){
        		editable = true;
        	} else{
        		editable = false;
        	}
        	
        	fireTableDataChanged();
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

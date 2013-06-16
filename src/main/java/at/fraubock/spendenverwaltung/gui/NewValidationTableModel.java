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
public class NewValidationTableModel extends AbstractValidationTableModel {

	private static final long serialVersionUID = 1L;
	
	public NewValidationTableModel(IPersonService personService, IAddressService addressService, IDonationService donationService, ImportValidationView parent){
		super(personService, addressService, donationService, parent);
	}
	
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Donation updateDonation = donations.get(rowIndex);
        Person updatePerson = updateDonation.getDonator();
		Person oldDonator = new Person();
        Address updateAddress = updatePerson.getMainAddress();
        
        if(columnIndex != 13){
			oldDonator.setId(updatePerson.getId());
			oldDonator.setAddresses(updatePerson.getAddresses());
			oldDonator.setCompany(updatePerson.getCompany());
			oldDonator.setSex(updatePerson.getSex());
			oldDonator.setSurname(updatePerson.getSurname());

			updateAddress = updatePerson.getMainAddress();
			
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
}
package at.fraubock.spendenverwaltung.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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
public class MatchValidationTableModel extends AbstractValidationTableModel {

	private static final long serialVersionUID = 1L;
	
	public MatchValidationTableModel(IPersonService personService, IAddressService addressService, IDonationService donationService, ImportValidationView parent){
		super(personService, addressService, donationService, parent);
	}
	
	@Override
	public void addDonation (Donation donation){
		JComboBox cb = new JComboBox(ImportValidator.ValidationType.toArray());
		donations.add(donation);
		comboBoxes.add(cb);
	}
	
	@Override
	public void removeDonation (Donation d){
		comboBoxes.remove(donations.indexOf(d));
		donations.remove(d);
		fireTableDataChanged();
	}
	
	public void removeDonation (int row){
		donations.remove(row);
		comboBoxes.remove(row);
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
	
	public void removeAll(){
		donations = new Vector<Donation>();
		comboBoxes = new Vector<JComboBox>();
		fireTableDataChanged();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Donation donation = (Donation)donations.get(rowIndex);
		Person person = donation.getDonator();
		JComboBox cb = (JComboBox)comboBoxes.get(rowIndex);
		
		switch(columnIndex){
			case 0: return person == null ? "" : person.getGivenName();
			case 1: return person == null ? "" : person.getSurname();
			case 2: return person == null ? "" : person.getTelephone();
			case 3: return person == null ? "" : person.getEmail();
			case 4: return (person == null || person.getAddresses().isEmpty()) ? "" : person.getMainAddress().getStreet();
			case 5: return (person == null || person.getAddresses().isEmpty()) ? "" : person.getMainAddress().getPostalCode();
			case 6: return (person == null || person.getAddresses().isEmpty()) ? "" : person.getMainAddress().getCity();
			case 7: return (person == null || person.getAddresses().isEmpty()) ? "" : person.getMainAddress().getCountry();
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
				} else if (matchedPersons.size() == 0){
					//add to newModel and create new person if there is no match to another confirmed person
					updatePerson.setId(null);
					updateDonation.setDonator(updatePerson);
					removeDonation(rowIndex);
					parent.addToNew(updateDonation);
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

	@Override
	public void setComboBox(Donation d, ValidationType option) {
		comboBoxes.get(donations.indexOf(d)).setSelectedIndex(ValidationType.indexOf(option));
		fireTableDataChanged();
	}
	
	public List<Donation> getAnonymList(){
		List<Donation> anonymList = new ArrayList<Donation>();
		String currentType;
		
		for(int i=0; i<donations.size(); i++){
			currentType = (String) comboBoxes.get(i).getSelectedItem();
			if(ValidationType.getByName(currentType) == ValidationType.ANONYM){
				anonymList.add(donations.get(i));
			}
		}
		
		return anonymList;
	}
	
	public List<Donation> getNoImportList(){
		List<Donation> noImportList = new ArrayList<Donation>();
		String currentType;
		
		for(int i=0; i<donations.size(); i++){
			currentType = (String) comboBoxes.get(i).getSelectedItem();
			if(ValidationType.getByName(currentType) == ValidationType.NOT_IMPORT){
				noImportList.add(donations.get(i));
			}
		}
		
		return noImportList;
	}
}

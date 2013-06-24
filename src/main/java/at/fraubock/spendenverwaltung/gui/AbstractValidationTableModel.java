package at.fraubock.spendenverwaltung.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

import at.fraubock.spendenverwaltung.gui.views.ImportValidationView;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
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
public class AbstractValidationTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	protected String[] columnNames = new String[]{ "Vorname", "Nachname", "Telefonnummer", "E-Mail Adresse", "Straße", "PLZ", "Ort", "Land", "Betrag", "Datum", "Widmung", "Typ", "Notiz", "Option" };
	protected Vector<Donation> donations = new Vector<Donation>();
	protected Vector<JComboBox> comboBoxes = new Vector<JComboBox>();
	protected IPersonService personService;
	protected IDonationService donationService;
	protected ImportValidationView parent;
	protected boolean editable;
	
	public AbstractValidationTableModel(IPersonService personService, IAddressService addressService, IDonationService donationService, ImportValidationView parent){
		this.personService = personService;
		this.donationService = donationService;
		this.parent = parent;
		
		this.editable = true;
	}

	/**
	 * Adds a donation to the model
	 * @param donation
	 */
	public void addDonation (Donation donation){
		JComboBox cb = new JComboBox(ImportValidator.ValidationType.toArray());
		donations.add(donation);
		comboBoxes.add(cb);
	}

	/**
	 * Removes a donation from the model
	 * @param d
	 */
	public void removeDonation (Donation d){
		comboBoxes.remove(donations.indexOf(d));
		donations.remove(d);
		fireTableDataChanged();
	}
	
	/**
	 * Adds all donations from the given donationList to the model
	 * @param donationList
	 */
	public void addList (List<Donation> donationList){
		for(int i=0; i<donationList.size(); i++){
			addDonation(donationList.get(i));
		}
	}
	
	/**
	 * Removes the donation in a specified row from the model
	 * @param row
	 */
	public void removeDonation (int row){
		donations.remove(row);
		comboBoxes.remove(row);
		fireTableDataChanged();
	}
	
	/**
	 * Returns donation in a specified row
	 * @param rowIndex
	 * @return
	 */
	public Donation getDonationRow(int rowIndex){
		return donations.get(rowIndex);
	}

	/**
	 * Returns list of all donations in this model
	 * @return
	 */
	public List<Donation> getDonationList(){
		return donations;
	}
	
	/**
	 * Returns the number of the donations in this model
	 */
	public int getRowCount() {
		return donations.size();
	}
	
	/**
	 * Removes all donations from this model
	 */
	public void removeAll(){
		donations = new Vector<Donation>();
		comboBoxes = new Vector<JComboBox>();
		fireTableDataChanged();
	}
	
	/**
	 * Returns the value at an existing row/column
	 */
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
			case 8: return (donation.getAmount()/100D);
			case 9: return donation.getDate();
			case 10: return donation.getDedication();
			case 11: return donation.getType();
			case 12: return donation.getNote();
			case 13: return cb.getSelectedItem();
			default: return null;
		}
	}
	
	/**
	 * Returns the type of a column in this model
	 */
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

	/**
	 * Returns the number of existing columns in this model
	 */
	public int getColumnCount() {
		return columnNames.length;
	}
	
	/**
	 * Returns the name of a specific column
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Returns true, if a specific cell should be editable
	 */
	public boolean isCellEditable(int row, int col) {
		if(editable){
			return true;
		} else{
			return (col == 13);
		}
	}

	/**
	 * Allows to set the value of a comboBox
	 * @param d
	 * @param option
	 */
	public void setComboBox(Donation d, ValidationType option) {
		comboBoxes.get(donations.indexOf(d)).setSelectedIndex(ValidationType.indexOf(option));
		fireTableDataChanged();
	}
	
	/**
	 * Returns a list of donations where comboBox option "anonym" is chosen
	 * @return
	 */
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
	
	/**
	 * Returns a list of donations where comboBox option "nicht importieren" is chosen
	 * @return
	 */
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

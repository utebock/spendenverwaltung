package at.fraubock.spendenverwaltung.gui;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.util.ImportValidator;
import at.fraubock.spendenverwaltung.util.ImportValidator.ValidationType;

public interface IValidationTableModel {
	/**
	 * Adds a Donation to the TableModel
	 * @param d
	 * 			Donation to add
	 */
	public void addDonation(Donation d);
	
	/**
	 * Removes a Donation from the TableModel
	 * @param d
	 * 			Donation to remove
	 */
	public void removeDonation(Donation d);
	
	/**
	 * Sets Standard Option for a ComboBox (Standard is edit)
	 * @param index
	 * 			Index of the combobox which should be updated
	 */
	public void setComboBox(Donation d, ValidationType option);
}

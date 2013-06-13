package at.fraubock.spendenverwaltung.gui;

import at.fraubock.spendenverwaltung.interfaces.domain.Donation;

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
}

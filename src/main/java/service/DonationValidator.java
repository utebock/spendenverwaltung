package service;

import domain.Donation;


public class DonationValidator {
	
	public synchronized void validate(Donation d){
		
		if(d == null)
			throw new IllegalArgumentException("Donation must not be null");
		if(d.getPerson() == null)
			throw new IllegalArgumentException("Person must not be null");
		if(d.getAmount() < 0)
			throw new IllegalArgumentException("Amount must not be less than 0");
		if(d.getDate() == null)
			throw new IllegalArgumentException("Date must not be null");
		if(d.getDedication() == null)
			throw new IllegalArgumentException("Dedication must not be null");
		if(d.getType() == null)
			throw new IllegalArgumentException("Type must not be null");
	}
}

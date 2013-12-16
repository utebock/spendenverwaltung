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
package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.Date;

import at.fraubock.spendenverwaltung.util.DateComparer;

/**
 * 
 * @author romanvoglhuber
 *
 */
public class Confirmation {
	private Integer id;
	
	private Person person;
	
	private ConfirmationTemplate template;
	
	private Donation donation;
	
	private Date date;
	
	private Date fromDate;
	private Date toDate;
	
	public Integer getId() {
		return id;
	}
	public Person getPerson() {
		return person;
	}
	public ConfirmationTemplate getTemplate() {
		return template;
	}
	public Donation getDonation() {
		return donation;
	}
	public Date getDate() {
		return date;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public void setTemplate(ConfirmationTemplate template) {
		this.template = template;
	}
	public void setDonation(Donation donation) {
		this.donation = donation;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Confirmation other = (Confirmation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!DateComparer.isSameDay(this.date, other.date))
			return false;
		if (fromDate == null) {
			if (other.fromDate != null)
				return false;
		} else if (!DateComparer.isSameDay(this.fromDate, other.fromDate))
			return false;
		if (toDate == null) {
			if (other.toDate != null)
				return false;
		} else if (!DateComparer.isSameDay(this.toDate, other.toDate))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		if (!person.equals(other.person))
			return false;
		if(donation == null){
			if(other.donation!=null)
				return false;
		}
		else if(!donation.equals(other.donation))
			return false;
		return true;
	}
	
	
}

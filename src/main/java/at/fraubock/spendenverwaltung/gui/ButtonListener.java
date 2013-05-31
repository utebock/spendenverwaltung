package at.fraubock.spendenverwaltung.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener{

	private Overview overview;
	private PersonOverview personOverview;
	private CreatePerson createPerson;
	private DeletePerson deletePerson;
	private AddDonation showPerson;
	private FilterPersons filterPersons;
	private FilterOverview filterOverview;
	private CreateFilter createFilter;
	
	public ButtonListener(Overview overview){
		this.overview = overview;
	}
	public ButtonListener(PersonOverview personOverview){
		this.personOverview = personOverview;
	}
	
	public ButtonListener(CreatePerson createPerson) {
		this.createPerson = createPerson;
	}
	
	public ButtonListener(DeletePerson deletePerson) {
		this.deletePerson = deletePerson;
	}
	
	public ButtonListener(AddDonation showPerson) {
		this.showPerson = showPerson;
	}
	
	public ButtonListener(FilterPersons filterPersons) {
		this.filterPersons = filterPersons;
	}
	
	public ButtonListener(FilterOverview filterOverview) {
		this.filterOverview = filterOverview;
		
	}
	public ButtonListener(CreateFilter createFilter) {
		this.createFilter = createFilter;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		
		if(cmd.equals("plusButton_create_filter")){
			createFilter.gainMore();
			createFilter.showButtons();
		}
		
		if(cmd.equals("cancel_filter_in_db")){
			createFilter.returnTo();
		}
		
		if(cmd.equals("person_overview")){
			overview.goToPersons();
		}
		
		if(cmd.equals("create_person")){
			personOverview.goToCreate();
		}
		
		if(cmd.equals("create_person_in_db")){
			createPerson.createPersonInDb();
		}
		
		if(cmd.equals("cancel_person_in_db")){
			createPerson.returnTo();
		}
		
		if(cmd.equals("search_person")){
			personOverview.goToShow();
		}
		
		if(cmd.equals("delete_person_from_db")){
			deletePerson.deletePersonFromDb();
		}
		
		if(cmd.equals("cancel_delete_person_from_db")){
			deletePerson.returnTo();
		}
		if(cmd.equals("add_donation_address")){
			filterPersons.goToShow();
		}
		if(cmd.equals("return_to_personOverview")){
			filterPersons.returnTo();
		}
		if(cmd.equals("return_to_overview")){
			filterOverview.returnTo();
		}
		if(cmd.equals("create_filter")){
			filterOverview.createFilter();
		}
		if(cmd.equals("create_donation_in_show_person")){
			showPerson.createDonationInDb();
		}
		
		if(cmd.equals("go_from_show_person_to_person_overview")){
			showPerson.returnTo();
		}
		
		if(cmd.equals("filter_overview")){
			overview.goToFilter();
		}
	}

}

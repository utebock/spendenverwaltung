package at.fraubock.spendenverwaltung.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import at.fraubock.spendenverwaltung.gui.filter.CreateFilter;

public class ButtonListener implements ActionListener{

	private Overview overview;
	private CreatePerson createPerson;
	private AddAttributes showPerson;
	private FilterPersons filterPersons;
	private FilterOverview filterOverview;
	private CreateFilter createFilter;
	private EditPerson editPerson;
	
	public ButtonListener(Overview overview){
		this.overview = overview;
	}
	
	public ButtonListener(CreatePerson createPerson) {
		this.createPerson = createPerson;
	}
	
	public ButtonListener(AddAttributes showPerson) {
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
	public ButtonListener(EditPerson editPerson) {
		this.editPerson = editPerson;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		
		if(cmd.equals("plusButton_create_filter")){
			createFilter.gainMore(e.getSource());
		}
		
		if(cmd.equals("minusButton_create_filter")){
			createFilter.removeSelector(e.getSource());
		}
		
		if(cmd.equals("cancel_filter_in_db")){
			createFilter.returnTo();
		}
		
		if(cmd.equals("create_filter_in_db")){
			createFilter.createFilter();
		}
		
		if(cmd.equals("create_person")){
			overview.goToCreate();
		}
		
		if(cmd.equals("create_person_in_db")){
			createPerson.createPersonInDb();
		}
		
		if(cmd.equals("cancel_person_in_db")){
			createPerson.returnTo();
		}
		
		if(cmd.equals("search_person")){
			overview.goToShow();
		}
		
		if(cmd.equals("delete_person_from_db")){
			filterPersons.deletePerson();
		}
		
		if(cmd.equals("edit_person")){
			filterPersons.editPerson();
		}
		if(cmd.equals("edit_person_in_db")){
			editPerson.editPerson();
		}
		if(cmd.equals("edit_address_in_db")){
			editPerson.editAddress();
		}
		if(cmd.equals("delete_address_in_db")){
			editPerson.deleteAddress();
		}
		if(cmd.equals("cancel_edit")){
			editPerson.returnTo();
		}
		
		if(cmd.equals("add_donation_address")){
			filterPersons.addAttributes();
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
		if(cmd.equals("open_create_donation_in_show_person")){
			showPerson.addDonation();
		}
		if(cmd.equals("open_create_address_in_show_person")){
			showPerson.addAddress();
		}
		
		if(cmd.equals("go_from_show_person_to_person_overview")){
			showPerson.returnTo();
		}
		
		if(cmd.equals("filter_overview")){
			overview.goToFilter();
		}
	}

}

package at.fraubock.spendenverwaltung.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import at.fraubock.spendenverwaltung.gui.filter.CreateFilter;
import at.fraubock.spendenverwaltung.gui.views.FindPersonsView;

public class ButtonListener implements ActionListener{

	private Overview overview;
	private CreatePerson createPerson;
	private AddAttributes showPerson;
	private FindPersonsView filterPersons;
	private MainFilterView filterOverview;
	private CreateFilter createFilter;
	private EditPerson editPerson;
	private ImportValidation importValidation;
	private ImportData importData;
	
	public ButtonListener(Overview overview){
		this.overview = overview;
	}
	
	public ButtonListener(CreatePerson createPerson) {
		this.createPerson = createPerson;
	}
	
	public ButtonListener(AddAttributes showPerson) {
		this.showPerson = showPerson;
	}
	
	public ButtonListener(FindPersonsView filterPersons) {
		this.filterPersons = filterPersons;
	}
	
	public ButtonListener(MainFilterView filterOverview) {
		this.filterOverview = filterOverview;
		
	}
	public ButtonListener(CreateFilter createFilter) {
		this.createFilter = createFilter;
	}
	public ButtonListener(EditPerson editPerson) {
		this.editPerson = editPerson;
	}
	public ButtonListener(ImportValidation importValidation){
		this.importValidation = importValidation;
	}

	public ButtonListener(ImportData importData) {
		this.importData = importData;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();		
		
		if(cmd.equals("edit_filter")){
			filterOverview.createFilter((JButton)e.getSource());
		}
		
		if(cmd.equals("delete_filter")){
			filterOverview.deleteFilter();
		}
		
		if(cmd.equals("plusButton_create_filter")){
			createFilter.gainMore();
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
		//	overview.goToCreate();
		}
		
		if(cmd.equals("create_person_in_db")){
			//createPerson.createPersonInDb();
		}
		
		if(cmd.equals("cancel_person_in_db")){
		//	createPerson.returnTo();
		}
		
		if(cmd.equals("search_person")){
			//overview.goToShow();
		}
		
		if(cmd.equals("delete_person_from_db")){
		//	filterPersons.deletePerson();
		}
		if(cmd.equals("donation_validation")){
			overview.goToValidation();
		}
		
		if(cmd.equals("edit_person")){
		//	filterPersons.editPerson();
		}
		if(cmd.equals("edit_person_in_db")){
		//	editPerson.editPerson();
		}
		if(cmd.equals("edit_address_in_db")){
			//editPerson.editAddress();
		}
		if(cmd.equals("delete_address_in_db")){
		//	editPerson.deleteAddress();
		}
		if(cmd.equals("cancel_edit")){
		//	editPerson.returnTo();
		}
		
		if(cmd.equals("add_donation_address")){
		//	filterPersons.addAttributes();
		}
		if(cmd.equals("return_to_personOverview")){
			//filterPersons.returnTo();
		}
		if(cmd.equals("return_to_overview")){
			filterOverview.returnTo();
		}
		if(cmd.equals("add_filter")){
			filterOverview.createFilter((JButton)e.getSource());
		}
		if(cmd.equals("open_create_donation_in_show_person")){
		//	showPerson.addDonation();
		}
		if(cmd.equals("open_create_address_in_show_person")){
		//	showPerson.addAddress();
		}
		
		if(cmd.equals("go_from_show_person_to_person_overview")){
		//	showPerson.returnTo();
		}
		
		if(cmd.equals("filter_overview")){
			overview.goToFilter();
		}
		if(cmd.equals("return_from_import_validation_to_overview")){
			importValidation.returnTo();
		}
		if(cmd.equals("donationImport_overview")){
			overview.goToImport();
		}
		if(cmd.equals("return_from_import_data_to_overview")){
			importData.returnTo();
		}
		if(cmd.equals("open_file_import_data")){
			importData.chooseFile();
		}
		if(cmd.equals("import_import_data")){
			importData.importData();
		}
	}

}

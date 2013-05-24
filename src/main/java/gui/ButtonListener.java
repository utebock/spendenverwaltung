package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener{

	private Overview overview;
	private PersonOverview personOverview;
	private CreatePerson createPerson;
	private DeletePerson deletePerson;
	
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
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		
		if(cmd.equals("person_overview")){
			overview.goToPersons();
		}
		
		if(cmd.equals("create_person")){
			personOverview.goToCreate();
		}
		
		if(cmd.equals("create_person_and_donation_in_db")){
			createPerson.createPersonInDb();
			
			//TODO why did this break on refactoring??
//			createPerson.createDonationInDb();
		}
		
		if(cmd.equals("cancel_person_in_db")){
			createPerson.returnTo();
		}
		
		if(cmd.equals("delete_person")){
			personOverview.goToDelete();
		}
		
		if(cmd.equals("delete_person_from_db")){
			deletePerson.deletePersonFromDb();
		}
		
		if(cmd.equals("cancel_delete_person_from_db")){
			deletePerson.returnTo();
		}
	}

}

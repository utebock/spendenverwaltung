package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ButtonListener implements ActionListener{

	private Overview overview;
	private PersonOverview personOverview;
	
	public ButtonListener(Overview overview){
		this.overview = overview;
	}
	public ButtonListener(PersonOverview personOverview){
		this.personOverview = personOverview;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		String cmd = e.getActionCommand();
		
		if(cmd.equals("person_overview")){
			overview.goToPersons();
		}
		
	}

}

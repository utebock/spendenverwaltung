package gui;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import service.IAddressService;
import service.IPersonService;

public class PersonOverview extends JPanel{

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;

	public PersonOverview(IPersonService personService, IAddressService addressService){
		super(new MigLayout());
		this.personService = personService;
		this.addressService = addressService;
		
		setUpPersons();
	}
	
	private void setUpPersons(){
		
	}
}

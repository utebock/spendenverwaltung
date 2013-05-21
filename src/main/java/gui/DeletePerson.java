package gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import service.IAddressService;
import service.IPersonService;

public class DeletePerson extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private PersonOverview personOverview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private JPanel panel;
	
	public DeletePerson(IPersonService personService, IAddressService addressService, PersonOverview personOverview){
		super(new MigLayout());
		
		this.personService = personService;
		this.addressService = addressService;
		this.personOverview = personOverview;
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		
		panel = builder.createPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(700, 700));
	}


}

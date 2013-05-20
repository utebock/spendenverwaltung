package gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import service.IAddressService;
import service.IPersonService;

public class Overview extends JPanel{

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private ComponentBuilder builder = new ComponentBuilder();
	private ButtonListener buttonListener = new ButtonListener();
	private JButton person;
	private JLabel general;
	public Overview(IPersonService personService, IAddressService addressService){
		super(new MigLayout());
		this.personService = personService;
		this.addressService = addressService;
		
		setUpPersons();
	}

	private void setUpPersons() {
		JPanel personsPanel = builder.createPanel();
		personsPanel.setLayout(new MigLayout());
		personsPanel.setPreferredSize(new Dimension(800,160));
		this.add(personsPanel);
		general = builder.createLabel("Allgemein");
		personsPanel.add(general, "wrap");
		person = builder.createImageButton("images/template.jpg", buttonListener);
		personsPanel.add(person);
		
	}
	

}

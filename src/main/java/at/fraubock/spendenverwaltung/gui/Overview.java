package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class Overview extends JPanel{

	private static final long serialVersionUID = 1L;
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private JButton person;
	private JLabel general;
	private JLabel empty;
	private JButton filter;
	private ImageIcon space;
	private ImageIcon separator;
	private JLabel separatorLabel;
	private JLabel persons;
	private JLabel filterLabel;
	
	public Overview(IPersonService personService, IAddressService addressService, IDonationService donationService){
		super(new MigLayout());
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		setUpPersons();
	}

	private void setUpPersons() {
		JPanel personsPanel = builder.createPanel();
		personsPanel.setLayout(new MigLayout());
		personsPanel.setPreferredSize(new Dimension(800,160));
		this.add(personsPanel);
		general = builder.createLabel("Allgemein");
		empty = builder.createLabel(" ");		
		personsPanel.add(general, "wrap");
		personsPanel.add(empty, "wrap");
		person = builder.createImageButton("/images/template.jpg", buttonListener, "person_overview");
		personsPanel.add(person);
		
		filter = builder.createImageButton("/images/template.jpg", buttonListener, "filter_overview");
		personsPanel.add(filter, "wrap, gap 25");
		//button labels
		persons = builder.createLabel("Personen");
		personsPanel.add(persons, "gap 22");
		filterLabel = builder.createLabel("Filter");
		personsPanel.add(filterLabel, "wrap, gap 58");
		
		/**
		separator = builder.createImageIcon("images/separator.gif");
		separatorLabel = builder.createImageLabel(separator);
		//personsPanel.add(separatorLabel);
		*/
		
	}
	
	public void goToPersons(){
		PersonOverview po = new PersonOverview(personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(po);
		

	}
	

}
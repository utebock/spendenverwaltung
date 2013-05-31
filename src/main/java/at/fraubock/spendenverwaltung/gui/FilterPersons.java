package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class FilterPersons extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FilterPersons.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private PersonOverview overview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private PersonTableModel personModel;
	private JTable showTable;
	private JScrollPane scrollPane;
	private List<Person> personList;
	private JPanel panel;
	private JToolBar toolbar;
	private JButton editButton;
	private JButton deleteButton;
	private JButton addAttribute;
	private ActionHandler handler;
	private JComboBox filterCombo;
	private JButton backButton;
	
	public FilterPersons(IPersonService personService, IAddressService addressService, IDonationService donationService, PersonOverview overview){
		super(new MigLayout());
	
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.overview = overview;
		initTable();
		setUp();
	}
	
	public void initTable(){
		personModel = new PersonTableModel();
		getPersons();
		showTable = new JTable(personModel);
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(800, 800));
		
	}
	
	public void setUp(){
		handler = new ActionHandler(this);
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		panel = builder.createPanel(800, 800);
		this.add(panel);
		
		toolbar = builder.createToolbar();
		addComponentsToToolbar(toolbar);
		panel.add(toolbar, "wrap");
		panel.add(scrollPane, "span 5, gaptop 25");
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		String[] combo = new String[]{"Filter 1", "Filter 2"};
		filterCombo = builder.createComboBox(combo, handler, "filterCombo");
		addAttribute = builder.createButton("Attribute hinzuf\u00FCgen", buttonListener, "add_donation_address");
		editButton = builder.createButton("Bearbeiten", buttonListener, "edit_person");
		deleteButton = builder.createButton("L\u00F6schen", buttonListener, "delete_person");
		backButton = builder.createButton("Zur\u00FCck", buttonListener, "return_to_personOverview");
		toolbar.add(filterCombo);
		toolbar.add(addAttribute);
		toolbar.add(editButton);
		toolbar.add(deleteButton);
		toolbar.add(backButton);
	}

	public PersonTableModel getPersonModel(){
		return this.personModel;
	}
	
	private void getPersons(){
		personList = new ArrayList<Person>();
		
		try{
			personList = personService.getAll();
			log.info("List " + personList.size() + " persons");
		}
		catch(ServiceException e){
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		if(personList == null){
			JOptionPane.showMessageDialog(this, "GetAll() returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Person p : personList){
			personModel.addPerson(p);
		}	
	}
	
	public void goToShow(){
		Person p = personModel.getPersonRow(showTable.getSelectedRow());
		AddDonation sp = new AddDonation(p, personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(sp);
	}
	
	public void returnTo(){
		this.removeAll();
		this.revalidate();
		this.repaint();
		overview.removeAll();
		overview.revalidate();
		overview.repaint();
		overview.setUp();
	}

	
}
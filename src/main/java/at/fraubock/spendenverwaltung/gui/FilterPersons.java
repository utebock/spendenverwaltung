package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private Overview overview;
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
	@SuppressWarnings("rawtypes")
	private JComboBox filterCombo;
	private JButton backButton;
	
	public FilterPersons(IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
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
		String[] combo = new String[]{"Platzhalter", "Platzhalter"};
		filterCombo = builder.createComboBox(combo, handler);
		addAttribute = builder.createButton("Attribute hinzuf\u00FCgen", buttonListener, "add_donation_address");
		editButton = builder.createButton("Bearbeiten", buttonListener, "edit_person");
		deleteButton = builder.createButton("L\u00F6schen", buttonListener, "delete_person_from_db");
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
	
	public void addAttributes(){
		Person p = personModel.getPersonRow(showTable.getSelectedRow());
		AddAttributes sp = new AddAttributes(p, personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(sp);
	}
	
	public void deletePerson(){
		Person p;
		int row = showTable.getSelectedRow();
		if(row == -1){
			JOptionPane.showMessageDialog(this, "Bitte Person zum Loeschen auswaehlen.");
			return;
		}
		
		int id = (Integer) personModel.getValueAt(row, 0);
		
		try{
			p = personService.getById(id);
		}
		catch(ServiceException e){
            JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
			return;
		}
		
		Object[] options = {"Abbrechen","Loeschen"};
		int ok = JOptionPane.showOptionDialog(this, "Diese Person sicher loeschen?", "Loeschen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
		if(ok == 1){
			try {
				personService.delete(p);       
			} 
			catch (ServiceException e) {
				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
	            e.printStackTrace();
				return;
			}
				
			personModel.removePerson(row);
			JOptionPane.showMessageDialog(this, "Person wurde geloescht", "Information", JOptionPane.INFORMATION_MESSAGE);
			
		}
		else{
			return;
		}
	}
	
	public void editPerson(){
		Person p;
		int row = showTable.getSelectedRow();
		if(row == -1){
			JOptionPane.showMessageDialog(this, "Bitte Person zum Bearbeiten auswaehlen.");
			return;
		}
		
		int id = (Integer) personModel.getValueAt(row, 0);
		
		try{
			p = personService.getById(id);
		}
		catch(ServiceException e){
            JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
			return;
		}
		
		EditPerson ep = new EditPerson(p, personService, addressService, this, overview);
		removeAll();
		revalidate();
		repaint();
		add(ep);
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
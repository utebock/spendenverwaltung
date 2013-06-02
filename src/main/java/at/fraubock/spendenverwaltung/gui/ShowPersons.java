package at.fraubock.spendenverwaltung.gui;
import java.awt.Dimension;
import java.awt.Font;
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

public class ShowPersons extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ShowPersons.class);
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
	private JToolBar toolbar;
	private JButton editButton;
	private JButton deleteButton;
	private JButton addAttribute;
	private ActionHandler handler;
	private JComboBox<String[]> filterCombo;
	private JButton backButton;
	private JPanel overviewPanel;
	
	public ShowPersons(IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
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
		
		overviewPanel = builder.createPanel(800, 850);
		//JScrollPane pane = new JScrollPane(overviewPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(overviewPanel);
		
		toolbar = builder.createToolbar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		addComponentsToToolbar(toolbar);
		overviewPanel.add(toolbar, "growx, wrap");
		overviewPanel.add(scrollPane);
	}
	
	@SuppressWarnings("unchecked")
	private void addComponentsToToolbar(JToolBar toolbar) {
		String[] combo = new String[]{"Filter ausw\u00E4hlen", "Platzhalter"};
		filterCombo = builder.createComboBox(combo, handler);
		addAttribute = builder.createButton("Attribute hinzuf\u00FCgen", buttonListener, "add_donation_address");
		addAttribute.setFont(new Font("Bigger", Font.PLAIN, 13));
		editButton = builder.createButton("Person bearbeiten", buttonListener, "edit_person");
		editButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		deleteButton = builder.createButton("Person l\u00F6schen", buttonListener, "delete_person_from_db");
		deleteButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		backButton = builder.createButton("Zur\u00FCck", buttonListener, "return_to_personOverview");
		backButton.setFont(new Font("Bigger", Font.PLAIN, 13));
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
		Person p;
		int row = showTable.getSelectedRow();
		if(row == -1){
			JOptionPane.showMessageDialog(this, "Bitte Person ausw\u00E4hlen.");
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
			JOptionPane.showMessageDialog(this, "Bitte Person zum L\u00F6schen ausw\u00E4hlen.");
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
		
		Object[] options = {"Abbrechen","L\u00F6schen"};
		int ok = JOptionPane.showOptionDialog(this, "Diese Person sicher l\u00F6schen?", "Loeschen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);
		
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
			JOptionPane.showMessageDialog(this, "Person wurde gel\u00F6scht.", "Information", JOptionPane.INFORMATION_MESSAGE);
			
		}
		else{
			return;
		}
	}
	
	public void editPerson(){
		Person p;
		int row = showTable.getSelectedRow();
		if(row == -1){
			JOptionPane.showMessageDialog(this, "Bitte Person zum Bearbeiten ausw\u00E4hlen.");
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
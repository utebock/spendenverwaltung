package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

import net.miginfocom.swing.MigLayout;

public class PersonOverview extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PersonOverview.class);
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
	private JButton create;
	private JButton edit;
	private JButton delete;
	private JButton show;
	private JButton search;
	private JPanel panel;
	private JLabel createLabel;
	private JLabel editLabel;
	private JLabel deleteLabel;
	private JLabel showLabel;
	private JLabel searchLabel;
	
	public PersonOverview(IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
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
		scrollPane.setPreferredSize(new Dimension(650,700));
		
		showTable.addMouseListener(this);
	}
	
	public void setUp(){
		//System.out.println("Hello");

		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		
		panel = builder.createPanel(800,800);
		this.add(panel);
		create = builder.createImageButton("/images/createPerson.jpg", buttonListener, "create_person");
		panel.add(create);
		
		edit = builder.createImageButton("/images/editPerson.jpg", buttonListener, "edit_person");
		panel.add(edit, "gap 30");
		
		delete = builder.createImageButton("/images/deletePerson.jpg", buttonListener, "delete_person");
		panel.add(delete, "gap 30");
		
		show = builder.createImageButton("/images/showPersons.jpg", buttonListener, "show_person");
		panel.add(show, "gap 30");
		
		search = builder.createImageButton("/images/getPersons.jpg", buttonListener, "search_person");
		panel.add(search, "gap 30, wrap");
		
		createLabel = builder.createLabel("Person anlegen");
		panel.add(createLabel, "gap 5");
		
		editLabel = builder.createLabel("Person bearbeiten");
		panel.add(editLabel, "gap 25");
		
		deleteLabel = builder.createLabel("Person loeschen");
		panel.add(deleteLabel, "gap 30");
		
		showLabel = builder.createLabel("Personen anzeigen");
		panel.add(showLabel, "gap 22");
		
		searchLabel = builder.createLabel("Personen suchen");
		panel.add(searchLabel, "gap 28, wrap");

		panel.add(scrollPane, "span 5, gaptop 25");
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
	
	public void goToCreate(){
		CreatePerson cp = new CreatePerson(personService, addressService, this);
		removeAll();
		revalidate();
		repaint();
		add(cp);
	}
	
	public void goToDelete(){
		DeletePerson cp = new DeletePerson(personService, addressService, this);
		removeAll();
		revalidate();
		repaint();
		add(cp);
	}
	
	public void goToShow(Person p){
		ShowPerson sp = new ShowPerson(p, personService, addressService, donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(sp);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2){
			goToShow(personModel.getPersonRow(showTable.getSelectedRow()));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
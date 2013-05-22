package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import domain.Person;
import exceptions.ServiceException;

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
	private JTable deleteTable;
	private DeleteTableModel deleteModel;
	private JScrollPane pane;
	private List<Person> list;
	
	public DeletePerson(IPersonService personService, IAddressService addressService, PersonOverview personOverview){
		super(new MigLayout());
		
		this.personService = personService;
		this.addressService = addressService;
		this.personOverview = personOverview;
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		
		deleteModel = new DeleteTableModel();
		setUp();
	}
	
	public void setUp(){

		panel = builder.createPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(700, 700));
		this.add(panel);
		
		deleteTable = new JTable(deleteModel);
		deleteTable.setFillsViewportHeight(true);
		deleteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane = new JScrollPane(deleteTable);
		pane.setPreferredSize(new Dimension(800,800));
		panel.add(pane);
		getColumns();
		
	}

	private void getColumns() {
		deleteTable.getColumn("Loeschen");
		deleteTable.getColumn("Id");
		deleteTable.getColumn("Vorname");
		deleteTable.getColumn("Nachname");
		deleteTable.getColumn("Adresse");
		getPersons();
		
	}
	
	private void getPersons(){
		list = new ArrayList<Person>();
		Vector<Object> obj;
		
		try{
			list = personService.getAll();
			System.out.println("list: "+list.toString());
		}
		catch(ServiceException e){
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		if(list == null){
			JOptionPane.showMessageDialog(this, "GetAll() returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		for(Person p : list){
			obj = new Vector<Object>();
			obj.add(p.getDeleted());
			obj.add(p.getId());
			obj.add(p.getGivenName());
			obj.add(p.getSurname());
			obj.add(p.getMailingAddress().getStreet());
			deleteModel.addRow(obj);
			deleteTable.revalidate();
		}
		
	}

}

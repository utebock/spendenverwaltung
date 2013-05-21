package gui;

import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		setUp();
	}
	
	public void setUp(){

		panel = builder.createPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(700, 700));
		this.add(panel);
		
		deleteTable = builder.createRTable(getPersons());
		pane = builder.createScrollPane(deleteTable);
		pane.setPreferredSize(new Dimension(800,800));
		panel.add(pane);
		
	}

	private DeleteTableModel getPersons() {
		Vector<String> header = new Vector<String>();
		
		header = new Vector<String>();
		header.add("Id");
    	header.add("Anrede");
    	header.add("Vorname");
    	header.add("Nachname");
    	header.add("Adresse");
    	try{
    		list = personService.getAll();
    	}
    	catch(ServiceException e){
    		JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
    		//return;
    	}
    	return new DeleteTableModel(list, header);
    	
	}


}

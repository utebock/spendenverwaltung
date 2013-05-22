package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
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
	private JButton ok;
	private JButton cancel;
	
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
		panel.add(pane, "wrap");
		
		ok = builder.createButton("Loeschen", buttonListener, "delete_person_from_db");
		panel.add(ok);
		
		cancel = builder.createButton("Abbrechen", buttonListener, "cancel_delete_person_from_db");
		panel.add(cancel, "split 2");
		
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
	public void removeRow(){
		int row = deleteTable.getSelectedRow();
		deleteModel.removeRow(row);
	}
	
	public void deletePersonFromDb(){
		Person p;
		int row = deleteTable.getSelectedRow();
		if(row == -1){
			JOptionPane.showMessageDialog(this, "Bitte Person zum Loeschen auswaehlen.");
			return;
		}
		
		int id = (Integer) deleteModel.getValueAt(row, 1);
		
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
				
			deleteModel.removeRow(row);
			JOptionPane.showMessageDialog(this, "Person wurde geloescht", "Information", JOptionPane.INFORMATION_MESSAGE);
			
		}
		else{
			return;
		}
	}

	public void returnTo(){
		this.removeAll();
		this.revalidate();
		this.repaint();
		personOverview.removeAll();
		personOverview.revalidate();
		personOverview.repaint();
		personOverview.setUp();
	}
}

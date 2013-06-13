package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.ImportValidator;
import at.fraubock.spendenverwaltung.util.ValidatedData;

public class ImportValidation extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ShowPersons.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private Overview overview;
	private ConflictValidationTableModel conflictModel;
	private NewValidationTableModel newModel;
	private MatchValidationTableModel matchModel;
	private JScrollPane conflictPane;
	private JScrollPane newPane;
	private JScrollPane matchPane;
	private JTable conflictTable;
	private JPanel conflictPanel;
	private JTable newTable;
	private JPanel newPanel;
	private JTable matchTable;
	private JPanel matchPanel;
	private ComponentBuilder builder;
	private JButton backBtn;
	private JButton saveBtn;
	private ButtonListener buttonListener;
	private List<Donation> donationList;
	private List<Person> personList;
	private ImportValidator importValidator;
	private ValidatedData validatedData;
	private JLabel newLabel;
	private JLabel conflictLabel;
	private JLabel matchLabel;
	private JComboBox conflictComboBox;

	public ImportValidation(IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
		super(new MigLayout());
		
		this.builder = new ComponentBuilder();
		this.buttonListener = new ButtonListener(this);
		this.importValidator = new ImportValidator(personService, donationService);
		this.validatedData = new ValidatedData();
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.overview = overview;

		conflictComboBox = new JComboBox(ImportValidator.ValidationType.toArray());
		
		this.conflictModel = new ConflictValidationTableModel(this, this.donationService, this.personService, this.addressService);
		this.newModel = new NewValidationTableModel(personService, addressService, donationService, this);
		this.matchModel = new MatchValidationTableModel(personService, addressService, donationService, this);
		
		setUp();
	}
	
	private void setUp(){
		
		conflictPanel = builder.createPanel(1200,250);
		newPanel = builder.createPanel(1200,250);
		matchPanel = builder.createPanel(1200,250);
		
		conflictTable = new JTable(conflictModel);
		newTable = new JTable(newModel);
		matchTable = new JTable(matchModel);
		
		conflictTable.getColumnModel().getColumn(13).setCellEditor(new DefaultCellEditor(conflictComboBox));
		
		
		conflictTable.setFillsViewportHeight(true);
		newTable.setFillsViewportHeight(true);
		matchTable.setFillsViewportHeight(true);
		
		conflictTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		newTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		matchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		conflictPane = new JScrollPane(conflictTable);
		newPane = new JScrollPane(newTable);
		matchPane = new JScrollPane(matchTable);
		
		conflictPane.setPreferredSize(new Dimension(1200, 200));
		newPane.setPreferredSize(new Dimension(1200, 200));
		matchPane.setPreferredSize(new Dimension(1200, 200));
		
		conflictLabel = builder.createLabel("Konflikte:");
		conflictPanel.add(conflictLabel, "wrap");
		
		newLabel = builder.createLabel("Liste neuer Spender:");
		newPanel.add(newLabel, "wrap");
		
		matchLabel = builder.createLabel("Liste zugewiesener Spender:");
		matchPanel.add(matchLabel, "wrap");
		
		conflictPanel.add(conflictPane, "wrap, growx");
		newPanel.add(newPane, "wrap, growx");
		matchPanel.add(matchPane, "wrap, growx, span 2");

		saveBtn = builder.createButton("Speichern", buttonListener, "save_validation");
		matchPanel.add(saveBtn, "");

		backBtn = builder.createButton("Abbrechen", buttonListener, "return_from_import_validation_to_overview");
		matchPanel.add(backBtn, "");

		this.add(conflictPanel, "wrap, growx");
		this.add(newPanel, "wrap, growx");
		this.add(matchPanel, "wrap, growx");
		
		getData();
	}
	
	private void getData(){
		donationList = new ArrayList<Donation>();
		personList = new ArrayList<Person>();

		try {
			donationList = donationService.getUnconfirmed();
		} catch(ServiceException e){
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		if(donationList == null){
			JOptionPane.showMessageDialog(this, "Donationlist returns null.", "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}
		
		for(Donation d : donationList)
			personList.add(d.getDonator());

		try {
			validatedData = importValidator.validate(personList, donationList);
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		
		conflictModel.addList(validatedData.getDonationListConflict());
		newModel.addList(validatedData.getDonationListNew());
		matchModel.addList(validatedData.getDonationListMatch(), validatedData.getPersonListMatch());
		
		conflictTable.revalidate();
		newTable.revalidate();
		matchTable.revalidate();
	}

	public void saveValidation(){
		List<Donation> newModelDonations = newModel.getDonationList();
		List<Donation> matchModelDonations = matchModel.getDonationList();
		List<Donation> conflictModelDonations = conflictModel.getDonationList();
		List<Person> matchModelDelete;
		List<Person> newModelDelete;
		
		try {
			
			newModelDelete = checkPersonDoublesInNewEntries(newModelDonations);
			
			updateDonationList(newModelDonations);
			updateDonationList(matchModelDonations);
			
			conflictModel.setPersonIdToNull();
			updateDonationList(conflictModelDonations);

			donationService.setImportToNull(newModelDonations);
			donationService.setImportToNull(matchModelDonations);
			
			
			deletePersonList(newModelDelete);
			deletePersonList(validatedData.getPersonsToDelete());
			deletePersonList(conflictModel.getPersonsToDelete());
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
	}
	
	private void updateDonationList(List<Donation> donations){
		for(Donation d : donations){
			try {
				personService.update(d.getDonator());
				donationService.update(d);
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
			    e.printStackTrace();
			    return;
			}
		}
	}
	
	private void deletePersonList(List<Person> persons){
		for(Person p : persons){
			try {
				personService.delete(p);
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
			    e.printStackTrace();
			    return;
			}
		}
	}
	
	public void openPersonDialog(){
		JDialog assignPerson = new AssignPerson(personService, addressService, donationService, this);
	}
	
	public void returnTo() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		overview.removeAll();
		overview.revalidate();
		overview.repaint();
		overview.setUp();
	}
	

	
	public List<Person> checkPersonDoublesInNewEntries(List<Donation> toCheck){
		List<Person> uniquePersons = new ArrayList<Person>();
		List<Person> toDelete = new ArrayList<Person>();
		Person doublePerson;
		
		for(Donation d : toCheck){
			doublePerson = getDoublePersonIdFromList(d.getDonator(), uniquePersons);
			
			if(doublePerson == null){
				uniquePersons.add(d.getDonator());
			} else{
				toDelete.add(d.getDonator());
				d.setDonator(doublePerson);
			}
		}
		
		return toDelete;
	}
	
	private Person getDoublePersonIdFromList(Person p, List<Person> checkList){
		Person doublePerson = null;
		
		for(Person donator : checkList){
			
			if(donator.getSurname().equals(p.getSurname())
					&& donator.getGivenName().equals(p.getGivenName())
					&& (donator.getEmail().equals(p.getEmail())
						|| (!donator.getTelephone().equals("") && donator.getTelephone().equals(p.getTelephone()))
						|| (donator.getMainAddress() != null && donator.getMainAddress().getCity().equals(p.getMainAddress().getCity())
								&& donator.getMainAddress().getPostalCode().equals(p.getMainAddress().getPostalCode())
								&& donator.getMainAddress().getStreet().equals(p.getMainAddress().getStreet())))){
				return donator;
			}
		}
		
		return doublePerson;
	}
	
	public void addToConflict(Donation donation){
		conflictModel.addEntry(donation);
		conflictModel.fireTableDataChanged();
	}
	
	public void addToMatch(Donation donation){
		matchModel.addEntry(donation, donation.getDonator());
		matchModel.fireTableDataChanged();
	}
	
	public void addToNew(Donation donation){
		newModel.addEntry(donation);
		newModel.fireTableDataChanged();
	}
}



//package at.fraubock.spendenverwaltung.gui;
//
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import java.util.EventObject;
//import java.util.List;
//
//import javax.swing.AbstractAction;
//import javax.swing.AbstractCellEditor;
//import javax.swing.DefaultCellEditor;
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JDialog;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.ListSelectionModel;
//import javax.swing.SwingUtilities;
//import javax.swing.event.CellEditorListener;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.PopupMenuEvent;
//import javax.swing.event.PopupMenuListener;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellEditor;
//import javax.swing.table.TableCellRenderer;
//import javax.swing.table.TableColumn;
//import javax.swing.table.TableColumnModel;
//
//import org.apache.log4j.Logger;
//
//import net.miginfocom.swing.MigLayout;
//import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
//import at.fraubock.spendenverwaltung.interfaces.domain.Person;
//import at.fraubock.spendenverwaltung.interfaces.domain.Donation.DonationType;
//import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
//import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
//import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
//import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
//import at.fraubock.spendenverwaltung.util.ImportValidator;
//import at.fraubock.spendenverwaltung.util.ValidatedData;
//
//public class ImportValidation extends JPanel {
//	private static final long serialVersionUID = 1L;
//	private static final Logger log = Logger.getLogger(ShowPersons.class);
//	private IPersonService personService;
//	private IAddressService addressService;
//	private IDonationService donationService;
//	private Overview overview;
//	private ConflictValidationTableModel conflictModel;
//	private NewValidationTableModel newModel;
//	private MatchValidationTableModel matchModel;
//	private JScrollPane conflictPane;
//	private JScrollPane newPane;
//	private JScrollPane matchPane;
//	private JTable conflictTable;
//	private JPanel conflictPanel;
//	private JTable newTable;
//	private JPanel newPanel;
//	private JTable matchTable;
//	private JPanel matchPanel;
//	private ComponentBuilder builder;
//	private JButton backBtn;
//	private JButton saveBtn;
//	private ButtonListener buttonListener;
//	private List<Donation> donationList;
//	private List<Person> personList;
//	private ImportValidator importValidator;
//	private ValidatedData validatedData;
//	private JLabel newLabel;
//	private JLabel conflictLabel;
//	private JLabel matchLabel;
//	private JComboBox conflictComboBox;
//
//	public ImportValidation(IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
//		super(new MigLayout());
//		
//		this.builder = new ComponentBuilder();
//		this.buttonListener = new ButtonListener(this);
//		this.importValidator = new ImportValidator(personService, donationService);
//		this.validatedData = new ValidatedData();
//		this.personService = personService;
//		this.addressService = addressService;
//		this.donationService = donationService;
//		this.overview = overview;
//		
//		conflictComboBox = new JComboBox(ImportValidator.ValidationType.toArray());
//		
//		this.conflictModel = new ConflictValidationTableModel(this, this.donationService, this.personService, this.addressService);
//		this.newModel = new NewValidationTableModel(personService, addressService, donationService, this);
//		this.matchModel = new MatchValidationTableModel(personService, addressService, donationService, this);
//		
//		setUp();
//	}
//	
//	private void setUp(){
//		
//		conflictPanel = builder.createPanel(1200,250);
//		newPanel = builder.createPanel(1200,250);
//		matchPanel = builder.createPanel(1200,250);
//		
//		conflictTable = new JTable(conflictModel);
//		newTable = new JTable(newModel);
//		matchTable = new JTable(matchModel);
//		
//		conflictTable.getColumnModel().getColumn(13).setCellEditor(new DefaultCellEditor(conflictComboBox));
//		
//		
//		conflictTable.setFillsViewportHeight(true);
//		newTable.setFillsViewportHeight(true);
//		matchTable.setFillsViewportHeight(true);
//		
//		conflictTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		newTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		matchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		
//		conflictPane = new JScrollPane(conflictTable);
//		newPane = new JScrollPane(newTable);
//		matchPane = new JScrollPane(matchTable);
//		
//		conflictPane.setPreferredSize(new Dimension(1200, 200));
//		newPane.setPreferredSize(new Dimension(1200, 200));
//		matchPane.setPreferredSize(new Dimension(1200, 200));
//		
//		conflictLabel = builder.createLabel("Konflikte:");
//		conflictPanel.add(conflictLabel, "wrap");
//		
//		newLabel = builder.createLabel("Liste neuer Spender:");
//		newPanel.add(newLabel, "wrap");
//		
//		matchLabel = builder.createLabel("Liste zugewiesener Spender:");
//		matchPanel.add(matchLabel, "wrap");
//		
//		conflictPanel.add(conflictPane, "wrap, growx");
//		newPanel.add(newPane, "wrap, growx");
//		matchPanel.add(matchPane, "wrap, growx, span 2");
//
//		saveBtn = builder.createButton("Speichern", buttonListener, "save_validation");
//		matchPanel.add(saveBtn, "");
//
//		backBtn = builder.createButton("Abbrechen", buttonListener, "return_from_import_validation_to_overview");
//		matchPanel.add(backBtn, "");
//
//		this.add(conflictPanel, "wrap, growx");
//		this.add(newPanel, "wrap, growx");
//		this.add(matchPanel, "wrap, growx");
//		
//		getData();
//	}
//	
//	private void getData(){
//		donationList = new ArrayList<Donation>();
//		personList = new ArrayList<Person>();
//
//		try {
//			donationList = donationService.getUnconfirmed();
//		} catch(ServiceException e){
//			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
//		    e.printStackTrace();
//		    return;
//		}
//		if(donationList == null){
//			JOptionPane.showMessageDialog(this, "Donationlist returns null.", "Error", JOptionPane.ERROR_MESSAGE);
//		    return;
//		}
//		
//		for(Donation d : donationList)
//			personList.add(d.getDonator());
//
//		try {
//			validatedData = importValidator.validate(personList, donationList);
//		} catch (ServiceException e) {
//			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
//		    e.printStackTrace();
//		    return;
//		}
//		
//		conflictModel.addList(validatedData.getDonationListConflict(), validatedData.getPersonListConflict());
//		newModel.addList(validatedData.getDonationListNew(), validatedData.getPersonListNew());
//		matchModel.addList(validatedData.getDonationListMatch(), validatedData.getPersonListMatch());
//		
//		conflictTable.revalidate();
//		newTable.revalidate();
//		matchTable.revalidate();
//	}
//
//	public void saveValidation(){
//		List<Donation> newModelDonations = newModel.getDonationList();
//		List<Donation> matchModelDonations = matchModel.getDonationList();
//		List<Person> matchModelDelete;
//		List<Person> newModelDelete;
//		
//		try {
//			
//			newModelDelete = checkPersonDoublesInNewEntries(newModelDonations);
//			
//			updateDonationList(newModelDonations);
//			updateDonationList(matchModelDonations);
//			
//			donationService.setImportToNull(newModelDonations);
//			donationService.setImportToNull(matchModelDonations);
//			
//			
//			deletePersonList(newModelDelete);
//			deletePersonList(validatedData.getPersonsToDelete());
//		} catch (ServiceException e) {
//			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
//		    e.printStackTrace();
//		    return;
//		}
//	}
//	
//	private void updateDonationList(List<Donation> donations){
//		for(Donation d : donations){
//			try {
//				donationService.update(d);
//			} catch (ServiceException e) {
//				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
//			    e.printStackTrace();
//			    return;
//			}
//		}
//	}
//	
//	private void deletePersonList(List<Person> persons){
//		for(Person p : persons){
//			try {
//				personService.delete(p);
//			} catch (ServiceException e) {
//				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
//			    e.printStackTrace();
//			    return;
//			}
//		}
//	}
//	
//	public void openPersonDialog(){
//		JDialog assignPerson = new AssignPerson(personService, addressService, donationService, this);
//	}
//	
//	public void returnTo() {
//		this.removeAll();
//		this.revalidate();
//		this.repaint();
//		overview.removeAll();
//		overview.revalidate();
//		overview.repaint();
//		overview.setUp();
//	}
//	
//
//	
//	public List<Person> checkPersonDoublesInNewEntries(List<Donation> toCheck){
//		List<Person> uniquePersons = new ArrayList<Person>();
//		List<Person> toDelete = new ArrayList<Person>();
//		Person doublePerson;
//		
//		for(Donation d : toCheck){
//			doublePerson = getDoublePersonIdFromList(d.getDonator(), uniquePersons);
//			
//			if(doublePerson == null){
//				uniquePersons.add(d.getDonator());
//			} else{
//				toDelete.add(d.getDonator());
//				d.setDonator(doublePerson);
//			}
//		}
//		
//		return toDelete;
//	}
//	
//	private Person getDoublePersonIdFromList(Person p, List<Person> checkList){
//		Person doublePerson = null;
//		
//		for(Person donator : checkList){
//			
//			if(donator.getSurname().equals(p.getSurname())
//					&& donator.getGivenName().equals(p.getGivenName())
//					&& (donator.getEmail().equals(p.getEmail())
//						|| (!donator.getTelephone().equals("") && donator.getTelephone().equals(p.getTelephone()))
//						|| (donator.getMainAddress() != null && donator.getMainAddress().getCity().equals(p.getMainAddress().getCity())
//								&& donator.getMainAddress().getPostalCode().equals(p.getMainAddress().getPostalCode())
//								&& donator.getMainAddress().getStreet().equals(p.getMainAddress().getStreet())))){
//				return donator;
//			}
//		}
//		
//		return doublePerson;
//	}
//	
//	public void addToConflict(Donation donation){
//		conflictModel.addEntry(donation, donation.getDonator());
//		conflictModel.fireTableDataChanged();
//	}
//	
//	public void addToMatch(Donation donation){
//		matchModel.addEntry(donation, donation.getDonator());
//		matchModel.fireTableDataChanged();
//	}
//	
//	public void addToNew(Donation donation){
//		newModel.addEntry(donation, donation.getDonator());
//		newModel.fireTableDataChanged();
//	}
//}

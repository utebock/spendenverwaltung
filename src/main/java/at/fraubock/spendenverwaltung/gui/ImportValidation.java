package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
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
	private ValidationTableModel conflictModel;
	private ValidationTableModel newModel;
	private ValidationTableModel matchModel;
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
		this.conflictModel = new ValidationTableModel(this, this.donationService, this.personService);
		this.newModel = new ValidationTableModel(this, this.donationService, this.personService);
		this.matchModel = new ValidationTableModel(this, this.donationService, this.personService);
		
		setUp();
	}
	
	private void setUp(){
		
		conflictPanel = builder.createPanel(1200,250);
		newPanel = builder.createPanel(1200,250);
		matchPanel = builder.createPanel(1200,250);
		
		conflictTable = new JTable(conflictModel);
		newTable = new JTable(newModel);
		matchTable = new JTable(matchModel);
		
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
		
		conflictModel.addList(validatedData.getDonationListConflict(), validatedData.getPersonListConflict());
		newModel.addList(validatedData.getDonationListNew(), validatedData.getPersonListNew());
		matchModel.addList(validatedData.getDonationListMatch(), validatedData.getPersonListMatch());
		
		conflictTable.revalidate();
		newTable.revalidate();
		matchTable.revalidate();
	}

	public void saveValidation(){
		try {
			
			validatedData.checkPersonDoublesInNewEntries();
			
			donationService.setImportToNull(validatedData.getDonationListNew());
			donationService.setImportToNull(validatedData.getDonationListMatch());
			
			updateDonationList(validatedData.getDonationListNew());
			updateDonationList(validatedData.getDonationListMatch());
			
			deletePersonList(validatedData.getPersonsToDelete());
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
	}
	
	private void updateDonationList(List<Donation> donations){
		for(Donation d : donations){
			try {
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
	
	public void returnTo() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		overview.removeAll();
		overview.revalidate();
		overview.repaint();
		overview.setUp();
	}
	
}

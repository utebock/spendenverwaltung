package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.AbstractValidationTableModel;
import at.fraubock.spendenverwaltung.gui.AssignPerson;
import at.fraubock.spendenverwaltung.gui.ComponentBuilder;
import at.fraubock.spendenverwaltung.gui.ConflictValidationTableModel;
import at.fraubock.spendenverwaltung.gui.MatchValidationTableModel;
import at.fraubock.spendenverwaltung.gui.NewValidationTableModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Import;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IImportService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.ImportValidator;
import at.fraubock.spendenverwaltung.util.ValidatedData;

public class ImportValidationView extends InitializableView {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(FindPersonsView.class);
	private IPersonService personService;
	private IDonationService donationService;
	private IImportService importService;
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
	private List<Person> personList;
	List<Import> imports;
	private ImportValidator importValidator;
	private ValidatedData validatedData;
	private JLabel newLabel;
	private JLabel conflictLabel;
	private JLabel matchLabel;
	@SuppressWarnings("rawtypes")
	private JComboBox conflictComboBox;
	@SuppressWarnings("rawtypes")
	private JComboBox importComboBox;
	private JLabel infoLabel;
	private ViewActionFactory actionFactory;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ImportValidationView(IPersonService personService, IAddressService addressService, IDonationService donationService, IImportService importService, ComponentFactory componentFactory, ViewActionFactory actionFactory){
		setLayout(new MigLayout());
		
		this.builder = new ComponentBuilder();
		this.importValidator = new ImportValidator(personService, donationService);
		this.validatedData = new ValidatedData();
		this.personService = personService;
		this.donationService = donationService;
		this.importService = importService;
		this.actionFactory = actionFactory;

		conflictComboBox = new JComboBox(ImportValidator.ValidationType.toArray());
		
		this.conflictModel = new ConflictValidationTableModel(personService, addressService, donationService, this);
		this.newModel = new NewValidationTableModel(personService, addressService, donationService, this);
		this.matchModel = new MatchValidationTableModel(personService, addressService, donationService, this);
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init(){
		
		conflictPanel = builder.createPanel(1200,250);
		newPanel = builder.createPanel(1200,250);
		matchPanel = builder.createPanel(1200,250);
		
		conflictTable = new JTable(conflictModel);
		newTable = new JTable(newModel);
		matchTable = new JTable(matchModel);
		
		conflictTable.getColumnModel().getColumn(13).setCellEditor(new DefaultCellEditor(conflictComboBox));
		newTable.getColumnModel().getColumn(13).setCellEditor(new DefaultCellEditor(conflictComboBox));
		matchTable.getColumnModel().getColumn(13).setCellEditor(new DefaultCellEditor(conflictComboBox));
		
		
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
		
		try {
			imports = importService.getAllUnconfirmed();
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		
		if(imports.size() > 0){
			importComboBox = new JComboBox();
			int counter = 1;
			for(Import i : imports){
				importComboBox.addItem(counter + ": " + i.getSource() + ": " + i.getImportDate());
				counter ++;
			}
	
			importComboBox.addActionListener(new ImportComboBoxListener(this));
			add(importComboBox, "wrap");
			
			conflictLabel = builder.createLabel("Konflikte:");
			conflictPanel.add(conflictLabel, "wrap");
			
			newLabel = builder.createLabel("Liste neuer Spender:");
			newPanel.add(newLabel, "wrap");
			
			matchLabel = builder.createLabel("Liste zugewiesener Spender:");
			matchPanel.add(matchLabel, "wrap");
			
			conflictPanel.add(conflictPane, "wrap, growx");
			newPanel.add(newPane, "wrap, growx");
			matchPanel.add(matchPane, "wrap, growx, span 2");
	
			SaveButtonListener saveAction = new SaveButtonListener();
			saveAction.putValue(Action.NAME, "Speichern");
			saveBtn = new JButton();
			saveBtn.setAction(saveAction);
			matchPanel.add(saveBtn, "");
	
			CancelButtonListener cancelAction = new CancelButtonListener();
			cancelAction.putValue(Action.NAME, "Abbrechen");
			cancelAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backInButton.png")));
			backBtn = new JButton();
			backBtn.setAction(cancelAction);
			matchPanel.add(backBtn, "");
	
			this.add(conflictPanel, "wrap, growx");
			this.add(newPanel, "wrap, growx");
			this.add(matchPanel, "wrap, growx");
			
			if(imports.size() > 0){
				try {
					getData(donationService.getUnconfirmed(imports.get(0)));
				} catch (ServiceException e) {
					JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
				    e.printStackTrace();
				    return;
				}
			}
		} else{
			infoLabel = new JLabel("Keine Importe zu validieren");
			infoLabel.setFont(new Font("bigger", Font.PLAIN, 14));
			this.add(infoLabel, "wrap 30px, gap 40");
			
			CancelButtonListener cancelAction = new CancelButtonListener();
			cancelAction.putValue(Action.NAME, "Zur\u00FCck");
			cancelAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backInButton.png")));
			backBtn = new JButton();
			backBtn.setAction(cancelAction);
			backBtn.setFont(new Font("bigger", Font.PLAIN, 13));
			this.add(backBtn, "gap 40");
		}
	}
	
	private void getData(List<Donation> donationList){
		//donationList = new ArrayList<Donation>();
		personList = new ArrayList<Person>();

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

		conflictModel.removeAll();

		newModel.removeAll();

		matchModel.removeAll();
		
		conflictModel.addList(validatedData.getDonationListConflict());
		newModel.addList(validatedData.getDonationListNew());
		matchModel.addList(validatedData.getDonationListMatch());
		
		conflictTable.revalidate();
		newTable.revalidate();
		matchTable.revalidate();
	}

	public void saveValidation(){
		List<Donation> newModelDonations = newModel.getDonationList();
		List<Donation> matchModelDonations = matchModel.getDonationList();
		List<Donation> conflictModelDonations = conflictModel.getDonationList();
		List<Person> newModelDelete;
		
		try {
			
			
			//check if there are new donations from the same donator.
			newModelDelete = importValidator.checkPersonDoublesInNewEntries(newModelDonations);

			deleteDonations(newModel.getNoImportList());
			deleteDonations(matchModel.getNoImportList());
			
			//sets new donations anonym and deletes from database
			setDonationsAnonym(newModel.getAnonymList());
			donationService.setImportToNull(newModel.getAnonymList());
			
			
			//set matched donations anonym and deletes from database
			setDonationsAnonym(matchModel.getAnonymList());
			donationService.setImportToNull(matchModel.getAnonymList());
			
			
			
			updateDonationList(newModelDonations);
			updateDonationList(matchModelDonations);
			
			//set to null, so persons will be created again for conflicts
			conflictModel.setPersonIdToNull();
			updateDonationList(conflictModelDonations);

			//update new and matched donations
			donationService.setImportToNull(newModelDonations);
			donationService.setImportToNull(matchModelDonations);
			
			
			deletePersonList(newModelDelete);
			deletePersonList(validatedData.getPersonsToDelete());
			deletePersonList(conflictModel.getPersonsToDelete());
			
			
			setDonationsAnonym(conflictModel.getAnonymList());
			donationService.setImportToNull(conflictModel.getAnonymList());
			
			deleteDonations(conflictModel.getNoImportList());
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		
		
		//Import is saved, go back to main menu. if there are another imports to validate, don't go back, but show the next import
		int conflicts = conflictModel.getDonationList().size() - conflictModel.getNoImportList().size() - conflictModel.getAnonymList().size();
		
		if(newModel.getDonationList().size() >= 1 || matchModel.getDonationList().size() >= 1){
			if(conflicts == 0)
				JOptionPane.showMessageDialog(this, "Import erfolgreich validiert", "Information", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "Import erfolgreich validiert. Es bestehen noch weiterhin " + conflicts + " Konflikte", "Information", JOptionPane.INFORMATION_MESSAGE);
		} else{
			if(conflicts == 0)
				JOptionPane.showMessageDialog(this, "Nichts zu importieren.", "Information", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, "Es bestehen noch weiterhin " + conflicts + " Konflikte", "Information", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(conflicts == 0){
			if(imports.size() == 1){
				returnToMain();
			} else if(imports.size() > 1) {
				this.removeAll();
				this.revalidate();
				init();
				return;
			}
					
		} else{
			try {
				getData(donationService.getUnconfirmed(imports.get(importComboBox.getSelectedIndex())));
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
			    e.printStackTrace();
			    return;
			}
		}
		
	}
	
	private void updateDonationList(List<Donation> donations){
		for(Donation d : donations){
			try {
				if(d.getDonator() != null)
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
	
	private void setDonationsAnonym(List<Donation> donations){
		Person oldPerson;
		
		for(Donation d : donations){
			oldPerson = d.getDonator();
			d.setDonator(null);
			try {
				donationService.update(d);
				
				if(oldPerson != null){
					List<Donation> donationsFromPerson = donationService.getByPerson(oldPerson);
				
					if(donationsFromPerson.size() == 0){
						personService.delete(oldPerson);
					}
				}
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
			    e.printStackTrace();
			    return;
			}
		}
	}
	
	private void deleteDonations(List<Donation> donations){
		Person oldPerson;
		
		for(Donation d : donations){
			oldPerson = d.getDonator();
			d.setDonator(null);
			try {
				if(oldPerson != null){
					List<Donation> donationsFromPerson = donationService.getByPerson(oldPerson);
				
					if(donationsFromPerson.size() == 0){
						personService.delete(oldPerson);
					}
					donationService.delete(d);
				}
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
			    e.printStackTrace();
			    return;
			}
		}
	}
	
	public void openPersonDialog(Donation donationToAssign, AbstractValidationTableModel deleteFromModel){
		new AssignPerson(personService, donationService, donationToAssign, matchModel, deleteFromModel);
	}
	
	public void returnTo() {
		Action switchToMenu = actionFactory.getMainMenuViewAction();
		switchToMenu.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}
	

	
	public void addToConflict(Donation donation){
		conflictModel.addDonation(donation);
		conflictModel.fireTableDataChanged();
	}
	
	public void addToMatch(Donation donation){
		matchModel.addDonation(donation);
		matchModel.fireTableDataChanged();
	}
	
	public void addToNew(Donation donation){
		newModel.addDonation(donation);
		newModel.fireTableDataChanged();
	}
	
	public void returnToMain(){
		Action switchToMenu = actionFactory.getMainMenuViewAction();
		switchToMenu.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}
	
	private class ImportComboBoxListener implements ActionListener{

		ImportValidationView parent;
		
		public ImportComboBoxListener(ImportValidationView parent){
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = importComboBox.getSelectedIndex();
			Import currentImport = imports.get(index);
			try {
				getData(donationService.getUnconfirmed(currentImport));
			} catch (ServiceException ex) {
				JOptionPane.showMessageDialog(parent, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
			    ex.printStackTrace();
			    return;
			}
		}
		
	}
	
	private class SaveButtonListener extends AbstractAction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveValidation();
		}
	}
	
	private class CancelButtonListener extends AbstractAction{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			returnToMain();
		}
	}
}
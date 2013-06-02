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

public class ImportValidation extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ShowPersons.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private Overview overview;
	private ValidationTableModel validationModel;
	private JScrollPane validationPane;
	private JTable validationTable;
	private JPanel validationPanel;
	private ComponentBuilder builder;
	private JButton backBtn;
	private ButtonListener buttonListener;
	private List<Donation> donationList;
	private List<Person> personList;

	public ImportValidation(IPersonService personService, IAddressService addressService, IDonationService donationService, Overview overview){
		super(new MigLayout());
		
		this.builder = new ComponentBuilder();
		this.buttonListener = new ButtonListener(this);
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.overview = overview;
		this.validationModel = new ValidationTableModel(this, this.donationService, this.personService);
		//initTable();
		setUp();
	}
	
	private void setUp(){
		
		validationPanel = builder.createPanel(800,250);
		
		validationTable = new JTable(validationModel);
		validationTable.setFillsViewportHeight(true);
		validationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		validationPane = new JScrollPane(validationTable);
		validationPane.setPreferredSize(new Dimension(800, 200));
		validationPanel.add(validationPane, "wrap, growx");

		backBtn = builder.createButton("Abbrechen", buttonListener, "return_from_import_validation_to_overview");
		validationPanel.add(backBtn, "");

		this.add(validationPanel);
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
		
		validationModel.addList(donationList, personList);
		validationTable.revalidate();
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

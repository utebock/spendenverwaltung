package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.views.ImportValidationView;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.ImportValidator.ValidationType;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

public class AssignPerson extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AssignPerson.class);
	private IPersonService personService;
	private IDonationService donationService;
	private ImportValidationView overview;
	private ComponentBuilder builder;
	private PersonTableModel personModel;
	private JTable showTable;
	private JScrollPane scrollPane;
	private JButton searchBtn;
	private JButton cancelBtn;
	private JLabel labelSurname;
	private JLabel labelGivenname;
	private JTextField tbSurname;
	private JTextField tbGivenname;
	private Donation donationToAssign;
	private MatchValidationTableModel matchModel;
	private AbstractValidationTableModel deleteFromModel;

	public AssignPerson(IPersonService personService, IDonationService donationService, Donation donationToAssign, MatchValidationTableModel matchModel, AbstractValidationTableModel deleteFromModel) {

		this.personService = personService;
		this.donationService = donationService;
		this.donationToAssign = donationToAssign;
		this.matchModel = matchModel;
		this.deleteFromModel = deleteFromModel;
		initTable();
		setUp();
	}

	public void initTable() {
		personModel = new PersonTableModel();
		showTable = new JTable(personModel);
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(800, 800));

	}

	public void setUp() {
		builder = new ComponentBuilder();

		setLayout(new MigLayout());
		setPreferredSize(new Dimension(800, 850));


		labelGivenname = builder.createLabel("Vorname:");
		labelSurname = builder.createLabel("Nachname:");
		tbSurname = builder.createTextField("");
		tbGivenname = builder.createTextField("");
		
		searchBtn = new JButton("Suche");
		searchBtn.addActionListener(new SearchListener());
		
		cancelBtn = new JButton("Abbrechen");
		cancelBtn.addActionListener(new CancelListener());
		
		showTable.addMouseListener(new DoubleClickListener(this));
		
		add(labelGivenname, "");
		add(tbGivenname, "w 130!");
		add(labelSurname, "");
		add(tbSurname, "w 130!");
		add(searchBtn, "wrap");
		
		add(scrollPane, "span 5, wrap");
		
		add(cancelBtn, "");
		
		pack();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		setVisible(true);
		setAlwaysOnTop(true);
	}

	public PersonTableModel getPersonModel() {
		return this.personModel;
	}

	public void returnTo() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		overview.removeAll();
		overview.revalidate();
		overview.repaint();
	}
	
	private void search(){
		personModel.clear();
		
		String surname = tbSurname.getText();
		String givenname = tbGivenname.getText();
		
		if(surname.equals("") && givenname.equals(""))
			return;
		
		List<Person> returnedList;
		Filter filter = new Filter();
		ConnectedCriterion searchCriterion = new ConnectedCriterion();
		
		filter.setType(FilterType.PERSON);
		
		PropertyCriterion criterionSurName = new PropertyCriterion();
		criterionSurName.compare(FilterProperty.PERSON_SURNAME, RelationalOperator.LIKE, surname);
		
		PropertyCriterion criterionGivenName = new PropertyCriterion();
		criterionGivenName.compare(FilterProperty.PERSON_GIVENNAME, RelationalOperator.LIKE, givenname);
		
		searchCriterion.connect(criterionSurName, LogicalOperator.AND, criterionGivenName);
		
		filter.setCriterion(searchCriterion);
		
		try {
			returnedList = personService.getByFilter(filter);
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this, "An error occured. Please see console for further information", "Error", JOptionPane.ERROR_MESSAGE);
		    e.printStackTrace();
		    return;
		}
		
		for(Person p : returnedList){
			personModel.addPerson(p);
		}
		
		personModel.fireTableDataChanged();
	}

	private class SearchListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			search();
		}
	}

	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			deleteFromModel.setComboBox(donationToAssign, ValidationType.EDIT);
			dispose();
		}
	}
	
	private class DoubleClickListener implements MouseListener{
		private JDialog assignPerson;
		
		public DoubleClickListener(JDialog assignPerson){
			this.assignPerson = assignPerson;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2){
				Person oldDonator = new Person();
				oldDonator.setId(donationToAssign.getDonator().getId());
				oldDonator.setAddresses(donationToAssign.getDonator().getAddresses());
				oldDonator.setCompany(donationToAssign.getDonator().getCompany());
				oldDonator.setSex(donationToAssign.getDonator().getSex());
				oldDonator.setSurname(donationToAssign.getDonator().getSurname());
				
				donationToAssign.setDonator(personModel.getPersonRow(showTable.getSelectedRow()));
				
				try{
					if(donationService.donationExists(donationToAssign)){
						donationToAssign.setDonator(oldDonator);
						JOptionPane.showMessageDialog(assignPerson, "Diese Spende existiert für diese Person bereits. Bitte weisen Sie die Spende einer anderen Person zu", "Information", JOptionPane.INFORMATION_MESSAGE);
						return;
					} else{
						matchModel.addDonation(donationToAssign);
						deleteFromModel.removeDonation(donationToAssign);
						
						matchModel.fireTableDataChanged();
						
						dispose();
						JOptionPane.showMessageDialog(assignPerson, "Spende wurde erfolgreich zugewiesen", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch(ServiceException ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(assignPerson, "Fehler beim Zuweisen der Spende. Bitte probieren Sie es später erneut!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
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
}

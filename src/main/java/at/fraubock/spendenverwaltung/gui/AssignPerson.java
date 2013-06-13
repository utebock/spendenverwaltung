package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.filter.ConfiguratorFactory;
import at.fraubock.spendenverwaltung.gui.filter.CreateFilter;
import at.fraubock.spendenverwaltung.interfaces.domain.Donation;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.to.FilterTO;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.FilterProperty;
import at.fraubock.spendenverwaltung.util.FilterType;
import at.fraubock.spendenverwaltung.util.LogicalOperator;
import at.fraubock.spendenverwaltung.util.RelationalOperator;

public class AssignPerson extends JDialog {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ShowPersons.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private IFilterService filterService;
	private ImportValidation overview;
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
	private JButton searchBtn;
	private ActionHandler handler;
	private JComboBox<Filter> filterCombo;
	private JButton backButton;
	private JPanel overviewPanel;
	private JLabel labelSurname;
	private JLabel labelGivenname;
	private JTextField tbSurname;
	private JTextField tbGivenname;
	private JLabel empty;
	private Filter showAllFilter;
	private Donation donationToAssign;
	private ImportValidation parent;
	private MatchValidationTableModel matchModel;
	private IValidationTableModel deleteFromModel;

	public AssignPerson(IPersonService personService, ImportValidation parent, Donation donationToAssign, MatchValidationTableModel matchModel, IValidationTableModel deleteFromModel) {

		this.personService = personService;
		this.donationToAssign = donationToAssign;
		this.parent = parent;
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
		//handler = new ActionHandler(this);
		//buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();

		setLayout(new MigLayout());
		setPreferredSize(new Dimension(800, 850));
		// JScrollPane pane = new JScrollPane(overviewPanel,
		// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


		labelGivenname = builder.createLabel("Vorname:");
		labelSurname = builder.createLabel("Nachname:");
		tbSurname = builder.createTextField("");
		tbGivenname = builder.createTextField("");
		
		searchBtn = new JButton("Suche");
		searchBtn.addActionListener(new SearchListener());
		
		showTable.addMouseListener(new DoubleClickListener());
		
		add(labelGivenname, "");
		add(tbGivenname, "w 130!");
		add(labelSurname, "");
		add(tbSurname, "w 130!");
		add(searchBtn, "wrap");
		
		add(scrollPane, "span 5");
		
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
		//overview.setUp();
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
	
	private class DoubleClickListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2){
				donationToAssign.setDonator(personModel.getPersonRow(showTable.getSelectedRow()));
				matchModel.addDonation(donationToAssign);
				deleteFromModel.removeDonation(donationToAssign);
				
				matchModel.fireTableDataChanged();
				dispose();
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
package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.Criterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.FilterType;

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

	public AssignPerson(IPersonService personService,
			IAddressService addressService, IDonationService donationService, IFilterService filterService, ImportValidation importValidation) {

		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.filterService = filterService;
		this.overview = importValidation;
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


		labelSurname = builder.createLabel("Vorname:");
		labelGivenname = builder.createLabel("Nachname:");
		tbSurname = builder.createTextField("");
		tbGivenname = builder.createTextField("");
		
		searchBtn = new JButton("Suche");
		searchBtn.addActionListener(new SearchListener());
		
		add(labelSurname, "");
		add(tbSurname, "w 130!");
		add(labelGivenname, "");
		add(tbGivenname, "w 130!");
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
	
	private List<Person> search(){
		String surname = tbSurname.getText();
		String givenname = tbGivenname.getText();
			
		
		return new ArrayList<Person>();
	}

	private class SearchListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
}
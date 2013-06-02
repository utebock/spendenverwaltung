package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.filter.CreateFilter;
import at.fraubock.spendenverwaltung.gui.filter.IPropertySelectorProvider;
import at.fraubock.spendenverwaltung.gui.filter.PropertyComponent;
import at.fraubock.spendenverwaltung.gui.filter.SelectorFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.FilterType;

public class FilterOverview extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FilterOverview.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private IFilterService filterService;
	private Overview overview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;

	private FilterTableModel filterModel;
	private JTable showTable;
	private JScrollPane scrollPane;
	private List<Filter> filterList;
	private JPanel panel;
	private JMenuBar menubar;
	private Container chooseFilter;
	private JMenu editFilter;
	private Component chooseMenuItem;
	private JMenuItem editItem;
	private JMenuItem deleteItem;
	private JToolBar toolbar;
	private JButton personFilter;
	private JButton sendingsFilter;
	private JButton donationFilter;
	private JButton backButton;
	private JButton delete;

	public FilterOverview(IFilterService filterService,
			IPersonService personService, IAddressService addressService,
			IDonationService donationService, Overview overview) {
		super(new MigLayout());

		this.filterService = filterService;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.overview = overview;
		// initTable();
		setUp();
	}

	public void initTable() {
		filterModel = new FilterTableModel();
		getFilter();
		showTable = new JTable(filterModel);
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(800, 800));

	}

	public void setUp() {
		new ActionHandler(this);
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		panel = builder.createPanel(800, 850);
		this.add(panel);

		toolbar = builder.createToolbar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		addComponentsToToolbar(toolbar);
		panel.add(toolbar, "growx, wrap");
		//panel.add(scrollPane);
	}

	private void addComponentsToToolbar(JToolBar toolbar) {
		personFilter = builder.createButton("Personenfilter erstellen", buttonListener, "add_person_filter");
		personFilter.setFont(new Font("Bigger", Font.PLAIN, 13));
		sendingsFilter = builder.createButton("Aussendungsfilter erstellen", buttonListener, "add_sendings_filter");
		sendingsFilter.setFont(new Font("Bigger", Font.PLAIN, 13));
		donationFilter = builder.createButton("Spendenfilter erstellen", buttonListener, "add_donation_filter");
		donationFilter.setFont(new Font("Bigger", Font.PLAIN, 13));
		delete = builder.createButton("Filter l\u00F6schen", buttonListener, "delete_filter");
		delete.setFont(new Font("Bigger", Font.PLAIN, 13));
		backButton = builder.createButton("Zur\u00FCck", buttonListener, "return_to_overview");
		backButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		toolbar.add(sendingsFilter);
		toolbar.add(personFilter);
		toolbar.add(donationFilter);
		toolbar.add(delete);
		toolbar.add(backButton);
		
	}

	public FilterTableModel getFilterModel() {
		return this.filterModel;
	}

	private void getFilter() {
		filterList = new ArrayList<Filter>();

		try {
			filterList = filterService.getAll();
			log.info("List " + filterList.size() + " filter");
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"An error occured. Please see console for further information",
							"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		if (filterList == null) {
			JOptionPane.showMessageDialog(this, "GetAll() returns null.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		for (Filter f : filterList) {
			filterModel.addFilter(f);
		}
	}

	public void createFilter() {
		IPropertySelectorProvider personModelReceiver = new IPropertySelectorProvider() {

			@Override
			public List<PropertyComponent> receiveModels() {
				return SelectorFactory.propertySelectorForPerson();
			}

		};

		CreateFilter cf = new CreateFilter(FilterType.PERSON, "Personen",
				personModelReceiver, filterService, this);
		removeAll();
		revalidate();
		repaint();
		add(cf);
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
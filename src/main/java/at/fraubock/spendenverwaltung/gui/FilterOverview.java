package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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
		panel = builder.createPanel(800, 800);
		this.add(panel);

		menubar = builder.createMenuBar();
		addComponentsToMenuBar(menubar);
		panel.add(menubar, ", growx, wrap");
	}

	private void addComponentsToMenuBar(JMenuBar menubar) {
		chooseFilter = builder.createMenu("Neuen Filter anlegen");
		chooseMenuItem = builder.createMenuItem("Personenfilter", buttonListener, "personFilter_clicked");
		chooseFilter.add(chooseMenuItem);
		
		editFilter = builder.createMenu("Filter bearbeiten");
		editItem = builder.createMenuItem("Bearbeiten", buttonListener, "edit_filter");
		deleteItem = builder.createMenuItem("L\u00F6schen", buttonListener, "delete_filter");
		//editFilter.add(editItem);
		editFilter.add(deleteItem);
		
		menubar.add(chooseFilter);
		menubar.add(editFilter);
		
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
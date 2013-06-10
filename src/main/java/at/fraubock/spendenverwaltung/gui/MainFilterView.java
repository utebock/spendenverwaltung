package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
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

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.filter.CreateFilter;
import at.fraubock.spendenverwaltung.gui.views.InitializableView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.FilterInUseException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.FilterType;

public class MainFilterView extends InitializableView {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(MainFilterView.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private IFilterService filterService;
	private Overview overview;
	private ComponentBuilder builder;
	private ButtonListener buttonListener;
	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	

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
	private JButton edit;
	private JButton delete;
	private JLabel empty;

	public MainFilterView(ComponentFactory componentFactory,
			ViewActionFactory viewActionFactory, IFilterService filterService) {

		this.setLayout(new MigLayout());
		this.filterService = filterService;
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
	}

	public void initTable() {
		filterModel = new FilterTableModel();
		getFilter();
		showTable = new JTable(filterModel);
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(800, 800));
		add(scrollPane);

	}

	public void init() {
		// new ActionHandler(this);
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();
		panel = builder.createPanel(800, 850);
		this.add(panel);

		toolbar = builder.createToolbar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		addComponentsToToolbar(toolbar);
		panel.add(toolbar, "growx, wrap");
		empty = builder.createLabel("		 ");
		panel.add(empty, "wrap");
		initTable();
		panel.add(scrollPane);
	}

	private void addComponentsToToolbar(JToolBar toolbar) {
		personFilter = builder.createButton(
				"<html>&nbsp;Personenfilter erstellen</html>", buttonListener,
				"add_filter");
		personFilter.setFont(new Font("Bigger", Font.PLAIN, 13));
		sendingsFilter = builder.createButton(
				"<html>&nbsp;Aussendungsfilter erstellen</html>",
				buttonListener, "add_filter");
		sendingsFilter.setFont(new Font("Bigger", Font.PLAIN, 13));
		donationFilter = builder.createButton(
				"<html>&nbsp;Spendenfilter erstellen</html>", buttonListener,
				"add_filter");
		donationFilter.setFont(new Font("Bigger", Font.PLAIN, 13));
		edit = builder.createButton("<html>&nbsp;Filter bearbeiten</html>",
				buttonListener, "edit_filter");
		edit.setFont(new Font("Bigger", Font.PLAIN, 13));
		delete = builder.createButton("<html>&nbsp;Filter l\u00F6schen</html>",
				buttonListener, "delete_filter");
		delete.setFont(new Font("Bigger", Font.PLAIN, 13));
//		backButton = builder.createButton("<html>&nbsp;Zur\u00FCck</html>",
//				buttonListener, "return_to_overview");
//		backButton.setFont(new Font("Bigger", Font.PLAIN, 13));

		backButton = new JButton("Zur\u00FCck");
		backButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		backButton.setAction(viewActionFactory.getMainMenuViewAction());
		toolbar.add(personFilter);
		toolbar.add(donationFilter);
		toolbar.add(sendingsFilter);
		toolbar.add(edit);
		toolbar.add(delete);
		toolbar.add(backButton, "wrap");

	}

	public FilterTableModel getFilterModel() {
		return this.filterModel;
	}

	private void getFilter() {
		filterList = new ArrayList<Filter>();

		try {
			filterList = filterService.getAllByAnonymous(false);
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

	public void createFilter(JButton button) {
		FilterType type = null;
		Filter filter = null;
		if (button == personFilter) {
			type = FilterType.PERSON;
		} else if (button == donationFilter) {
			type = FilterType.DONATION;
		} else if (button == sendingsFilter) {
			type = FilterType.MAILING;
		} else if (button == edit) {
			if (showTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this,
						"Bitte Filter zum Bearbeiten ausw\u00E4hlen.");
				return;
			}
			filter = filterModel.getFilterRow(showTable.getSelectedRow());
			type = filter.getType();
		}

		CreateFilter cf = new CreateFilter(type, filterService, this, filter);
		removeAll();
		revalidate();
		repaint();
		add(cf);
	}

	public void deleteFilter() {
		if (showTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this,
					"Bitte Filter zum L\u00F6schen ausw\u00E4hlen.");
			return;
		}

		Filter filter = filterModel.getFilterRow(showTable.getSelectedRow());
		int answer = JOptionPane.showConfirmDialog(this,
				"Wollen Sie den Filter " + filter.getName()
						+ " wirklich l\u00F6schen?");

		if (answer != 0) {
			return;
		}

		try {
			filterService.delete(filter);
			filterModel.removeFilter(showTable.getSelectedRow());
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarter Fehler ist aufgetreten!", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			log.error(e);
		} catch (FilterInUseException e) {
			JOptionPane.showMessageDialog(this,
					"Dieser Filter ist mit einem anderen verkn\u00FCpft. "
							+ "Bitte l\u00F6schen Sie die Verkn\u00FCpfung zuerst.",
					"Fehler", JOptionPane.ERROR_MESSAGE);
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
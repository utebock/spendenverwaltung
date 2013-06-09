package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.FilterType;

public class ShowPersons extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ShowPersons.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private IFilterService filterService;
	private Overview overview;
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
	private ActionHandler handler;
	private JComboBox<Filter> filterCombo;
	private JButton backButton;
	private JPanel overviewPanel;
	private JLabel label;
	private JLabel empty;
	private Filter showAllFilter;

	public ShowPersons(IPersonService personService,
			IAddressService addressService, IDonationService donationService,
			IFilterService filterService, Overview overview) {
		super(new MigLayout());

		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.filterService = filterService;
		this.overview = overview;
		initTable();
		setUp();
	}

	public void initTable() {
		personModel = new PersonTableModel();
		showAllFilter = new Filter(FilterType.PERSON, null, "Alle anzeigen");
		getPersons(showAllFilter);
		showTable = new JTable(personModel);
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(800, 800));

	}

	public void setUp() {
		handler = new ActionHandler(this);
		buttonListener = new ButtonListener(this);
		builder = new ComponentBuilder();

		overviewPanel = builder.createPanel(800, 850);
		// JScrollPane pane = new JScrollPane(overviewPanel,
		// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(overviewPanel);

		toolbar = builder.createToolbar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		addComponentsToToolbar(toolbar);

		overviewPanel.add(toolbar, "growx, span, wrap");

		label = builder.createLabel("Filter ausw\u00E4hlen: ");
		overviewPanel.add(label, "split2");

		List<Filter> personFilters = new ArrayList<Filter>();
		personFilters.add(showAllFilter);
		try {
			personFilters.addAll(filterService
					.getAllByFilter(FilterType.PERSON));
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"Ein unerwarteter Fehler ist aufgetreten.",
							"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		filterCombo = builder.createComboBox(new SimpleComboBoxModel<Filter>(
				personFilters), new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getPersons((Filter) filterCombo.getModel().getSelectedItem());

			}

		});

		overviewPanel.add(filterCombo, "growx, wrap");
		empty = builder.createLabel("			");
		overviewPanel.add(empty, "wrap");
		overviewPanel.add(scrollPane);
	}

	private void addComponentsToToolbar(JToolBar toolbar) {

		addAttribute = builder.createButton(
				"<html>&nbsp;Attribute hinzuf\u00FCgen&nbsp;</html>",
				buttonListener, "add_donation_address");
		addAttribute.setFont(new Font("Bigger", Font.PLAIN, 13));
		editButton = builder.createButton(
				"<html>&nbsp;Person bearbeiten</html>", buttonListener,
				"edit_person");
		editButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		deleteButton = builder.createButton(
				"<html>&nbsp;Person l\u00F6schen</html>", buttonListener,
				"delete_person_from_db");
		deleteButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		backButton = builder.createButton("<html>&nbsp;Zur\u00FCck</html>",
				buttonListener, "return_to_personOverview");
		backButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		toolbar.add(addAttribute, "split 4, growx");
		toolbar.add(editButton, "growx");
		toolbar.add(deleteButton, "growx");
		toolbar.add(backButton, "growx");
	}

	public PersonTableModel getPersonModel() {
		return this.personModel;
	}

	private void getPersons(Filter filter) {
		personList = new ArrayList<Person>();

		try {
			if (filter == null) {
				personList = personService.getAll();
			} else {
				personList = personService.getByFilter(filter);
			}
			log.info("List " + personList.size() + " persons");
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"An error occured. Please see console for further information",
							"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		if (personList == null) {
			JOptionPane.showMessageDialog(this, "GetAll() returns null.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		personModel.clear();
		for (Person p : personList) {
			personModel.addPerson(p);
		}
	}

	public void addAttributes() {
		Person p;
		int row = showTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Bitte Person ausw\u00E4hlen.");
			return;
		}

		int id = (Integer) personModel.getValueAt(row, 3);

		try {
			p = personService.getById(id);
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"An error occured. Please see console for further information",
							"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		AddAttributes sp = new AddAttributes(p, personService, addressService,
				donationService, this);
		removeAll();
		revalidate();
		repaint();
		add(sp);
	}

	public void deletePerson() {
		Person p;
		int row = showTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this,
					"Bitte Person zum L\u00F6schen ausw\u00E4hlen.");
			return;
		}

		int id = (Integer) personModel.getValueAt(row, 3);

		try {
			p = personService.getById(id);
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"An error occured. Please see console for further information",
							"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}

		Object[] options = { "Abbrechen", "L\u00F6schen" };
		int ok = JOptionPane.showOptionDialog(this,
				"Diese Person sicher l\u00F6schen?", "Loeschen",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				options, options[1]);

		if (ok == 1) {
			try {
				personService.delete(p);
			} catch (ServiceException e) {
				JOptionPane
						.showMessageDialog(
								this,
								"An error occured. Please see console for further information",
								"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return;
			}

			personModel.removePerson(row);
			JOptionPane.showMessageDialog(this, "Person wurde gel\u00F6scht.",
					"Information", JOptionPane.INFORMATION_MESSAGE);

		} else {
			return;
		}
	}

	public void editPerson() {
		Person p;
		int row = showTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this,
					"Bitte Person zum Bearbeiten ausw\u00E4hlen.");
			return;
		}

		int id = (Integer) personModel.getValueAt(row, 3);

		try {
			p = personService.getById(id);
		} catch (ServiceException e) {
			JOptionPane
					.showMessageDialog(
							this,
							"An error occured. Please see console for further information",
							"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}

		EditPerson ep = new EditPerson(p, personService, addressService, this,
				overview);
		removeAll();
		revalidate();
		repaint();
		add(ep);
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
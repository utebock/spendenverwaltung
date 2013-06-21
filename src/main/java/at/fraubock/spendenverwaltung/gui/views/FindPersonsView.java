package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.ConnectedCriterion;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.criterion.PropertyCriterion;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.filter.FilterProperty;
import at.fraubock.spendenverwaltung.util.filter.FilterType;
import at.fraubock.spendenverwaltung.util.filter.LogicalOperator;
import at.fraubock.spendenverwaltung.util.filter.RelationalOperator;

public class FindPersonsView extends InitializableView {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FindPersonsView.class);
	private IPersonService personService;
	private IAddressService addressService;
	private IDonationService donationService;
	private IFilterService filterService;
	private PersonTableModel personModel;
	private JTable showTable;
	private JScrollPane scrollPane;
	private List<Person> personList;
	private JToolBar toolbar;
	private JButton editButton, deleteButton, viewAddressesButton,
			viewDonationsButton;
	private JComboBox<Filter> filterCombo;
	private JButton backButton;
	private JPanel overviewPanel;
	private JLabel label;
	private JLabel empty;
	private JLabel feedbackLabel;
	private JLabel quickSearchLabel;
	private JTextField quickSearchField;
	private Filter showAllFilter;
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;

	public FindPersonsView(IPersonService personService,
			IAddressService addressService, IDonationService donationService,
			IFilterService filterService, ComponentFactory componentFactory,
			ViewActionFactory viewActionFactory, PersonTableModel personModel) {

		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.filterService = filterService;
		this.personModel = personModel;
	}

	public void init() {
		this.setLayout(new MigLayout());
		overviewPanel = componentFactory.createPanel(800, 800);
		this.add(overviewPanel);

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		addComponentsToToolbar(toolbar);
		overviewPanel.add(toolbar, "growx, span, wrap");
		label = componentFactory.createLabel("Filter ausw\u00E4hlen: ");
		overviewPanel.add(label, "split2");
		initTable();
		overviewPanel.add(filterCombo, "growx, wrap");

		quickSearchLabel = componentFactory.createLabel("Schnellsuche");
		quickSearchField = new JTextField(30);
		quickSearchField.addActionListener(new QuickSearchAction());

		overviewPanel.add(quickSearchLabel, "split 2");
		overviewPanel.add(quickSearchField, "wrap");

		overviewPanel.add(scrollPane, "wrap");

		JButton exportButton = new JButton("Liste exportieren");
		exportButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		final JFileChooser fileChooser = new JFileChooser();
		exportButton.addActionListener(new exportActionListener(fileChooser));
		overviewPanel.add(exportButton, "wrap");

		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Bigger", Font.PLAIN, 13));

		overviewPanel.add(feedbackLabel, "wrap");
	}

	public void initTable() {
		if (personModel == null) {
			personModel = new PersonTableModel();
		}

		showTable = new JTable(personModel);
		showTable.setFillsViewportHeight(true);
		showTable.setAutoCreateRowSorter(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));

		List<Filter> personFilters = new ArrayList<Filter>();
		personFilters.add(showAllFilter);
		log.info("PersonFilter-List: " + personFilters.size());
		try {
			personFilters.addAll(filterService
					.getAllByFilter(FilterType.PERSON).a);
		} catch (ServiceException e) {
			log.warn(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		filterCombo = new JComboBox<Filter>(new SimpleComboBoxModel<Filter>(
				personFilters));
		filterCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getPersons((Filter) filterCombo.getModel().getSelectedItem());
			}
		});
	}

	private void addComponentsToToolbar(JToolBar toolbar) {

		backButton = new JButton();
		Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("/images/backButton.jpg")));
		backButton.setAction(getBack);

		viewAddressesButton = new JButton();
		viewAddressesButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ViewAddressesAction viewAddressesAction = new ViewAddressesAction();
		viewAddressesAction.putValue(Action.NAME, "Adressen anzeigen");
		viewAddressesButton.setAction(viewAddressesAction);

		viewDonationsButton = new JButton();
		viewDonationsButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ViewDonationsAction viewDonationsAction = new ViewDonationsAction();
		viewDonationsAction.putValue(Action.NAME, "Spenden anzeigen");
		viewDonationsButton.setAction(viewDonationsAction);

		editButton = new JButton();
		editButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		EditAction editAction = new EditAction();
		editAction
				.putValue(Action.NAME, "<html>&nbsp;Person bearbeiten</html>");
		editButton.setAction(editAction);

		deleteButton = new JButton();
		deleteButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		DeleteAction deleteAction = new DeleteAction();
		deleteAction.putValue(Action.NAME,
				"<html>&nbsp;Person l\u00F6schen</html>");
		deleteButton.setAction(deleteAction);

		toolbar.add(backButton, "split 4, growx");
		toolbar.add(viewAddressesButton);
		toolbar.add(viewDonationsButton);
		toolbar.add(editButton);
		toolbar.add(deleteButton);

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
							"Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
			log.error(e);
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

	private final class QuickSearchAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (quickSearchField.getText().equals("")) {
				return;
			} else {
				Filter quickSearchFilter = new Filter();
				quickSearchFilter.setType(FilterType.PERSON);

				PropertyCriterion likeFirstName = new PropertyCriterion();
				likeFirstName.compare(FilterProperty.PERSON_GIVENNAME,
						RelationalOperator.LIKE, quickSearchField.getText());

				PropertyCriterion likeSurname = new PropertyCriterion();
				likeSurname.compare(FilterProperty.PERSON_SURNAME,
						RelationalOperator.LIKE, quickSearchField.getText());

				PropertyCriterion likeCompany = new PropertyCriterion();
				likeCompany.compare(FilterProperty.PERSON_COMPANY,
						RelationalOperator.LIKE, quickSearchField.getText());

				ConnectedCriterion firstNameOrSurname = new ConnectedCriterion();
				firstNameOrSurname.connect(likeFirstName, LogicalOperator.OR,
						likeSurname);

				ConnectedCriterion companyOrNames = new ConnectedCriterion();
				companyOrNames.connect(firstNameOrSurname, LogicalOperator.OR,
						likeCompany);

				quickSearchFilter.setCriterion(companyOrNames);

				try {
					List<Person> results = personService
							.getByFilter(quickSearchFilter);
					personModel.clear();
					personModel.addAll(results);
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel
							.setText("Ein Fehler passierte während der Schnellsuche.");
				}
			}
		}
	}

	private final class exportActionListener implements ActionListener {
		private final JFileChooser fileChooser;

		private exportActionListener(JFileChooser fileChooser) {
			this.fileChooser = fileChooser;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			List<Person> persons = ((PersonTableModel) showTable.getModel())
					.getPersons();

			String csv = personService.convertToCSV(persons);

			fileChooser.setSelectedFile(new File("personen.csv"));
			fileChooser.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".csv")
							|| f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "CSV Dateien(*.csv)";
				}

			});

			if (fileChooser.showSaveDialog(FindPersonsView.this) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				FileWriter writer = null;
				try {
					writer = new FileWriter(file);
					writer.write(csv);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(FindPersonsView.this,
							"Die Datei konnte nicht beschrieben werden.",
							"Error", JOptionPane.ERROR_MESSAGE);
					log.error("File could not be written to. path='"
							+ file.getAbsolutePath() + "', text='" + csv + "'");
				}
			}
		}
	}

	// TODO CHANGE TO EDIT ONLY THE SELECTED PERSON IN A NEW JDIALOG
	private final class EditAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			// FindPersonsView findPersonsView = new
			// FindPersonsView(personService, addressService, donationService,
			// filterService, componentFactory, viewActionFactory, personModel);
			// Person p; int row = showTable.getSelectedRow();
			// if (row == -1) {
			// JOptionPane.showMessageDialog(overviewPanel,
			// "Bitte Person zum Bearbeiten ausw\u00E4hlen."); return; }
			//
			// int id = (Integer) personModel.getValueAt(row, 3);
			//
			// try {
			// p = personService.getById(id);
			// } catch (ServiceException ex) {
			// JOptionPane.showMessageDialog( overviewPanel,
			// "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
			// "Fehler", JOptionPane.ERROR_MESSAGE); ex.printStackTrace();
			// log.error(ex);
			// return;
			// }
			//
			// EditPerson ep = new EditPerson(componentFactory,
			// viewActionFactory, p, personService, addressService,
			// findPersonsView, personModel);
			// removeAll();
			// revalidate();
			// repaint();
			// add(ep);
		}
	}

	private final class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Person p;
			int row = showTable.getSelectedRow();
			if (row == -1) {
				feedbackLabel
						.setText("Bitte Person zum L\u00F6schen ausw\u00E4hlen.");
				return;
			}

			p = personModel.getPersonRow(row);

			Object[] options = { "Abbrechen", "L\u00F6schen" };
			int ok = JOptionPane.showOptionDialog(overviewPanel,
					"Diese Person sicher l\u00F6schen?", "Loeschen",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[1]);

			if (ok == 1) {
				try {
					personService.delete(p);
					personModel.removePerson(row);
					feedbackLabel.setText("Person wurde gel\u00F6scht.");
				} catch (ServiceException ex) {
					feedbackLabel
							.setText("Ein unerwarter Fehler ist während der Löschoperation aufgetreten.");
					log.warn(ex.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * this action is required in order to check whether a person has been
	 * selected before entering the other view
	 */
	private final class ViewAddressesAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Person p;
			int row = showTable.getSelectedRow();
			if (row == -1) {
				feedbackLabel
						.setText("Es muss eine Person ausgewählt werden bevor die Addressen angezeigt werden können.");
				return;
			}

			p = personModel.getPersonRow(row);

			log.debug("Person is " + p);

			Action switchView = viewActionFactory.getPersonAddressesViewAction(
					personModel, p);
			switchView.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, null));
		}

	}

	/**
	 * this action is required in order to check whether a person has been
	 * selected before entering the other view
	 */
	private final class ViewDonationsAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Person p;
			int row = showTable.getSelectedRow();
			if (row == -1) {
				feedbackLabel
						.setText("Es muss eine Person ausgewählt werden bevor die Spenden angezeigt werden können.");
				return;
			}

			p = personModel.getPersonRow(row);

			log.debug("Person is " + p);

			Action switchView = viewActionFactory.getPersonDonationsViewAction(
					personModel, p);
			switchView.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, null));
		}

	}
}
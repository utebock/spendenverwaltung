package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dialog;
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
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentConstants;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.EmailTextField;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.StringTextField;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.Person.Sex;
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

		quickSearchLabel = componentFactory.createLabel("Schnellsuche: ");
		quickSearchField = new JTextField(30);
		quickSearchField.addActionListener(new QuickSearchAction());

		overviewPanel.add(quickSearchLabel, "split 2");
		overviewPanel.add(quickSearchField, "gap 25, wrap 20px, growx");

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
					"Ein unerwarteter Fehler ist aufgetreten.", "Fehler",
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
				.getResource("/images/backButton.png")));
		backButton.setAction(getBack);

		viewAddressesButton = new JButton();
		viewAddressesButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ViewAddressesAction viewAddressesAction = new ViewAddressesAction();
		viewAddressesAction.putValue(Action.NAME,
				"<html>&nbsp;Adressen anzeigen</html>");
		viewAddressesButton.setAction(viewAddressesAction);

		viewDonationsButton = new JButton();
		viewDonationsButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ViewDonationsAction viewDonationsAction = new ViewDonationsAction();
		viewDonationsAction.putValue(Action.NAME,
				"<html>&nbsp;Spenden anzeigen</html>");
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
		toolbar.add(editButton);
		toolbar.add(deleteButton);
		toolbar.add(viewAddressesButton);
		toolbar.add(viewDonationsButton);

	}

	public PersonTableModel getPersonModel() {
		return this.personModel;
	}

	private void getPersons(Filter filter) {
		personList = new ArrayList<Person>();

		try {
			if (filter == null) {
				personList = personService.getConfirmed();
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
					// feedbackLabel
					// .setText("Ein Fehler passierte waehrend der Schnellsuche.");
					JOptionPane
							.showMessageDialog(
									overviewPanel,
									"Ein Fehler ist w\u00E4hrend der Schnellsuche aufgetreten. Bitte kontaktieren Sie Ihren Administrator.",
									"Fehler", JOptionPane.ERROR_MESSAGE);
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

	private final class EditAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private JDialog editPersonDialog;
		private JPanel editPersonPanel;

		String[] salutCombo = new String[] { "Herr", "Frau", "Fam.", "Firma" };
		JComboBox<String> salutation = new JComboBox<String>(salutCombo);

		private JLabel titleLabel;
		private StringTextField titleField;

		private JLabel companyLable;
		private StringTextField companyField;

		private JLabel givenNameLable;
		private StringTextField givenNameField;

		private JLabel surnameLable;
		private StringTextField surnameField;

		private JLabel telephoneLable;
		private StringTextField telephoneField;

		private JLabel emailLable;
		private EmailTextField emailField;

		private JLabel noteLable;
		private StringTextField noteField;

		private JLabel validationFeedbackLabel;
		private JButton submitButton;
		private JButton cancelButton;

		Person person;

		public EditAction() {
			super("<html>&nbsp;Adresse bearbeiten</html>");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int row = showTable.getSelectedRow();

			// avoid costs of initializing the frame if no address has been
			// selected
			if (row == -1) {
				// feedbackLabel.setText("Bitte die Adresse zum bearbeiten ausw\u00E4hlen.");
				JOptionPane.showMessageDialog(overviewPanel,
						"Bitte Person ausw\u00E4hlen.", "Information",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			person = personModel.getPersonRow(row);

			editPersonDialog = new JDialog(
					SwingUtilities.getWindowAncestor(overviewPanel),
					Dialog.ModalityType.APPLICATION_MODAL);
			editPersonPanel = componentFactory.createPanel(420, 360);

			JLabel salutLabel = componentFactory.createLabel("Anrede: ");
			salutation.setSelectedIndex(-1);
			editPersonPanel.add(salutLabel, "split2");
			editPersonPanel.add(salutation, "gap 28, wrap");

			titleLabel = componentFactory.createLabel("Titel:");
			titleField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			titleField.setText(person.getTitle());
			editPersonPanel.add(titleLabel, "split2");
			editPersonPanel.add(titleField, "gap 50, wrap, growx");

			companyLable = componentFactory.createLabel("Firma:");
			companyField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			companyField.setText(person.getCompany());
			editPersonPanel.add(companyLable, "split 2");
			editPersonPanel.add(companyField, "gap 43, wrap, growx");

			givenNameLable = componentFactory.createLabel("Vorname:");
			givenNameField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			givenNameField.setText(person.getGivenName());
			editPersonPanel.add(givenNameLable, "split2");
			editPersonPanel.add(givenNameField, "gap 23, wrap, growx");

			surnameLable = componentFactory.createLabel("Nachname:");
			surnameField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			surnameField.setText(person.getSurname());
			editPersonPanel.add(surnameLable, "split2");
			editPersonPanel.add(surnameField, "gap 12, wrap, growx");

			telephoneLable = componentFactory.createLabel("Telefon:");
			telephoneField = new StringTextField(ComponentConstants.MEDIUM_TEXT);
			telephoneField.setText(person.getTelephone());
			editPersonPanel.add(telephoneLable, "split2");
			editPersonPanel.add(telephoneField, "gap 32, wrap, growx");

			emailLable = componentFactory.createLabel("Email:");
			emailField = new EmailTextField(ComponentConstants.MEDIUM_TEXT);
			emailField.setText(person.getEmail());
			editPersonPanel.add(emailLable, "split2");
			editPersonPanel.add(emailField, "gap 45, wrap, growx");

			noteLable = componentFactory.createLabel("Notiz:");
			noteField = new StringTextField(ComponentConstants.LONG_TEXT);
			noteField.setText(person.getNote());
			editPersonPanel.add(noteLable, "split2");
			editPersonPanel.add(noteField, "gap 45, wrap 20px, growx");

			cancelButton = new JButton();
			cancelButton.setAction(new CancelEditAction());

			submitButton = new JButton();
			submitButton.setAction(new SubmitEditAction());
			editPersonPanel.add(submitButton, "split2");
			editPersonPanel.add(cancelButton, "wrap");

			validationFeedbackLabel = componentFactory.createLabel("");
			editPersonPanel.add(validationFeedbackLabel, "wrap");

			editPersonDialog.add(editPersonPanel);
			editPersonDialog.pack();
			editPersonDialog.setLocationRelativeTo(SwingUtilities
					.getWindowAncestor(overviewPanel));
			editPersonDialog.setVisible(true);
		}

		private final class CancelEditAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public CancelEditAction() {
				super("Abbrechen");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				editPersonDialog.dispose();
			}
		}

		private final class SubmitEditAction extends AbstractAction {

			private static final long serialVersionUID = 1L;

			public SubmitEditAction() {
				super("Speichern");
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Criteria to edit a person: GivenName, Surname, Company - one
				 * of these must be set Components must validate
				 */

				validationFeedbackLabel.setText("");
				boolean validation = true;

				if ((givenNameField.getText().equals("") || surnameField
						.getText().equals(""))
						&& companyField.getText().equals("")) {
					validation = false;
					// validationFeedbackLabel.setText("Bitte Vorname und Nachname oder Firma setzen.");
					JOptionPane.showMessageDialog(editPersonPanel,
							"Bitte Vorname und Nachname oder Firma setzen.",
							"Information", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				if (givenNameField.getText().equals("")
						&& surnameField.getText().equals("")) {
					person.setSex(Sex.COMPANY);
				} else {
					if (salutation.getSelectedIndex() != -1) {
						person.setSex(Person.Sex.values()[salutation
								.getSelectedIndex()]);
					}
				}

				if (validation && titleField.validateContents()
						&& givenNameField.validateContents()
						&& surnameField.validateContents()
						&& companyField.validateContents()
						&& telephoneField.validateContents()
						&& emailField.validateContents()
						&& noteField.validateContents()) {

					if (givenNameField.getText().equals("")) {
						person.setGivenName(null);
					} else {
						person.setGivenName(givenNameField.getText());
					}

					if (surnameField.getText().equals("")) {
						person.setSurname(null);
					} else {
						person.setSurname(surnameField.getText());
					}

					if (titleField.getText().equals("")) {
						person.setTitle(null);
					} else {
						person.setTitle(titleField.getText());
					}

					if (telephoneField.getText().equals("")) {
						person.setTelephone(null);
					} else {
						person.setTelephone(telephoneField.getText());
					}

					if (emailField.getText().equals("")) {
						person.setEmail(null);
					} else {
						person.setEmail(emailField.getText());
					}

					if (companyField.getText().equals("")) {
						person.setCompany(null);
					} else {
						person.setCompany(companyField.getText());
					}

					if (noteField.getText().equals("")) {
						person.setNote(null);
					} else {
						person.setNote(noteField.getText());
					}

					try {
						personService.update(person);
						// feedbackLabel.setText("Person erfolgreich ge\u00E4ndert.");
						editPersonDialog.dispose();
						JOptionPane.showMessageDialog(editPersonPanel,
								"Person erfolgreich ge\u00E4ndert.",
								"Information", JOptionPane.INFORMATION_MESSAGE);
					} catch (ServiceException e1) {
						log.warn(e1.getLocalizedMessage());
						// validationFeedbackLabel.setText("Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.");
						JOptionPane
								.showMessageDialog(
										editPersonPanel,
										"Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.",
										"Fehler", JOptionPane.ERROR_MESSAGE);
					}

				} else {
					JOptionPane
							.showMessageDialog(
									editPersonPanel,
									"Es konnten nicht alle Eingabefelder validiert werden.",
									"Warnung", JOptionPane.WARNING_MESSAGE);
					// validationFeedbackLabel.setText("Es konnten nicht alle Eingabefelder validiert werden");
				}

			}

		}
	}

	private final class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Person p;
			int row = showTable.getSelectedRow();
			if (row == -1) {
				// feedbackLabel
				// .setText("Bitte Person zum L\u00F6schen ausw\u00E4hlen.");
				JOptionPane.showMessageDialog(overviewPanel,
						"Bitte Person zum L\u00F6schen ausw\u00E4hlen.",
						"Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			p = personModel.getPersonRow(row);

			Object[] options = { "Abbrechen", "L\u00F6schen" };
			int ok = JOptionPane.showOptionDialog(overviewPanel,
					"Diese Person sicher l\u00F6schen?", "L\u00F6schen",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[1]);

			if (ok == 1) {
				try {
					personService.delete(p);
					personModel.removePerson(row);
					feedbackLabel.setText("Person wurde gel\u00F6scht.");
				} catch (ServiceException ex) {
					// feedbackLabel
					// .setText("Ein unerwarter Fehler ist waehrend der Loeschoperation aufgetreten.");
					log.warn(ex.getLocalizedMessage());
					JOptionPane
							.showMessageDialog(
									overviewPanel,
									"Ein Fehler ist w\u00E4hrend des L\u00F6schens aufgetreten. Bitte kontaktieren Sie Ihren Administrator.",
									"Fehler", JOptionPane.ERROR_MESSAGE);
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
				// feedbackLabel
				// .setText("Es muss eine Person ausgewaehlt werden bevor die Addressen angezeigt werden koennen.");
				JOptionPane.showMessageDialog(overviewPanel,
						"Bitte Person ausw\u00E4hlen.", "Information",
						JOptionPane.INFORMATION_MESSAGE);
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
				// feedbackLabel
				// .setText("Es muss eine Person ausgewaehlt werden bevor die Spenden angezeigt werden koennen.");
				JOptionPane.showMessageDialog(overviewPanel,
						"Bitte Person ausw\u00E4hlen.", "Information",
						JOptionPane.INFORMATION_MESSAGE);
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
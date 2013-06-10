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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.AddAttributes;
import at.fraubock.spendenverwaltung.gui.ComponentBuilder;
import at.fraubock.spendenverwaltung.gui.EditPerson;
import at.fraubock.spendenverwaltung.gui.Overview;
import at.fraubock.spendenverwaltung.gui.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IAddressService;
import at.fraubock.spendenverwaltung.interfaces.service.IDonationService;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;
import at.fraubock.spendenverwaltung.util.FilterType;

public class FindPersonsView extends JPanel {

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
	private JButton editButton;
	private JButton deleteButton;
	private JButton addAttribute;
	private JComboBox<Filter> filterCombo;
	private JButton backButton;
	private JPanel overviewPanel;
	private JLabel label;
	private JLabel empty;
	private Filter showAllFilter;
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;

	public FindPersonsView(IPersonService personService,
			IAddressService addressService, IDonationService donationService,
			IFilterService filterService, ComponentFactory componentFactory,
			ViewActionFactory viewActionFactory, PersonTableModel personModel) {
		super(new MigLayout());

		this.componentFactory = componentFactory;
		this.viewActionFactory = viewActionFactory;
		this.personService = personService;
		this.addressService = addressService;
		this.donationService = donationService;
		this.filterService = filterService;
		this.personModel = personModel;
	}

	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public void setFilterService(IFilterService filterService) {
		this.filterService = filterService;
	}

	public void setAddressService(IAddressService addressService) {
		this.addressService = addressService;
	}

	public void setDonationService(IDonationService donationService) {
		this.donationService = donationService;
	}

	public void setComponentFactory(ComponentFactory componentFactory) {
		this.componentFactory = componentFactory;
	}

	public void setViewActionFactory(ViewActionFactory viewActionFactory) {
		this.viewActionFactory = viewActionFactory;
	}

	public void setPersonModel(PersonTableModel personModel) {
		this.personModel = personModel;
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

		List<Filter> personFilters = new ArrayList<Filter>();
		personFilters.add(showAllFilter);
		log.info("PersonFilter-List: " + personFilters.size());
		try {
			personFilters.addAll(filterService
					.getAllByFilter(FilterType.PERSON));
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
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

	public void init() {
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
		empty = componentFactory.createLabel("			");
		overviewPanel.add(empty, "wrap");
		overviewPanel.add(scrollPane, "wrap");
		JButton export = new JButton("Liste exportieren");

		final JFileChooser fileChooser = new JFileChooser();

		export.addActionListener(new ActionListener() {

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
								+ file.getAbsolutePath() + "', text='" + csv
								+ "'");
					}
				}
			}
		});
		overviewPanel.add(export);
	}

	private void addComponentsToToolbar(JToolBar toolbar) {

		addAttribute = new JButton();
		addAttribute.setFont(new Font("Bigger", Font.PLAIN, 13));
		AddAction addAction = new AddAction();
		addAction.putValue(Action.NAME,
				"<html>&nbsp;Attribute hinzuf\u00FCgen&nbsp;</html>");
		addAttribute.setAction(addAction);

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

		backButton = new JButton();
		backButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(Action.NAME, "<html>&nbsp;Zur\u00FCck</html>");
		backButton.setAction(getBack);

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

	private final class AddAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			FindPersonsView findPersonsView = new FindPersonsView(personService, addressService, donationService, filterService, componentFactory, viewActionFactory, personModel);	
			Person p; 
			int row = showTable.getSelectedRow(); 
			if (row == -1) {
				JOptionPane.showMessageDialog(overviewPanel,
						  "Bitte Person ausw\u00E4hlen."); 
			return; 
			}
			int id = (Integer) personModel.getValueAt(row, 3);
			
			try { 
				p = personService.getById(id); 
			  } catch (ServiceException ex) { 
				  JOptionPane .showMessageDialog( overviewPanel,
						  "An error occured. Please see console for further information",
						  "Error", JOptionPane.ERROR_MESSAGE); ex.printStackTrace();
						  return; 
			}
			AddAttributes sp = new AddAttributes(p,personService, addressService, donationService, findPersonsView);
			removeAll(); 
			revalidate(); 
			repaint(); 
			add(sp);
		}

	}

	private final class EditAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			FindPersonsView findPersonsView = new FindPersonsView(personService, addressService, donationService, filterService, componentFactory, viewActionFactory, personModel);
			Person p; int row = showTable.getSelectedRow(); 
			if (row == -1) {
				JOptionPane.showMessageDialog(overviewPanel,
						"Bitte Person zum Bearbeiten ausw\u00E4hlen."); return; }
			
			int id = (Integer) personModel.getValueAt(row, 3);
			
			try { 
				p = personService.getById(id); 
			} catch (ServiceException ex) { 
				JOptionPane .showMessageDialog( overviewPanel,
						"An error occured. Please see console for further information",
						"Error", JOptionPane.ERROR_MESSAGE); ex.printStackTrace();
			 return; 
			 }
			  
			 EditPerson ep = new EditPerson(p, personService, addressService, findPersonsView, personModel); 
			 removeAll(); 
			 revalidate();
			 repaint(); 
			 add(ep);
			 }
	}

	private final class DeleteAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Person p;
			int row = showTable.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(overviewPanel,
						"Bitte Person zum L\u00F6schen ausw\u00E4hlen.");
				return;
			}

			int id = (Integer) personModel.getValueAt(row, 3);

			try {
				p = personService.getById(id);
			} catch (ServiceException ex) {
				JOptionPane
						.showMessageDialog(
								overviewPanel,
								"An error occured. Please see console for further information",
								"Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
				return;
			}

			Object[] options = { "Abbrechen", "L\u00F6schen" };
			int ok = JOptionPane.showOptionDialog(overviewPanel,
					"Diese Person sicher l\u00F6schen?", "Loeschen",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, options[1]);

			if (ok == 1) {
				try {
					personService.delete(p);
				} catch (ServiceException ex) {
					JOptionPane
							.showMessageDialog(
									overviewPanel,
									"An error occured. Please see console for further information",
									"Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
					return;
				}

				personModel.removePerson(row);
				JOptionPane.showMessageDialog(overviewPanel,
						"Person wurde gel\u00F6scht.", "Information",
						JOptionPane.INFORMATION_MESSAGE);

			} else {
				return;
			}

		}

	}
}
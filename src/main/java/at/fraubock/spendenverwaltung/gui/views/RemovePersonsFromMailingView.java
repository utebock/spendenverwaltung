package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.MailingTableModel;
import at.fraubock.spendenverwaltung.gui.components.PersonTableModel;
import at.fraubock.spendenverwaltung.gui.components.UnconfirmedMailingTableModel;

import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.domain.Person;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

/**
 * 
 * @author Chris
 *
 */
public class RemovePersonsFromMailingView extends InitializableView {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger
			.getLogger(RemovePersonsFromMailingView.class);
	
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;
	
	private IPersonService personService;
	private IMailingService mailingService;
	
	private JButton backButton, deleteButton;
	
	private JPanel contentPanel;
	private JTable personsTable;
	private PersonTableModel tableModel;
	private JToolBar toolbar;
	
	private Mailing mailing;
	
	private MailingTableModel parentMailingTableModel;
	private UnconfirmedMailingTableModel unconfirmedMailingTableModel;
	
	private JLabel feedbackLabel;
	
	public RemovePersonsFromMailingView(ViewActionFactory viewActionFactory, ComponentFactory componentFactory,
			IPersonService personService, IMailingService mailingService, Mailing mailing, MailingTableModel parentTableModel) {
		
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.personService = personService;
		this.mailing = mailing;
		this.parentMailingTableModel = parentTableModel;
		
		setUpLayout();
	}
	
	public RemovePersonsFromMailingView(ViewActionFactory viewActionFactory, ComponentFactory componentFactory,
			IPersonService personService, IMailingService mailingService, Mailing mailing, UnconfirmedMailingTableModel unconfirmedMailingTableModel) {
		
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.personService = personService;
		this.mailing = mailing;
		this.unconfirmedMailingTableModel = unconfirmedMailingTableModel;
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
		
		tableModel = new PersonTableModel();
		personsTable = new JTable(tableModel);
		personsTable.setAutoCreateRowSorter(true);
		personsTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(personsTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
		
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "wrap, growx");
		contentPanel.add(scrollPane, "wrap, growx");
				
		personsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Headline", Font.PLAIN, 16));
		contentPanel.add(feedbackLabel);
	}
	
	public void init() {
		tableModel.clear();
		
		addComponentsToToolbar(toolbar);	
		
		try {
			List<Person> persons = personService.getPersonsByMailing(mailing);
			
			for(Person entry : persons) {
				tableModel.addPerson(entry);
			}
		} catch (ServiceException e) {
			log.warn(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(this, "Ein Fehler trat während der Initialisierung der Tabelle auf");
		}
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		
		if(parentMailingTableModel != null) {
			backButton = new JButton();
			Action getBack = viewActionFactory.getFindMailingsViewAction(parentMailingTableModel);
			getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.jpg")));
			backButton.setAction(getBack);
		} else if(unconfirmedMailingTableModel != null) {
			backButton = new JButton();
			Action getBack = viewActionFactory.getConfirmMailingsViewAction(unconfirmedMailingTableModel);
			getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.jpg")));
			backButton.setAction(getBack);
		}
		
		deleteButton = new JButton();
		deleteButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		DeleteAction deleteAction = new DeleteAction();
		deleteButton.setAction(deleteAction);
		
		toolbar.add(backButton, "growx");
		toolbar.add(deleteButton, "growx");
		
	}

	private final class DeleteAction extends AbstractAction {


		private static final long serialVersionUID = 1L;

		public DeleteAction() {
			super("<html>&nbsp;Person l\u00F6schen</html>");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = personsTable.getSelectedRow();
			if(selectedRow != -1) {
				try {
					mailingService.deletePersonFromMailing(tableModel.getPersonRow(selectedRow), mailing);
					tableModel.removePerson(selectedRow);
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Es ist beim Löschen ein Fehler aufgetreten.");
				}
			} else {
				feedbackLabel.setText("Es muss zum Löschen eine Person zuerst ausgewählt werden.");
			}
		}
		
	}
	
	

}

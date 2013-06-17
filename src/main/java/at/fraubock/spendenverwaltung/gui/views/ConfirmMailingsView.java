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
import at.fraubock.spendenverwaltung.gui.components.UnconfirmedMailingTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.UnconfirmedMailing;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;

public class ConfirmMailingsView extends InitializableView {
	
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger
			.getLogger(ConfirmMailingsView.class);

	private ViewActionFactory viewActionFactory;
	private ComponentFactory componentFactory;
	private IMailingService mailingService;
	
	private JPanel contentPanel;

	private JTable unconfirmedTable;
	private UnconfirmedMailingTableModel tableModel;
	
	private JLabel feedbackLabel;
	
	private JToolBar toolbar;
	
	public ConfirmMailingsView(ViewActionFactory viewActionFactory,
			ComponentFactory componentFactory, IMailingService mailingService) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
		
		tableModel = new UnconfirmedMailingTableModel();
		unconfirmedTable = new JTable(tableModel);
		unconfirmedTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(unconfirmedTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
		
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "wrap, growx");
		contentPanel.add(scrollPane, "wrap, growx");
		
		unconfirmedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		feedbackLabel = componentFactory.createLabel("");
		feedbackLabel.setFont(new Font("Headline", Font.PLAIN, 16));
		contentPanel.add(feedbackLabel);
	}
	
	@Override
	public void init() {
		addComponentsToToolbar(toolbar);		
		initTable();
	}
	
	private void initTable() {
		tableModel.clear();
		try {
			tableModel.addUnconfirmedMailings(mailingService.getUnconfirmedMailingsWithCreator());
			unconfirmedTable.setAutoCreateRowSorter(true);
		} catch (ServiceException e) {
			log.warn(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(this, "Ein Fehler tritt während der Initialisierung der Tabelle auf");
		}
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {

		JButton backButton = new JButton();
		Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.jpg")));
		backButton.setAction(getBack);
		
		JButton confirmButton = new JButton();
		confirmButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ConfirmAction confirmAction = new ConfirmAction();
		confirmButton.setAction(confirmAction);

		JButton confirmAllButton = new JButton();
		confirmAllButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ConfirmAllAction confirmAllAction = new ConfirmAllAction();
		confirmAllButton.setAction(confirmAllAction);

		JButton deleteButton = new JButton();
		deleteButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		DeleteAction deleteAction = new DeleteAction();
		deleteButton.setAction(deleteAction);

		toolbar.add(backButton, "split 4, growx");
		toolbar.add(confirmButton, "split 4, growx");
		toolbar.add(confirmAllButton, "growx");
		toolbar.add(deleteButton, "growx");
	}
	
	private final class DeleteAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public DeleteAction() {
			super("Aussendung löschen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow;
			if((selectedRow = unconfirmedTable.getSelectedRow()) != -1){
				UnconfirmedMailing mailing = tableModel.getRow(selectedRow);
				try {
					mailingService.delete(mailing.getMailing());
					tableModel.removeUnconfirmedMailing(mailing);
					feedbackLabel.setText("Aussendung wurde gelöscht.");
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Ein Fehler trat während des Löschens auf");
				}
			}
		}
		
	}
	
	private final class ConfirmAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ConfirmAction() {
			super("Aussendung bestätigen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow;
			if((selectedRow = unconfirmedTable.getSelectedRow()) != -1){
				UnconfirmedMailing mailing = tableModel.getRow(selectedRow);
				try {
					mailingService.confirmMailing(mailing.getMailing());
					tableModel.removeUnconfirmedMailing(mailing);
					feedbackLabel.setText("Aussendung wurde bestätigt.");
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Ein Fehler trat während der Bestätigung auf");
				}
			}
		}	
	}
	
	private final class ConfirmAllAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ConfirmAllAction() {
			super("Alle Aussendungen bestätigen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			List<UnconfirmedMailing> unconfirmedMailings = tableModel.getUnconfirmedMailings();
			
			try {
				for(UnconfirmedMailing umailing : unconfirmedMailings) {
					mailingService.confirmMailing(umailing.getMailing());
				}
				tableModel.clear();
				feedbackLabel.setText("Alle Aussendungen wurden bestätigt.");
			} catch (ServiceException e1) {
				log.warn(e1.getLocalizedMessage());
				feedbackLabel.setText("Ein Fehler trat während der Bestätigung auf");
			}
		}	
	}
	
}

package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

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
import org.jdesktop.swingx.JXDatePicker;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.MailingTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IMailingService;
import at.fraubock.spendenverwaltung.interfaces.service.IPersonService;

public class FindMailingsView extends InitializableView {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger
			.getLogger(FindMailingsView.class);
	
	private ComponentFactory componentFactory;
	private ViewActionFactory viewActionFactory;
	
	private IPersonService personService;
	private IMailingService mailingService;
	
	private MailingTableModel tableModel;
	
	private JPanel contentPanel;
	private JLabel feedbackLabel, beforeDateLabel, afterDateLabel;
	
	private JXDatePicker beforeDate, afterDate;
	
	JToolBar toolbar;
	JTable mailingsTable;
	
	public FindMailingsView(ViewActionFactory viewActionFactory, ComponentFactory componentFactory,
			IPersonService personService, IMailingService mailingService) {
		
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.personService = personService;
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(800, 800);
		
		this.add(contentPanel);
		
		tableModel = new MailingTableModel();
		mailingsTable = new JTable(tableModel);
		mailingsTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(mailingsTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
		
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "wrap, growx");
		contentPanel.add(scrollPane, "wrap, growx");
		
		afterDateLabel = componentFactory.createLabel("Nach Datum");
		contentPanel.add(afterDateLabel, "split 4");
		afterDate = new JXDatePicker();
		afterDate.addActionListener(new AfterDatePickedListener());
		contentPanel.add(afterDate);
		
		beforeDateLabel = componentFactory.createLabel("Vor Datum");
		contentPanel.add(beforeDateLabel);
		beforeDate = new JXDatePicker();
		beforeDate.addActionListener(new BeforeDatePickedListener());
		contentPanel.add(beforeDate, "wrap");		
				
		mailingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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
			tableModel.addMailings(mailingService.getAllConfirmed());
			mailingsTable.setAutoCreateRowSorter(true);
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
		
		JButton reproduceButton = new JButton();
		reproduceButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ReproduceAction confirmAction = new ReproduceAction();
		reproduceButton.setAction(confirmAction);

		JButton viewPersonsButton = new JButton();
		viewPersonsButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ViewPersonsAction viewPersonsAction = new ViewPersonsAction();
		viewPersonsButton.setAction(viewPersonsAction);

		JButton deleteButton = new JButton();
		deleteButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		DeleteAction deleteAction = new DeleteAction();
		deleteButton.setAction(deleteAction);

		toolbar.add(backButton, "split 4, growx");
		toolbar.add(reproduceButton, "split 4, growx");
		toolbar.add(viewPersonsButton, "growx");
		toolbar.add(deleteButton, "growx");
	}
	
	private final class AfterDatePickedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Date bDate = beforeDate.getDate();
			Date aDate = afterDate.getDate();
			
			if(aDate != null && bDate != null) {
				if(aDate.after(bDate)) {
					feedbackLabel.setText("Ungültiger Datumsbereich");
				} else {
					try {
						tableModel.clear();
						tableModel.addMailings(mailingService.getBetweenDates(aDate, bDate));
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Es passierte ein Fehler beim Auswerten der Abfrage");
				}
				}
			} else if (aDate != null) {
				try {
					tableModel.clear();
					tableModel.addMailings(mailingService.getAfterDate(aDate));
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Es passierte ein Fehler beim Auswerten der Abfrage");
				}
			}
		}
	}
	
	private final class BeforeDatePickedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Date aDate = afterDate.getDate();
			Date bDate = beforeDate.getDate();
			
			if(aDate != null && bDate != null) {
				if(aDate.after(bDate)) {
					feedbackLabel.setText("Ungültiger Datumsbereich");
				} else {
					try {
						tableModel.clear();
						tableModel.addMailings(mailingService.getBetweenDates(aDate, bDate));
					} catch (ServiceException e1) {
						log.warn(e1.getLocalizedMessage());
						feedbackLabel.setText("Es passierte ein Fehler beim Auswerten der Abfrage");
					}
				}
			} else if (bDate != null) {
				try {
					tableModel.clear();
					tableModel.addMailings(mailingService.getBeforeDate(bDate));
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Es passierte ein Fehler beim Auswerten der Abfrage");
				}
			}
		}
	}

	private final class DeleteAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public DeleteAction() {
			super("Aussendung Löschen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow;
			if((selectedRow = mailingsTable.getSelectedRow()) != -1){
				Mailing mailing = tableModel.getRow(selectedRow);
				try {
					mailingService.delete(mailing);
					tableModel.removeMailing(mailing);
					feedbackLabel.setText("Aussendung wurde gelöscht.");
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					feedbackLabel.setText("Ein Fehler trat während des Löschens auf");
				}
			}
		}
		
	}

	private final class ReproduceAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public ReproduceAction() {
			super("Aussendung wiederherstellen");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow;
			if((selectedRow = mailingsTable.getSelectedRow()) != -1){
				Mailing mailing = tableModel.getRow(selectedRow);
				
				if(mailing.getMedium() == Mailing.Medium.EMAIL) {
					feedbackLabel.setText("Email Aussendungen können nicht wiederhergestellt werden.");
				} else if(mailing.getTemplate() == null) {
					feedbackLabel.setText("Aussendungen ohne Vorlage können nicht wiederhergestellt werden.");
				} else {
					try {
						mailingService.reproduceDocument(mailing);
					} catch (ServiceException e1) {
						log.warn(e1.getLocalizedMessage());
						feedbackLabel.setText("Die Aussendung konnte nicht wiederhergestellt werden.");
					}
				}
			}
			
		}
		
	}
	
	private final class ViewPersonsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ViewPersonsAction() {
			super("Empfänger anzeigen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}
}

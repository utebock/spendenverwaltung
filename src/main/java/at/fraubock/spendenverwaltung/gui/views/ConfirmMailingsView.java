/*******************************************************************************
 * Copyright (c) 2013 Bockmas TU Vienna Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Bockmas TU Vienna Team: Cornelia Hasil, Chris Steele, Manuel Bichler, Philipp Muhoray, Roman Voglhuber, Thomas Niedermaier
 * 
 * http://www.fraubock.at/
 ******************************************************************************/
package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Date;
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

import at.fraubock.spendenverwaltung.gui.CellRenderer;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.UnconfirmedMailingTableModel;
import at.fraubock.spendenverwaltung.interfaces.domain.Mailing;
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
			ComponentFactory componentFactory, IMailingService mailingService, UnconfirmedMailingTableModel tableModel) {
		this.viewActionFactory = viewActionFactory;
		this.componentFactory = componentFactory;
		this.mailingService = mailingService;
		this.tableModel = tableModel;
		
		setUpLayout();
	}
	
	public void setUpLayout() {
		contentPanel = componentFactory.createPanel(750, 800);
		
		this.add(contentPanel);
		
		if(tableModel == null) {
			tableModel = new UnconfirmedMailingTableModel();
		}
		unconfirmedTable = new JTable(tableModel);
		unconfirmedTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(unconfirmedTable);
		scrollPane.setPreferredSize(new Dimension(700, 550));
		
		toolbar = new JToolBar();
		contentPanel.add(toolbar, "wrap 20px, growx");
		contentPanel.add(scrollPane, "wrap, growx");
		
		unconfirmedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		unconfirmedTable.setDefaultRenderer(Object.class, new CellRenderer());
		unconfirmedTable.setDefaultRenderer(Date.class, new CellRenderer());
		
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
		if(tableModel.getRowCount() == 0) {
			try {
				tableModel.addUnconfirmedMailings(mailingService.getUnconfirmedMailingsWithCreator());
				unconfirmedTable.setAutoCreateRowSorter(true);
			} catch (ServiceException e) {
				log.warn(e.getLocalizedMessage());
				JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void addComponentsToToolbar(JToolBar toolbar) {
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		
		JButton backButton = new JButton();
		Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/backButton.png")));
		backButton.setAction(getBack);
		
		JButton confirmButton = new JButton();
		confirmButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ConfirmAction confirmAction = new ConfirmAction();
		confirmButton.setAction(confirmAction);

		JButton confirmAllButton = new JButton();
		confirmAllButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		ConfirmAllAction confirmAllAction = new ConfirmAllAction();
		confirmAllButton.setAction(confirmAllAction);
		
		JButton removePersonsButton = new JButton();
		removePersonsButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		RemovePersonsAction removePersonsAction = new RemovePersonsAction();
		removePersonsButton.setAction(removePersonsAction);

		JButton deleteButton = new JButton();
		deleteButton.setFont(new Font("Bigger", Font.PLAIN, 13));
		DeleteAction deleteAction = new DeleteAction();
		deleteButton.setAction(deleteAction);

		toolbar.add(backButton, "split 5, growx");
		toolbar.add(confirmButton, "growx");
		toolbar.add(confirmAllButton, "growx");
		toolbar.add(deleteButton, "growx");
		toolbar.add(removePersonsButton, "growx");
		
	}
	
	private final class DeleteAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public DeleteAction() {
			super("Aussendung l\u00F6schen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow;
			if((selectedRow = unconfirmedTable.getSelectedRow()) != -1){
				UnconfirmedMailing mailing = tableModel.getRow(selectedRow);
				try {
					mailingService.delete(mailing.getMailing());
					tableModel.removeUnconfirmedMailing(mailing);
					//feedbackLabel.setText("Aussendung wurde geloescht.");
					JOptionPane.showMessageDialog(contentPanel, "Aussendung wurde gel\u00F6scht.", "Information", JOptionPane.INFORMATION_MESSAGE);
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					//feedbackLabel.setText("Ein Fehler trat waehrend des Loeschens auf");
					JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
	}
	
	private final class ConfirmAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ConfirmAction() {
			super("Aussendung best\u00E4tigen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow;
			if((selectedRow = unconfirmedTable.getSelectedRow()) != -1){
				UnconfirmedMailing mailing = tableModel.getRow(selectedRow);
				try {
					mailingService.confirmMailing(mailing.getMailing());
					tableModel.removeUnconfirmedMailing(mailing);
					//feedbackLabel.setText("Aussendung wurde best\u00E4tigt.");
					JOptionPane.showMessageDialog(contentPanel, "Aussendung wurde best\u00E4tigt.", "Information", JOptionPane.INFORMATION_MESSAGE);
				} catch (ServiceException e1) {
					log.warn(e1.getLocalizedMessage());
					//feedbackLabel.setText("Ein Fehler trat waehrend der Bestaetigung auf");
					JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}	
	}
	
	private final class ConfirmAllAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public ConfirmAllAction() {
			super("Alle Aussendungen best\u00E4tigen");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			List<UnconfirmedMailing> unconfirmedMailings = tableModel.getUnconfirmedMailings();
			
			try {
				for(UnconfirmedMailing umailing : unconfirmedMailings) {
					mailingService.confirmMailing(umailing.getMailing());
				}
				tableModel.clear();
				//feedbackLabel.setText("Alle Aussendungen wurden bestaetigt.");
				JOptionPane.showMessageDialog(contentPanel, "Alle Aussendungen wurde best\u00E4tigt.", "Information", JOptionPane.INFORMATION_MESSAGE);
			} catch (ServiceException e1) {
				log.warn(e1.getLocalizedMessage());
			//	feedbackLabel.setText("Ein Fehler trat waehrend der Bestaetigung auf");
				JOptionPane.showMessageDialog(contentPanel, "Ein Fehler ist aufgetreten. Bitte kontaktieren Sie Ihren Administrator.", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		}	
	}
	
	private final class RemovePersonsAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RemovePersonsAction() {
			super("<html>&nbsp;Personen entfernen</html>");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			int rowIndex = unconfirmedTable.getSelectedRow();
			if(rowIndex != -1) {
				Mailing selectedMailing = tableModel.getRow(rowIndex).getMailing();
				
				Action viewToRemoveAction = viewActionFactory.getRemovePersonFromMailingViewAction(selectedMailing, tableModel);
				
				viewToRemoveAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			}
		}
		
	}
}

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
package at.fraubock.spendenverwaltung.gui;

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

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.components.FilterTableModel;
import at.fraubock.spendenverwaltung.gui.filter.CreateFilter;
import at.fraubock.spendenverwaltung.gui.views.InitializableView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter;
import at.fraubock.spendenverwaltung.interfaces.domain.filter.Filter.FilterPrivacyStatus;
import at.fraubock.spendenverwaltung.interfaces.exceptions.FilterInUseException;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IFilterService;
import at.fraubock.spendenverwaltung.util.Pair;
import at.fraubock.spendenverwaltung.util.filter.FilterType;

public class MainFilterView extends InitializableView {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(MainFilterView.class);
	private IFilterService filterService;;
	private ViewActionFactory viewActionFactory;
	private FilterTableModel filterModel;
	private JTable showTable;
	private JScrollPane scrollPane;
	private List<Filter> filterList;
	private String userName;
	private JPanel panel;
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
	}

	public void initTable() {
		filterModel = new FilterTableModel();
		getFilter();
		showTable = new JTable(filterModel);
		showTable.setFillsViewportHeight(true);
		showTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		showTable.setDefaultRenderer(Object.class, new CellRenderer());
		scrollPane = new JScrollPane(showTable);
		scrollPane.setPreferredSize(new Dimension(748, 550));
		this.add(scrollPane);

	}

	public void init() {
		panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(700, 800));
		this.add(panel);

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		addComponentsToToolbar(toolbar);
		panel.add(toolbar, "wrap");
		empty = new JLabel("		 ");
		panel.add(empty, "wrap");
		initTable();
		panel.add(scrollPane);
	}

	private void addComponentsToToolbar(JToolBar toolbar) {

		backButton = new JButton();
		Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("/images/backButton.png")));
		backButton.setAction(getBack);

		personFilter = new JButton();
		personFilter.setAction(new AbstractAction("Personenfilter erstellen") {
			private static final long serialVersionUID = 7948990257221071839L;

			@Override
			public void actionPerformed(ActionEvent a) {
				createFilter(FilterType.PERSON, null);
			}

		});
		personFilter.setFont(new Font("Bigger", Font.PLAIN, 13));

		sendingsFilter = new JButton();
		sendingsFilter.setAction(new AbstractAction(
				"Aussendungsfilter erstellen") {
			private static final long serialVersionUID = 7948990257221071839L;

			@Override
			public void actionPerformed(ActionEvent a) {
				createFilter(FilterType.MAILING, null);
			}

		});
		sendingsFilter.setFont(new Font("Bigger", Font.PLAIN, 13));

		donationFilter = new JButton();
		donationFilter.setAction(new AbstractAction("Spendenfilter erstellen") {
			private static final long serialVersionUID = 7948990257221071839L;

			@Override
			public void actionPerformed(ActionEvent a) {
				createFilter(FilterType.DONATION, null);
			}

		});
		donationFilter.setFont(new Font("Bigger", Font.PLAIN, 13));

		edit = new JButton();
		edit.setAction(new AbstractAction("Filter bearbeiten") {
			private static final long serialVersionUID = 7948990257221071839L;

			@Override
			public void actionPerformed(ActionEvent a) {
				if (showTable.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(MainFilterView.this,
							"Bitte Filter zum Bearbeiten ausw\u00E4hlen.");
					return;
				}
				Filter filter = filterModel.getFilterRow(showTable
						.getSelectedRow());

				createFilter(filter.getType(), filter);
			}

		});
		edit.setFont(new Font("Bigger", Font.PLAIN, 13));

		delete = new JButton();
		delete.setAction(new AbstractAction("Filter l\u00F6schen") {
			private static final long serialVersionUID = 7948990257221071839L;

			@Override
			public void actionPerformed(ActionEvent a) {
				deleteFilter();
			}

		});
		delete.setFont(new Font("Bigger", Font.PLAIN, 13));

		toolbar.add(backButton);
		toolbar.add(personFilter);
		toolbar.add(donationFilter);
		toolbar.add(sendingsFilter);
		toolbar.add(edit);
		toolbar.add(delete);

	}

	public FilterTableModel getFilterModel() {
		return this.filterModel;
	}

	private void getFilter() {
		try {
			Pair<List<Filter>, String> filterPair = filterService
					.getAllByAnonymous(false);
			filterList = filterPair.a;
			userName = filterPair.b;
			log.info("List " + filterList.size() + " filter");
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		if (filterList == null) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		for (Filter f : filterList) {
			filterModel.addFilter(f);
		}
	}

	public void createFilter(FilterType type, Filter filter) {
		CreateFilter cf = new CreateFilter(type, filterService, this, filter,
				userName, viewActionFactory);
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

		if (!filter.getOwner().equals(userName)
				&& filter.getPrivacyStatus() != FilterPrivacyStatus.READ_UPDATE_DELETE) {
			JOptionPane.showMessageDialog(MainFilterView.this,
					"Sie sind nicht berechtigt diesen Filter zu l\u00F6schen."
							+ (filter.getOwner().equals("default_filter_owner")?"":(" Er geh\u00f6rt "+filter.getOwner())));
			return;
		}

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
			JOptionPane
					.showMessageDialog(
							this,
							"Dieser Filter ist mit einem anderen verkn\u00FCpft. "
									+ "Bitte l\u00F6schen Sie die Verkn\u00FCpfung zuerst.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}
}

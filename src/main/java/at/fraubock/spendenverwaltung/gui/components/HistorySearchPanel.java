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
package at.fraubock.spendenverwaltung.gui.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXDatePicker;
import org.springframework.util.StringUtils;

import at.fraubock.spendenverwaltung.gui.SimpleComboBoxModel;
import at.fraubock.spendenverwaltung.gui.views.HistoryView;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;

public class HistorySearchPanel extends JPanel {
	private static final long serialVersionUID = -8530985294754797605L;

	private JTextField actorSearch;
	private JComboBox<ActionTypeMapper> actionSearch;
	private JComboBox<ActionEntityMapper> entitySearch;
	private JXDatePicker dateFrom;
	private JXDatePicker dateTo;
	private JTextField dataSearch;
	private HistoryView view;
	private ComponentFactory componentFactory;

	private JLabel nameLabel;

	private JLabel entityLabel;

	private JLabel dateLabel;

	public HistorySearchPanel(HistoryView viewParam) {
		this.view = viewParam;
		setLayout(new MigLayout());
		componentFactory = new ComponentFactory();
	
		// WHO
		nameLabel = componentFactory.createLabel("Benutzer: ");
		add(nameLabel, "split 2");
		actorSearch = new JTextField(17);
		add(actorSearch, "gap 150, wrap, growx");
		
		actorSearch.addKeyListener(new SearchKeyListener());

		// DID
		add((new JLabel("Get\u00E4tigte Aktion: ")), "split 2");
		add((actionSearch = new JComboBox<ActionTypeMapper>(
				new SimpleComboBoxModel<ActionTypeMapper>(
						ActionTypeMapper.values()))), "gap 100, wrap, growx");
		
		actionSearch.setPreferredSize(new Dimension(147, JComboBox.HEIGHT));
		actionSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				search();
			}
		});
		// WHAT
		List<ActionEntityMapper> mappers = new ArrayList<ActionEntityMapper>();
		mappers.add(new ActionEntityMapper(null));
		for (Action.Entity ent : Action.Entity.values()) {
			mappers.add(new ActionEntityMapper(ent));
		}
		
		entityLabel = componentFactory.createLabel("Objekt: ");
		add(entityLabel, "split 2");
		
		entitySearch = new JComboBox<ActionEntityMapper>(
				new SimpleComboBoxModel<ActionEntityMapper>(mappers));
		
		entitySearch.setPreferredSize(new Dimension(205, JComboBox.HEIGHT));
		entitySearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				search();
			}
		});
		add(entitySearch, "gap 162, wrap, growx");

		JPanel datePanel = new JPanel();
		datePanel.setLayout(new MigLayout());
		//add(datePanel);
		
		// WHEN
		dateLabel = componentFactory.createLabel("Beginn: ");
		add(dateLabel, "split 2");
		add(dateFrom = new JXDatePicker(new java.util.Date()), "gap 164, wrap, growx");
		dateFrom.setDate(null);
		dateFrom.getEditor().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(dateFrom.isEditValid()) {
						dateFrom.commitEdit();
						search();
					}
				} catch (ParseException ex) {
					// nothing
				}
			}
		});
		
		add((new JLabel("Ende:")), "split2");
		add((dateTo = new JXDatePicker(new java.util.Date())), "gap 179, wrap, growx");
		dateTo.setDate(null);
		dateTo.getEditor().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(dateTo.isEditValid()) {
						dateTo.commitEdit();
						search();
					}
				} catch (ParseException ex) {
					// nothing
				}
			}
		});

		add((new JLabel("Daten: ")), "split2");
		add((dataSearch = new JTextField(42)), "gap 167, wrap, growx");
		dataSearch.addKeyListener(new SearchKeyListener());

	}

	private class SearchKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			search();
		}
	}

	private void search() {
		ActionSearchVO searchVO = new ActionSearchVO();

		searchVO.setActor(StringUtils.isEmpty(actorSearch.getText()) ? null
				: actorSearch.getText());

		searchVO.setType(((ActionTypeMapper) actionSearch.getSelectedItem())
				.getType());

		searchVO.setEntity(((ActionEntityMapper) entitySearch.getSelectedItem())
				.getEntity());

		searchVO.setFrom(dateFrom.getDate());

		if(dateTo.getDate()!=null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(dateTo.getDate());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			searchVO.setTo(cal.getTime());
		}
		

		searchVO.setPayload(StringUtils.isEmpty(dataSearch.getText()) ? null
				: dataSearch.getText());

		view.applyExtendedSearch(searchVO);
	}

	private enum ActionTypeMapper {
		ALL(null), INSERT(Action.Type.INSERT), UPDATE(Action.Type.UPDATE), DELETE(
				Action.Type.DELETE);

		private Action.Type type;

		private ActionTypeMapper(Action.Type type) {
			this.type = type;
		}

		public Action.Type getType() {
			return type;
		}

		@Override
		public String toString() {
			return type == null ? "Alle" : type.toString();
		}

	}

	private class ActionEntityMapper {

		private Action.Entity entity;

		public ActionEntityMapper(Action.Entity entity) {
			this.entity = entity;
		}

		public Action.Entity getEntity() {
			return entity;
		}

		@Override
		public String toString() {
			return entity == null ? "Alle" : entity.toString();
		}

	}

}

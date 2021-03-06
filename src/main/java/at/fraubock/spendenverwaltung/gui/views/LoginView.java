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

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.ActionPolling;
import at.fraubock.spendenverwaltung.gui.MainFrame;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;

/**
 * View providing the user textfields for typing in their username, password and
 * database URL. When done, a connection is tried to establish. If it fails, an
 * error is shown and the user may re-enter their data. When done, a
 * {@link MainMenuView} will be created and shown.
 * 
 * @author manuel-bichler
 * 
 */
public class LoginView extends InitializableView {

	private static final Logger log = Logger.getLogger(LoginView.class);

	private static final long serialVersionUID = -3964066846071628014L;
	private JLabel userLabel;
	private JLabel pwdLabel;
	private JLabel urlLabel;
	private JTextField userField, urlField;
	private JPasswordField pwdField;
	private JButton loginBtn;
	private JButton cancelBtn;
	private MainFrame parent;
	private ComponentFactory componentFactory;
	private ViewActionFactory actionFactory;
	private ViewDisplayer viewDisplayer;
	private BasicDataSource databaseDataSource;
	private String defaultUser = "", defaultPassword = "", defaultUrl = "";
	private ActionPolling polling;

	/**
	 * Creates a new instance.
	 * 
	 * @param databaseDataSource
	 *            the data source whose username, password and URL is to be set
	 * @param componentFactory
	 *            the component factory for obtaining components from
	 * @param actionFactory
	 *            the action factory for creating actions
	 * @param viewDisplayer
	 *            the view displayer used to switch view after successful login
	 * @param defaultUser
	 *            the pre-filled text for the username. May be the empty string
	 *            or null.
	 * @param defaultPassword
	 *            the pre-filled text for the password. May be the empty string
	 *            or null.
	 * @param defaultUrl
	 *            the pre-filled text for the URL. May be the empty string or
	 *            null. Driver name and database name omitted. Example:
	 *            "localhost:3306/ubspenderverwaltung"
	 */
	public LoginView(BasicDataSource databaseDataSource,
			ComponentFactory componentFactory, ViewActionFactory actionFactory,
			ViewDisplayer viewDisplayer, String defaultUser,
			String defaultPassword, String defaultUrl, ActionPolling polling) {
		this.databaseDataSource = databaseDataSource;
		this.componentFactory = componentFactory;
		this.actionFactory = actionFactory;
		this.viewDisplayer = viewDisplayer;
		this.polling = polling;
		if (defaultPassword != null)
			this.defaultPassword = defaultPassword;
		if (defaultUrl != null)
			this.defaultUrl = defaultUrl;
		if (defaultUser != null)
			this.defaultUser = defaultUser;
	}

	/**
	 * Creates a new instance. The text fields will be initialized empty.
	 * 
	 * @param databaseDataSource
	 *            the data source whose username, password and URL is to be set
	 * @param componentFactory
	 *            the component factory for obtaining components from
	 * @param actionFactory
	 *            the action factory for creating actions
	 * @param viewDisplayer
	 *            the view displayer used to switch view after successful login
	 */
	public LoginView(BasicDataSource databaseDataSource,
			ComponentFactory componentFactory, ViewActionFactory actionFactory,
			ViewDisplayer viewDisplayer, ActionPolling polling) {
		this(databaseDataSource, componentFactory, actionFactory,
				viewDisplayer, null, null, null, polling);
	}

	private void login() {
		String userName = userField.getText();
		String password = String.valueOf(pwdField.getPassword());
		String url = urlField.getText();

		databaseDataSource.setUrl("jdbc:mysql://" + url);
		databaseDataSource.setUsername(userName);
		databaseDataSource.setPassword(password);

		try {
			// throw away former connections in the pool that have wrong login
			databaseDataSource.close();
			// try database connection to see if login data is correct
			Connection c = databaseDataSource.getConnection();
			c.close();
		} catch (SQLException e) {
			log.warn("User login failed", e);
			JOptionPane.showMessageDialog(
					parent,
					"Verbindung zur Datenbank fehlgeschlagen. Details:\n"
							+ e.getLocalizedMessage(), "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		MainMenuView mainMenu = new MainMenuView(actionFactory,
				componentFactory);
		mainMenu.init();
		viewDisplayer.changeView(mainMenu);
		polling.start();
	}

	@Override
	public void init() {
		this.setLayout(new MigLayout());

		userLabel = componentFactory.createLabel("Username:");
		pwdLabel = componentFactory.createLabel("Passwort:");
		urlLabel = componentFactory.createLabel("Datenbank-URL:");
		userField = componentFactory.createTextField(defaultUser);
		userField.setText(defaultUser);
		pwdField = new JPasswordField(defaultPassword);
		pwdField.setText(defaultPassword);
		urlField = componentFactory.createTextField(defaultUrl);
		loginBtn = new JButton("Login");
		cancelBtn = new JButton("Abbrechen");

		add(userLabel, "gaptop 270, gapleft 200");
		add(userField, "wrap, w 200!, gapleft 30");
		add(pwdLabel, "gapleft 200, gaptop 10");
		add(pwdField, "wrap, w 200!, gapleft 30");
		add(urlLabel, "gapleft 200, gaptop 10");
		add(urlField, "wrap, w 200!, gapleft 30");
		add(loginBtn, "gapleft 200, gaptop 10");
		add(cancelBtn, "wrap, gapleft 30");

		cancelBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -7953434061209248267L;

			@Override
			public void actionPerformed(ActionEvent e) {
				viewDisplayer.closeView();
			}
		});
		loginBtn.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -7953434061209248267L;

			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});

		Action loginButtonPress = new AbstractAction() {
			private static final long serialVersionUID = -2009858949645282327L;

			@Override
			public void actionPerformed(ActionEvent e) {
				loginBtn.doClick();
			}
		};
		userField.addActionListener(loginButtonPress);
		pwdField.addActionListener(loginButtonPress);
		urlField.addActionListener(loginButtonPress);

	}
}

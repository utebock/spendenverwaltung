package at.fraubock.spendenverwaltung.gui.views;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

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
			String defaultPassword, String defaultUrl) {
		this.databaseDataSource = databaseDataSource;
		this.componentFactory = componentFactory;
		this.actionFactory = actionFactory;
		this.viewDisplayer = viewDisplayer;
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
			ViewDisplayer viewDisplayer) {
		this(databaseDataSource, componentFactory, actionFactory,
				viewDisplayer, null, null, null);
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
	}

	// check is user is valid if login is pressed
	private final class LoginAction extends AbstractAction {

		private static final long serialVersionUID = -8303170214117697889L;
		private LoginView dialog;

		private LoginAction(LoginView dialog) {
			this.dialog = dialog;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.login();
		}
	}

	// close GUI if cancelButton is pressed
	private final class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 6697635107905733270L;

		@Override
		public void actionPerformed(ActionEvent e) {
			viewDisplayer.closeView();
		}
	}

	@Override
	public void init() {
		this.setLayout(new MigLayout());

		userLabel = componentFactory.createLabel("Username:");
		pwdLabel = componentFactory.createLabel("Passwort:");
		urlLabel = componentFactory.createLabel("Datenbank-URL:");
		userField = componentFactory.createTextField(defaultUser);
		userField.addKeyListener(new EnterKeyListener());
		userField.setText("ubadministrative");
		pwdField = new JPasswordField(defaultPassword);
		pwdField.addKeyListener(new EnterKeyListener());
		pwdField.setText("ubadmin");
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

		cancelBtn.addActionListener(new CancelAction());
		loginBtn.addActionListener(new LoginAction(this));

	}
	
	private class EnterKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			if(arg0.getKeyCode()==0) {
				loginBtn.doClick();
			}
			
		}
		
	}
}

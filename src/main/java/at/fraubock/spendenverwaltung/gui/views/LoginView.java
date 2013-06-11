package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import at.fraubock.spendenverwaltung.gui.GuiStarter;
import at.fraubock.spendenverwaltung.gui.MainFrame;
import at.fraubock.spendenverwaltung.gui.components.ComponentFactory;
import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IUserService;
import at.fraubock.spendenverwaltung.util.CurrentUser;

import net.miginfocom.swing.MigLayout;

public class LoginView extends InitializableView {

	private JLabel userLabel;
	private JLabel pwdLabel;
	private JTextField user;
	private JPasswordField pwd;
	private JButton loginBtn;
	private JButton cancelBtn;
	private MainFrame parent;
	private IUserService userService;
	private ComponentFactory componentFactory;
	private ViewActionFactory actionFactory;
	private ViewDisplayer viewDisplayer;
	
	public LoginView(IUserService userService, ComponentFactory componentFactory, ViewActionFactory actionFactory, ViewDisplayer viewDisplayer){
		this.userService = userService;
		this.componentFactory = componentFactory;
		this.actionFactory = actionFactory;
		this.viewDisplayer = viewDisplayer;
		setUp();
	}
	
	public void setUp(){
	}
	
	private void login(){
		String userName = user.getText();
		String password = String.valueOf(pwd.getPassword());
		String loggedInUser;
		
		try {
			loggedInUser = userService.isUserValid(userName, password);
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(parent, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
					  "Fehler", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		
		if(loggedInUser != null){
			CurrentUser.userName = loggedInUser;
			MainMenuView mainMenu = new MainMenuView(actionFactory, componentFactory);
			mainMenu.init();
			viewDisplayer.changeView(mainMenu);
		} else{
			JOptionPane.showMessageDialog(parent, "Benutzername oder Passwort ist falsch. Bitte probieren Sie es erneut",
					  "Benutzer/Passwort Falsch", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	// check is user is valid if login is pressed
	private final class LoginAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;
		private LoginView dialog;
		
		private LoginAction(LoginView dialog){
			this.dialog = dialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.login();
		}
	}
	
	// close GUI if cancelButton is pressed
	private final class CancelAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;
		private LoginView dialog;
		
		private CancelAction(LoginView dialog){
			this.dialog = dialog;
		}
		
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
		user = componentFactory.createTextField("");
		pwd = new JPasswordField();
		loginBtn = new JButton("Login");
		cancelBtn = new JButton("Abbrechen");
		
		//just for testing
		user.setText("ubadministrative");
		pwd.setText("ubadmin");

		add(userLabel, "gaptop 270, gapleft 200");
		add(user, "wrap, w 200!, gapleft 30");
		add(pwdLabel, "gapleft 200, gaptop 10");
		add(pwd, "wrap, w 200!, gapleft 30");
		add(loginBtn, "gapleft 200, gaptop 10");
		add(cancelBtn, "wrap, gapleft 30");
		
		cancelBtn.addActionListener(new CancelAction(this));
		loginBtn.addActionListener(new LoginAction(this));
		
	}
}

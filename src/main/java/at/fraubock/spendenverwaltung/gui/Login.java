package at.fraubock.spendenverwaltung.gui;

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

import at.fraubock.spendenverwaltung.interfaces.domain.Address;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IUserService;

import net.miginfocom.swing.MigLayout;

public class Login extends JDialog {

	private JLabel userLabel;
	private JLabel pwdLabel;
	private JTextField user;
	private JPasswordField pwd;
	private JButton loginBtn;
	private JButton cancelBtn;
	private MainFrame parent;
	private IUserService userService;
	private GuiStarter guiStarter;
	
	public Login(IUserService userService, GuiStarter guiStarter){
		this.userService = userService;
		this.guiStarter = guiStarter;
		setUp();
	}
	
	public void setUp(){
		this.setLayout(new MigLayout());
		
		userLabel = new JLabel("Username:");
		pwdLabel = new JLabel("Passwort:");
		user = new JTextField();
		pwd = new JPasswordField();
		loginBtn = new JButton("Login");
		cancelBtn = new JButton("Abbrechen");
		
		add(userLabel, "");
		add(user, "wrap, w 108!");
		add(pwdLabel, "");
		add(pwd, "wrap, w 108!");
		add(loginBtn, "");
		add(cancelBtn, "wrap");
		
		cancelBtn.addActionListener(new CancelAction(this));
		loginBtn.addActionListener(new LoginAction(this));
		
		pack();
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - getWidth()) / 2;
		final int y = (screenSize.height - getHeight()) / 2;
		setLocation(x, y);
		setVisible(true);
	}
	
	private void login(){
		String userName = user.getText();
		String password = String.valueOf(pwd.getPassword());
		boolean isValid = false;
		
		try {
			isValid = userService.isUserValid(userName, password);
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(parent, "Ein unerwarter Fehler ist aufgetreten! Bitte kontaktieren Sie Ihren Administrator.",
					  "Fehler", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		
		if(isValid){
			guiStarter.startGui();
			this.dispose();
		} else{
			JOptionPane.showMessageDialog(parent, "Benutzername oder Passwort ist falsch. Bitte probieren Sie es erneut",
					  "Benutzer/Passwort Falsch", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private final class LoginAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;
		private Login dialog;
		
		private LoginAction(Login dialog){
			this.dialog = dialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.login();
		}
	}
	
	private final class CancelAction extends AbstractAction{
		
		private static final long serialVersionUID = 1L;
		private Login dialog;
		
		private CancelAction(Login dialog){
			this.dialog = dialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	}
}

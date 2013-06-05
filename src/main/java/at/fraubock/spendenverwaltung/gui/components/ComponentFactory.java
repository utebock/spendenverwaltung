package at.fraubock.spendenverwaltung.gui.components;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import net.miginfocom.swing.MigLayout;

public class ComponentFactory {

	public JButton createImageButton(String path, AbstractAction action) {
		java.net.URL url = getClass().getResource(path);
		JButton button = new JButton(new ImageIcon(url));
		button.setAction(action);
		return button;
	}

	public JPanel createPanel(int x, int y) {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(x, y));
		return panel;
	}

	public JLabel createLabel(String text) {
		return new JLabel(text);
	}

	public JLabel createImageLabel(ImageIcon img) {
		return new JLabel(img);
	}

	public ImageIcon createImageIcon(String path) {
		java.net.URL url = getClass().getResource(path);
		return new ImageIcon(url);
	}

	public JSeparator createSeparator() {
		JSeparator sep = new JSeparator();
		return sep;
	}

	public JTextField createTextField(int chars) {
		return new JTextField(chars);
	}
	
	public CustomTextField createCustomTextField(int chars) {
		CustomTextField textField = new CustomTextField(chars);
		
		return textField;
	}
	
	public EmailTextField createEmailTextField(ComponentConstants size) {
		EmailTextField textField = new EmailTextField(size);
		
		return textField;
	}
	
	public NumericTextField createNumericTextField(ComponentConstants size) {
		NumericTextField textField = new NumericTextField(size);
		
		return textField;
	}
	
	public AlphabeticTextField createAlphabeticTextField(ComponentConstants size) {
		AlphabeticTextField textField = new AlphabeticTextField(size);
		
		return textField;
	}

	public JTextArea createTextArea(int row, int col) {
		return new JTextArea(row, col);
	}

	public JButton createButton(String text, ActionListener listener,
			String actionCommand) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		button.setActionCommand(actionCommand);
		return button;
	}

	public JToolBar createToolbar() {
		return new JToolBar();
	}

	public JTextField createTextField(String title) {
		return new JTextField(title);
	}

	public JTextArea createTextArea(String note) {
		return new JTextArea(note);
	}

	public JCheckBox createCheckbox() {
		return new JCheckBox();
	}

	public JMenuBar createMenuBar() {
		return new JMenuBar();
	}

	public JMenu createMenu(String text) {
		return new JMenu(text);
	}

	public JMenuItem createMenuItem(String string,
			ActionListener buttonListener, String actionCommand) {
		JMenuItem item = new JMenuItem(string);
		item.addActionListener(buttonListener);
		item.setActionCommand(actionCommand);
		return item;
	}

}

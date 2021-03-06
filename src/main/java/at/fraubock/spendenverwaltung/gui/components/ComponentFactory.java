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
import javax.swing.Action;
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

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;

public class ComponentFactory {

	private static final Logger log = Logger.getLogger(ComponentFactory.class);

	public JButton createImageButton(String path, Action action) {
		java.net.URL url = getClass().getResource(path);
		JButton button = new JButton(new ImageIcon(url));
		button.setAction(action);
		return button;
	}
	
	public ImageIcon createIcon(String path) {
		java.net.URL url = getClass().getResource(path);
		return new ImageIcon(url);
	}
	
	public JButton createImageButton(String path) {
		log.info("createImageButton path "+path);
		java.net.URL url = getClass().getResource(path);
		log.info("URL path "+url.getPath());

		JButton button = new JButton(new ImageIcon(url));
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

	public JTextArea createTextArea(int row, int col) {
		return new JTextArea(row, col);
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
			Action action) {
		JMenuItem item = new JMenuItem(string);
		item.setAction(action);
		return item;
	}

}

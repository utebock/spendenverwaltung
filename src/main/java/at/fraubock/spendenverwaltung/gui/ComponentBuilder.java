package at.fraubock.spendenverwaltung.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class ComponentBuilder {

	public JButton createImageButton(String path, ActionListener listener, String actionCommand){
		java.net.URL url = getClass().getResource(path);
		JButton button = new JButton(new ImageIcon(url));
		button.addActionListener(listener);
		button.setActionCommand(actionCommand);
		return button;
	}
	
	public JPanel createPanel(int x, int y){
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setPreferredSize(new Dimension(x,y));
		return panel;
	}
	
	public JLabel createLabel(String text){
		return new JLabel(text);
	}
	
	public JLabel createImageLabel(ImageIcon img){
		return new JLabel(img);
	}

	public ImageIcon createImageIcon(String path) {
		java.net.URL url = getClass().getResource(path);
		return new ImageIcon(url);
	}
	
	public JSeparator createSeparator(){
		JSeparator sep = new JSeparator();
		return sep;
	}
	
	public JRadioButton createRadioButton(String text, ActionListener listener, String actionCommand){
		JRadioButton radioButton = new JRadioButton(text);
		radioButton.addActionListener(listener);
		radioButton.setActionCommand(actionCommand);
		return radioButton;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox createComboBox(String[] s, ActionListener listener, String actionCommand){
		@SuppressWarnings("unchecked")
		JComboBox combo = new JComboBox(s);
		combo.addActionListener(listener);
		combo.setActionCommand(actionCommand);
		return combo;
	}
	
	public JTextField createTextField(int chars){
		return new JTextField(chars);
	}
	
	public JTextArea createTextArea(int row, int col){
		return new JTextArea(row, col);
	}
	
	public JButton createButton(String text, ActionListener listener, String actionCommand){
		JButton button = new JButton(text);
		button.addActionListener(listener);
		button.setActionCommand(actionCommand);
		return button;
	}

	public JToolBar createToolbar() {
		return new JToolBar();
	}

	
}

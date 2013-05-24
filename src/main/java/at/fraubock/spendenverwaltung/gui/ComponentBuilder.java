package at.fraubock.spendenverwaltung.gui;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ComponentBuilder {

	public JButton createImageButton(String path, ActionListener listener, String actionCommand){
		java.net.URL url = getClass().getResource(path);
		JButton button = new JButton(new ImageIcon(url));
		button.addActionListener(listener);
		button.setActionCommand(actionCommand);
		return button;
	}
	
	public JPanel createPanel(){
		return new JPanel();
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
	
	
}

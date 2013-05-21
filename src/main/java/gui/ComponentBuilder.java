package gui;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

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
	
	public JScrollPane createTable(Object...columns) {
		JTable table = new JTable(new Object[0][0], columns);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		return scrollPane;
	}
	
	@SuppressWarnings("serial")
	public JScrollPane createTable(Vector<Vector<String>> rows, Vector<String> header) {
		JTable table = new JTable(rows, header){
			@Override
			public boolean isCellEditable(int rowIndex, int colIndex){
				return false;
			}
		};
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return scrollPane;
	}
	
	@SuppressWarnings("serial")
	public JTable createRTable(AbstractTableModel model) {
		JTable table = new JTable(model){
			@Override
			public boolean isCellEditable(int rowIndex, int colIndex){
				return false;
			}
		};
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return table;
	}
	
	public JScrollPane createScrollPane(JTable table){
		return new JScrollPane(table);
	}
	
	@SuppressWarnings("serial")
	public JScrollPane createTable(AbstractTableModel model){
		JTable table = new JTable(model){
			@Override
			public boolean isCellEditable(int rowIndex, int colIndex){
				return false;
			}
		};
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return scrollPane;
	}
}

package at.fraubock.spendenverwaltung.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer {
    Color color = new Color(240,240,240);
    
	@Override
	public Component getTableCellRendererComponent(JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
	    JLabel label = (JLabel)super.getTableCellRendererComponent(table, obj, isSelected,hasFocus, row, column);
	    
	    if((row % 2) == 1 && !isSelected){
	        label.setBackground(color);
	    }
	    else if((row % 2) == 0 && !isSelected){
	        label.setBackground(Color.WHITE);
	    }
	    
	    return label;
	}
}

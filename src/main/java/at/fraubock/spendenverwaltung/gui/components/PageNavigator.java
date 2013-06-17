package at.fraubock.spendenverwaltung.gui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public class PageNavigator extends JPanel {
	
	private static final long serialVersionUID = -5031954162149446462L;
	private static final Logger log = Logger.getLogger(PageNavigator.class);
	
	private PageableTableModel model;
	private JLabel prev, next;

	public PageNavigator(PageableTableModel modelParam) {
		setLayout(new MigLayout());
		setBackground(new Color(200,200,200));
		
		this.model = modelParam;
		
		prev = new JLabel("prev");
		prev.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					model.showPage(model.getPosition()-1);
					modelRefreshed();
				} catch (ServiceException e) {
					JOptionPane.showMessageDialog(PageNavigator.this,
							"Ein unerwarteter Fehler ist aufgetreten.", "Error",
							JOptionPane.ERROR_MESSAGE);
					log.error("Error when showing page: " + e.getMessage());
				}
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				PageNavigator.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				PageNavigator.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent arg0) {	}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		add(prev);
		
		next = new JLabel("next");
		next.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					model.showPage(model.getPosition()+1);
					modelRefreshed();
				} catch (ServiceException e) {
					JOptionPane.showMessageDialog(PageNavigator.this,
							"Ein unerwarteter Fehler ist aufgetreten.", "Error",
							JOptionPane.ERROR_MESSAGE);
					log.error("Error when showing page: " + e.getMessage());
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				PageNavigator.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				PageNavigator.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent arg0) {	}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
		add(next);
		
		modelRefreshed();
	}
	
	public void modelRefreshed() {
		try {
			if(model.getPageCount()-1==model.getPosition()) {
				next.setVisible(false);
			} else {
				next.setVisible(true);
			}
			
			if(model.getPosition()==0) {
				prev.setVisible(false);
			} else {
				prev.setVisible(true);
			}
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(PageNavigator.this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			log.error("Error when getting page count: " + e.getMessage());
		}
	}

}

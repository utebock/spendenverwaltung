package at.fraubock.spendenverwaltung.gui.components;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;

public class PageNavigator extends JPanel {

	private static final long serialVersionUID = -5031954162149446462L;
	private static final Logger log = Logger.getLogger(PageNavigator.class);

	private PageableTableModel model;
	private JLabel prev, next, first, last, pageNumber;
	private JPanel navigation;

	public PageNavigator(PageableTableModel modelParam) {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		this.model = modelParam;
		navigation = new JPanel();

		pageNumber = new JLabel();
		first = new JLabel("<< ");
		prev = new JLabel(" < ");
		next = new JLabel(" > ");
		last = new JLabel(" >>");

		first.addMouseListener(new NavigationListener());
		prev.addMouseListener(new NavigationListener());
		next.addMouseListener(new NavigationListener());
		last.addMouseListener(new NavigationListener());

		navigation.add(first);
		navigation.add(prev);
		navigation.add(pageNumber);
		navigation.add(next);
		navigation.add(last);

		add(navigation);
	}

	private void drawNavigation() {
		try {
			if (model.getPageCount() == 0) {
				first.setVisible(false);
				prev.setVisible(false);
				pageNumber.setVisible(false);
				next.setVisible(false);
				last.setVisible(false);
				return;
			}

			pageNumber.setVisible(true);

			pageNumber.setText(" " + (model.getPosition() + 1) + "/"
					+ model.getPageCount() + " ");

			if (model.getPageCount() - 1 == model.getPosition()) {
				next.setVisible(false);
				last.setVisible(false);
			} else {
				next.setVisible(true);
				last.setVisible(true);
			}

			if (model.getPosition() == 0) {
				prev.setVisible(false);
				first.setVisible(false);
			} else {
				prev.setVisible(true);
				first.setVisible(true);
			}

		} catch (ServiceException e1) {
			JOptionPane.showMessageDialog(PageNavigator.this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			log.error("Error when reading page count: " + e1.getMessage());
		}

	}

	public void modelRefreshed() {
		drawNavigation();
	}

	private class NavigationListener implements MouseListener {

		public NavigationListener() {

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
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
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			try {
				if (arg0.getSource() == first) {
					model.showPage(0);
				} else if (arg0.getSource() == prev) {
					model.showPage(model.getPosition() - 1);
				} else if (arg0.getSource() == next) {
					model.showPage(model.getPosition() + 1);
				} else if (arg0.getSource() == last) {
					model.showPage((int) model.getPageCount() - 1);
				}
				modelRefreshed();
			} catch (ServiceException e) {
				JOptionPane.showMessageDialog(PageNavigator.this,
						"Ein unerwarteter Fehler ist aufgetreten.", "Error",
						JOptionPane.ERROR_MESSAGE);
				log.error("Error when showing page: " + e.getMessage());
			}
		}
	}

}

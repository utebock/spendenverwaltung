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
package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.plaf.metal.MetalBorders;

import net.miginfocom.swing.MigLayout;
import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.gui.views.HistoryView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;

/**
 * panel that renders a list of actions. will fade in to the given layered panel
 * and fade out automatically. when clicking it, the history view will be shown
 * with the actions in the table.
 * 
 * @author philipp muhoray
 * 
 */
public class NotificationPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 3957378274808663996L;

	private JLayeredPane rootLayeredPane;
	private ViewDisplayer viewDisplayer;
	private ViewActionFactory viewFactory;
	private List<Action> actions;

	public NotificationPanel(ViewDisplayer viewDisplayer,
			ViewActionFactory viewFactory) {
		this.rootLayeredPane = viewDisplayer.getLayeredPane();
		this.viewDisplayer = viewDisplayer;
		this.viewFactory = viewFactory;

		setLayout(new MigLayout());
		rootLayeredPane.add(this, new Integer(50));
		setBorder(BorderFactory.createTitledBorder(
				MetalBorders.getTextFieldBorder(), "Aktuelle Aktionen"));
		this.addMouseListener(this);
	}

	public void present(List<Action> actions) {
		this.actions = actions;
		int size = actions.size();

		for (Action a : actions) {
			add(new ActionPanel(a), "wrap,growx");
		}
		setVisible(true);

		for (int i = 0; i < 100 + size * 50; i++) {
			setBounds(rootLayeredPane.getWidth() - 320, 770 - i, 300,
					30 + size * 50);
			rootLayeredPane.repaint();
			rootLayeredPane.revalidate();
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// nothing
			}
		}

		// hide after a while
		try {
			Thread.sleep(ActionPolling.REMOVE_ACTION_VIEW_IN_MILLI);
		} catch (InterruptedException e) {
			// nothing
		}

		fadeOut();

	}

	private void fadeOut() {
		if (!isVisible())
			return; // already invisible

		int size = this.getComponents().length;
		for (int i = 0; i < 100 + size * 50; i++) {
			setBounds(rootLayeredPane.getWidth() - 320,
					(770 - (100 + size * 50)) + i, 300, 30 + size * 50);
			rootLayeredPane.repaint();
			rootLayeredPane.revalidate();
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// nothing
			}
		}

		setVisible(false);

		for (Component comp : this.getComponents()) {
			this.remove(comp);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		HistoryView view = (HistoryView) viewFactory
				.getViewForAction(viewFactory.getHistoryViewAction());
		view.init();
		view.showActions(actions);
		viewDisplayer.changeView(view);
	}

	/**
	 * panel rendering a single action
	 * 
	 * @author philipp muhoray
	 * 
	 */
	private class ActionPanel extends JPanel {
		private static final long serialVersionUID = 2033492844139154802L;

		public ActionPanel(Action a) {
			setLayout(new MigLayout());
			JPanel textPanel = new JPanel();
			textPanel.add(new JLabel(a.getActor() + " hat " + a.getEntity()
					+ " " + a.getType().toString()));
			add(textPanel, "wrap");
			JSeparator sep = new JSeparator();
			add(sep, "growx");
		}

	}

}
